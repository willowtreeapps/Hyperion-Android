package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.willowtreeapps.hyperion.core.internal.AppComponent;

public final class Hyperion {

    /**
     * Bit flags for activating Hyperion drawer
     */
    public static final int SHAKE = 0;
    public static final int TWOFINGER_DOUBLETAP = 1;

    @NonNull
    public static View createPluginView(Activity activity) {
        return AppComponent.Holder.getInstance().getLifecycleListener().createPluginView(activity);
    }

    public static boolean isEmbeddedDrawerEnabled() {
        return AppComponent.Holder.getInstance().getLifecycleListener().isEmbeddedDrawerEnabled();
    }

    public static void setEmbeddedDrawerEnabled(boolean enabled) {
        AppComponent.Holder.getInstance().getLifecycleListener().setEmbeddedDrawerEnabled(enabled);
    }

    private Hyperion() {
        throw new AssertionError("No instances.");
    }

    /**
     * Lets user specify which activation gesture(s) to use
     * Combine multiple options with |
     * Ex. "SHAKE|TWOFINGER_DOUBLETAP"
     *
     * Currently not working and shake hard coded in HyperionDrawerLayout
     */
    private void setDefaultActivationGesture(int gestureTypes) {
        //TODO later
        if ((gestureTypes & SHAKE) == SHAKE) {
            //TODO: activate shake detector
        }

        if ((gestureTypes & TWOFINGER_DOUBLETAP) == TWOFINGER_DOUBLETAP) {
            //TODO: activate two finger double tap detector
        }
    }
}