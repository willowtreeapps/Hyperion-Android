package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.willowtreeapps.hyperion.core.Hyperion;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import static java.util.Collections.emptyList;

public class HyperionInitializer implements Initializer<HyperionInitializer> {
    private static final Boolean DEFAULT_INIT = true;

    @NonNull
    @Override
    public HyperionInitializer create(@NonNull Context context) {
        Hyperion.setApplication(context);

        try {
            if (shouldEnableOnStart(context)) {
                Hyperion.enable();
            }
        } catch (Exception e) {
            Log.e("Hyperion", "Initializer failed.", e);
        }

        Log.d("Hyperion", "Initialized from androidx.startup.InitializationProvider");
        return this;
    }

    private boolean shouldEnableOnStart(Context context) throws PackageManager.NameNotFoundException {
        Bundle metaData = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                .metaData;

        if (metaData == null) return DEFAULT_INIT;

        return metaData.getBoolean(Hyperion.ENABLE_ON_START_METADATA_KEY, DEFAULT_INIT);
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return emptyList();
    }
}