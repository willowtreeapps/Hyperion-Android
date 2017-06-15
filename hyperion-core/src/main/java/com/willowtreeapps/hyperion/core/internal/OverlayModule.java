package com.willowtreeapps.hyperion.core.internal;

import dagger.Module;
import dagger.Provides;

@Module
class OverlayModule {

    private final HyperionOverlayFragment fragment;

    OverlayModule(HyperionOverlayFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    HyperionOverlayFragment provideOverlayFragment() {
        return this.fragment;
    }

}