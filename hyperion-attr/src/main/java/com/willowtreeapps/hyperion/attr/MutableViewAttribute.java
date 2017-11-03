package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;
import android.util.Log;

public abstract class MutableViewAttribute<T> extends ViewAttribute<T> {

    private boolean activated;

    MutableViewAttribute(String key, @NonNull T value) {
        super(key, value);
    }

    void setValue(T value) {
        try {
            mutate(value);
            this.value = value;
        } catch (Exception e) {
            Log.e("Hyperion", "Error mutating view", e);
        }
    }

    protected abstract void mutate(T value) throws Exception;

    boolean isActivated() {
        return this.activated;
    }

    void setActivated(boolean activated) {
        this.activated = activated;
    }
}