package com.willowtreeapps.hyperion.core;

import android.app.Activity;

public interface PublicControl {

    boolean open();

    boolean open(Activity activity);

    boolean close();

    boolean close(Activity activity);

    void setShakeGestureSensitivity(float sensitivity);

    float getShakeGestureSensitivity();

    void setPluginSource(PluginSource pluginSource);

    PluginSource getPluginSource();
}