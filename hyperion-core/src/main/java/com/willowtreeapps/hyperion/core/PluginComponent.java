package com.willowtreeapps.hyperion.core;

import android.support.annotation.Nullable;

public abstract class PluginComponent {

    @Nullable
    public ContentView getContentView() {
        return null;
    }

    @Nullable
    public DrawerView getDrawerView() {
        return null;
    }

}