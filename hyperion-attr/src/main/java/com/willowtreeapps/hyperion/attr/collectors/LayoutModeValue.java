package com.willowtreeapps.hyperion.attr.collectors;

import android.view.ViewGroup;

import com.willowtreeapps.hyperion.attr.AttributeValue;

public class LayoutModeValue implements AttributeValue {

    private final int mode;

    public LayoutModeValue(int mode) {
        this.mode = mode;
    }

    @Override
    public CharSequence getDisplayValue() {
        switch (mode) {
            case ViewGroup.LAYOUT_MODE_CLIP_BOUNDS:
                return "CLIP_BOUNDS";
            case ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS:
                return "OPTICAL_BOUNDS";
            default:
                return "UNDEFINED";
        }
    }
}