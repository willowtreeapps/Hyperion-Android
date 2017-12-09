package com.willowtreeapps.hyperion.attr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;
import com.willowtreeapps.hyperion.core.plugins.v1.ExtensionProvider;
import com.willowtreeapps.hyperion.core.plugins.v1.PluginExtension;

public class AttributeOverlayView extends FrameLayout implements ViewTreeObserver.OnPreDrawListener {

    private final BottomSheetBehavior bottomSheetBehavior;
    private final ViewGroup contentRoot;
    private final ViewTarget target;
    private final MeasurementHelper measurementHelper;
    private final Rect rect = new Rect();
    private final Paint selectionPaint;

    AttributeOverlayView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.ha_view_attribute_overlay, this, true);
        AttributeDetailView detailView = findViewById(R.id.bottom_sheet);

        PluginExtension extension = ExtensionProvider.get(context);
        contentRoot = extension.getContentRoot();
        target = extension.getViewTarget();
        measurementHelper = extension.getMeasurementHelper();

        bottomSheetBehavior = BottomSheetBehavior.from(detailView);

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
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnPreDrawListener(this);
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
            View touchTarget = measurementHelper.findTarget(this, contentRoot, x, y);

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

    private void setTarget(View view) {
        target.setTarget(view);

        measurementHelper.getScreenLocation(this, view, rect);
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public boolean onPreDraw() {
        View current = target.getTarget();
        if (current != null) {
            measurementHelper.getScreenLocation(this, current, rect);
        }
        return true;
    }
}