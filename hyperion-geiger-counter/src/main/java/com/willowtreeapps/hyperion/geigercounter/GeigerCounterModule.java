package com.willowtreeapps.hyperion.geigercounter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class GeigerCounterModule extends PluginModule implements View.OnClickListener {

    private View view;

    // Will be null when the API level requirement is not met.
    @Nullable
    private DroppedFrameObserver observer;

    GeigerCounterModule(@Nullable DroppedFrameObserver observer) {
        this.observer = observer;
    }

    // PluginModule

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.hgc_item_plugin, parent, false);
        view.setOnClickListener(this);
        view.setSelected(observer != null && observer.isEnabled());
        return view;
    }

    // OnClickListener

    @Override
    public void onClick(View v) {
        if (observer == null) {
            String message = DroppedFrameObserverFactory.getRequiredAPIVersionMessage();
            Toast.makeText(getExtension().getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isObserverEnabled = !observer.isEnabled();
        view.setSelected(isObserverEnabled);
        observer.setEnabled(isObserverEnabled);
    }

}
