package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;
import com.willowtreeapps.hyperion.core.plugins.OverlayContainer;

import dagger.Component;

@ActivityScope
@Component(modules = {
        CoreModule.class,
        ActivityModule.class,
        OverlayModule.class,
        ActivityResultModule.class
}, dependencies = {
        AppComponent.class
})
interface CoreComponent {

    void inject(HyperionPluginView view);

    MeasurementHelper getMeasurementHelper();

    AttributeTranslator getAttributeTranslator();

    Activity getActivity();

    OverlayContainer getOverlayContainer();

    ActivityResults getActivityResults();

    DisplayMetrics getDisplayMetrics();

    ViewTarget getViewTarget();
}