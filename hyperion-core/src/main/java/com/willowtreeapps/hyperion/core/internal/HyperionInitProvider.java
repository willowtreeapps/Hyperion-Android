package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class HyperionInitProvider extends EmptyContentProvider {

    @Override
    public boolean onCreate() {
        try {
            final Context context = getContext();
            // noinspection ConstantConditions
            final Application application = (Application) context.getApplicationContext();
            final Lifecycle lifecycle = new Lifecycle();
            AppComponent.Holder.init(application, lifecycle);
            application.registerActivityLifecycleCallbacks(lifecycle);
            return true;
        } catch (Exception e) {
            Log.e("Hyperion", "Init failed.", e);
            return false;
        }
    }
}