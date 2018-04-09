package com.willowtreeapps.hyperion.geigercounter;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class GeigerCounterPlugin extends Plugin {

    static final String LOG_TAG = "Hyperion Geiger Counter";

    @Nullable
    private static DroppedFrameObserver observer;

    // Plugin

    protected void onApplicationCreated(@NonNull Context context) {
        super.onApplicationCreated(context);

        AssetManager assetManager = context.getAssets();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        observer = DroppedFrameObserverFactory.getInstance(assetManager, display);
    }

    @Override
    public PluginModule createPluginModule() {
        return new GeigerCounterModule(observer);
    }

}