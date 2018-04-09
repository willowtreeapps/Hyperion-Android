package com.willowtreeapps.hyperion.geigercounter;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.Display;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

class DroppedFrameObserverFactory {

    protected static final int REQUIRED_API_VERSION = JELLY_BEAN;

    @Nullable
    @SuppressLint("NewApi")
    public static DroppedFrameObserver getInstance(AssetManager assetManager, Display display) {
        if (Build.VERSION.SDK_INT < REQUIRED_API_VERSION) {
            return null;
        }

        return new DroppedFrameObserverImpl(assetManager, display);
    }

    public static String getRequiredAPIVersionMessage() {
        return "Geiger Counter requires API level " + REQUIRED_API_VERSION + ".";
    }

}
