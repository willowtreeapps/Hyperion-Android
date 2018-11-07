package com.willowtreeapps.hyperion.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.willowtreeapps.hyperion.sample.api.RetrofitService;
import com.willowtreeapps.hyperion.sample.database.Wearther;
import com.willowtreeapps.hyperion.sample.debug.CustomLog;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

        CustomLog.debug("I'm a custom message!");

        // Log samples for Chuck plugin.

        OkHttpClient.Builder client = new OkHttpClient().newBuilder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(interceptor);

            client.addInterceptor(new ChuckInterceptor(this));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://samples.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        service.getWeather("London,uk", "b6907d289e10d714a6e88b30761fae22")
                .enqueue(new Callback<Wearther>() {
                    @Override
                    public void onResponse(Call<Wearther> call, Response<Wearther> response) {
                        Timber.w(String.valueOf(response.body().id));
                    }

                    @Override
                    public void onFailure(Call<Wearther> call, Throwable t) {
                        Timber.w(t.getMessage());
                    }
                });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new PagerFragment())
                    .commit();
        }
    }
}