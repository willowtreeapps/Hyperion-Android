package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;

import java.util.List;

class Section<T> implements Comparable<Section> {

    private Class<?> type;
    private List<T> list;

    Section(Class<?> type, List<T> list) {
        this.type = type;
        this.list = list;
    }

    String getName() {
        return this.type.getSimpleName();
    }

    List<T> getList() {
        return this.list;
    }

    @Override
    public int compareTo(@NonNull Section o) {
        if (type.isAssignableFrom(o.type)) {
            return 1;
        }
        if (o.type.isAssignableFrom(type)) {
            return -1;
        }
        return 0;
    }
}