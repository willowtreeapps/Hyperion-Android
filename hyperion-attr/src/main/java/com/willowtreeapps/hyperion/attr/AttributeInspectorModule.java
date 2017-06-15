package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.willowtreeapps.hyperion.core.plugins.OnOverlayViewChangedListener;
import com.willowtreeapps.hyperion.core.plugins.OverlayContainer;
import com.willowtreeapps.hyperion.core.plugins.PluginModule;

class AttributeInspectorModule extends PluginModule
        implements View.OnClickListener, OnOverlayViewChangedListener {

    private static final String OVERLAY_TAG = "attribute_inspector_overlay";
    private OverlayContainer overlay;
    private CheckBox checkBox;

    @Override
    protected void onCreate() {
        overlay = getExtension().getOverlayContainer();
        overlay.addOnOverlayViewChangedListener(this);
    }

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.ha_item_plugin, parent, false);
        view.setOnClickListener(this);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);

        View overlayView = overlay.getOverlayView();
        checkBox.setChecked(overlayView != null && OVERLAY_TAG.equals(overlayView.getTag()));
        return view;
    }

    @Override
    protected void onDestroy() {
        overlay.removeOnOverlayViewChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        View currentOverlay = overlay.getOverlayView();
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
        checkBox.setChecked(view != null && OVERLAY_TAG.equals(view.getTag()));
    }
}