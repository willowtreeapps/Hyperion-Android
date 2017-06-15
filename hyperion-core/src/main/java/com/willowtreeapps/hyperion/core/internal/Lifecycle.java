package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Lifecycle extends LifecycleAdapter {

    private static final String OVERLAY_TAG = "hyperion_overlay";
    private static final String DRAWER_TAG = "hyperion_drawer";

    private final SimpleArrayMap<Activity, CoreComponent> components = new SimpleArrayMap<>();
    private boolean embeddedDrawerEnabled = true;

    @Override
    public void onActivityStarted(Activity activity) {

        // TODO Support SDK fragments
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();

        HyperionOverlayFragment fragment = (HyperionOverlayFragment)
                fragmentManager.findFragmentByTag(OVERLAY_TAG);
        if (fragment == null) {
            fragment = new HyperionOverlayFragment();
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment, OVERLAY_TAG)
                    .commitNow();
        }

        CoreComponent component = DaggerCoreComponent.builder()
                .appComponent(AppComponent.Holder.getInstance())
                .coreModule(new CoreModule())
                .activityModule(new ActivityModule(activity))
                .overlayModule(new OverlayModule(fragment))
                .build();

        components.put(activity, component);

        if (embeddedDrawerEnabled && fragmentManager.findFragmentByTag(DRAWER_TAG) == null) {
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, new HyperionDrawerFragment(), DRAWER_TAG)
                    .commitNow();
        }
    }

    CoreComponent getComponent(Activity activity) {
        return components.get(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        components.remove(activity);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        components.get(activity).getActivityResults()
                .notifyActivityResult(requestCode, resultCode, data);
    }

    public boolean isEmbeddedDrawerEnabled() {
        return embeddedDrawerEnabled;
    }

    public void setEmbeddedDrawerEnabled(boolean enabled) {
        this.embeddedDrawerEnabled = enabled;

        int count = components.size();
        for (int i = 0; i < count; i++) {
            Activity activity = components.keyAt(i);
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();

            if (enabled) {
                if (fragmentManager.findFragmentByTag(DRAWER_TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(android.R.id.content, new HyperionDrawerFragment(), DRAWER_TAG)
                            .commit();
                }
            } else {
                Fragment fragment = fragmentManager.findFragmentByTag(DRAWER_TAG);
                fragmentManager.beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        }
    }

    @NonNull
    public View createPluginView(Activity activity) {
        CoreComponent component = components.get(activity);
        if (component == null) {
            throw new IllegalStateException("Could not locate Hyperion component for given activity. Is the Activity stopped?");
        }

        return new HyperionPluginView(new ComponentContextWrapper(activity, component));
    }
}