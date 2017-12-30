package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.R;

public class Lifecycle extends LifecycleAdapter {

    private static final String ACTIVITY_RESULT_TAG = "hyperion_activity_result";

    private final SimpleArrayMap<Activity, CoreComponent> components = new SimpleArrayMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean embeddedDrawerEnabled = true;

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // reorganize the layout.
                final ViewGroup contentViewRoot = activity.findViewById(android.R.id.content);
                final View contentView = contentViewRoot.getChildAt(0);
                contentViewRoot.removeView(contentView);

                // embed content view within overlay
                final HyperionOverlayLayout overlayLayout = new HyperionOverlayLayout(activity);
                overlayLayout.setId(R.id.hyperion_overlay);
                overlayLayout.addView(contentView);

                // embed overlay + content within menu
                final HyperionMenuLayout menuLayout = new HyperionMenuLayout(activity);
                menuLayout.setId(R.id.hyperion_menu);
                contentViewRoot.addView(menuLayout);

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
                menuLayout.addView(pluginView);
                menuLayout.addView(overlayLayout);
            }
        });
    }

    CoreComponent getComponent(Activity activity) {
        return components.get(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        components.remove(activity);
    }

    public void setShakeGestureSensitivity(float sensitivity) {
        for (int i = 0; i < components.size(); i++) {
            final Activity activity = components.keyAt(i);
            final HyperionMenuLayout menu = activity.findViewById(R.id.hyperion_menu);
            menu.setShakeGestureSensitivity(sensitivity);
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