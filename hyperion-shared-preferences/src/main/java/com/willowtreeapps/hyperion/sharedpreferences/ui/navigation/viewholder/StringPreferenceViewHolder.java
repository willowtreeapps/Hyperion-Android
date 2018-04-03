package com.willowtreeapps.hyperion.sharedpreferences.ui.navigation.viewholder;

import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sharedpreferences.R;

public class StringPreferenceViewHolder extends PreferenceViewHolder<String> {

    private final EditText editTextValue;

    public StringPreferenceViewHolder(View itemView, SharedPreferences sharedPreferences) {
        super(itemView);
        editTextValue = itemView.findViewById(R.id.hsp_navigation_preference_value);
        editTextValue.setOnEditorActionListener(new EditorListener(sharedPreferences));
    }

    @Override
    public void bind(String preferenceKey, String preferenceValue) {
        super.bind(preferenceKey, preferenceValue);
        editTextValue.setText(preferenceValue);
    }

    private class EditorListener extends SharedPreferenceEditorListener {

        EditorListener(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            String value = v.getText().toString();
            if (!value.equals(sharedPreferences.getString(getKey(), ""))) {
                sharedPreferences.edit()
                        .putString(getKey(), value)
                        .apply();
            }
            return true;
        }

    }

}
