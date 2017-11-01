package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.willowtreeapps.hyperion.core.plugins.v1.ExtensionProvider;
import com.willowtreeapps.hyperion.core.plugins.v1.PluginExtension;

class PluginExtensionContextWrapper extends ContextWrapper {

    @NonNull
    private final PluginExtension extension;
    @Nullable
    private LayoutInflater inflater;

    public PluginExtensionContextWrapper(@NonNull Context base, @NonNull PluginExtension extension) {
        super(base);
        this.extension = extension;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (inflater == null) {
                inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            }
            return inflater;
        }
        if (ExtensionProvider.NAME.equals(name)) {
            return extension;
        }
        return super.getSystemService(name);
    }
}