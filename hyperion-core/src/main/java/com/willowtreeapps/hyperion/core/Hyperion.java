package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.willowtreeapps.hyperion.core.internal.AppComponent;

public final class Hyperion {

    private static final int SHAKE = 0;
    private static final int TWOFINGER_DOUBLETAP = 1;

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
     * Lets user specify which activation gesture to use
     * Only using shake for now
     */
    private void setDefaultActivationGesture(int gestureType) {
        //TODO later
        switch (gestureType) {
            case TWOFINGER_DOUBLETAP:
                break;
            default:    //SHAKE
                break;
        }
    }
}