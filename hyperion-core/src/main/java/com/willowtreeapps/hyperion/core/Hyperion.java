package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.willowtreeapps.hyperion.core.internal.AppComponent;

public final class Hyperion {

    private static Application application;

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static void setApplication(@NonNull Context context) {
        application = (Application) context.getApplicationContext();
    }

    /**
     * Get the sensitivity threshold of shake detection in G's. Default is 3
     *
     * @return sensitivity Sensitivity of shake detection in G's. Lower is easier to activate.
     */
    public static float getShakeGestureSensitivity() {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPublicControl().getShakeGestureSensitivity();
    }

    /**
     * Set the sensitivity threshold of shake detection in G's. Default is 3
     *
     * @param sensitivity Sensitivity of shake detection in G's. Lower is easier to activate.
     */
    public static void setShakeGestureSensitivity(float sensitivity) {
        requireApplication();
        AppComponent.Holder.getInstance(application).getPublicControl().setShakeGestureSensitivity(sensitivity);
    }

    /**
     * Manually trigger the Hyperion menu embedded in the current foreground {@link Activity}
     **/
    public static void open() {
        requireApplication();
        AppComponent.Holder.getInstance(application).getPublicControl().open();
    }

    /**
     * Manually trigger the Hyperion menu embedded in the given {@link Activity} to open.
     *
     * @param activity the {@link Activity} containing the menu to open.
     */
    public static void open(Activity activity) {
        requireApplication();
        AppComponent.Holder.getInstance(application).getPublicControl().open(activity);
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
        requireApplication();
        AppComponent.Holder.getInstance(application).getPublicControl().setPluginSource(pluginSource);
    }

    /**
     * Get a plugin source.
     * Clients can wrap and delegate the {@link PluginSource}.
     */
    public static PluginSource getPluginSource() {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPublicControl().getPluginSource();
    }

    private static void requireApplication() {
        if (application == null) {
            throw new IllegalStateException("Application has not been set.");
        }
    }

    private Hyperion() {
        throw new AssertionError("No instances.");
    }
}