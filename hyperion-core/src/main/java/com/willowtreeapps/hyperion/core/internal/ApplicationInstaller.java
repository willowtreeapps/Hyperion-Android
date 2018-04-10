package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;
import android.support.annotation.MainThread;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

class ApplicationInstaller {

    private final Provider<PluginSource> pluginSourceProvider;
    private final Application application;
    private final ApplicationExtension applicationExtension;
    private boolean applicationCreated;

    @Inject
    ApplicationInstaller(Provider<PluginSource> pluginSourceProvider,
                         Application application,
                         ApplicationExtension applicationExtension) {
        this.pluginSourceProvider = pluginSourceProvider;
        this.application = application;
        this.applicationExtension = applicationExtension;
    }

    @MainThread
    void installIfNeeded() {
        if (!applicationCreated) {
            final Set<Plugin> plugins = pluginSourceProvider.get().getPlugins();
            for (Plugin plugin : plugins) {
                plugin.create(application, applicationExtension);
            }
            applicationCreated = true;
        }
    }

}