package com.willowtreeapps.hyperion.plugin.v1;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

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