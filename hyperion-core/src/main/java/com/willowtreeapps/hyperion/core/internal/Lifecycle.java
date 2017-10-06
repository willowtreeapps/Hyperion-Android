package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.plugins.OverlayContainer;

public class Lifecycle extends LifecycleAdapter {

    private static final String OVERLAY_TAG = "hyperion_overlay";
    private static final String DRAWER_TAG = "hyperion_drawer";
    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final SimpleArrayMap<Activity, CoreComponent> components = new SimpleArrayMap<>();
    private boolean embeddedDrawerEnabled = true;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        OverlayContainer fragment;
        ActivityResults results;

        if (activity instanceof AppCompatActivity) {
            FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
            fragment = (HyperionOverlayFragment)
                    fragmentManager.findFragmentByTag(OVERLAY_TAG);

            if (fragment == null) {
                fragment = new HyperionOverlayFragment();
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, (HyperionOverlayFragment) fragment, OVERLAY_TAG)
                        .commit();
            }

            results = (ActivityResultsFragment)
                    fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
            if (results == null) {
                results = new ActivityResultsFragment();
                fragmentManager.beginTransaction()
                        .add((ActivityResultsFragment) results, ACTIVITY_RESULT_TAG)
                        .commit();
            }
        } else {    //not appcompat
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            fragment = (HyperionOverlayFragmentNonAppCompat)
                    fragmentManager.findFragmentByTag(OVERLAY_TAG);

            if (fragment == null) {
                fragment = new HyperionOverlayFragmentNonAppCompat();
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, (HyperionOverlayFragmentNonAppCompat) fragment, OVERLAY_TAG)
                        .commit();
            }

            results = (ActivityResultsFragmentNonAppCompat)
                    fragmentManager.findFragmentByTag(ACTIVITY_RESULT_TAG);
            if (results == null) {
                results = new ActivityResultsFragmentNonAppCompat();
                fragmentManager.beginTransaction()
                        .add((ActivityResultsFragmentNonAppCompat) results, ACTIVITY_RESULT_TAG)
                        .commit();
            }
        }

        CoreComponent component = DaggerCoreComponent.builder()
                .appComponent(AppComponent.Holder.getInstance())
                .coreModule(new CoreModule())
                .activityModule(new ActivityModule(activity))
                .overlayModule(new OverlayModule(fragment))
                .activityResultModule(new ActivityResultModule(results))
                .build();

        components.put(activity, component);

        if (activity instanceof AppCompatActivity) {
            FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
            if (embeddedDrawerEnabled && fragmentManager.findFragmentByTag(DRAWER_TAG) == null) {
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, new HyperionDrawerFragment(), DRAWER_TAG)
                        .commit();
            }
        } else {
            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
            if (embeddedDrawerEnabled && fragmentManager.findFragmentByTag(DRAWER_TAG) == null) {
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, new HyperionDrawerFragmentNonAppCompat(), DRAWER_TAG)
                        .commit();
            }
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

        return new HyperionPluginView(new ComponentContextThemeWrapper(activity, component));
    }
}