package com.willowtreeapps.hyperion.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.willowtreeapps.hyperion.timber.TimberPlugin;
import com.willowtreeapps.hyperion.timber.model.Level;
import com.willowtreeapps.hyperion.timber.model.LogItem;

import java.util.Date;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log samples for Timber plugin.
        Timber.wtf("Hello Timber Assert!");
        Timber.d("Hello Timber Debug!");
        Timber.e("Hello Timber Error!");
        Timber.i("Hello Timber Info!");
        Timber.v("Hello Timber Verbose!");
        Timber.w("Hello Timber Warn!");

        TimberPlugin.logItemBuffer
                .enqueue(new LogItem(Level.DEBUG, new Date(), "I'm a custom message!"));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new PagerFragment())
                    .commit();
        }
    }
}