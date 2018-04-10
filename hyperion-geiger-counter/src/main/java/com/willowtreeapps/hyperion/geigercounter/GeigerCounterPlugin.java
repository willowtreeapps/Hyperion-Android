package com.willowtreeapps.hyperion.geigercounter;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

@AutoService(Plugin.class)
@SuppressWarnings("NewApi")
public class GeigerCounterPlugin extends Plugin {

    static final int API_VERSION = JELLY_BEAN;
    static final String LOG_TAG = "Hyperion Geiger Counter";

    private static DroppedFrameObserver observer;

    // Plugin

    @Override
    protected int minimumRequiredApi() {
        return API_VERSION;
    }

    protected void onApplicationCreated(@NonNull Context context) {
        super.onApplicationCreated(context);

        AssetManager assetManager = context.getAssets();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        observer = new DroppedFrameObserver(assetManager, display);
    }

    @Override
    public PluginModule createPluginModule() {
        return new GeigerCounterModule(observer);
    }

}