package com.willowtreeapps.hyperion.core;

import android.content.Context;
import android.support.annotation.NonNull;

public abstract class Plugin {

    @NonNull
    protected String name() {
        return getClass().getSimpleName();
    }

    public abstract PluginComponent createComponent(@NonNull Context context);
}