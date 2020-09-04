package com.willowtreeapps.hyperion.attr.collectors;

import android.graphics.PorterDuff;
import androidx.annotation.Nullable;

import com.willowtreeapps.hyperion.attr.AttributeValue;

public class PorterDuffModeValue implements AttributeValue {

    @Nullable
    private final PorterDuff.Mode mode;

    public PorterDuffModeValue(@Nullable PorterDuff.Mode mode) {
        this.mode = mode;
    }

    @Override
    public CharSequence getDisplayValue() {
        if (mode == null) {
            return "None";
        } else {
            return mode.name();
        }
    }
}
