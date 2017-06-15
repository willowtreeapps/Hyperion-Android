package com.willowtreeapps.hyperion.core.internal;

import android.support.annotation.Px;

import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;

import javax.inject.Inject;

class AttributeTranslatorImpl implements AttributeTranslator {

    private final MeasurementHelper measurementHelper;

    @Inject
    AttributeTranslatorImpl(MeasurementHelper measurementHelper) {
        this.measurementHelper = measurementHelper;
    }

    public String translateDp(int dp) {
        return dp + " dp, " + measurementHelper.toPx(dp) + " px";
    }

    public String translatePx(@Px int px) {
        return measurementHelper.toDp(px) + " dp, " + px + " px";
    }

    public String translatePxToSp(float px) {
        return measurementHelper.toSp(px) + " sp, " + px + " px";
    }

}
