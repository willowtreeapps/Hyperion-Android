package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

final class ActivityUtil {

    @Nullable
    public static Activity findActivity(@NonNull View view) {
        View v = view;
        Activity activity = findActivity(v.getContext());
        while (activity == null) {
            if (!(v.getParent() instanceof View)) {
                return null;
            }
            v = (View) v.getParent();
            activity = findActivity(v.getContext());
        }
        return activity;
    }

    @Nullable
    public static Activity findActivity(@NonNull Context context) {
        Context c = context;
        // assume the context is a FragmentActivity, or a wrapper around a FragmentActivity
        // keep unwrapping the context until you find a FragmentActivity
        while (!(c instanceof Activity)) {
            if (!(c instanceof ContextWrapper)) {
                // no activity at the bottom of the rabbit hole
                return null;
            }
            ContextWrapper contextWrapper = (ContextWrapper) c;
            if (contextWrapper == contextWrapper.getBaseContext()) {
                // found infinite rabbit hole
                return null;
            }
            // deeper down the rabbit hole we go
            c = contextWrapper.getBaseContext();
        }
        return (Activity) c;
    }

    private ActivityUtil() {
        throw new AssertionError("No instances.");
    }

}