package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.ServiceConnection;
import android.util.DisplayMetrics;

import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;

@Module
class CoreModule {

    @Provides
    @ActivityScope
    ServiceConnection provideServiceConnection(Activity activity) {
        return new HyperionService.Connection(activity);
    }

    @Provides
    @ActivityScope
    ViewTarget provideViewTarget() {
        return new ViewTargetImpl();
    }

    @Provides
    @ActivityScope
    MeasurementHelper provideMeasurementHelper(DisplayMetrics displayMetrics) {
        return new MeasurementHelperImpl(displayMetrics);
    }

    @Provides
    @ActivityScope
    AttributeTranslator provideAttributeTranslator(MeasurementHelper measurementHelper) {
        return new AttributeTranslatorImpl(measurementHelper);
    }

    @Provides
    @ActivityScope
    @Worker
    Executor provideWorkerThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @ActivityScope
    @Main
    Executor provideUiThreadExecutor() {
        return new UiThreadExecutor();
    }

}