package com.willowtreeapps.hyperion.plugin.v1;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

public final class ExtensionProvider {

    public static final String NAME = "extension";

    @NonNull
    @SuppressLint("WrongConstant")
    public static PluginExtension get(@NonNull Context context) {
        //noinspection WrongConstant, ConstantConditions
        return (PluginExtension) context.getSystemService(NAME);
    }

    private ExtensionProvider() {
        throw new AssertionError("No instances.");
    }

}