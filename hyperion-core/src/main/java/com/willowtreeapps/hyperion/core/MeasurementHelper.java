package com.willowtreeapps.hyperion.core;

import android.graphics.Rect;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.view.View;

@MainThread
public interface MeasurementHelper {

    void getParentRelativeRect(@NonNull View view, @NonNull Rect rect);

    int getRelativeLeft(@NonNull View view);

    int getRelativeTop(@NonNull View view);

    int getRelativeRight(@NonNull View view);

    int getRelativeBottom(@NonNull View view);

    void getScreenLocation(@NonNull View view, Rect rect);

    @Px
    int toPx(int dp);

    int toDp(@Px int px);

    int toSp(float px);

}