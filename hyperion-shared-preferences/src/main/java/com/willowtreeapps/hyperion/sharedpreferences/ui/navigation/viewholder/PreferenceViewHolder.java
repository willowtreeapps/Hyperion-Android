package com.willowtreeapps.hyperion.sharedpreferences.ui.navigation.viewholder;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sharedpreferences.R;

public abstract class PreferenceViewHolder<T> extends RecyclerView.ViewHolder {

    protected final TextView textViewKey;

    PreferenceViewHolder(View itemView) {
        super(itemView);
        textViewKey = itemView.findViewById(R.id.hsp_navigation_preference_key);
    }

    @CallSuper
    public void bind(String preferenceKey, T preferenceValue) {
        textViewKey.setText(preferenceKey);
    }

    protected String getKey() {
        return textViewKey.getText().toString();
    }

}
