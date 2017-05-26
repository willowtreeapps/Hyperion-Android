package com.willowtreeapps.hyperion.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

public class ContentView extends View {

    public ContentView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ContentView(@NonNull Context context,
                      @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContentView(@NonNull Context context,
                      @Nullable AttributeSet attrs,
                      @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContentView(@NonNull Context context,
                      @Nullable AttributeSet attrs,
                      @AttrRes int defStyleAttr,
                      @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

    }

}