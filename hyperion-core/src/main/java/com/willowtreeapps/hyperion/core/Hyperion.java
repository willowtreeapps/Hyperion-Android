package com.willowtreeapps.hyperion.core;

import android.app.Activity;

import com.willowtreeapps.hyperion.core.internal.AppComponent;

public final class Hyperion {

    /**
     * Get the sensitivity threshold of shake detection in G's. Default is 3
     *
     * @return sensitivity Sensitivity of shake detection in G's. Lower is easier to activate.
     */
    public static float getShakeGestureSensitivity() {
        return AppComponent.Holder.getInstance().getPublicControl().getShakeGestureSensitivity();
    }

    /**
     * Set the sensitivity threshold of shake detection in G's. Default is 3
     *
     * @param sensitivity Sensitivity of shake detection in G's. Lower is easier to activate.
     */
    public static void setShakeGestureSensitivity(float sensitivity) {
        AppComponent.Holder.getInstance().getPublicControl().setShakeGestureSensitivity(sensitivity);
    }

    /**
     * Manually trigger the Hyperion menu embedded in the current foreground {@link Activity}
     **/
    public static void open() {
        AppComponent.Holder.getInstance().getPublicControl().open();
    }

    /**
     * Manually trigger the Hyperion menu embedded in the given {@link Activity} to open.
     *
     * @param activity the {@link Activity} containing the menu to open.
     */
    public static void open(Activity activity) {
        AppComponent.Holder.getInstance().getPublicControl().open(activity);
    }

    /**
     * Hook to manually register a plugin source.
     * This API does not update the Hyperion menu retroactively,
     * so clients should call this as early as possible.
     *
     * NOTE: For most users, the default {@link java.util.ServiceLoader} implementation will suffice.
     *
     * @param pluginSource the {@link PluginSource} invoked in place of the default implementation.
     */
    public static void setPlugins(PluginSource pluginSource) {
        AppComponent.Holder.getInstance().getPublicControl().setPluginSource(pluginSource);
    }

    private Hyperion() {
        throw new AssertionError("No instances.");
    }
}