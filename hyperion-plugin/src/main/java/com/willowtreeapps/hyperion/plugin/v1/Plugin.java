package com.willowtreeapps.hyperion.plugin.v1;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class Plugin {

    private Context context;
    private ApplicationExtension extension;

    public final void create(@NonNull Context context, @NonNull ApplicationExtension extension) {
        this.context = context;
        this.extension = extension;
        onApplicationCreated(context);
    }

    public final boolean deviceMeetsMinimumApiRequirement() {
        return Build.VERSION.SDK_INT >= minimumRequiredApi();
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

    /**
     * Define the minimum required API level for the plugin. The Hyperion minimum supported API is ICS_MR1 (15).
     *
     * @return minimum required api level
     */
    protected int minimumRequiredApi() {
        return Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

}