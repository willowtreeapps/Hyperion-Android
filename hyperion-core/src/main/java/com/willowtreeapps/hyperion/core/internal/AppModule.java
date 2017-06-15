package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    private final Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return this.application;
    }

}