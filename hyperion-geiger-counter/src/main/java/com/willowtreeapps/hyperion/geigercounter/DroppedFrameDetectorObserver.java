package com.willowtreeapps.hyperion.geigercounter;

import androidx.annotation.Nullable;
import android.view.View;

interface DroppedFrameDetectorObserver {

    void droppedFrameDetectorIsEnabledDidChange(DroppedFrameDetector detector, boolean isEnabled);

    @Nullable
    View getHostViewForDroppedFrameHapticFeedback();

}
