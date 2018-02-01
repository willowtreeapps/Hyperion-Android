package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.willowtreeapps.hyperion.core.internal.AppComponent;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;

public final class Hyperion {

    /**
     * Creates the {@link Plugin} list view found within the embedded drawer. Most
     * clients will rely on the drawer, but in cases where a drawer does not
     * make sense it may be useful to embed this {@link View} in a custom {@link android.app.Dialog}
     * or {@link android.app.Fragment} within the host app.
     *
     * @param activity {@link Activity}
     * @return a View containing all Hyperion plugins on the classpath.
     */
    @NonNull
    public static View createPluginView(Activity activity) {
        return AppComponent.Holder.getInstance().getLifecycle().createPluginView(activity);
    }


    /**
     * Get the sensitivity threshold of shake detection in G's. Default is 3
     *
     * @return sensitivity Sensitivity of shake detection in G's. Lower is easier to activate.
     */
    public static float getShakeGestureSensitivity() {
        return AppComponent.Holder.getInstance().getLifecycle().getShakeGestureSensitivity();
    }

    /**
     * Set the sensitivity threshold of shake detection in G's. Default is 3
     *
     * @param sensitivity Sensitivity of shake detection in G's. Lower is easier to activate.
     */
    public static void setShakeGestureSensitivity(float sensitivity) {
        AppComponent.Holder.getInstance().getLifecycle().setShakeGestureSensitivity(sensitivity);
    }

    /**
     * Manually trigger the Hyperion menu embedded in the current foreground {@link Activity}
     **/
    public static void open() {
        AppComponent.Holder.getInstance().getLifecycle().open();
    }

    /**
     * Manually trigger the Hyperion menu embedded in the given {@link Activity} to open.
     *
     * @param activity the {@link Activity} containing the menu to open.
     */
    public static void open(Activity activity) {
        AppComponent.Holder.getInstance().getLifecycle().open(activity);
    }

    private Hyperion() {
        throw new AssertionError("No instances.");
    }
}