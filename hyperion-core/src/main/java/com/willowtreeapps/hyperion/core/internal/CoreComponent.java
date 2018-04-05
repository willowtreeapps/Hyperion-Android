package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.ServiceConnection;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;

import dagger.BindsInstance;
import dagger.Component;

@ActivityScope
@Component(modules = {
        CoreModule.class,
        ActivityModule.class
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

    ServiceConnection getServiceConnection();

    @Component.Builder
    interface Builder {

        Builder appComponent(AppComponent appComponent);

        @BindsInstance
        Builder activity(Activity activity);

        @BindsInstance
        Builder pluginSource(PluginSource pluginSource);

        @BindsInstance
        Builder overlayContainer(OverlayContainer overlayContainer);

        @BindsInstance
        Builder activityResults(ActivityResults activityResults);

        CoreComponent build();
    }

}