package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;
import androidx.annotation.MainThread;
import androidx.annotation.RestrictTo;

import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

import javax.inject.Inject;

public class ApplicationInstaller {

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
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void installIfNeeded() {
        if (!applicationCreated) {
            final Plugins plugins = pluginRepository.getPlugins();
            for (Plugin plugin : plugins.get()) {
                plugin.create(application, applicationExtension);
            }
            applicationCreated = true;
        }
    }

}