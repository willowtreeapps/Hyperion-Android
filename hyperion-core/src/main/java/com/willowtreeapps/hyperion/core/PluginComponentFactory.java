package com.willowtreeapps.hyperion.core;

import android.content.Context;
import android.support.annotation.NonNull;

public interface PluginComponentFactory {
    PluginComponent create(@NonNull Context context);
}