package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.willowtreeapps.hyperion.core.internal.AppComponent;
import com.willowtreeapps.hyperion.core.plugins.Plugin;

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
     * Embedded drawer is enabled by default.
     *
     * @see #setEmbeddedDrawerEnabled(boolean)
     * @return true if the embedded drawer is enabled, false otherwise.
     */
    public static boolean isEmbeddedDrawerEnabled() {
        return AppComponent.Holder.getInstance().getLifecycle().isEmbeddedDrawerEnabled();
    }

    /**
     * Embedded drawer is enabled by default.
     *
     * @see #isEmbeddedDrawerEnabled()
     * @param enabled true if enabling, false otherwise.
     */
    public static void setEmbeddedDrawerEnabled(boolean enabled) {
        AppComponent.Holder.getInstance().getLifecycle().setEmbeddedDrawerEnabled(enabled);
    }

    /**
     * Shake gesture is disabled by default.
     *
     * @see #setShakeGestureEnabled(boolean)
     * @return true if the shake to open gesture is enabled, false otherwise.
     */
    public static boolean isShakeGestureEnabled() {
        return AppComponent.Holder.getInstance().getLifecycle().isShakeGestureEnabled();
    }

    /**
     * Shake gesture is disabled by default.
     *
     * @see #isShakeGestureEnabled()
     * @param enabled true if enabling, false otherwise.
     */
    public static void setShakeGestureEnabled(boolean enabled) {
        AppComponent.Holder.getInstance().getLifecycle().setShakeGestureEnabled(enabled);
    }

    private Hyperion() {
        throw new AssertionError("No instances.");
    }
}