package com.willowtreeapps.hyperion.core.internal;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.OnMenuStateChangedListener;

import java.util.ArrayList;
import java.util.List;

public class HyperionMenuLayout extends FrameLayout implements ShakeDetector.OnShakeListener, HyperionMenu {

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
    private final List<OnMenuStateChangedListener> listeners = new ArrayList<>(4);

    private HyperionPluginView pluginView;
    private View overlayView;
    private MenuState menuState = MenuState.CLOSE;

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
        setFocusable(true);
        setFocusableInTouchMode(true);
        setId(R.id.hyperion_menu);
        ViewCompat.setImportantForAccessibility(
                this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isMenuOpen()) {
            collapse();
            return true;
        }
        return super.onKeyUp(keyCode, event);
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
            collapse();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onShake() {
        expand();
    }

    public void expand() {
        if (!isMenuOpen()) {
            setMenuState(MenuState.OPENING);
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
                            requestFocus();
                            setMenuState(MenuState.OPEN);
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

    public void collapse() {
        if (isMenuOpen()) {
            setMenuState(MenuState.CLOSING);
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
                            clearFocus();
                            setMenuState(MenuState.CLOSE);
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
    }

    private HyperionPluginView getPluginView() {
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
        return menuState == MenuState.OPEN;
    }

    @Override
    public MenuState getMenuState() {
        return menuState;
    }

    public void setMenuState(MenuState menuState) {
        if (this.menuState != menuState) {
            this.menuState = menuState;
            getPluginView().setMenuState(menuState);
            for (OnMenuStateChangedListener listener : listeners) {
                listener.onMenuStateChanged(menuState);
            }
        }
    }

    @Override
    public void addOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener) {
        return listeners.remove(listener);
    }
}