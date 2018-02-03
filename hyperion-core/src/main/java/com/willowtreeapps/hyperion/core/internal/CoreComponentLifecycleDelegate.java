package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;

import javax.inject.Inject;

@AppScope
class CoreComponentLifecycleDelegate extends LifecycleDelegate {

    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final CoreComponentContainer container;

    @Inject
    CoreComponentLifecycleDelegate(CoreComponentContainer container) {
        this.container = container;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // reorganize the layout
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final View contentView = decorView.getChildAt(0);
        if (decorView.getChildCount() < 1) {
            // no content, abort install
            return;
        }
        decorView.removeView(contentView);

        // embed content view within overlay
        final HyperionOverlayLayout overlayLayout = new HyperionOverlayLayout(activity);
        overlayLayout.addView(contentView);

        // embed overlay + content within menu
        final HyperionMenuLayout menuLayout = new HyperionMenuLayout(activity);
        decorView.addView(menuLayout);

        FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);

        ActivityResults results = fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
        if (results == null) {
            results = fragmentManager.isSupport() ? new ActivityResultsSupportFragment() : new ActivityResultsFragment();
            fragmentManager.beginTransaction()
                    .add(results, ACTIVITY_RESULT_TAG)
                    .commit();
        }

        CoreComponent component = DaggerCoreComponent.builder()
                .appComponent(AppComponent.Holder.getInstance())
                .activityModule(new ActivityModule(activity))
                .overlayModule(new OverlayModule(overlayLayout))
                .activityResultModule(new ActivityResultModule(results))
                .build();

        container.putComponent(activity, component);

        // embed plugins list into menu
        final Context coreContext = new ComponentContextThemeWrapper(activity, component);
        final HyperionPluginView pluginView = new HyperionPluginView(coreContext);
        pluginView.setAlpha(0.0f);
        menuLayout.addView(pluginView);
        menuLayout.addView(overlayLayout);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        container.removeComponent(activity);
    }
}