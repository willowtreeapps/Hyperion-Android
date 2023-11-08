package com.willowtreeapps.hyperion.plugin.v1;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import android.view.ViewGroup;

public interface PluginExtension {

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    String NOTIFICATION_CHANNEL_ID = "hyperion-activation-channel";

    @NonNull
    Activity getActivity();

    @NonNull
    ViewGroup getContentRoot();

    @NonNull
    OverlayContainer getOverlayContainer();

    @NonNull
    ActivityResults getActivityResults();

    @NonNull
    AttributeTranslator getAttributeTranslator();

    @NonNull
    MeasurementHelper getMeasurementHelper();

    @Nullable
    HyperionMenu getHyperionMenu();

}