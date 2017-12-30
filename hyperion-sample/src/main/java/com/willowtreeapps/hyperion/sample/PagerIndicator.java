package com.willowtreeapps.hyperion.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.FrameLayout;

@ViewPager.DecorView
public class PagerIndicator extends FrameLayout {

    private ViewPager pager;

    public PagerIndicator(@NonNull Context context) {
        super(context);
    }

    public PagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final ViewParent parent = getParent();
        if (!(parent instanceof ViewPager)) {
            throw new IllegalStateException(
                    "PagerTitleStrip must be a direct child of a ViewPager.");
        }

        this.pager = (ViewPager) parent;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pager != null) {
            pager = null;
        }
    }


}