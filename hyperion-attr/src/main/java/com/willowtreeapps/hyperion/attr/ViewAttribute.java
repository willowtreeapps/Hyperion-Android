package com.willowtreeapps.hyperion.attr;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewAttribute<T> implements AttributeDetailItem {

    private final String key;
    protected @Nullable T value;
    private final @Nullable Drawable drawable;

    public ViewAttribute(String key, @Nullable T value) {
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

    @NonNull
    CharSequence getDisplayValue() {
        if (value == null) {
            return "";
        }

        if (value instanceof AttributeValue) {
            return ((AttributeValue) value).getDisplayValue();
        }

        return this.value.toString();
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