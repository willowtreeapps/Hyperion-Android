package com.willowtreeapps.hyperion.core;

import android.content.Intent;
import android.os.Bundle;

public interface ActivityResults {

    void register(Listener listener);

    boolean unregister(Listener listener);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivityForResult(Intent intent, int requestCode, Bundle options);

    interface Listener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}