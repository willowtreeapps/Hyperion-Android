package com.willowtreeapps.hyperion.attr;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ViewAttribute<T> implements AttributeDetailItem {

    private final String key;
    protected @Nullable T value;
    private final @Nullable Drawable drawable;

    public ViewAttribute(String key, @NonNull T value) {
        this.key = key;
        this.value = value;
        this.drawable = null;
    }

    public ViewAttribute(String key, @Nullable Drawable drawable) {
        this.key = key;
        this.value = null;
        this.drawable = drawable;
    }

    public ViewAttribute(String key, @NonNull T value, @Nullable Drawable drawable) {
        this.key = key;
        this.value = value;
        this.drawable = drawable;
    }

    String getKey() {
        return this.key;
    }

    @Nullable
    T getValue() {
        return this.value;
    }

    @Nullable
    Drawable getDrawable() {
        if (this.drawable == null) {
            return null;
        }
        return this.drawable.mutate();
    }

    @Override
    public int getViewType() {
        return AttributeDetailView.ITEM_ATTRIBUTE;
    }
}