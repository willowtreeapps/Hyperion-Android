package com.willowtreeapps.hyperion.core.plugins.v1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;

public interface PluginExtension {

    @NonNull
    Activity getActivity();

    @NonNull
    ViewGroup getContentRoot();

    @NonNull
    OverlayContainer getOverlayContainer();

    @NonNull
    ActivityResults getActivityResults();

    @NonNull
    ViewTarget getViewTarget();

    @NonNull
    AttributeTranslator getAttributeTranslator();

    @NonNull
    MeasurementHelper getMeasurementHelper();

}