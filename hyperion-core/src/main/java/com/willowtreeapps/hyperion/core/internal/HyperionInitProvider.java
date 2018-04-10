package com.willowtreeapps.hyperion.core.internal;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.willowtreeapps.hyperion.core.Hyperion;

public class HyperionInitProvider extends EmptyContentProvider {

    @Override
    public boolean onCreate() {
        try {
            final Context context = requireContext();
            Hyperion.setApplication(context);
            final Application application = (Application) context.getApplicationContext();
            final AppComponent component = AppComponent.Holder.getInstance(context);
            application.registerActivityLifecycleCallbacks(component.getActivityLifecycleCallbacks());
            return true;
        } catch (Exception e) {
            Log.e("Hyperion", "Init failed.", e);
            return false;
        }
    }

    @NonNull
    private Context requireContext() {
        final Context context = getContext();
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        return context;
    }
}