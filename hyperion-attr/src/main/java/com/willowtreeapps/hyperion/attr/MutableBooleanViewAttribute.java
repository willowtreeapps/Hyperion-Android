package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;

abstract class MutableBooleanViewAttribute extends MutableViewAttribute<Boolean> {
    MutableBooleanViewAttribute(String key, @NonNull Boolean value) {
        super(key, value);
    }

    boolean getBoolean() {
        Boolean bool = getValue();
        return bool == null ? false : bool;
    }

    @Override
    public int getViewType() {
        return AttributeDetailView.ITEM_MUTABLE_BOOLEAN_ATTRIBUTE;
    }
}