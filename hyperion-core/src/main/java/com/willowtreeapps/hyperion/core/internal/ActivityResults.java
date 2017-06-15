package com.willowtreeapps.hyperion.core.internal;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ActivityScope
public class ActivityResults {

    private final List<Listener> listeners = new ArrayList<>();

    @Inject
    ActivityResults() {

    }

    public void register(Listener listener) {
        listeners.add(listener);
    }

    public boolean unregister(Listener listener) {
        return listeners.remove(listener);
    }

    void notifyActivityResult(int requestCode, int resultCode, Intent data) {
        for (Listener listener : listeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public interface Listener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}