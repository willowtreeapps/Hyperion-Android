package com.willowtreeapps.hyperion.geigercounter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@SuppressLint("NewApi")
class GeigerCounterModule extends PluginModule
        implements View.OnClickListener, Choreographer.FrameCallback {

    private static String LOG_TAG = "Hyperion Geiger Counter";

    // Menu item in the Hyperion plugins list, acting as a toggle button
    private View view;

    // Player for the Geiger counter tick sound
    private SoundPool soundPool;
    private int tickSoundID;

    // Whether we are watching for dropped frames
    private boolean isActive;

    // e.g. 0.01666 for a 60 Hz screen
    private double hardwareFrameIntervalSeconds;

    // Last timestamp received from a frame callback, used to measure the next frame interval
    private long lastTimestampNanoseconds = NEVER;

    // Sentinel value indicating we have not yet received a frame callback since the counter was enabled
    private static long NEVER = -1;

    // Helpers

    private static boolean arePrerequisitesAvailable() {
        return 16 <= Build.VERSION.SDK_INT;
    }

    private void setActive(boolean isActive) {
        if (!arePrerequisitesAvailable()) {
            String message = "Geiger Counter requires SDK version 16.";
            Toast.makeText(getExtension().getActivity(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        this.isActive = isActive;
        view.setSelected(isActive);

        Choreographer choreographer = Choreographer.getInstance();

        if (isActive) {
            choreographer.removeFrameCallback(this);
            choreographer.postFrameCallback(this);
        } else {
            choreographer.removeFrameCallback(this);
            lastTimestampNanoseconds = NEVER;
        }
    }

    private void playTickSound() {
        soundPool.play(tickSoundID, 1, 1, 1, 0, 1);
    }

    private void logDroppedFrame(double frameIntervalSeconds) {
        int frameIntervalMilliseconds = (int) (frameIntervalSeconds * 1000);
        int hardwareFrameIntervalMilliseconds = (int) (hardwareFrameIntervalSeconds * 1000);

        StringBuilder message = new StringBuilder();
        message.append("Dropped frame: ");
        message.append(frameIntervalMilliseconds);
        message.append("ms vs. hardware ");
        message.append(hardwareFrameIntervalMilliseconds);
        message.append("ms");

        Log.i(LOG_TAG, message.toString());
    }

    // PluginModule

    @Override
    protected void onCreate() {
        if (!arePrerequisitesAvailable()) {
            return;
        }

        Activity activity = getExtension().getActivity();

        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        try {
            AssetFileDescriptor afd = activity.getAssets().openFd("sounds/GeigerCounterTick.wav");
            tickSoundID = soundPool.load(afd, 1);
        } catch (Exception exception) {
            Log.e(LOG_TAG, exception.toString());
        }

        double hardwareFramesPerSecond = activity.getWindowManager().getDefaultDisplay().getRefreshRate();
        hardwareFrameIntervalSeconds = 1.0 / hardwareFramesPerSecond;
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
        long frameIntervalNanoseconds = timestampNanoseconds - lastTimestampNanoseconds;

        // To detect a dropped frame, we need to know the interval between two frame callbacks.
        // If this is the first, wait for the second.
        if (lastTimestampNanoseconds != NEVER) {
            // With no dropped frames, frame intervals will roughly equal the hardware interval.
            // 2x the hardware interval means we definitely dropped one frame.
            // So our measuring stick is 1.5x.
            double droppedFrameIntervalSeconds = hardwareFrameIntervalSeconds * 1.5;

            double frameIntervalSeconds = frameIntervalNanoseconds / 1_000_000_000.0;

            if (droppedFrameIntervalSeconds < frameIntervalSeconds) {
                playTickSound();
                logDroppedFrame(frameIntervalSeconds);
            }
        }

        lastTimestampNanoseconds = timestampNanoseconds;
        Choreographer.getInstance().postFrameCallback(this);
    }

}
