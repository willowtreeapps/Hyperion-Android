package com.willowtreeapps.hyperion.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface ViewTarget {

    @Nullable
    View getTarget();

    void setTarget(@Nullable View target);

    void registerObserver(@NonNull Observer observer);

    boolean unregisterObserver(@NonNull Observer observer);

    interface Observer {
        void onTargetChanged(View target);
    }

}