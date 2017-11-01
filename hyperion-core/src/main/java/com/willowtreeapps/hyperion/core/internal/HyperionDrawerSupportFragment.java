package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.R;

public class HyperionDrawerSupportFragment extends Fragment implements HyperionDrawer {

    private HyperionDrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final CoreComponent component = AppComponent.Holder.getInstance()
                .getLifecycle().getComponent(activity);
        return inflater.cloneInContext(new ComponentContextThemeWrapper(activity, component))
                .inflate(R.layout.hype_fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        drawer = view.findViewById(R.id.drawer);
    }

    @Override
    public HyperionDrawerLayout getLayout() {
        return drawer;
    }
}