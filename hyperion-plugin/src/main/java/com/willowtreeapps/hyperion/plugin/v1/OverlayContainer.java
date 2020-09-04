package com.willowtreeapps.hyperion.plugin.v1;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

public interface OverlayContainer {

    void setOverlayView(@NonNull View view);

    void setOverlayView(@LayoutRes int layout);

    @Nullable
    View getOverlayView();

    boolean removeOverlayView();

    void addOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener);

    boolean removeOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener);

}