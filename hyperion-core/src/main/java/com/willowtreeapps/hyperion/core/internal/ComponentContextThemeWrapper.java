package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.willowtreeapps.hyperion.core.R;

public class ComponentContextThemeWrapper extends ContextThemeWrapper {

    private final CoreComponent component;

    public ComponentContextThemeWrapper(Context base, CoreComponent component) {
        super(base, R.style.Hype_Base);
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