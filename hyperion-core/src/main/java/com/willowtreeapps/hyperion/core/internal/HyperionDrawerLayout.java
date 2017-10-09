package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.Px;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.willowtreeapps.hyperion.core.R;

public class HyperionDrawerLayout extends DrawerLayout implements ShakeDetector.OnShakeListener {

    private final @Px int drawerEdge;
    private View drawerView;

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
        drawerEdge = getResources().getDimensionPixelSize(R.dimen.hype_overlay_edge);
        addShakeDetector();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        drawerView = getChildAt(1);

        //set margins if needed to fit system windows
        Pair<Integer, Integer> margins = FitWindowHelper.getFitWindowMargins(getContext());
        if (!(margins.first == 0 && margins.second == 0) && getLayoutParams() != null) {
            MarginLayoutParams lp = new MarginLayoutParams(getLayoutParams());
            lp.setMargins(0, margins.first, 0, margins.second);
            setLayoutParams(lp);
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return !isTouchingDrawerEdge(event)
                || isDrawerOpen(drawerView)
                || isDrawerVisible(drawerView);
    }

    private boolean isTouchingDrawerEdge(MotionEvent event) {
        return event.getX() < getWidth() - drawerEdge
                && event.getAction() == MotionEvent.ACTION_DOWN;
    }

    @Override
    public void onShake() {
        openDrawer(drawerView);
    }
}