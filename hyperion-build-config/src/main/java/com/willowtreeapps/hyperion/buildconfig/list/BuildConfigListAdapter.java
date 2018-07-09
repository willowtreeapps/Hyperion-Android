package com.willowtreeapps.hyperion.buildconfig.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.buildconfig.R;
import com.willowtreeapps.hyperion.buildconfig.model.BuildConfigValue;

import java.util.ArrayList;
import java.util.List;

class BuildConfigListAdapter extends RecyclerView.Adapter<BuildConfigViewHolder> {

    private final List<BuildConfigValue> buildConfigValues;

    BuildConfigListAdapter(List<BuildConfigValue> buildConfigValues) {
        this.buildConfigValues = new ArrayList<>(buildConfigValues);
    }

    @NonNull
    @Override
    public BuildConfigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hbc_value_row, parent, false);
        return new BuildConfigViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildConfigViewHolder holder, int position) {
        BuildConfigValue buildConfigValue = buildConfigValues.get(position);
        holder.bind(buildConfigValue);
    }

    @Override
    public int getItemCount() {
        return buildConfigValues.size();
    }
}