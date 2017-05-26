package com.willowtreeapps.hyperion.core;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.willowtreeapps.hyperion.core.HyperionCoreComponent.CONTENT_CONTAINER;
import static com.willowtreeapps.hyperion.core.HyperionCoreComponent.DRAWER_CONTAINER;

@Module
class HyperionCoreModule {

    private final OverlayView overlayView;

    HyperionCoreModule(OverlayView overlayView) {
        this.overlayView = overlayView;
    }

    @Provides
    OverlayView provideOverlayView() {
        return this.overlayView;
    }

    @Provides
    @ActivityScope
    @Named(CONTENT_CONTAINER)
    ViewGroup provideContentContainer(OverlayView overlayView) {
        return (ViewGroup) overlayView.findViewById(R.id.content_container);
    }

    @Provides
    @ActivityScope
    @Named(DRAWER_CONTAINER)
    ViewGroup provideDrawerContainer(OverlayView overlayView) {
        return (ViewGroup) overlayView.findViewById(R.id.drawer_container);
    }

    @Provides
    @ActivityScope
    RecyclerView provideRecyclerView(OverlayView overlayView) {
        return (RecyclerView) overlayView.findViewById(R.id.drawer_recycler);
    }
}