package com.willowtreeapps.hyperion.geigercounter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.WINDOW_SERVICE;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.view.HapticFeedbackConstants.CONTEXT_CLICK;
import static android.view.HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING;
import static com.willowtreeapps.hyperion.geigercounter.GeigerCounterPlugin.LOG_TAG;

@RequiresApi(GeigerCounterPlugin.API_VERSION)
class DroppedFrameDetector implements Choreographer.FrameCallback {

    private final Context context;

    private final Set<DroppedFrameDetectorObserver> observers;

    // Player for the Geiger counter tick sound
    private final SoundPool soundPool;
    private final int tickSoundID;
    // Sentinel value indicating we failed to load the tick sound
    private static final int NOT_LOADED = -1;

    // Whether we are watching for dropped frames
    private boolean isEnabled;

    // Whether dropped frames play haptic effects in addition to sound
    private boolean areHapticsEnabled;

    // e.g. 0.01666 for a 60 Hz screen
    private final double hardwareFrameIntervalSeconds;

    // Last timestamp received from a frame callback, used to measure the next frame interval
    private long lastTimestampNanoseconds = NEVER;
    // Sentinel value indicating we have not yet received a frame callback since the observer was enabled
    private static final long NEVER = -1;

    DroppedFrameDetector(Context context) {
        this.context = context;

        observers = new HashSet<>();

        soundPool = new SoundPool(1, STREAM_MUSIC, 0);
        int tickSoundID;
        try {
            AssetFileDescriptor tickSoundFileDescriptor = context.getAssets().openFd("sounds/GeigerCounterTick.wav");
            tickSoundID = soundPool.load(tickSoundFileDescriptor, 1);
        } catch (Exception exception) {
            Log.e(LOG_TAG, exception.toString());
            tickSoundID = NOT_LOADED;
        }
        this.tickSoundID = tickSoundID;

        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        double hardwareFramesPerSecond = display.getRefreshRate();
        hardwareFrameIntervalSeconds = 1.0 / hardwareFramesPerSecond;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;

        Choreographer choreographer = Choreographer.getInstance();

        if (isEnabled) {
            choreographer.removeFrameCallback(this);
            choreographer.postFrameCallback(this);
        } else {
            choreographer.removeFrameCallback(this);
            lastTimestampNanoseconds = NEVER;
        }

        for (DroppedFrameDetectorObserver observer : observers) {
            observer.droppedFrameDetectorIsEnabledDidChange(this, isEnabled);
        }
    }

    public boolean areHapticsEnabled() {
        return areHapticsEnabled;
    }

    public void setHapticsEnabled(boolean areHapticsEnabled) {
        this.areHapticsEnabled = areHapticsEnabled;
    }

    public void addObserver(DroppedFrameDetectorObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DroppedFrameDetectorObserver observer) {
        observers.remove(observer);
    }

    // Helpers

    private void playTickSound() {
        if (tickSoundID != NOT_LOADED) {
            soundPool.play(tickSoundID, 1, 1, 1, 0, 1);
        }
    }

    private void playTickHaptics() {
        if (canUseVibrationEffect()) {
            playVibrationEffect();
        } else {
            playHapticFeedback();
        }
    }

    private boolean canUseVibrationEffect() {
        return Build.VERSION_CODES.O <= Build.VERSION.SDK_INT;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void playVibrationEffect() {
        VibrationEffect effect = VibrationEffect.createOneShot(4, 255);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) {
            // Fall back to standard haptic feedback
            playHapticFeedback();
            return;
        }

        vibrator.vibrate(effect);
    }

    private void playHapticFeedback() {
        // Use any observer's view to play haptic feedback.
        for (DroppedFrameDetectorObserver observer : observers) {
            View view = observer.getHostViewForDroppedFrameHapticFeedback();
            if (view != null) {
                view.performHapticFeedback(CONTEXT_CLICK, FLAG_IGNORE_GLOBAL_SETTING);
                break;
            }
        }
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

                if (areHapticsEnabled) {
                    playTickHaptics();
                }
            }
        }

        lastTimestampNanoseconds = timestampNanoseconds;
        Choreographer.getInstance().postFrameCallback(this);
    }

}
