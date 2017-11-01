package com.willowtreeapps.hyperion.core;

import android.support.annotation.Px;

public interface AttributeTranslator {

    String translateDp(int dp);

    String translatePx(@Px int px);

    String translatePxToSp(int px);

}