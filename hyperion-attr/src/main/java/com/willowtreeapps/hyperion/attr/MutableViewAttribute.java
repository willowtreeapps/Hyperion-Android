package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;

public abstract class MutableViewAttribute<T> extends ViewAttribute<T> {

    private boolean activated;

    MutableViewAttribute(String key, @NonNull T value) {
        super(key, value);
    }

    void setValue(T value) {
        this.value = value;
        mutate(value);
    }

    protected abstract void mutate(T value);

    boolean isActivated() {
        return this.activated;
    }

    void setActivated(boolean activated) {
        this.activated = activated;
    }
}