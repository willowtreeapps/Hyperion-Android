package com.willowtreeapps.hyperion.core.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.willowtreeapps.hyperion.plugin.v1.ViewTarget;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

class ViewTargetImpl implements ViewTarget {

    private final CopyOnWriteArrayList<Observer> observers = new CopyOnWriteArrayList<>();
    private View current;

    @Inject
    ViewTargetImpl() {

    }

    @Nullable
    @Override
    public View getTarget() {
        return current;
    }

    @Override
    public void setTarget(@NonNull View target) {
        current = target;
        notifyObservers(target);
    }

    private void notifyObservers(View newTarget) {
        for (Observer observer : observers) {
            observer.onTargetChanged(newTarget);
        }
    }

    @Override
    public void registerObserver(@NonNull Observer observer) {
        observers.add(observer);
    }

    @Override
    public boolean unregisterObserver(@NonNull Observer observer) {
        return observers.remove(observer);
    }

}