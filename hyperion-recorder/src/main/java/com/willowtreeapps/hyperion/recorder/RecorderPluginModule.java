package com.willowtreeapps.hyperion.recorder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class RecorderPluginModule extends PluginModule implements View.OnClickListener,
        RecordingManager.OnRecordingChangedListener, ActivityResults.Listener {

    private static final String TAG = "HyperionRecorder";
    private static final int REQUEST_CODE = 1000;

    private View view;

    @Override
    protected void onCreate() {
        RecordingManager.addOnRecordingChangedListener(this);
        getExtension().getActivityResults().register(this);
    }

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.hr_item_plugin, parent, false);
        view.setOnClickListener(this);
        view.setSelected(RecordingManager.isRecording());
        View recordingsButton = view.findViewById(R.id.recordings_button);
        recordingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                RecordingsActivity.start(context);
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            view.setEnabled(false);
        }
        this.view = view;
        return view;
    }

    @Override
    protected void onDestroy() {
        RecordingManager.removeOnRecordingChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (RecordingManager.isRecording()) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        try {
            RecordingManager.prepare(getContext());
            RecordingManager.requestStart(getExtension().getActivityResults(), REQUEST_CODE);
        } catch (RecordingException ex) {
            ex.printStackTrace();
            // TODO
        }
    }

    private void stopRecording() {
        try {
            RecordingManager.stop();
        } catch (RecordingException ex) {
            // TODO
        }
    }

    @Override
    public void onRecordingChanged(boolean recording) {
        view.setSelected(recording);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(getContext(),
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            RecordingManager.reset();
            view.setSelected(false);
            return;
        }

        RecordingManager.start(resultCode, data);
    }
}