package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.R;

public class HyperionMenuLayout extends FrameLayout implements ShakeDetector.OnShakeListener {

    private static final Interpolator EXPAND_COLLAPSE_INTERPOLATOR = new FastOutSlowInInterpolator();

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final ShakeDetector shakeDetector;

    private boolean menuOpen = false;

    public HyperionMenuLayout(Context context) {
        this(context, null);
    }

    public HyperionMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyperionMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(this);
        setBackgroundColor(ContextCompat.getColor(context, R.color.hype_menu_background));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final View pluginView = getPluginView();
        final MarginLayoutParams params = (MarginLayoutParams) pluginView.getLayoutParams();
        final int offset = (int) ((getMeasuredWidth() * 0.8f) / 3f);
        MarginLayoutParamsCompat.setMarginStart(params, offset);
        pluginView.setLayoutParams(params);
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

    public void setShakeGestureSensitivity(float sensitivity) {
        shakeDetector.setShakeGestureSensitivity(sensitivity);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int offset = getMeasuredWidth() / 3;
        final float x = ev.getX();
        // menu is open and the user is pressing the app content area
        return (x < offset && isMenuOpen()) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ViewCompat.animate(getContentView())
                    .translationX(0)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .translationZ(0.0f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View view) {
                            menuOpen = false;
                        }
                    })
                    .start();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void onShake() {
        if (!isMenuOpen()) {
            final int width = getMeasuredWidth();
            int offset = width - (width / 3);
            ViewCompat.animate(getContentView())
                    .translationX(-offset)
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .translationZ(10.f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View view) {
                            menuOpen = true;
                        }
                    })
                    .start();
        }
    }

    private View getPluginView() {
        return findViewById(R.id.hyperion_plugins);
    }

    private View getOverlayView() {
        return findViewById(R.id.hyperion_overlay);
    }

    private View getContentView() {
        return getChildAt(1);
    }

    private boolean isMenuOpen() {
        return menuOpen;
    }
}