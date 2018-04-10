package com.willowtreeapps.hyperion.plugin.v1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.R;

public abstract class PluginModule {

    private PluginExtension extension;
    private Context context;

    public final void create(PluginExtension extension, Context context) {
        this.extension = extension;
        this.context = context;
        onCreate();
    }

    /**
     * The name of the plugin module. This should be the same name as displayed by the returned plugin view.
     *
     * @return the name of the plugin module
     */
    @StringRes
    public int getName() {
        return R.string.hype_module_name;
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