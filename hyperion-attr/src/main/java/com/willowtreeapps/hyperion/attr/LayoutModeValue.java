package com.willowtreeapps.hyperion.attr;

import android.view.ViewGroup;

public class LayoutModeValue {

    private final int mode;

    public LayoutModeValue(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
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