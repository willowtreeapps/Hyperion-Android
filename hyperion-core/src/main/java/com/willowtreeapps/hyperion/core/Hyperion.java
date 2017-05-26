package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.SimpleArrayMap;

public final class Hyperion {

    private static final SimpleArrayMap<Activity, Interceptor> interceptors = new SimpleArrayMap<>();

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        interceptors.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    static void retainInterceptor(Activity activity, Interceptor interceptor) {
        interceptors.put(activity, interceptor);
    }

    static void releaseInterceptor(Activity activity) {
        interceptors.remove(activity);
    }

    private Hyperion() {
        throw new AssertionError("No instances.");
    }
}