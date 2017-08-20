package com.willowtreeapps.hyperion.core.internal;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class HyperionDrawerLayout extends FrameLayout implements ShakeDetector.OnShakeListener {

    private View drawerView;

    private boolean drawerShown = true;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    public HyperionDrawerLayout(Context context) {
        this(context, null);
    }

    public HyperionDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyperionDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAlpha(0.9f);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        drawerView = getChildAt(1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouchOutside(event)) {
            closeDrawer();
            return super.onTouchEvent(event);
        }

        return isDrawerShown();
    }

    public boolean isDrawerShown() {
        return drawerShown;
    }

    private void closeDrawer() {
        if (drawerShown) {
            drawerShown = false;
            ObjectAnimator.ofFloat(this, TRANSLATION_X,
                    drawerView.getWidth()).setDuration(1000).start();
        }
    }

    private void openDrawer() {
        if (!drawerShown) {
            drawerShown = true;
            ObjectAnimator.ofFloat(this, TRANSLATION_X,
                    0).setDuration(1000).start();
        }
    }

    /**
     * If the tap is outside (to the left) of the drawer
     */
    private boolean isTouchOutside(MotionEvent event) {
        return event.getX() < drawerView.getX();
    }

    public void onShake() {
        openDrawer();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sensorManager.unregisterListener(shakeDetector);
    }
}