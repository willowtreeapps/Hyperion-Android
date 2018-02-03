package com.willowtreeapps.hyperion.plugin.v1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class Plugin {

    private Context context;
    private ApplicationExtension extension;

    public final void create(@NonNull Context context, @NonNull ApplicationExtension extension) {
        this.context = context;
        this.extension = extension;
        onApplicationCreated(context);
    }

    @SuppressWarnings("WeakerAccess")
    protected void onApplicationCreated(@NonNull Context context) {

    }

    @Nullable
    public PluginModule createPluginModule() {
        return null;
    }

    protected Context getContext() {
        return context;
    }

    protected ApplicationExtension getApplicationExtension() {
        return extension;
    }

}