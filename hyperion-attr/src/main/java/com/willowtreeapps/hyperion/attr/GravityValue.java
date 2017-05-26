package com.willowtreeapps.hyperion.attr;

import android.annotation.SuppressLint;

import static android.view.Gravity.*;

public class GravityValue {

    private final int gravity;

    public GravityValue(int gravity) {
        this.gravity = gravity;
    }

    int getGravity() {
        return this.gravity;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ((gravity & START) == START) {
            sb.append("Start|");
        }
        if ((gravity & LEFT) == LEFT) {
            sb.append("Left|");
        }
        if ((gravity & END) == END) {
            sb.append("End|");
        }
        if ((gravity & RIGHT) == RIGHT) {
            sb.append("Right|");
        }
        if ((gravity & TOP) == TOP) {
            sb.append("Top|");
        }
        if ((gravity & BOTTOM) == BOTTOM) {
            sb.append("Bottom|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}