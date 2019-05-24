package com.willowtreeapps.hyperion.sharedpreferences.list;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sharedpreferences.R;
import com.willowtreeapps.hyperion.sharedpreferences.detail.SharedPreferencesDetailActivity;

class SharedPreferencesFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView label;
    private String prefsName;

    SharedPreferencesFileViewHolder(View itemView) {
        super(itemView);
        label = itemView.findViewById(R.id.hsp_pref_name);
        itemView.setOnClickListener(this);
    }

    void bind(String prefsName) {
        this.prefsName = prefsName;
        label.setText(prefsName);
    }

    @Override
    public void onClick(View v) {
        SharedPreferencesDetailActivity.start(v.getContext(), prefsName);
    }
}