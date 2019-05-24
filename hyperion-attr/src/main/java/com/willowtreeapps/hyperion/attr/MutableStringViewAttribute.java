package com.willowtreeapps.hyperion.attr;

import androidx.annotation.NonNull;

public abstract class MutableStringViewAttribute extends MutableViewAttribute<CharSequence> {

    public MutableStringViewAttribute(String key, @NonNull CharSequence value) {
        super(key, value);
    }

    @Override
    public int getViewType() {
        return AttributeDetailView.ITEM_MUTABLE_STRING_ATTRIBUTE;
    }
}