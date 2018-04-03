package com.willowtreeapps.hyperion.sharedpreferences.ui.navigation.viewholder;

import android.content.SharedPreferences;
import android.widget.TextView;

abstract class SharedPreferenceEditorListener implements TextView.OnEditorActionListener {

    protected final SharedPreferences sharedPreferences;

    SharedPreferenceEditorListener(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

}
