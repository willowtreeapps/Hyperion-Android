package com.willowtreeapps.hyperion.core.internal;

import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Detector for 2 finger double tab gesture to open side drawer
 * Timeout is from first finger down to last finger up (extra time added to prevent false detection)
 * Relies on the class holding this object to send it the touch events
 */
public abstract class TwoFingerDoubleTapDetector {
    private final int TIMEOUT = ViewConfiguration.getDoubleTapTimeout() + 100;
    private long firstDownTime = 0;
    private boolean separateTouches = false;
    private byte twoFingerTapCount = 0;

    private void reset(long time) {
        firstDownTime = time;
        separateTouches = false;
        twoFingerTapCount = 0;
    }

    /**
     * Watches touches and counts 2 finger taps
     * On 2nd 2 finger tap, calls onTwoFingerDoubleTap()
     */
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (firstDownTime == 0 || event.getEventTime() - firstDownTime > TIMEOUT)
                    reset(event.getDownTime());

                //must consume event on down to receive up
                //could interfere with clicking other things
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2)
                    twoFingerTapCount++;
                else
                    firstDownTime = 0;
                break;
            case MotionEvent.ACTION_UP:
                if (!separateTouches)
                    separateTouches = true;
                else if (twoFingerTapCount == 2 && event.getEventTime() - firstDownTime < TIMEOUT) {
                    onTwoFingerDoubleTap();
                    firstDownTime = 0;
                    return true;
                }
        }

        return false;
    }

    public abstract void onTwoFingerDoubleTap();
}
