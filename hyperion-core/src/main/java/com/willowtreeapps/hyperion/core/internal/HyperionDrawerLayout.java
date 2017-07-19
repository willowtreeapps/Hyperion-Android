package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.willowtreeapps.hyperion.core.R;

public class HyperionDrawerLayout extends DrawerLayout {

    private final @Px int drawerEdge;
    private View drawerView;

    public HyperionDrawerLayout(Context context) {
        this(context, null);
    }

    public HyperionDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HyperionDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        drawerEdge = getResources().getDimensionPixelSize(R.dimen.hype_overlay_edge);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        drawerView = getChildAt(1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return !isTouchingDrawerEdge(event)
                || isDrawerOpen(drawerView)
                || isDrawerVisible(drawerView);
    }

    private boolean isTouchingDrawerEdge(MotionEvent event) {
        return event.getX() < getWidth() - drawerEdge
                && event.getAction() == MotionEvent.ACTION_DOWN;
    }

}