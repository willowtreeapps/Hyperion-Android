package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.R;
import com.willowtreeapps.hyperion.core.plugins.OnOverlayViewChangedListener;
import com.willowtreeapps.hyperion.core.plugins.OverlayContainer;

import java.util.ArrayList;
import java.util.List;

public class HyperionOverlayFragment extends Fragment implements OverlayContainer {

    private final List<OnOverlayViewChangedListener> listeners = new ArrayList<>();
    private FrameLayout container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final CoreComponent component = AppComponent.Holder.getInstance()
                .getLifecycleListener().getComponent(activity);
        return inflater.cloneInContext(new ComponentContextWrapper(activity, component))
                .inflate(R.layout.hype_fragment_overlay, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        container = (FrameLayout) view.findViewById(R.id.container);
    }

    @Override
    public void setOverlayView(@NonNull View view) {
        container.removeAllViews();
        container.addView(view);
        notifyOverlayViewChanged(view);
    }

    private void notifyOverlayViewChanged(@Nullable View view) {
        for (OnOverlayViewChangedListener listener : listeners) {
            listener.onOverlayViewChanged(view);
        }
    }

    @Override
    public void setOverlayView(@LayoutRes int view) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        container.removeAllViews();
        inflater.inflate(view, container, true);
        View overlayView = container.getChildAt(0);
        notifyOverlayViewChanged(overlayView);
    }

    @Nullable
    @Override
    public View getOverlayView() {
        return container.getChildCount() > 0 ? container.getChildAt(0) : null;
    }

    @Override
    public boolean removeOverlayView() {
        if (container.getChildCount() > 0) {
            container.removeAllViews();
            notifyOverlayViewChanged(null);
            return true;
        }
        return false;
    }

    @Override
    public void addOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean removeOnOverlayViewChangedListener(@NonNull OnOverlayViewChangedListener listener) {
        return listeners.remove(listener);
    }
}