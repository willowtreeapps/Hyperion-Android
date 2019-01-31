package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;

import javax.inject.Inject;

@AppScope
class InstallationLifecycleDelegate extends LifecycleDelegate {

    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final CoreComponentContainer container;
    private final ApplicationInstaller applicationInstaller;

    @Inject
    InstallationLifecycleDelegate(CoreComponentContainer container,
                                  ApplicationInstaller applicationInstaller) {
        this.container = container;
        this.applicationInstaller = applicationInstaller;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        applicationInstaller.installIfNeeded();

        final ViewGroup windowContentView = activity.getWindow().findViewById(android.R.id.content);
        final HyperionMenuController controller = new HyperionMenuController(windowContentView);

        FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);

        ActivityResults activityResults = fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
        if (activityResults == null) {
            activityResults = fragmentManager.isSupport() ? new ActivityResultsSupportFragment() : new ActivityResultsFragment();
            fragmentManager.beginTransaction()
                    .add(activityResults, ACTIVITY_RESULT_TAG)
                    .commit();
        }

        CoreComponent component = DaggerCoreComponent.builder()
                .appComponent(AppComponent.Holder.getInstance(activity))
                .activity(activity)
                .pluginSource(container.getPluginSource())
                .menuController(controller)
                .container(windowContentView)
                .activityResults(activityResults)
                .build();

        container.putComponent(activity, component);

        // embed plugins list into menu
        HyperionPluginView pluginView = new HyperionPluginView(new ComponentContextThemeWrapper(activity, component));
        controller.setPluginView(pluginView);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        container.removeComponent(activity);
    }
}