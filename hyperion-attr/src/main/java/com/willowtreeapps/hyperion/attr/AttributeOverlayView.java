package com.willowtreeapps.hyperion.attr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.willowtreeapps.hyperion.plugin.v1.ExtensionProvider;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;
import com.willowtreeapps.hyperion.plugin.v1.ViewTarget;

class AttributeOverlayView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {

    private final AttributeDetailView detailView;
    private final ViewGroup contentRoot;
    private final ViewTarget target;
    private final MeasurementHelper measurementHelper;
    private final Rect rect = new Rect();
    private final Paint selectionPaint;

    private PopupWindow currentDetailWindow;

    AttributeOverlayView(Context context) {
        super(context);

        detailView = new AttributeDetailView(getContext());
        PluginExtension extension = ExtensionProvider.get(context);
        contentRoot = extension.getContentRoot();
        target = extension.getViewTarget();
        measurementHelper = extension.getMeasurementHelper();

        selectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectionPaint.setColor(ContextCompat.getColor(context, R.color.ha_selection));
        selectionPaint.setStyle(Paint.Style.FILL);

        final View current = target.getTarget();
        if (current != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    setTarget(current);
                    invalidate();
                }
            });
        }

        setWillNotDraw(false);
        setClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnPreDrawListener(this);
        requestFocus();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentDetailWindow != null && currentDetailWindow.isShowing()) {
                currentDetailWindow.dismiss();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rect, selectionPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            View touchTarget = findTarget(contentRoot, x, y);

            View newTarget;
            if (target.getTarget() == touchTarget) {
                newTarget = (View) touchTarget.getParent();
            } else {
                newTarget = touchTarget;
            }

            setTarget(newTarget);
            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private View findTarget(View root, float x, float y) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                measurementHelper.getScreenLocation(child, rect);
                if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                    return findTarget(child, x, y);
                }
            }
        }
        return root;
    }

    private void setTarget(View view) {
        measurementHelper.getScreenLocation(view, rect);

        if (currentDetailWindow != null) {
            currentDetailWindow.dismiss();
        }

        currentDetailWindow = createDetailWindowForView(view);
        PopupWindowCompat.showAsDropDown(currentDetailWindow, view, 0, 24, Gravity.CENTER_HORIZONTAL);

        target.setTarget(view);
    }

    private PopupWindow createDetailWindowForView(View view) {
        final Context context = view.getContext();
        final int width = (int) (getMeasuredWidth() * (4f / 5));
        final int height = getMeasuredHeight() / 2;
        PopupWindow popupWindow = new PopupWindow(detailView, width, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.ha_popup_background)));
        return popupWindow;
    }

    @Override
    public boolean onPreDraw() {
        View current = target.getTarget();
        if (current != null) {
            measurementHelper.getScreenLocation(current, rect);
        }
        return true;
    }
}