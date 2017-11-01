package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.List;

public abstract class TypedAttributeCollector<T extends View> {

    private final Class<T> type;

    public TypedAttributeCollector(Class<T> type) {
        this.type = type;
    }

    public final boolean acceptsType(Class<?> type) {
        return this.type.isAssignableFrom(type);
    }

    public final Class<T> getType() {
        return type;
    }

    @NonNull
    public abstract List<ViewAttribute> collect(T view, AttributeTranslator attributeTranslator);

    @NonNull
    protected CharSequence nonNull(@Nullable CharSequence s) {
        return s == null ? "" : s;
    }

}