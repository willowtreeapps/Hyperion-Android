package com.willowtreeapps.hyperion.core.internal;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.MeasurementHelper;

class MeasurementHelperImpl implements MeasurementHelper {

    private static final int[] OUT_LOCATION = new int[2];
    private final DisplayMetrics displayMetrics;
    private final Rect rect = new Rect();

    MeasurementHelperImpl(DisplayMetrics displayMetrics) {
        this.displayMetrics = displayMetrics;
    }

    public void getParentRelativeRect(@NonNull View view, @NonNull Rect rect) {
        rect.left = getRelativeLeft(view);
        rect.top = getRelativeTop(view);
        rect.right = getRelativeRight(view);
        rect.bottom = getRelativeBottom(view);
    }

    public int getRelativeLeft(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getLeft();
        } else {
            return view.getLeft() + getRelativeLeft((View) view.getParent());
        }
    }

    public int getRelativeTop(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        } else {
            return view.getTop() + getRelativeTop((View) view.getParent());
        }
    }

    public int getRelativeRight(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getRight();
        } else {
            return view.getRight() + getRelativeRight((View) view.getParent());
        }
    }

    public int getRelativeBottom(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getBottom();
        } else {
            return view.getBottom() + getRelativeBottom((View) view.getParent());
        }
    }

    public void getScreenLocation(@NonNull View globalView, @NonNull View view, Rect rect) {
        int topOffset = displayMetrics.heightPixels - globalView.getMeasuredHeight();

        view.getLocationOnScreen(OUT_LOCATION);

        OUT_LOCATION[1] -= topOffset;
        rect.left = OUT_LOCATION[0];
        rect.top = OUT_LOCATION[1];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
    }

    @Px
    public int toPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public int toDp(@Px int px) {
        return (int) (px / ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public int toSp(float px) {
        float scaledDensity = displayMetrics.scaledDensity;
        return (int) (px / scaledDensity);
    }

    public View findTarget(View globalView, View root, float x, float y) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                getScreenLocation(globalView, child, rect);
                if (x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
                    return findTarget(globalView, child, x, y);
                }
            }
        }
        return root;
    }

}