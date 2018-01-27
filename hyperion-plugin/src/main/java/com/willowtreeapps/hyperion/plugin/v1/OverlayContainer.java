package com.willowtreeapps.hyperion.plugin.v1;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface OverlayContainer {

    void setOverlayView(@NonNull View view);

    void setOverlayView(@LayoutRes int view);

    @Nullable
    View getOverlayView();

    boolean removeOverlayView();

    void addOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener);

    boolean removeOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener);

}