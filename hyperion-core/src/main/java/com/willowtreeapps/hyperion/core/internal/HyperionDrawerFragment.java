package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.R;

public class HyperionDrawerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final CoreComponent component = AppComponent.Holder.getInstance()
                .getLifecycleListener().getComponent(activity);
        return inflater.cloneInContext(new ComponentContextWrapper(activity, component))
                .inflate(R.layout.hype_fragment_drawer, container, false);
    }

}