package com.willowtreeapps.hyperion.timber;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;
import com.willowtreeapps.hyperion.timber.timber.TimberLogTree;

import timber.log.Timber;

@AutoService(Plugin.class)
public class TimberPlugin extends Plugin {

    /**
     * Item buffer for injecting custom logging for projects not using Timber or for non log
     * records.
     */
    public static CircularBuffer<LogItem> logItemBuffer;

    @Override
    protected void onApplicationCreated(@NonNull Context context) {
        super.onApplicationCreated(context);
        int count = context.getResources().getInteger(R.integer.tmb_log_buffer);
        logItemBuffer = new CircularBuffer<>(count);
        Timber.plant(new TimberLogTree(logItemBuffer));
        Timber.i("TimberLogTree planted.");
    }

    @Nullable
    @Override
    public PluginModule createPluginModule() {
        return new TimberPluginModule();
    }

}
