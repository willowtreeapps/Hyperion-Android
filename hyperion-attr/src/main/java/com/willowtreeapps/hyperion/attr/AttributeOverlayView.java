package com.willowtreeapps.hyperion.attr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.willowtreeapps.hyperion.plugin.v1.ExtensionProvider;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;

class AttributeOverlayView extends FrameLayout {

    private final ViewGroup contentRoot;
    private final MeasurementHelper measurementHelper;
    private final Rect rect = new Rect();
    private final Paint selectionPaint;

    private View currentView;
    private PopupWindow currentDetailWindow;

    AttributeOverlayView(Context context) {
        super(context);

        PluginExtension extension = ExtensionProvider.get(context);
        contentRoot = extension.getContentRoot();
        measurementHelper = extension.getMeasurementHelper();

        selectionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectionPaint.setColor(ContextCompat.getColor(context, R.color.ha_selection));
        selectionPaint.setStyle(Paint.Style.FILL);

        setWillNotDraw(false);
        setClickable(false);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupIfNeeded();
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
            if (currentView == touchTarget) {
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
        currentView = view;
        measurementHelper.getScreenLocation(view, rect);

        dismissPopupIfNeeded();

        currentDetailWindow = createDetailWindowForView(view);
        PopupWindowCompat.showAsDropDown(currentDetailWindow, view, 0, 24, Gravity.CENTER_HORIZONTAL);
    }

    private PopupWindow createDetailWindowForView(View view) {
        final Context context = getContext();
        final int width = (int) (getMeasuredWidth() * (4f / 5));
        final int height = getMeasuredHeight() / 2;
        final AttributeDetailView detailView = new AttributeDetailView(context);
        detailView.setTarget(view);
        PopupWindow popupWindow = new PopupWindow(detailView, width, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.ha_popup_background)));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        return popupWindow;
    }

    void dismissPopupIfNeeded() {
        if (currentDetailWindow != null && currentDetailWindow.isShowing()) {
            currentDetailWindow.dismiss();
        }
    }
}