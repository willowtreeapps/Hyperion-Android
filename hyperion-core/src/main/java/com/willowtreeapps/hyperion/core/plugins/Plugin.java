package com.willowtreeapps.hyperion.core.plugins;

import android.support.annotation.NonNull;

public interface Plugin<T extends PluginModule> {

    @NonNull
    T createPluginModule();

}