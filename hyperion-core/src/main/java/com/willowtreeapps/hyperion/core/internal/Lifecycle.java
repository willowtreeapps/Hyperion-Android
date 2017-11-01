package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.plugins.v1.OverlayContainer;

public class Lifecycle extends LifecycleAdapter {

    private static final String OVERLAY_TAG = "hyperion_overlay";
    private static final String DRAWER_TAG = "hyperion_drawer";
    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final SimpleArrayMap<Activity, CoreComponent> components = new SimpleArrayMap<>();
    private boolean embeddedDrawerEnabled = true;
    private boolean shakeGestureEnabled = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);

        OverlayContainer fragment = fragmentManager.findFragmentByTag(OVERLAY_TAG);
        if (fragment == null) {
            fragment = fragmentManager.isSupport() ? new HyperionOverlaySupportFragment() : new HyperionOverlayFragment();
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment, OVERLAY_TAG)
                    .commit();
        }

        ActivityResults results = fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
        if (results == null) {
            results = fragmentManager.isSupport() ? new ActivityResultsSupportFragment() : new ActivityResultsFragment();
            fragmentManager.beginTransaction()
                    .add(results, ACTIVITY_RESULT_TAG)
                    .commit();
        }

        CoreComponent component = DaggerCoreComponent.builder()
                .appComponent(AppComponent.Holder.getInstance())
                .coreModule(new CoreModule())
                .activityModule(new ActivityModule(activity))
                .overlayModule(new OverlayModule(fragment))
                .activityResultModule(new ActivityResultModule(results))
                .build();

        components.put(activity, component);

        HyperionDrawer drawer = fragmentManager.findFragmentByTag(DRAWER_TAG);
        if (drawer == null && embeddedDrawerEnabled) {
            drawer = fragmentManager.isSupport() ? new HyperionDrawerSupportFragment() : new HyperionDrawerFragment();
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, drawer, DRAWER_TAG)
                    .commit();
        }
    }

    CoreComponent getComponent(Activity activity) {
        return components.get(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        components.remove(activity);
    }

    public boolean isEmbeddedDrawerEnabled() {
        return embeddedDrawerEnabled;
    }

    public void setEmbeddedDrawerEnabled(boolean enabled) {
        this.embeddedDrawerEnabled = enabled;

        int count = components.size();
        for (int i = 0; i < count; i++) {
            Activity activity = components.keyAt(i);
            FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);
            HyperionDrawer drawer = fragmentManager.findFragmentByTag(DRAWER_TAG);
            if (enabled) {
                if (drawer == null) {
                    drawer = fragmentManager.isSupport() ? new HyperionDrawerSupportFragment() : new HyperionDrawerFragment();
                    fragmentManager.beginTransaction()
                            .add(android.R.id.content, drawer, DRAWER_TAG)
                            .commit();
                }
            } else if (drawer != null) {
                fragmentManager.beginTransaction()
                        .remove(drawer)
                        .commit();
            }
        }
    }

    public boolean isShakeGestureEnabled() {
        return this.shakeGestureEnabled;
    }

    public void setShakeGestureEnabled(boolean enabled) {
        this.shakeGestureEnabled = enabled;
        int count = components.size();
        for (int i = 0; i < count; i++) {
            Activity activity = components.keyAt(i);
            FragmentManagerCompat fragmentManager = FragmentManagerCompat.create(activity);
            HyperionDrawer drawer = fragmentManager.findFragmentByTag(DRAWER_TAG);
            if (drawer != null) {
                drawer.getLayout().setShakeGestureEnabled(enabled);
            }
        }
    }

    @NonNull
    public View createPluginView(Activity activity) {
        CoreComponent component = components.get(activity);
        if (component == null) {
            throw new IllegalStateException("Could not locate Hyperion component for given activity. Is the Activity destroyed?");
        }

        return new HyperionPluginView(new ComponentContextThemeWrapper(activity, component));
    }
}