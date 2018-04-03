package com.willowtreeapps.hyperion.sharedpreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

@HyperionIgnore
public class SharedPreferencesViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsp_activity_shared_preferences_viewer);
    }

}
