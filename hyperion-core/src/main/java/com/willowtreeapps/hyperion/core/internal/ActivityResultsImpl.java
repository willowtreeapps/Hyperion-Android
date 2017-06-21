package com.willowtreeapps.hyperion.core.internal;

import android.content.Intent;

import com.willowtreeapps.hyperion.core.ActivityResults;

import java.util.ArrayList;
import java.util.List;

class ActivityResultsImpl implements ActivityResults {

    private final List<Listener> listeners = new ArrayList<>();

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

}