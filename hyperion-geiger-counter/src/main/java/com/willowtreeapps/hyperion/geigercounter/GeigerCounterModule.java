package com.willowtreeapps.hyperion.geigercounter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@RequiresApi(GeigerCounterPlugin.API_VERSION)
class GeigerCounterModule extends PluginModule implements View.OnClickListener, DroppedFrameDetectorObserver {

    private View view;
    private DroppedFrameDetector detector;

    GeigerCounterModule(DroppedFrameDetector detector) {
        this.detector = detector;
    }

    // Helpers

    private void updateView(boolean detectorIsEnabled) {
        if (view != null) {
            view.setSelected(detectorIsEnabled);
        }
    }

    // PluginModule

    @Override
    protected void onCreate() {
        super.onCreate();

        detector.addObserver(this);
    }

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.hgc_item_plugin, parent, false);
        view.setOnClickListener(this);
        updateView(detector.isEnabled());
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        detector.removeObserver(this);
    }

    // OnClickListener

    @Override
    public void onClick(View v) {
        detector.setEnabled(!detector.isEnabled());
    }

    // DroppedFrameDetectorObserver

    @Override
    public void droppedFrameDetectorIsEnabledDidChange(DroppedFrameDetector detector, boolean isEnabled) {
        updateView(isEnabled);
    }

}
