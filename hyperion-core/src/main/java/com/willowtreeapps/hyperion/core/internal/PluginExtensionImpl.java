package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;
import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MeasurementHelper;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;

class PluginExtensionImpl implements PluginExtension {

    private final CoreComponent component;

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
        return (ViewGroup) component.getMenuController().getContentView();
    }

    @NonNull
    @Override
    public OverlayContainer getOverlayContainer() {
        return component.getMenuController();
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
        return component.getMenuController();
    }
}