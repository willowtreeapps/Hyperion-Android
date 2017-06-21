package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;

abstract class MutableStringViewAttribute extends MutableViewAttribute<CharSequence> {
    MutableStringViewAttribute(String key, @NonNull CharSequence value) {
        super(key, value);
    }

    @Override
    public int getViewType() {
        return AttributeDetailView.ITEM_MUTABLE_STRING_ATTRIBUTE;
    }
}