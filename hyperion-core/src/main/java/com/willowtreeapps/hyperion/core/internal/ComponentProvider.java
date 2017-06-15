package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;

final class ComponentProvider {

    static final String COMPONENT = "component";

    public static CoreComponent get(Context context) {
        //noinspection WrongConstant
        return (CoreComponent) context.getSystemService(COMPONENT);
    }

    private ComponentProvider() {
        throw new AssertionError("No instances.");
    }

}