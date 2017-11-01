package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.ColorInt;

public class ColorValue {

    private final @ColorInt int color;

    public ColorValue(@ColorInt int color) {
        this.color = color;
    }

    @ColorInt int getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "#" + Integer.toHexString(color);
    }
}