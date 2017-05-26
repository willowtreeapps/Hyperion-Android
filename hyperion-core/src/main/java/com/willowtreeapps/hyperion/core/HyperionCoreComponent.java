package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import javax.inject.Named;

import dagger.Component;

@ActivityScope
@Component(modules = {
        HyperionCoreModule.class,
        ActivityModule.class
})
public interface HyperionCoreComponent {

    String CONTENT_CONTAINER = "content_container";
    String DRAWER_CONTAINER = "drawer_container";

    void inject(OverlayView view);
    void inject(DrawerWrapperView view);

    @Named(CONTENT_CONTAINER)
    ViewGroup getContentContainer();

    @Named(DRAWER_CONTAINER)
    ViewGroup getDrawerContainer();

    Measurements getMeasurements();

    AttributeTranslator getAttributeTranslator();

    Activity getActivity();

    ActivityResults getActivityResults();

    DisplayMetrics getDisplayMetrics();

    Target getTarget();
}