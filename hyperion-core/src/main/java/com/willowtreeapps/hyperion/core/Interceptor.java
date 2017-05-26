package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.willowtreeapps.hyperion.R;

class Interceptor {

    private final Activity activity;
    private OverlayView view;

    Interceptor(Activity activity) {
        this.activity = activity;
    }

    void addOverlay() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        final Window window = activity.getWindow();
        view = (OverlayView) inflater.inflate(R.layout.view_overlay, null);
        window.addContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        view.component.getActivityResults().notifyActivityResult(requestCode, resultCode, data);
    }

}