package com.willowtreeapps.hyperion.sample;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class PluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        final ImageView imageView = findViewById(R.id.plugin_image);
        final TextView titleTextView = findViewById(R.id.plugin_title);
        final TextView descriptionTextView = findViewById(R.id.plugin_description);
        final View backButton = findViewById(R.id.back_button);

        imageView.setImageResource(getPluginImage());
        titleTextView.setText(getPluginTitle());
        descriptionTextView.setText(getPluginDescription());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @DrawableRes
    protected abstract int getPluginImage();

    @StringRes
    protected abstract int getPluginTitle();

    @StringRes
    protected abstract int getPluginDescription();

}