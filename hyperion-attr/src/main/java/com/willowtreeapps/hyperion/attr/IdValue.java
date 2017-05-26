package com.willowtreeapps.hyperion.attr;

import android.content.res.Resources;
import android.support.annotation.IdRes;

public class IdValue {

    private final Resources resources;
    private final @IdRes int id;

    public IdValue(Resources resources, @IdRes int id) {
        this.resources = resources;
        this.id = id;
    }

    @Override
    public String toString() {
        if (id > 0) {
            return resources.getResourceEntryName(id);
        } else {
            return "";
        }
    }
}