package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.content.ContextWrapper;

public class ComponentContextWrapper extends ContextWrapper {

    private final CoreComponent component;

    public ComponentContextWrapper(Context base, CoreComponent component) {
        super(base);
        this.component = component;
    }

    @Override
    public Object getSystemService(String name) {
        if (name.equals(ComponentProvider.COMPONENT)) {
            return component;
        }
        return super.getSystemService(name);
    }
}