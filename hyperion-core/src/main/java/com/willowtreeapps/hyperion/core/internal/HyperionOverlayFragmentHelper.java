package com.willowtreeapps.hyperion.core.internal;


import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.willowtreeapps.hyperion.core.R;
import com.willowtreeapps.hyperion.core.plugins.OnOverlayViewChangedListener;

import java.util.ArrayList;
import java.util.List;

class HyperionOverlayFragmentHelper {

    final List<OnOverlayViewChangedListener> listeners = new ArrayList<>();
    FrameLayout container;

    void onViewCreated(View view) {
        container = view.findViewById(R.id.container);

        //set margins if needed to fit system windows
        Pair<Integer, Integer> margins = FitWindowHelper.getFitWindowMargins(view.getContext());
        if (!(margins.first == 0 && margins.second == 0) && container.getLayoutParams() != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(container.getLayoutParams());
            lp.setMargins(0, margins.first, 0, margins.second);
            container.setLayoutParams(lp);
        }
    }

    View getInflatedView(Activity activity, LayoutInflater inflater, @Nullable ViewGroup container) {
        final CoreComponent component = AppComponent.Holder.getInstance()
                .getLifecycleListener().getComponent(activity);
        return inflater.cloneInContext(new ComponentContextThemeWrapper(activity, component))
                .inflate(R.layout.hype_fragment_overlay, container, false);
    }

    void setOverlayView(@NonNull View view) {
        container.removeAllViews();
        container.addView(view);
        notifyOverlayViewChanged(view);
    }

    private void notifyOverlayViewChanged(@Nullable View view) {
        for (OnOverlayViewChangedListener listener : listeners) {
            listener.onOverlayViewChanged(view);
        }
    }

    void setOverlayView(@LayoutRes int view, LayoutInflater inflater) {
        container.removeAllViews();
        inflater.inflate(view, container, true);
        View overlayView = container.getChildAt(0);
        notifyOverlayViewChanged(overlayView);
    }

    View getOverlayView() {
        return container.getChildCount() > 0 ? container.getChildAt(0) : null;
    }

    boolean removeOverlayView() {
        if (container.getChildCount() > 0) {
            container.removeAllViews();
            notifyOverlayViewChanged(null);
            return true;
        }
        return false;
    }
}
