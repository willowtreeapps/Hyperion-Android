package com.willowtreeapps.hyperion.core;

import android.app.Activity;

public interface PublicControl {

    void open();

    void open(Activity activity);

    void close();

    void close(Activity activity);

    void setShakeGestureSensitivity(float sensitivity);

    float getShakeGestureSensitivity();

    void setPluginSource(PluginSource pluginSource);

    PluginSource getPluginSource();
}