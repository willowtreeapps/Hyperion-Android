package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.OnMenuStateChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.OnOverlayViewChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.OverlayContainer;
import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class AttributeInspectorModule extends PluginModule
        implements View.OnClickListener, OnOverlayViewChangedListener, OnMenuStateChangedListener {

    private static final String OVERLAY_TAG = "attribute_inspector_overlay";
    private OverlayContainer overlay;
    private HyperionMenu menu;
    private View view;

    @Override
    protected void onCreate() {
        final PluginExtension extension = getExtension();
        overlay = extension.getOverlayContainer();
        menu = extension.getHyperionMenu();
        overlay.addOnOverlayViewChangedListener(this);
        menu.addOnMenuStateChangedListener(this);
    }

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.ha_item_plugin, parent, false);
        view.setOnClickListener(this);

        View overlayView = overlay.getOverlayView();
        view.setSelected(overlayView != null && OVERLAY_TAG.equals(overlayView.getTag()));
        this.view = view;
        return view;
    }

    @Override
    protected void onDestroy() {
        overlay.removeOnOverlayViewChangedListener(this);
        menu.removeOnMenuStateChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        final View currentOverlay = overlay.getOverlayView();
        if (currentOverlay == null || !OVERLAY_TAG.equals(currentOverlay.getTag())) {
            View newOverlay = new AttributeOverlayView(getContext());
            newOverlay.setTag(OVERLAY_TAG);
            overlay.setOverlayView(newOverlay);
        } else {
            overlay.removeOverlayView();
        }
    }

    @Override
    public void onOverlayViewChanged(@Nullable View view) {
        this.view.setSelected(view != null && OVERLAY_TAG.equals(view.getTag()));
    }

    @Override
    public void onMenuStateChanged(@NonNull MenuState menuState) {
        if (menuState == MenuState.OPENING) {
            final View currentOverlay = overlay.getOverlayView();
            if (currentOverlay != null && OVERLAY_TAG.equals(currentOverlay.getTag())) {
                final AttributeOverlayView attributeOverlayView = (AttributeOverlayView) currentOverlay;
                attributeOverlayView.dismissPopupIfNeeded();
            }
        }
    }
}