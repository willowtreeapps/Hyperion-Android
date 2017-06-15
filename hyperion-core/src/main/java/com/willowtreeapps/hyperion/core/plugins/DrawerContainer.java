package com.willowtreeapps.hyperion.core.plugins;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

public interface DrawerContainer {

    void addView(@NonNull View view);

    void addView(@LayoutRes int view);

}