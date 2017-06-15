package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.willowtreeapps.hyperion.core.internal.AppComponent;

public final class Hyperion {

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        AppComponent.Holder.getInstance().getLifecycleListener()
                .onActivityResult(activity, requestCode, resultCode, data);
    }

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