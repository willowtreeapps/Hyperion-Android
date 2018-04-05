package com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sharedpreferences.R;

public class LongPreferenceViewHolder extends PreferenceViewHolder<Long> {

    private static final String TAG = "IntPrefViewHolder";

    private final EditText editTextValue;

    public LongPreferenceViewHolder(View itemView, SharedPreferences sharedPreferences) {
        super(itemView);
        editTextValue = itemView.findViewById(R.id.hsp_navigation_preference_value);
        editTextValue.setOnEditorActionListener(new EditorListener(sharedPreferences));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bind(String preferenceKey, Long preferenceValue) {
        super.bind(preferenceKey, preferenceValue);
        editTextValue.setText(preferenceValue.toString());
    }

    private class EditorListener extends SharedPreferenceEditorListener {

        EditorListener(SharedPreferences sharedPreferences) {
            super(sharedPreferences);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            try {
                long value = Long.parseLong(v.getText().toString());
                if (value != sharedPreferences.getLong(getKey(), 0)) {
                    sharedPreferences.edit()
                            .putLong(getKey(), value)
                            .apply();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to parse integer", e);
            }

            return true;
        }

    }

}
