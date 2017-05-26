package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

class LifecycleListener implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Interceptor interceptor = new Interceptor(activity);
        Hyperion.retainInterceptor(activity, interceptor);
        interceptor.addOverlay();
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Hyperion.releaseInterceptor(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}