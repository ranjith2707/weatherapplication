package com.choudhary.mausamapp;

import com.choudhary.mausamapp.Models.Food;
import com.choudhary.mausamapp.Models.MausamData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceAPI {

    @GET("weather")
    Call<MausamData> getData(
            @Query("q") String city,
            @Query("appid") String API_Key,
            @Query("units") String units
    );

    @GET("random.php")
    Call<Food> getFoods();

}
