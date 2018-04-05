package com.willowtreeapps.hyperion.core.internal;

import android.support.annotation.AnyThread;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import java.util.Set;
import java.util.concurrent.Executor;

import javax.inject.Inject;

@ActivityScope
class PluginLoader {

    private final PluginSource pluginSource;
    private final Executor workerThreadExecutor;
    private final Executor mainThreadExecutor;

    @Inject
    PluginLoader(PluginSource pluginSource,
                 @Worker Executor workerThreadExecutor,
                 @Main Executor mainThreadExecutor) {
        this.pluginSource = pluginSource;
        this.workerThreadExecutor = workerThreadExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    @AnyThread
    void load(final Callback<Plugins> callback) {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Try<Plugins> result;
                try {
                    final Set<Plugin> plugins = pluginSource.getPlugins();
                    result = Try.successful(new Plugins(plugins));
                } catch (Throwable t) {
                    result = Try.failure(t);
                }
                deliver(result, callback);
            }
        });
    }

    private void deliver(final Try<Plugins> result,
                         final Callback<Plugins> callback) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.call(result);
            }
        });
    }
}