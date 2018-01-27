package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.core.plugins.v1.HyperionIgnore;

public class Lifecycle extends LifecycleAdapter {

    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final SimpleArrayMap<Activity, CoreComponent> components = new SimpleArrayMap<>();
    private Activity foregroundActivity;
    private float sensitivity = 3.0f;

    @Override
    public void onActivityResumed(Activity activity) {
        foregroundActivity = activity;
        final ServiceConnection connection = getComponent(activity).getServiceConnection();
        foregroundActivity.bindService(
                new Intent(activity, HyperionService.class),
                connection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (foregroundActivity == activity) {
            final ServiceConnection connection = getComponent(activity).getServiceConnection();
            foregroundActivity.unbindService(connection);
            foregroundActivity = null;
        }
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        HyperionIgnore ignore = activity.getClass().getAnnotation(HyperionIgnore.class);
        if (ignore == null) {
            install(activity);
        }
    }

    private void install(final Activity activity) {
        // reorganize the layout
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final View contentView = decorView.getChildAt(0);
        if (decorView.getChildCount() < 1) {
            // no content, abort install
            return;
        }
        // prevent clicking through to menu behind content
        contentView.setClickable(true);
        decorView.removeView(contentView);

        // embed content view within overlay
        final HyperionOverlayLayout overlayLayout = new HyperionOverlayLayout(activity);
        overlayLayout.setId(R.id.hyperion_overlay);
        overlayLayout.addView(contentView);

        // embed overlay + content within menu
        final HyperionMenuLayout menuLayout = new HyperionMenuLayout(activity);
        menuLayout.setId(R.id.hyperion_menu);
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
                .coreModule(new CoreModule())
                .activityModule(new ActivityModule(activity))
                .overlayModule(new OverlayModule(overlayLayout))
                .activityResultModule(new ActivityResultModule(results))
                .build();

        components.put(activity, component);

        // embed plugins list into menu
        final Context coreContext = new ComponentContextThemeWrapper(activity, component);
        final HyperionPluginView pluginView = new HyperionPluginView(coreContext);
        pluginView.setId(R.id.hyperion_plugins);
        pluginView.setAlpha(0.0f);
        menuLayout.addView(pluginView);
        menuLayout.addView(overlayLayout);
    }

    CoreComponent getComponent(Activity activity) {
        return components.get(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        components.remove(activity);
    }

    public float getShakeGestureSensitivity() {
        return sensitivity;
    }

    public void setShakeGestureSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
        for (int i = 0; i < components.size(); i++) {
            final Activity activity = components.keyAt(i);
            final HyperionMenuLayout menu = activity.findViewById(R.id.hyperion_menu);
            menu.setShakeGestureSensitivity(sensitivity);
        }
    }

    public void open(Activity activity) {
        final HyperionMenuLayout menu = activity.findViewById(R.id.hyperion_menu);
        if (menu == null) {
            Log.d("Hyperion", "Could not find Hyperion menu in this activity.");
            return;
        }
        menu.expand();
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