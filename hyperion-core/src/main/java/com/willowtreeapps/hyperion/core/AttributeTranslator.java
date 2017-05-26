package com.willowtreeapps.hyperion.core;

import android.support.annotation.Px;

import javax.inject.Inject;

@ActivityScope
public class AttributeTranslator {

    private final Measurements measurements;

    @Inject
    AttributeTranslator(Measurements measurements) {
        this.measurements = measurements;
    }

    public String translateDp(int dp) {
        return dp + " dp, " + measurements.toPx(dp) + " px";
    }

    public String translatePx(@Px int px) {
        return measurements.toDp(px) + " dp, " + px + " px";
    }

    public String translatePxToSp(float px) {
        return measurements.toSp(px) + " sp, " + px + " px";
    }
}