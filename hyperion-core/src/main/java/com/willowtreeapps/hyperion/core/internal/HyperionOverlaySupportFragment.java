package com.willowtreeapps.hyperion.core.internal;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.plugins.v1.OnOverlayViewChangedListener;
import com.willowtreeapps.hyperion.core.plugins.v1.OverlayContainer;

public class HyperionOverlaySupportFragment extends Fragment implements OverlayContainer {

    private HyperionOverlayFragmentHelper helper = new HyperionOverlayFragmentHelper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return helper.getInflatedView(getActivity(), inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        helper.onViewCreated(view);
    }

    @Override
    public void setOverlayView(@NonNull View view) {
        helper.setOverlayView(view);
    }

    @Override
    public void setOverlayView(@LayoutRes int view) {
        helper.setOverlayView(view, LayoutInflater.from(getContext()));
    }

    @Nullable
    @Override
    public View getOverlayView() {
        return helper.getOverlayView();
    }

    @Override
    public boolean removeOverlayView() {
        return helper.removeOverlayView();
    }

    @Override
    public void addOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        helper.listeners.add(listener);
    }

    @Override
    public boolean removeOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        return helper.listeners.remove(listener);
    }
}