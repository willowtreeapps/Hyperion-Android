package com.willowtreeapps.hyperion.sharedpreferences.ui.navigation.viewholder;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.willowtreeapps.hyperion.sharedpreferences.R;

public class BooleanPreferenceViewHolder extends PreferenceViewHolder<Boolean> {

    private final Switch switchValue;

    public BooleanPreferenceViewHolder(View itemView, SharedPreferences sharedPreferences) {
        super(itemView);
        switchValue = itemView.findViewById(R.id.hsp_navigation_preference_value);
        switchValue.setOnCheckedChangeListener(new CheckChangedListener(sharedPreferences));
    }

    @Override
    public void bind(String preferenceKey, Boolean preferenceValue) {
        super.bind(preferenceKey, preferenceValue);
        switchValue.setChecked(preferenceValue);

    }

    private class CheckChangedListener implements CompoundButton.OnCheckedChangeListener {

        private final SharedPreferences sharedPreferences;

        private CheckChangedListener(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (sharedPreferences.getBoolean(getKey(), false) != isChecked) {
                sharedPreferences.edit()
                        .putBoolean(getKey(), isChecked)
                        .apply();
            }
        }
    }

}
