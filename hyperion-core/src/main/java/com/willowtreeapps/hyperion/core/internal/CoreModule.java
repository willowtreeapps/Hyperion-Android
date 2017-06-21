package com.willowtreeapps.hyperion.core.internal;

import android.util.DisplayMetrics;

import com.willowtreeapps.hyperion.core.ActivityResults;
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
    ActivityResults provideActivityResults() {
        return new ActivityResultsImpl();
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