package com.willowtreeapps.hyperion.core.internal;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class HyperionDrawerLayout extends FrameLayout
        implements ShakeDetector.OnShakeListener,
        TwoFingerDoubleTapDetector.TwoFingerDoubleTapListener {

    private View drawerView;

    private boolean drawerShown = true;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    private boolean tripleFingerEnabled, twoFingerDoubleEnabled;
    private TwoFingerDoubleTapDetector twoFingerDoubleDetector;

    public HyperionDrawerLayout(Context context) {
        this(context, null);
    }

    public HyperionDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyperionDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAlpha(0.9f);

        //TODO: add support for detector choosing and use shake when none chosen
        addShakeDetector();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        drawerView = getChildAt(1);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sensorManager != null && shakeDetector != null && accelerometer != null) {
            sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sensorManager != null && shakeDetector != null) {
            sensorManager.unregisterListener(shakeDetector);
        }
    }

    public void addShakeDetector() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(this);
    }

    public void addTripleFingerDetector() {
        tripleFingerEnabled = true;
    }

    public void addTwoFingerDoubleTapDetector() {
        if (twoFingerDoubleDetector == null) {
            twoFingerDoubleDetector = new TwoFingerDoubleTapDetector();
            twoFingerDoubleDetector.setListener(this);
        }
        twoFingerDoubleEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouchOutside(event)) {
            //check for gestures if drawer closed
            if (!drawerShown) {
                if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN &&
                        tripleFingerEnabled && event.getPointerCount() >= 3) {
                    openDrawer();
                    return true;
                } else if (twoFingerDoubleEnabled && twoFingerDoubleDetector.onTouchEvent(event)) {
                    return true;
                }
            }

            //drawer open or no gesture detected
            //only act on down to prevent closing drawer after open gesture
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                closeDrawer();
                //must return true here to make tap gestures work
                return super.onTouchEvent(event);
            }
        }

        return isDrawerShown();
    }

    public boolean isDrawerShown() {
        return drawerShown;
    }

    private void closeDrawer() {
        if (drawerShown) {
            drawerShown = false;
            ObjectAnimator.ofFloat(drawerView, TRANSLATION_X,
                    drawerView.getWidth()).setDuration(1000).start();
        }
    }

    private void openDrawer() {
        if (!drawerShown) {
            drawerShown = true;
            ObjectAnimator.ofFloat(drawerView, TRANSLATION_X,
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
    public void onTwoFingerDoubleTap() {
        openDrawer();
    }
}