package com.willowtreeapps.hyperion.core.internal;

import javax.inject.Provider;

import dagger.Lazy;

class Cached<T> implements Lazy<T> {

    private final Provider<T> provider;
    private T item;

    Cached(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T get() {
        if (item == null) {
            item = provider.get();
        }
        return item;
    }
}