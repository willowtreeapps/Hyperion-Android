package com.willowtreeapps.hyperion.core;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willowtreeapps.hyperion.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.willowtreeapps.hyperion.core.HyperionCoreComponent.CONTENT_CONTAINER;
import static com.willowtreeapps.hyperion.core.HyperionCoreComponent.DRAWER_CONTAINER;

class OverlayView extends DrawerLayout {

    private final @Px int drawerEdge;

    HyperionCoreComponent component;
    List<Plugin> plugins;

    @Inject
    @Named(CONTENT_CONTAINER)
    ViewGroup contentContainer;

    @Inject
    @Named(DRAWER_CONTAINER)
    ViewGroup drawerContainer;

    @Inject
    RecyclerView drawerRecycler;


    public OverlayView(Context context, AttributeSet attr) {
        super(context, attr);
        drawerEdge = getResources().getDimensionPixelSize(R.dimen.overlay_edge);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode())
            return;
        this.component = DaggerHyperionCoreComponent.builder()
                .activityModule(new ActivityModule((Activity) getContext()))
                .hyperionCoreModule(new HyperionCoreModule(this))
                .build();
        this.component.inject(this);
        this.plugins = Plugins.get();

        drawerRecycler.setHasFixedSize(true);
        drawerRecycler.setAdapter(new PluginAdapter(new ArrayList<>(plugins)));
        drawerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        drawerRecycler.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getX() < getWidth() - drawerEdge
                && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isDrawerOpen(drawerContainer) || isDrawerVisible(drawerContainer)) {
                return true;
            } else{
                return false;
            }
        }

        return true;
    }

    private class PluginAdapter extends RecyclerView.Adapter<PluginViewHolder> {

        private final List<Plugin> plugins;

        private PluginAdapter(List<Plugin> plugins) {
            this.plugins = plugins;
        }

        @Override
        public PluginViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_menu, parent, false);
            return new PluginViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PluginViewHolder holder, int position) {
            holder.bind(plugins.get(position));
        }

        @Override
        public int getItemCount() {
            return plugins.size();
        }
    }

    private class PluginViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final TextView textView;
        private Plugin plugin;

        private PluginViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        void bind(Plugin plugin) {
            this.plugin = plugin;
            textView.setText(plugin.name());
        }

        @Override
        public void onClick(View v) {
            contentContainer.removeAllViews();

            final Context context = new ComponentContextWrapper<>(v.getContext(), component);
            PluginComponent component = plugin.createComponent(context);
            ContentView content = component.getContentView();
            DrawerView drawer = component.getDrawerView();

            if (content != null) {
                contentContainer.addView(content);
            }

            if (drawer != null) {
                drawerContainer.addView(new DrawerWrapperView(context, drawer));
            }
        }
    }
}