package com.willowtreeapps.hyperion.core.internal;

import android.content.Intent;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;

import com.willowtreeapps.hyperion.core.ActivityResults;

import java.util.ArrayList;
import java.util.List;

public class ActivityResultsFragment extends Fragment implements ActivityResults {

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