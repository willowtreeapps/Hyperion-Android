package com.willowtreeapps.hyperion.core.internal;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;

import javax.inject.Inject;

class MeasurementHelperImpl implements MeasurementHelper {

    private static final int[] OUT_LOCATION = new int[2];
    private final DisplayMetrics displayMetrics;
    private ViewGroup container;

    @Inject
    MeasurementHelperImpl(DisplayMetrics displayMetrics, ViewGroup container) {
        this.displayMetrics = displayMetrics;
        this.container = container;
    }

    @Override
    public void getParentRelativeRect(@NonNull View view, @NonNull Rect rect) {
        rect.left = getRelativeLeft(view);
        rect.top = getRelativeTop(view);
        rect.right = getRelativeRight(view);
        rect.bottom = getRelativeBottom(view);
    }

    @Override
    public int getRelativeLeft(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getLeft();
        } else {
            return view.getLeft() + getRelativeLeft((View) view.getParent());
        }
    }

    @Override
    public int getRelativeTop(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        } else {
            return view.getTop() + getRelativeTop((View) view.getParent());
        }
    }

    @Override
    public int getRelativeRight(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getRight();
        } else {
            return view.getRight() + getRelativeRight((View) view.getParent());
        }
    }

    @Override
    public int getRelativeBottom(@NonNull View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getBottom();
        } else {
            return view.getBottom() + getRelativeBottom((View) view.getParent());
        }
    }

    @Override
    public void getScreenLocation(@NonNull View view, Rect rect) {
        view.getLocationOnScreen(OUT_LOCATION);

        rect.left = OUT_LOCATION[0];
        rect.top = OUT_LOCATION[1];
        rect.right = rect.left + view.getMeasuredWidth();
        rect.bottom = rect.top + view.getMeasuredHeight();
    }

    @Override
    public void getContentRootLocation(@NonNull View view, Rect rect) {
        view.getDrawingRect(rect);
        container.offsetDescendantRectToMyCoords(view, rect);
    }

    @Px
    @Override
    public int toPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    @Override
    public int toDp(@Px int px) {
        return (int) (px / ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int toSp(float px) {
        float scaledDensity = displayMetrics.scaledDensity;
        return (int) (px / scaledDensity);
    }

}