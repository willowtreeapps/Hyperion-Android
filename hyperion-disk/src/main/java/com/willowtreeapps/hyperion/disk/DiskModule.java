package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class DiskModule extends PluginModule implements View.OnClickListener {

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.hd_item_plugin, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        FileExplorerActivity.start(context);
    }
}