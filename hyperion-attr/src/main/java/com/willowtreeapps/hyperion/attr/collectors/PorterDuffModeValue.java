package com.willowtreeapps.hyperion.attr.collectors;

import android.graphics.PorterDuff;
import android.support.annotation.Nullable;

public class PorterDuffModeValue {

    @Nullable
    private final PorterDuff.Mode mode;

    public PorterDuffModeValue(@Nullable PorterDuff.Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        if (mode == null) {
            return "None";
        } else {
            return mode.name();
        }
    }
}
