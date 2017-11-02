package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.ColorInt;

import com.willowtreeapps.hyperion.attr.AttributeValue;

public class ColorValue implements AttributeValue {

    private final @ColorInt int color;

    public ColorValue(@ColorInt int color) {
        this.color = color;
    }

    @ColorInt int getColor() {
        return this.color;
    }

    @Override
    public CharSequence getDisplayValue() {
        return "#" + Integer.toHexString(color);
    }
}