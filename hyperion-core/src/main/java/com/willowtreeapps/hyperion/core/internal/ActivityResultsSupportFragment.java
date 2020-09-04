package com.willowtreeapps.hyperion.core.internal;

import android.content.Intent;
import androidx.fragment.app.Fragment;


import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;

import java.util.ArrayList;
import java.util.List;

public class ActivityResultsSupportFragment extends Fragment implements ActivityResults {

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