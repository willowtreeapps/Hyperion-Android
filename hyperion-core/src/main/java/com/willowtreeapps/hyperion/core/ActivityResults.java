package com.willowtreeapps.hyperion.core;

import android.content.Intent;

public interface ActivityResults {

    void register(Listener listener);

    boolean unregister(Listener listener);

    interface Listener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}