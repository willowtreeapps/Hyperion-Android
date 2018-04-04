package com.willowtreeapps.hyperion.sample;

import android.app.Application;
import android.preference.PreferenceManager;

import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize some sample preferences
        Set<String> stringSet = new LinkedHashSet<>(Arrays.asList("Hello", "Hyperion!"));
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean("KEY_BOOLEAN", true)
                .putFloat("KEY_FLOAT", 12.34f)
                .putInt("KEY_INT", 1234)
                .putLong("KEY_LONG", 1234L)
                .putString("KEY_STRING", "Hello Hyperion!")
                .putStringSet("KEY_STRING_SET", stringSet)
                .apply();

        //Create some network traffic
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ChuckInterceptor(this))
                .build();

        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/posts/1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}
