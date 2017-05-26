package com.willowtreeapps.hyperion.core;

import android.content.Context;

public final class HyperionCore {

    static final String COMPONENT = "component";

    public static <T> T getComponent(Context context) {
        //noinspection unchecked, WrongConstant
        return (T) context.getSystemService(COMPONENT);
    }

    private HyperionCore() {
        throw new AssertionError("No instances.");
    }

}