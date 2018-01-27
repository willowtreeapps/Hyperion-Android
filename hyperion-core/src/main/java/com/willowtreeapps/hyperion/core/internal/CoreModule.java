package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.ServiceConnection;

import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.ViewTarget;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
abstract class CoreModule {

    @Provides
    @ActivityScope
    static ServiceConnection provideServiceConnection(Activity activity) {
        return new HyperionService.Connection(activity);
    }

    @Binds
    @ActivityScope
    abstract ViewTarget bindViewTarget(ViewTargetImpl viewTarget);

    @Binds
    @ActivityScope
    abstract MeasurementHelper bindMeasurementHelper(MeasurementHelperImpl measurementHelper);

    @Binds
    @ActivityScope
    abstract AttributeTranslator bindAttributeTranslator(AttributeTranslatorImpl attributeTranslator);

    @Provides
    @ActivityScope
    @Worker
    static Executor provideWorkerThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @ActivityScope
    @Main
    static Executor provideUiThreadExecutor() {
        return new UiThreadExecutor();
    }

}