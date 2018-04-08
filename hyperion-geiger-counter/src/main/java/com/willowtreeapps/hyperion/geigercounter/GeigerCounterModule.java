package com.willowtreeapps.hyperion.geigercounter;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class GeigerCounterModule extends PluginModule
        implements View.OnClickListener, Choreographer.FrameCallback {

    // Menu item in the Hyperion plugin list
    private View view;

    private boolean isActive;

    private SoundPool soundPool;
    private int tickSoundID;
    private double hardwareFrameIntervalSeconds;
    private long lastTimestampNanoseconds = -1;

    private void setActive(boolean isActive) {
        view.setSelected(isActive);

        if (isActive) {
            Choreographer.getInstance().removeFrameCallback(this);
            Choreographer.getInstance().postFrameCallback(this);
        } else {
            Choreographer.getInstance().removeFrameCallback(this);
        }

        this.isActive = isActive;
    }

    // PluginModule

    @Override
    protected void onCreate() {
        Activity activity = getExtension().getActivity();

        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        try {
            AssetFileDescriptor afd = activity.getAssets().openFd("sounds/GeigerCounterTick.wav");
            tickSoundID = soundPool.load(afd, 1);
        } catch (Exception exception) {
            Log.e("Geiger Counter", exception.toString());
        }
        hardwareFrameIntervalSeconds = 1.0 / activity.getWindowManager().getDefaultDisplay().getRefreshRate();
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
        setActive(false);
    }

    // OnClickListener

    @Override
    public void onClick(View v) {
        setActive(!isActive);
    }

    // Choreographer.FrameCallback

    @Override
    public void doFrame(long timestampNanoseconds) {
        // Ideally, frame intervals will be exactly 1x the hardware interval,
        // and 2x means you definitely dropped one frame. So 1.5x is our point of comparison.
        double droppedFrameIntervalSeconds = hardwareFrameIntervalSeconds * 1.5;

        long frameIntervalNanoseconds = timestampNanoseconds - lastTimestampNanoseconds;
        if (0 < lastTimestampNanoseconds) {
            // Compare if we have received at least two frame callbacks
            double frameIntervalSeconds = frameIntervalNanoseconds / 1_000_000_000.0;
            if (droppedFrameIntervalSeconds < frameIntervalSeconds) {
                soundPool.play(tickSoundID, 1, 1, 1, 0, 1);

                int frameIntervalMilliseconds = (int) (frameIntervalSeconds * 1000);
                int hardwareFrameIntervalMilliseconds = (int) (hardwareFrameIntervalSeconds * 1000);

                StringBuilder message = new StringBuilder();
                message.append("Dropped frame: ");
                message.append(frameIntervalMilliseconds);
                message.append("ms, out of ");
                message.append(hardwareFrameIntervalMilliseconds);
                message.append("ms");

                Log.d("Geiger Counter", message.toString());
            }
        }

        lastTimestampNanoseconds = timestampNanoseconds;
        Choreographer.getInstance().postFrameCallback(this);
    }
}
