package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.view.View;

public interface PluginViewFactory {
    View create(Activity activity);
    void destroy(Activity activity);
}
