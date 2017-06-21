package com.willowtreeapps.hyperion.attr;

import android.graphics.Color;
import android.support.annotation.NonNull;

abstract class MutableColorViewAttribute extends MutableViewAttribute<Color> {
    MutableColorViewAttribute(String key, @NonNull Color value) {
        super(key, value);
    }

    @Override
    public int getViewType() {
        return AttributeDetailView.ITEM_MUTABLE_COLOR_ATTRIBUTE;
    }
}