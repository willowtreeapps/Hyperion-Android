package com.willowtreeapps.hyperion.core.internal;

interface Callback<T> {
    void call(Try<T> result);
}