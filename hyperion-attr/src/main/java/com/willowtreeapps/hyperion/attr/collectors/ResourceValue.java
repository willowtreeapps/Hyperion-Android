package com.willowtreeapps.hyperion.attr.collectors;

import android.content.res.Resources;
import androidx.annotation.AnyRes;

import com.willowtreeapps.hyperion.attr.AttributeValue;

public class ResourceValue implements AttributeValue {

    private final Resources resources;
    private final @AnyRes int id;

    public ResourceValue(Resources resources, @AnyRes int id) {
        this.resources = resources;
        this.id = id;
    }

    @Override
    public CharSequence getDisplayValue() {
        if (id > 0) {
            return resources.getResourceEntryName(id);
        } else {
            return "";
        }
    }
}