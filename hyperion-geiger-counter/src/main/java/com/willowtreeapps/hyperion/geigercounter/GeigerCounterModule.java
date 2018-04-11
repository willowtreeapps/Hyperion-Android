package com.willowtreeapps.hyperion.geigercounter;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        boolean isEnabled = detector.isEnabled();

        // When enabling while the speaker is muted, remind the user to unmute.
        if (!isEnabled) {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            switch (audioManager.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                case AudioManager.RINGER_MODE_VIBRATE:
                    String message = "Please turn up the sound volume.";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    break;
            }
        }

        detector.setEnabled(!isEnabled);
    }

    // DroppedFrameDetectorObserver

    @Override
    public void droppedFrameDetectorIsEnabledDidChange(DroppedFrameDetector detector, boolean isEnabled) {
        updateView(isEnabled);
    }

}
