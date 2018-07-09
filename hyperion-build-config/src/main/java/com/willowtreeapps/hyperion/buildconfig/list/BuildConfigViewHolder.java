package com.willowtreeapps.hyperion.buildconfig.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.willowtreeapps.hyperion.buildconfig.R;
import com.willowtreeapps.hyperion.buildconfig.model.BuildConfigValue;

class BuildConfigViewHolder extends RecyclerView.ViewHolder {

    private final TextView logDateTextView;
    private final TextView logMsgTextView;

    BuildConfigViewHolder(View itemView) {
        super(itemView);
        logDateTextView = itemView.findViewById(R.id.hbc_name);
        logMsgTextView = itemView.findViewById(R.id.hbc_value);
    }

    void bind(BuildConfigValue buildConfigValue) {
        logDateTextView.setText(buildConfigValue.getName());
        logMsgTextView.setText(buildConfigValue.getValue());
    }

}