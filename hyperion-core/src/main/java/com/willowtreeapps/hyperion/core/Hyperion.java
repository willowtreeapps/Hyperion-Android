package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.willowtreeapps.hyperion.core.internal.AppComponent;

public final class Hyperion {

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
}