package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

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
     *
     * @return true if the menu was opened, otherwise false
     **/
    public static boolean open() {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPublicControl().open();
    }

    /**
     * Manually trigger the Hyperion menu embedded in the given {@link Activity} to open.
     *
     * @param activity the {@link Activity} containing the menu to open.
     * @return true if the menu was opened, otherwise false
     */
    public static boolean open(Activity activity) {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPublicControl().open(activity);
    }

    /**
     * Manually trigger closing the Hyperion menu embedded in the current foreground {@link Activity}
     *
     * @return true if the menu was closed, otherwise false
     **/
    public static boolean close() {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPublicControl().close();
    }

    /**
     * Manually trigger the Hyperion menu embedded in the given {@link Activity} to close.
     *
     * @param activity the {@link Activity} containing the menu to close.
     * @return true if the menu was closed, otherwise false
     */
    public static boolean close(Activity activity) {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPublicControl().close(activity);
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

    public static PluginViewFactory getPluginViewFactory() {
        requireApplication();
        return AppComponent.Holder.getInstance(application).getPluginViewFactory();
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