package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;

class PluginExtensionImpl implements PluginExtension {

    private final CoreComponent component;
    private HyperionMenu menu;

    PluginExtensionImpl(CoreComponent component) {
        this.component = component;
    }

    @NonNull
    @Override
    public Activity getActivity() {
        return component.getActivity();
    }

    @NonNull
    @Override
    public ViewGroup getContentRoot() {
        final HyperionOverlayLayout overlayLayout = getActivity().findViewById(R.id.hyperion_overlay);
        return (ViewGroup) overlayLayout.getChildAt(0);
    }

    @NonNull
    @Override
    public OverlayContainer getOverlayContainer() {
        return component.getOverlayContainer();
    }

    @NonNull
    @Override
    public ActivityResults getActivityResults() {
        return component.getActivityResults();
    }

    @NonNull
    @Override
    public AttributeTranslator getAttributeTranslator() {
        return component.getAttributeTranslator();
    }

    @NonNull
    @Override
    public MeasurementHelper getMeasurementHelper() {
        return component.getMeasurementHelper();
    }

    @Nullable
    @Override
    public HyperionMenu getHyperionMenu() {
        return menu;
    }

    void setHyperionMenu(@Nullable HyperionMenu menu) {
        this.menu = menu;
    }
}