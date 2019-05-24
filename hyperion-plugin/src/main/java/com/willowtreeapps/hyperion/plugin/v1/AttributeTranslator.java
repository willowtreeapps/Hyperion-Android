package com.willowtreeapps.hyperion.plugin.v1;

import androidx.annotation.Px;

public interface AttributeTranslator {

    String translateDp(int dp);

    String translatePx(@Px int px);

    String translatePxToSp(int px);

}