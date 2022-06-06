package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.ServiceConnection;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.PluginSource;
import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import java.util.Set;

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

    ActivityResults getActivityResults();

    ServiceConnection getServiceConnection();

    HyperionMenuController getMenuController();

    Set<PluginModule> getPluginModules();

    PluginFilter getPluginFilter();

    @Component.Builder
    interface Builder {

        Builder appComponent(AppComponent appComponent);

        @BindsInstance
        Builder activity(Activity activity);

        @BindsInstance
        Builder activityResults(ActivityResults activityResults);

        @BindsInstance
        Builder menuController(HyperionMenuController menuController);

        @BindsInstance
        Builder container(ViewGroup container);

        @BindsInstance
        Builder pluginFilter(PluginFilter pluginFilter);

        CoreComponent build();
    }

}