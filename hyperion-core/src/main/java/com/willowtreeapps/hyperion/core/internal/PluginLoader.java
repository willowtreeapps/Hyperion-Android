package com.willowtreeapps.hyperion.core.internal;

import android.support.annotation.AnyThread;
import android.support.annotation.WorkerThread;

import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;

import javax.inject.Inject;

@ActivityScope
class PluginLoader {

    private final Executor workerThreadExecutor;
    private final Executor mainThreadExecutor;

    @Inject
    PluginLoader(@Worker Executor workerThreadExecutor, @Main Executor mainThreadExecutor) {
        this.workerThreadExecutor = workerThreadExecutor;
        this.mainThreadExecutor = mainThreadExecutor;
    }

    @AnyThread
    void load(final Callback<List<PluginModule>> callback) {
        workerThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Try<List<PluginModule>> result;
                try {
                    result = Try.successful(load());
                } catch (Throwable t) {
                    result = Try.failure(t);
                }
                deliver(result, callback);
            }
        });
    }

    @WorkerThread
    List<PluginModule> load() {
        List<PluginModule> plugins = new ArrayList<>();
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : loader) {
            PluginModule module = plugin.createPluginModule();
            if (module != null) {
                plugins.add(module);
            }
        }
        return plugins;
    }

    private void deliver(final Try<List<PluginModule>> result,
                         final Callback<List<PluginModule>> callback) {
        mainThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                callback.call(result);
            }
        });
    }

}