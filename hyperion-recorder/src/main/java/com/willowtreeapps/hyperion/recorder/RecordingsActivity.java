package com.willowtreeapps.hyperion.recorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

@HyperionIgnore
public class RecordingsActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, RecordingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hr_activity_recordings);
    }
}