package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.view.View;

@SuppressWarnings("unused")
public final class Hyperion {

    public static View createPluginView(Activity activity) {
        // no-op
        return null;
    }

    public static float getShakeGestureSensitivity() {
        return Float.MIN_VALUE;
    }

    public static void setShakeGestureSensitivity(float sensitivity) {
        // no-op
    }

    public static void open() {
        // no-op
    }

    public static void open(Activity activity) {
        // no-op
    }

    public static void setPluginSource(Object... pluginSource) {
        // no-op
    }
}