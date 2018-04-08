package com.willowtreeapps.hyperion.geigercounter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class GeigerCounterModule extends PluginModule
        implements View.OnClickListener {

    private View view;
    private DroppedFrameObserver observer;

    // Helpers

    private static boolean arePrerequisitesAvailable() {
        return 16 <= Build.VERSION.SDK_INT;
    }

    // PluginModule

    @Override
    protected void onCreate() {
        if (!arePrerequisitesAvailable()) {
            return;
        }

        observer = new DroppedFrameObserver(getExtension().getActivity());
    }

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.hgc_item_plugin, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    protected void onDestroy() {
        if (observer != null) {
            observer.setEnabled(false);
        }
    }

    // OnClickListener

    @Override
    public void onClick(View v) {
        if (!arePrerequisitesAvailable()) {
            String message = "Geiger Counter requires SDK version 16.";
            Toast.makeText(getExtension().getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isObserverEnabled = !observer.isEnabled();

        view.setSelected(isObserverEnabled);
        observer.setEnabled(isObserverEnabled);
    }

}
