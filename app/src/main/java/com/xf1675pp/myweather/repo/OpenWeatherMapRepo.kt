package com.xf1675pp.myweather.repo

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.data.Forecast
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import javax.inject.Inject

class OpenWeatherMapRepo @Inject constructor(private val openWeatherMapInterface: OpenWeatherMapInterface) {

    private val currentConditionsLiveData = MutableLiveData<CurrentConditions>()
    val conditions: LiveData<CurrentConditions>
    get() = currentConditionsLiveData

    private val imageDrawable: MutableLiveData<Drawable> = MutableLiveData<Drawable>()
    val image: LiveData<Drawable>
    get() = imageDrawable

    private val forecastsLiveData = MutableLiveData<Forecast>()
    val forecasts: LiveData<Forecast>
        get() = forecastsLiveData

    suspend fun getCurrentConditions(lat: String, lon: String,  units: String, appId: String)
    {
        val result = openWeatherMapInterface.getCurrentConditions(lat, lon, units, appId)

        if (result.body() != null)
        {
            currentConditionsLiveData.postValue(result.body())
        }
        else
        {
            Log.d("FAIL", "========================================")
        }
    }


    suspend fun getForecast(lat: String, lon: String,  cnt: String, units:String, appId: String)
    {
        val result = openWeatherMapInterface.getForecast(lat, lon, cnt, units, appId)

        if (result.body() != null)
        {
            forecastsLiveData.postValue(result.body())
        }
        else
        {
            Log.d("FAIL", "========================================")
        }
    }


    fun downloadImage(context: Context) {
        val imageUrl = "https://openweathermap.org/img/wn/10d@2x.png"
        val image = Glide.with(context).asDrawable().load(imageUrl).submit(300, 300)
        imageDrawable.postValue(image.get())
    }
}