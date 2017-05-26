package com.willowtreeapps.hyperion.core;

import android.content.Context;
import android.content.ContextWrapper;

public class ComponentContextWrapper<T> extends ContextWrapper {

    private final T component;

    public ComponentContextWrapper(Context base, T component) {
        super(base);
        this.component = component;
    }

    @Override
    public Object getSystemService(String name) {
        if (name.equals(HyperionCore.COMPONENT)) {
            return component;
        }
        return super.getSystemService(name);
    }
}