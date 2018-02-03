package com.willowtreeapps.hyperion.core.internal;

import com.willowtreeapps.hyperion.plugin.v1.ApplicationExtension;
import com.willowtreeapps.hyperion.plugin.v1.ForegroundManager;

import javax.inject.Inject;

@AppScope
class ApplicationExtensionImpl implements ApplicationExtension {

    private final ForegroundManager foregroundManager;

    @Inject
    ApplicationExtensionImpl(ForegroundManager foregroundManager) {
        this.foregroundManager = foregroundManager;
    }

    @Override
    public ForegroundManager getForegroundManager() {
        return foregroundManager;
    }
}