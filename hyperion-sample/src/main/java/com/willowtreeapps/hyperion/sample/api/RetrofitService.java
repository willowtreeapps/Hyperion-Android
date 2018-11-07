package com.willowtreeapps.hyperion.sample.api;

import com.willowtreeapps.hyperion.sample.database.Wearther;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("weather")
    Call<Wearther> getWeather(@Query("q") String cityId,
                              @Query("appid") String appid);
}
