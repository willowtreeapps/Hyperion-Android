package com.willowtreeapps.hyperion.core.internal;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;

import javax.inject.Inject;

class MeasurementHelperImpl implements MeasurementHelper {

    private static final int[] OUT_LOCATION = new int[2];
    private final DisplayMetrics displayMetrics;

    @Inject
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

    public void getScreenLocation(@NonNull View view, Rect rect) {
        view.getLocationOnScreen(OUT_LOCATION);

        rect.left = OUT_LOCATION[0];
        rect.top = OUT_LOCATION[1];
        rect.right = rect.left + view.getMeasuredWidth();
        rect.bottom = rect.top + view.getMeasuredHeight();
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

}