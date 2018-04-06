package com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sharedpreferences.R;

public class FloatPreferenceViewHolder extends PreferenceViewHolder<Float> {

    private static final String TAG = "FloatPrefViewHolder";

    private final EditText editTextValue;

    public FloatPreferenceViewHolder(View itemView, SharedPreferences sharedPreferences) {
        super(itemView);
        editTextValue = itemView.findViewById(R.id.hsp_navigation_preference_value);
        editTextValue.setOnEditorActionListener(new EditorListener(sharedPreferences));
    }

    private static Boolean floatEquals(float left, float right) {
        return Math.abs(left - right) < 1e-12;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bind(String preferenceKey, Float preferenceValue) {
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
                float value = Float.parseFloat(v.getText().toString());
                if (!floatEquals(value, sharedPreferences.getFloat(getKey(), 0))) {
                    sharedPreferences.edit()
                            .putFloat(getKey(), value)
                            .apply();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to parse integer", e);
            }

            return true;
        }

    }

}
