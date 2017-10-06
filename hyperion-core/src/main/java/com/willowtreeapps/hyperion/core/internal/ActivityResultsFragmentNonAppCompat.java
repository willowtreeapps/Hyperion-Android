package com.willowtreeapps.hyperion.core.internal;

import android.app.Fragment;
import android.content.Intent;

import com.willowtreeapps.hyperion.core.ActivityResults;

import java.util.ArrayList;
import java.util.List;

public class ActivityResultsFragmentNonAppCompat extends Fragment implements ActivityResults {

    private final List<Listener> listeners = new ArrayList<>();

    public void register(Listener listener) {
        listeners.add(listener);
    }

    public boolean unregister(Listener listener) {
        return listeners.remove(listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Listener listener : listeners) {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }
}