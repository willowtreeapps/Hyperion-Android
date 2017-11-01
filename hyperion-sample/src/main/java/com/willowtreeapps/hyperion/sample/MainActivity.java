package com.willowtreeapps.hyperion.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.willowtreeapps.hyperion.core.Hyperion;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editPath = findViewById(R.id.edit_path);
        final EditText editContent = findViewById(R.id.edit_content);
        Button button = findViewById(R.id.file_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                            openFileOutput(editPath.getText().toString(), Context.MODE_PRIVATE));
                    outputStreamWriter.write(editContent.getText().toString());
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        });

        final Switch drawerSwitch = findViewById(R.id.drawer_switch);
        final Switch shakeSwitch = findViewById(R.id.shake_switch);

        drawerSwitch.setChecked(Hyperion.isEmbeddedDrawerEnabled());
        shakeSwitch.setChecked(Hyperion.isShakeGestureEnabled());

        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hyperion.setEmbeddedDrawerEnabled(isChecked);
            }
        });

        shakeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hyperion.setShakeGestureEnabled(isChecked);
            }
        });
    }
}