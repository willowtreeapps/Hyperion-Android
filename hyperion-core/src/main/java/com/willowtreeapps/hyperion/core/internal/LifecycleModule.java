package com.willowtreeapps.hyperion.core.internal;

import dagger.Module;
import dagger.Provides;

@Module
class LifecycleModule {

    private final Lifecycle lifecycle;

    LifecycleModule(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Provides
    Lifecycle provideLifecycleListener() {
        return this.lifecycle;
    }

}