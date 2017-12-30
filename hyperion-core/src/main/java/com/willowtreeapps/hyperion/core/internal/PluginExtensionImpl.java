package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.ActivityResults;
import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.core.ViewTarget;
import com.willowtreeapps.hyperion.core.plugins.v1.DrawerContainer;
import com.willowtreeapps.hyperion.core.plugins.v1.OverlayContainer;
import com.willowtreeapps.hyperion.core.plugins.v1.PluginExtension;

class PluginExtensionImpl implements PluginExtension {

    private final CoreComponent component;
    private final HyperionPluginView pluginView;

    PluginExtensionImpl(CoreComponent component, HyperionPluginView pluginView) {
        this.component = component;
        this.pluginView = pluginView;
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
    public DrawerContainer getDrawerContainer() {
        return pluginView;
    }

    @NonNull
    @Override
    public ActivityResults getActivityResults() {
        return component.getActivityResults();
    }

    @NonNull
    @Override
    public ViewTarget getViewTarget() {
        return component.getViewTarget();
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
}