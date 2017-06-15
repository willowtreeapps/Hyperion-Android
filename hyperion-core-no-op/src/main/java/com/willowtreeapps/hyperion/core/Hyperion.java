package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public final class Hyperion {

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        // no-op
    }

    public static View createPluginView(Activity activity) {
        // no-op
        return null;
    }

    public static boolean isEmbeddedDrawerEnabled() {
        // no-op
        return false;
    }

    public static void setEmbeddedDrawerEnabled(boolean enabled) {
        // no-op
    }

}