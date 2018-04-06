package com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder;

import android.view.View;
import android.widget.EditText;

import com.willowtreeapps.hyperion.sharedpreferences.R;

import java.util.Arrays;
import java.util.Set;

public class StringSetPreferenceViewHolder extends PreferenceViewHolder<Set<String>> {

    private final EditText editTextValue;

    public StringSetPreferenceViewHolder(View itemView) {
        super(itemView);
        editTextValue = itemView.findViewById(R.id.hsp_navigation_preference_value);
        editTextValue.setEnabled(false);
    }

    @Override
    public void bind(String preferenceKey, Set<String> preferenceValue) {
        super.bind(preferenceKey, preferenceValue);
        String displayValue = Arrays.toString(preferenceValue.toArray());
        editTextValue.setText(displayValue);
    }

}
