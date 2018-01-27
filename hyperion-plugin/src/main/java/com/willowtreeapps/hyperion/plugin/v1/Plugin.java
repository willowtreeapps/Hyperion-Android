package com.willowtreeapps.hyperion.plugin.v1;

import android.support.annotation.NonNull;

public interface Plugin<T extends PluginModule> {

    @NonNull
    T createPluginModule();

}