package com.willowtreeapps.hyperion.attr.collectors;

import android.graphics.Typeface;

public class TypefaceValue {

    private final Typeface typeface;

    public TypefaceValue(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public String toString() {
        // TODO sift through assets to get the name.
        return typeface.toString();
    }
}