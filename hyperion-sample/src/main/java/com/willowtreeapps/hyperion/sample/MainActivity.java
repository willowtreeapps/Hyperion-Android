package com.willowtreeapps.hyperion.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editPath = (EditText) findViewById(R.id.edit_path);
        final EditText editContent = (EditText) findViewById(R.id.edit_content);
        Button button = (Button) findViewById(R.id.file_button);
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
    }
}