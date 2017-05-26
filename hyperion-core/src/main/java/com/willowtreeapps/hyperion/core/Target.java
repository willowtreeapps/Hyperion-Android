package com.willowtreeapps.hyperion.core;

import android.support.annotation.Nullable;
import android.view.View;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

@ActivityScope
public class Target {

    private final CopyOnWriteArrayList<Observer> observers = new CopyOnWriteArrayList<>();
    private View current;

    @Inject
    Target() {

    }

    @Nullable
    public View get() {
        return current;
    }

    public void set(View newTarget) {
        current = newTarget;
        notifyObservers(newTarget);
    }

    private void notifyObservers(View newTarget) {
        for (Observer observer : observers) {
            observer.onTargetChanged(newTarget);
        }
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public boolean unregisterObserver(Observer observer) {
        return observers.remove(observer);
    }

    public interface Observer {
        void onTargetChanged(View target);
    }

}