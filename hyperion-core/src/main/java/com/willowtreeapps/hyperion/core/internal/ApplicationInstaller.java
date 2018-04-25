package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;
import android.support.annotation.MainThread;

import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import javax.inject.Inject;

class ApplicationInstaller {

    private final PluginRepository pluginRepository;
    private final Application application;
    private final ApplicationExtension applicationExtension;
    private boolean applicationCreated;

    @Inject
    ApplicationInstaller(PluginRepository pluginRepository,
                         Application application,
                         ApplicationExtension applicationExtension) {
        this.pluginRepository = pluginRepository;
        this.application = application;
        this.applicationExtension = applicationExtension;
    }

    @MainThread
    void installIfNeeded() {
        if (!applicationCreated) {
            final Plugins plugins = pluginRepository.getPlugins();
            for (Plugin plugin : plugins.get()) {
                plugin.create(application, applicationExtension);
            }
            applicationCreated = true;
        }
    }

}