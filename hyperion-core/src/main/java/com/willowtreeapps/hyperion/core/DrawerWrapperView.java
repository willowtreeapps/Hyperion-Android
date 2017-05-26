package com.willowtreeapps.hyperion.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.willowtreeapps.hyperion.R;

import javax.inject.Inject;
import javax.inject.Named;

import static com.willowtreeapps.hyperion.core.HyperionCoreComponent.CONTENT_CONTAINER;
import static com.willowtreeapps.hyperion.core.HyperionCoreComponent.DRAWER_CONTAINER;

@SuppressLint("ViewConstructor")
class DrawerWrapperView extends LinearLayout implements View.OnClickListener {

    @Inject
    @Named(CONTENT_CONTAINER)
    ViewGroup contentContainer;

    @Inject
    @Named(DRAWER_CONTAINER)
    ViewGroup drawerContainer;

    private final DrawerView drawerView;
    private final ImageButton buttonClose;
    private final ViewGroup container;

    public DrawerWrapperView(Context context, DrawerView drawerView) {
        super(context);
        HyperionCore.<HyperionCoreComponent>getComponent(context).inject(this);
        this.drawerView = drawerView;
        inflate(getContext(), R.layout.view_drawer_wrapper, this);
        container = (ViewGroup) findViewById(R.id.container);
        buttonClose = (ImageButton) findViewById(R.id.close);
        container.addView(drawerView);

        buttonClose.setOnClickListener(this);
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
    }

    @Override
    public void onClick(View v) {
        if (!drawerView.onBackPressed()) {
            contentContainer.removeAllViews();
            drawerContainer.removeView(this);
        }
    }
}