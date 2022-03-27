package com.xf1675pp.myweather.retrofit


import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.data.Forecast
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenWeatherMapInterface {

    @Headers("Content-Type: application/json")
    @GET("data/2.5/weather")
    suspend fun getCurrentConditionsByZip(
        @Query("zip") zip: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<CurrentConditions>

    @GET("/data/2.5/forecast/daily")
    suspend fun getForecastByZip(
        @Query("zip") zip: String,
        @Query("cnt") cnt: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<Forecast>

    @Headers("Content-Type: application/json")
    @GET("data/2.5/weather")
    suspend fun getCurrentConditionsByLatLong(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<CurrentConditions>

    @GET("/data/2.5/forecast/daily")
    suspend fun getForecastByLatLong(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("cnt") cnt: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<Forecast>


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