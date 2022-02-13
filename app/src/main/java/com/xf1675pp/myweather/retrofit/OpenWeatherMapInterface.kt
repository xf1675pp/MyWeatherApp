package com.xf1675pp.myweather.retrofit

import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.data.Forecast
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenWeatherMapInterface {
    // API KEY, LAT, LONG ARE HARDCODED
    @Headers("Content-Type: application/json")
    @GET("data/2.5/weather")
    fun getCurrentConditions(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Call<CurrentConditions>

    @GET("/data/2.5/forecast/daily")
    fun getForecast(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("cnt") cnt: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Call<Forecast>

    companion object {

        var BASE_URL = "http://pro.openweathermap.org/"

        fun create(): OpenWeatherMapInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(OpenWeatherMapInterface::class.java)

        }
    }

}