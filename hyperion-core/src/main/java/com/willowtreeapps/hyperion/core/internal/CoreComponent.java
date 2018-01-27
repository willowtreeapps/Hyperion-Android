package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.ServiceConnection;

import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;
import com.willowtreeapps.hyperion.plugin.v1.ViewTarget;

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

    ViewTarget getViewTarget();

    ServiceConnection getServiceConnection();
}