package com.willowtreeapps.hyperion.geigercounter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@RequiresApi(GeigerCounterPlugin.API_VERSION)
class GeigerCounterModule extends PluginModule implements View.OnClickListener {

    private View view;
    private DroppedFrameObserver observer;

    GeigerCounterModule(DroppedFrameObserver observer) {
        this.observer = observer;
    }

    // PluginModule

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.hgc_item_plugin, parent, false);
        view.setOnClickListener(this);
        view.setSelected(observer.isEnabled());
        return view;
    }

    // OnClickListener

    @Override
    public void onClick(View v) {
        boolean isObserverEnabled = !observer.isEnabled();
        view.setSelected(isObserverEnabled);
        observer.setEnabled(isObserverEnabled);
    }

}
