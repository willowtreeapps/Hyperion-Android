package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;

import dagger.Component;

@ActivityScope
@Component(modules = {
        CoreModule.class,
        ActivityModule.class,
        OverlayModule.class
}, dependencies = {
        AppComponent.class
})
interface CoreComponent {

    void inject(HyperionPluginView view);

    MeasurementHelper getMeasurementHelper();

    AttributeTranslator getAttributeTranslator();

    Activity getActivity();

    HyperionOverlayFragment getOverlayFragment();

    ActivityResults getActivityResults();

    DisplayMetrics getDisplayMetrics();

    ViewTarget getViewTarget();
}