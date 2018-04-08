package com.willowtreeapps.hyperion.geigercounter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;
import android.util.Log;
import android.view.Choreographer;

import static android.media.AudioManager.STREAM_SYSTEM;
import static com.willowtreeapps.hyperion.geigercounter.GeigerCounterPlugin.LOG_TAG;

@SuppressLint("NewApi")
public class DroppedFrameObserver implements Choreographer.FrameCallback {

    // Player for the Geiger counter tick sound
    private SoundPool soundPool;
    private int tickSoundID;

    // Whether we are watching for dropped frames
    private boolean isEnabled;

    // e.g. 0.01666 for a 60 Hz screen
    private double hardwareFrameIntervalSeconds;

    // Last timestamp received from a frame callback, used to measure the next frame interval
    private long lastTimestampNanoseconds = NEVER;

    // Sentinel value indicating we have not yet received a frame callback since the observer was enabled
    private static long NEVER = -1;

    DroppedFrameObserver(Activity activity) {
        soundPool = new SoundPool(1, STREAM_SYSTEM, 0);
        try {
            AssetFileDescriptor afd = activity.getAssets().openFd("sounds/GeigerCounterTick.wav");
            tickSoundID = soundPool.load(afd, 1);
        } catch (Exception exception) {
            Log.e(LOG_TAG, exception.toString());
        }

        double hardwareFramesPerSecond = activity.getWindowManager().getDefaultDisplay().getRefreshRate();
        hardwareFrameIntervalSeconds = 1.0 / hardwareFramesPerSecond;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;

        Choreographer choreographer = Choreographer.getInstance();

        if (isEnabled) {
            choreographer.removeFrameCallback(this);
            choreographer.postFrameCallback(this);
        } else {
            choreographer.removeFrameCallback(this);
            lastTimestampNanoseconds = NEVER;
        }
    }

    // Helpers

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
