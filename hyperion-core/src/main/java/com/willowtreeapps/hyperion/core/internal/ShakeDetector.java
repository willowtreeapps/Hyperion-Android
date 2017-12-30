package com.willowtreeapps.hyperion.core.internal;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Detects a shake to open the side drawer
 */
public class ShakeDetector implements SensorEventListener {

    private static float shakeThresholdGravity = 3.0F;
    private static final int SHAKE_SPACING_TIME_MS = 500;

    private OnShakeListener listener;
    private long shakeTimestamp;

    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    public interface OnShakeListener {
        void onShake();
    }

    public void setShakeGestureSensitivity(float sensitivity) {
        shakeThresholdGravity = sensitivity;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (listener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > shakeThresholdGravity) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (shakeTimestamp + SHAKE_SPACING_TIME_MS > now) {
                    return;
                }

                shakeTimestamp = now;
                listener.onShake();
            }
        }
    }
}