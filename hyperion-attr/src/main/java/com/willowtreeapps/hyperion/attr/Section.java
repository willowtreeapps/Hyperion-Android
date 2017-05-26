package com.willowtreeapps.hyperion.attr;

import java.util.List;

class Section<T> {

    private String name;
    private List<T> list;

    Section(String name, List<T> list) {
        this.name = name;
        this.list = list;
    }

    String getName() {
        return this.name;
    }

    List<T> getList() {
        return this.list;
    }

}