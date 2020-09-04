package com.willowtreeapps.hyperion.buildconfig;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.buildconfig.list.BuildConfigListActivity;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class BuildConfigPluginModule extends PluginModule implements View.OnClickListener {

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.hbc_item_plugin, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), BuildConfigListActivity.class);
        v.getContext().startActivity(intent);
    }
}