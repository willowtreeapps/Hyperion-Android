package com.willowtreeapps.hyperion.core.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.R;

public class HyperionMenuLayout extends FrameLayout implements ShakeDetector.OnShakeListener {

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
        setClickable(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //set margins if needed to fit system windows
        Pair<Integer, Integer> margins = FitWindowHelper.getFitWindowMargins(getContext());
        if (!(margins.first == 0 && margins.second == 0) && getLayoutParams() != null) {
            MarginLayoutParams lp = new MarginLayoutParams(getLayoutParams());
            lp.setMargins(0, margins.first, 0, margins.second);
            setLayoutParams(lp);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final View pluginView = getPluginView();
        final MarginLayoutParams params = (MarginLayoutParams) pluginView.getLayoutParams();
        final int offset = getMeasuredWidth() / 3;
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
        if (!isMenuOpen()) {
            // we only open for shake.
            return false;
        }
        final int offset = getMeasuredWidth() / 3;
        final float x = ev.getX();
        // menu is open and the user is pressing the app content area
        return x < offset || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getContentView().animate()
                    .translationX(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            menuOpen = false;
                            setClickable(true);
                        }
                    })
                    .start();
        }
        return true;
    }

    @Override
    public void onShake() {
        final int width = getMeasuredWidth();
        int offset = width - (width / 3);
        getContentView().animate()
                .translationX(-offset)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        menuOpen = true;
                        setClickable(false);
                    }
                })
                .start();
    }

    private View getPluginView() {
        return findViewById(R.id.hyperion_plugins);
    }

    private View getContentView() {
        return getChildAt(1);
    }

    private boolean isMenuOpen() {
        return menuOpen;
    }
}