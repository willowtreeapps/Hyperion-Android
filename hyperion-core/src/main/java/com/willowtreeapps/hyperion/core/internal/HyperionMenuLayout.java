package com.willowtreeapps.hyperion.core.internal;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.R;

public class HyperionMenuLayout extends FrameLayout implements ShakeDetector.OnShakeListener {

    private static final Interpolator EXPAND_COLLAPSE_INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final Property<HyperionMenuLayout, Integer> BACKGROUND_ALPHA =
            new IntProperty<HyperionMenuLayout>("BackgroundAlpha") {
        @Override
        public void setValue(HyperionMenuLayout object, int value) {
            object.getBackground().setAlpha(value);
        }

        @Override
        public Integer get(HyperionMenuLayout object) {
            return DrawableCompat.getAlpha(object.getBackground());
        }
    };

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final ShakeDetector shakeDetector;

    private View pluginView;
    private View overlayView;
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
        getBackground().setAlpha(0);
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
        final int offset = (int) ((getMeasuredWidth() * 0.8f) / 3f);
        final float x = ev.getX();
        // menu is open and the user is pressing the app content area
        return (x < offset && isMenuOpen()) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final View pluginView = getPluginView();
            final View overlayView = getOverlayView();
            ViewCompat.animate(overlayView)
                    .translationX(0)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .translationZ(0.0f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View view) {
                            ViewCompat.setBackground(overlayView, null);
                            menuOpen = false;
                        }
                    })
                    .start();
            ViewCompat.animate(pluginView)
                    .withLayer()
                    .alpha(0.0f)
                    .translationX(160.0f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .start();
            ObjectAnimator alpha = ObjectAnimator.ofInt(this, BACKGROUND_ALPHA, 0);
            alpha.setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR);
            alpha.start();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return super.onApplyWindowInsets(insets);
    }

    @Override
    public void onShake() {
        expand();
    }

    public void expand() {
        if (!isMenuOpen()) {
            final View pluginView = getPluginView();
            final View overlayView = getOverlayView();
            pluginView.setTranslationX(160.0f);
            final int width = getMeasuredWidth();
            int offset = width - (width / 3);
            ViewCompat.animate(overlayView)
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
            ViewCompat.animate(pluginView)
                    .withLayer()
                    .alpha(1.0f)
                    .translationX(0.0f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .start();
            ObjectAnimator alpha = ObjectAnimator.ofInt(this, BACKGROUND_ALPHA, 255);
            alpha.setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR);
            alpha.start();

            // What's the point of this, you might ask?
            // This allows apps who have backgrounds set on the window to look correct when we push their content away.
            // Basically, we "fake" their window background onto our overlay container before
            // animating it out to the left, so it appears the window background has remained with the content.
            // Then we animate in our plugin menu background over top of their ACTUAL window background.
            Activity activity = ActivityUtil.findActivity(this);
            if (activity != null) {
                Window window = activity.getWindow();
                View decor = window.getDecorView();
                ViewCompat.setBackground(overlayView, decor.getBackground());
            }
        }
    }

    private View getPluginView() {
        if (pluginView == null) {
            pluginView = findViewById(R.id.hyperion_plugins);
        }
        return pluginView;
    }

    private View getOverlayView() {
        if (overlayView == null) {
            overlayView = findViewById(R.id.hyperion_overlay);
        }
        return overlayView;
    }

    private boolean isMenuOpen() {
        return menuOpen;
    }
}