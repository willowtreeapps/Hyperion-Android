package com.willowtreeapps.hyperion.core.internal;

import androidx.annotation.Px;

import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;

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

    public String translatePxToSp(int px) {
        return measurementHelper.toSp(px) + " sp, " + px + " px";
    }

}