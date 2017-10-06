package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.R;

public class HyperionDrawerFragmentNonAppCompat extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final CoreComponent component = AppComponent.Holder.getInstance()
                .getLifecycleListener().getComponent(activity);
        return inflater.cloneInContext(new ComponentContextThemeWrapper(activity, component))
                .inflate(R.layout.hype_fragment_drawer, container, false);
    }

}