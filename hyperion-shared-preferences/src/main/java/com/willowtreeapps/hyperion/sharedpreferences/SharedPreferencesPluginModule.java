package com.willowtreeapps.hyperion.sharedpreferences;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;
import com.willowtreeapps.hyperion.sharedpreferences.list.SharedPreferencesListActivity;

class SharedPreferencesPluginModule extends PluginModule implements View.OnClickListener {

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.hsp_item_plugin, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), SharedPreferencesListActivity.class);
        v.getContext().startActivity(intent);
    }
}