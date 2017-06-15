package com.willowtreeapps.hyperion.core.plugins;

import android.content.Context;
import android.support.annotation.NonNull;

public final class ExtensionProvider {

    public static final String NAME = "extension";

    @NonNull
    public static PluginExtension get(@NonNull Context context) {
        //noinspection WrongConstant
        return (PluginExtension) context.getSystemService(NAME);
    }

    private ExtensionProvider() {
        throw new AssertionError("No instances.");
    }

}