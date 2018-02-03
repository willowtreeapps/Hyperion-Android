package com.willowtreeapps.hyperion.plugin.v1;

import android.app.Activity;
import android.support.annotation.NonNull;

public interface ForegroundManager {

    Activity getForegroundActivity();
    boolean isApplicationForeground();

    void addOnApplicationForegroundListener(@NonNull OnApplicationForegroundListener listener);
    boolean removeOnApplicationForegroundListener(@NonNull OnApplicationForegroundListener listener);

    void addOnApplicationBackgroundListener(@NonNull OnApplicationBackgroundListener listener);
    boolean removeOnApplicationBackgroundListener(@NonNull OnApplicationBackgroundListener listener);

}