package com.willowtreeapps.hyperion.core.plugins;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PluginModule {

    private PluginExtension extension;
    private Context context;

    public final void create(PluginExtension extension, Context context) {
        this.extension = extension;
        this.context = context;
        onCreate();
    }

    protected void onCreate() {

    }

    @Nullable
    public abstract View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent);

    public final void destroy() {
        this.extension = null;
        this.context = null;
        onDestroy();
    }

    protected <T> T getSystemService(String name) {
        //noinspection unchecked
        return (T) context.getSystemService(name);
    }

    protected void onDestroy() {

    }

    @NonNull
    public final PluginExtension getExtension() {
        return this.extension;
    }

    public final Context getContext() {
        return this.context;
    }

}