package com.willowtreeapps.hyperion.attr.collectors;

import android.graphics.Typeface;

import com.willowtreeapps.hyperion.attr.AttributeValue;

public class TypefaceValue implements AttributeValue {

    private final Typeface typeface;

    public TypefaceValue(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public CharSequence getDisplayValue() {
        // TODO sift through assets to get the name.
        return typeface.toString();
    }
}