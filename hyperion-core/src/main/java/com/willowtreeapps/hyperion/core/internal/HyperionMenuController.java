package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;

import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.OnMenuStateChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.OnOverlayViewChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates show/hiding the plugin menu, overlay, and scaling the content view.
 */
public class HyperionMenuController implements HyperionMenu, OverlayContainer {

    private static final Interpolator EXPAND_COLLAPSE_INTERPOLATOR = new FastOutSlowInInterpolator();

    private final ViewGroup container;
    private HyperionPluginView pluginView;
    private final View backgroundView;
    private final View foregroundView;
    private final List<OnMenuStateChangedListener> onMenuStateChangedListeners = new ArrayList<>();
    private MenuState menuState = MenuState.CLOSE;
    private final List<OnOverlayViewChangedListener> onOverlayViewChangedListeners = new ArrayList<>();

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final ShakeDetector shakeDetector;

    private Drawable background;

    public HyperionMenuController(ViewGroup container) {
        this.container = container;
        backgroundView = new View(container.getContext());
        backgroundView.setId(R.id.hyperion_background);
        backgroundView.setBackgroundColor(ContextCompat.getColor(container.getContext(), R.color.hype_menu_background));
        foregroundView = new View(container.getContext());
        ViewCompat.setElevation(foregroundView, 10.0f);
        foregroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse();
            }
        });

        sensorManager = (SensorManager) container.getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                collapse();
            }
        });
    }

    public void setPluginView(HyperionPluginView view) {
        if (pluginView != null) {
            pluginView.setOnKeyListener(null);
        }
        pluginView = view;

        pluginView.setFocusable(true);
        pluginView.setFocusableInTouchMode(true);
        pluginView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        collapse();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void expand() {
        if (menuState != MenuState.CLOSE) {
            return;
        }
        setMenuState(MenuState.OPENING);
        View contentView = getContentView();

        int width = contentView.getMeasuredWidth();
        final int menuOffset = (int) ((width * 0.8f) / 3f);

        container.addView(backgroundView, 0,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ViewGroup.MarginLayoutParams pluginParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        MarginLayoutParamsCompat.setMarginStart(pluginParams, menuOffset);
        container.addView(pluginView, pluginParams);
        pluginView.setTranslationX(160.0f);

        container.addView(foregroundView, new ViewGroup.LayoutParams(menuOffset, ViewGroup.LayoutParams.MATCH_PARENT));

        final int offset = width - (width / 3);

        ViewCompat.animate(contentView)
                .translationX(-offset)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .translationZ(10.f)
                .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        setMenuState(MenuState.OPEN);
                        // request focus so we can respond to key presses.
                        pluginView.requestFocus();
                    }
                })
                .start();

        View overlayView = getOverlayView();
        if (overlayView != null) {
            ViewCompat.animate(overlayView)
                    .translationX(-offset)
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .translationZ(10.f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .start();
        }

        ViewCompat.animate(pluginView)
                .withLayer()
                .alpha(1.0f)
                .translationX(0.0f)
                .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                .start();

        // What's the point of this, you might ask?
        // This allows apps who have backgrounds set on the window to look correct when we push their content away.
        // Basically, we "fake" their window background onto our overlay container before
        // animating it out to the left, so it appears the window background has remained with the content.
        // Then we animate in our plugin menu background over top of their ACTUAL window background.
        Activity activity = ActivityUtil.findActivity(container);
        if (activity != null) {
            Window window = activity.getWindow();
            Drawable decorBackground = cloneDrawable(window.getDecorView().getBackground());
            background = contentView.getBackground();
            if (decorBackground != null) {
                ViewCompat.setBackground(contentView, decorBackground);
            }
        }
    }

    @Nullable
    private static Drawable cloneDrawable(@Nullable Drawable drawable) {
        // Safely clone the drawable so the original one doesn't get messed up.
        if (drawable == null) {
            return null;
        }
        Drawable.ConstantState constantState = drawable.getConstantState();
        return constantState != null ? constantState.newDrawable() : drawable.mutate();
    }

    public void collapse() {
        if (menuState != MenuState.OPEN) {
            return;
        }
        setMenuState(MenuState.CLOSING);
        final View contentView = getContentView();

        ViewCompat.animate(contentView)
                .translationX(0)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationZ(0.0f)
                .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        ViewCompat.setBackground(contentView, background);
                        background = null;
                        container.removeView(backgroundView);
                        container.removeView(pluginView);
                        container.removeView(foregroundView);
                        setMenuState(MenuState.CLOSE);
                    }
                })
                .start();

        View overlayView = getOverlayView();
        if (overlayView != null) {
            ViewCompat.animate(overlayView)
                    .translationX(0)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .translationZ(0.0f)
                    .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                    .start();
        }

        ViewCompat.animate(pluginView)
                .withLayer()
                .alpha(0.0f)
                .translationX(160.0f)
                .setInterpolator(EXPAND_COLLAPSE_INTERPOLATOR)
                .start();
    }

    public void onResume() {
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void onPause() {
        sensorManager.unregisterListener(shakeDetector);
    }

    public void setShakeGestureSensitivity(float sensitivity) {
        shakeDetector.setShakeGestureSensitivity(sensitivity);
    }

    public View getContentView() {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            int id = child.getId();
            if (id != R.id.hyperion_background && id != R.id.hyperion_plugins && id != R.id.hyperion_overlay) {
                return child;
            }
        }
        throw new IllegalStateException("Missing content view");
    }

    @Override
    public MenuState getMenuState() {
        return menuState;
    }

    @Override
    public void setMenuState(MenuState menuState) {
        if (this.menuState != menuState) {
            this.menuState = menuState;
            for (OnMenuStateChangedListener listener : onMenuStateChangedListeners) {
                listener.onMenuStateChanged(menuState);
            }
        }
    }

    @Override
    public void addOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener) {
        onMenuStateChangedListeners.add(listener);
    }

    @Override
    public boolean removeOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener) {
        return onMenuStateChangedListeners.remove(listener);
    }

    @Override
    public void setOverlayView(@NonNull View view) {
        removeOverlayView();
        view.setId(R.id.hyperion_overlay);
        // overlay always right above content
        View contentView = getContentView();
        container.addView(view,
                container.indexOfChild(contentView) + 1,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        notifyOverlayViewChanged(view);
    }

    @Override
    public void setOverlayView(@LayoutRes int layout) {
        View overlay = LayoutInflater.from(container.getContext())
                .inflate(layout, container, false);
        setOverlayView(overlay);
    }

    @Nullable
    @Override
    public View getOverlayView() {
        return container.findViewById(R.id.hyperion_overlay);
    }

    @Override
    public boolean removeOverlayView() {
        View overlay = container.findViewById(R.id.hyperion_overlay);
        if (overlay != null) {
            container.removeView(overlay);
            notifyOverlayViewChanged(null);
            return true;
        }
        return false;
    }

    @Override
    public void addOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        onOverlayViewChangedListeners.add(listener);
    }

    @Override
    public boolean removeOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        return onOverlayViewChangedListeners.remove(listener);
    }

    private void notifyOverlayViewChanged(@Nullable View view) {
        for (OnOverlayViewChangedListener listener : onOverlayViewChangedListeners) {
            listener.onOverlayViewChanged(view);
        }
    }
}
