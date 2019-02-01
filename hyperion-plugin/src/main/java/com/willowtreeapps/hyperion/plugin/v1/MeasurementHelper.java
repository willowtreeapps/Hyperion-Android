package com.willowtreeapps.hyperion.plugin.v1;

import android.graphics.Rect;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.view.View;

@MainThread
public interface MeasurementHelper {

    @MainThread
    void getParentRelativeRect(@NonNull View view, @NonNull Rect rect);

    @MainThread
    int getRelativeLeft(@NonNull View view);

    @MainThread
    int getRelativeTop(@NonNull View view);

    @MainThread
    int getRelativeRight(@NonNull View view);

    @MainThread
    int getRelativeBottom(@NonNull View view);

    @MainThread
    void getScreenLocation(@NonNull View view, Rect rect);

    /**
     * Returns the view's coordinates relative to the content root
     * ({@link PluginExtension#getContentRoot()}).
     */
    @MainThread
    void getContentRootLocation(@NonNull View view, Rect rect);

    @Px
    int toPx(int dp);

    int toDp(@Px int px);

    int toSp(float px);

}