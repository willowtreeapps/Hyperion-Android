package com.willowtreeapps.hyperion.attr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.ContentView;
import com.willowtreeapps.hyperion.core.HyperionCore;
import com.willowtreeapps.hyperion.core.Measurements;
import com.willowtreeapps.hyperion.core.Target;

import javax.inject.Inject;

public class AttributeInspectorView extends ContentView {

    private final Rect rect = new Rect();

    @Inject
    Activity activity;
    @Inject
    Target target;
    @Inject
    Measurements measurements;

    private final Paint red;

    AttributeInspectorView(Context context) {
        super(context);
        HyperionCore.<AttributeInspectorComponent>getComponent(context).inject(this);
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(Color.GREEN);
        red.setAlpha(120);
        red.setStyle(Paint.Style.FILL);

        View current = target.get();
        if (current != null) {
            measurements.getScreenLocation(this, current, rect);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rect, red);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            View root = activity.findViewById(android.R.id.content);
            View touchTarget = findTarget(root, x, y);

            View newTarget;
            if (target.get() == touchTarget) {
                newTarget = (View) touchTarget.getParent();
            } else {
                newTarget = touchTarget;
            }

            target.set(newTarget);

            measurements.getScreenLocation(this, newTarget, rect);
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
                measurements.getScreenLocation(this, child, rect);
                if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                    return findTarget(child, x, y);
                }
            }
        }
        return root;
    }
}