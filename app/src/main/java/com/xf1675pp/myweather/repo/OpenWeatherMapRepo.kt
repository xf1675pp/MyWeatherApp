package com.xf1675pp.myweather.repo

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.data.Forecast
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import javax.inject.Inject

class OpenWeatherMapRepo @Inject constructor(private val openWeatherMapInterface: OpenWeatherMapInterface) {

    var context: Context? = null
    private val currentConditionsLiveData = MutableLiveData<CurrentConditions>()
    val conditions: LiveData<CurrentConditions>
        get() = currentConditionsLiveData

    private val imageDrawable: MutableLiveData<Drawable> = MutableLiveData<Drawable>()
    val image: LiveData<Drawable>
        get() = imageDrawable

    private val forecastsLiveData = MutableLiveData<Forecast>()
    val forecasts: LiveData<Forecast>
        get() = forecastsLiveData

    suspend fun getCurrentConditionsByZip(zip: String, units: String, appId: String) {
        val result = openWeatherMapInterface.getCurrentConditionsByZip(zip, units, appId)
        if (result.body() != null) {
            currentConditionsLiveData.postValue(result.body())
        }
    }


    suspend fun getForecastByZip(zip: String, cnt: String, units: String, appId: String) {
        val result = openWeatherMapInterface.getForecastByZip(zip, cnt, units, appId)

        if (result.body() != null) {
            forecastsLiveData.postValue(result.body())
        } else {
            val failure: ForecasteFailureInterface = object :ForecasteFailureInterface{
                override fun failed() {
                }

            }
            failure.failed()
        }
    }


    fun downloadImage() {
        val imageUrl = "https://openweathermap.org/img/wn/10d@2x.png"
        val image = Glide.with(context!!).asDrawable().load(imageUrl).submit(300, 300)
        imageDrawable.postValue(image.get())
    }

    fun giveContext(context: Context) {
        this.context = context
    }


    suspend fun getCurrentConditionsByLatLong(lat: String, long: String, units: String, appId: String)
    {
        val result = openWeatherMapInterface.getCurrentConditionsByLatLong(lat, long, units, appId)
        if (result.body() != null) {
            currentConditionsLiveData.postValue(result.body())
        } else {
            val failure: FailInterface = object : FailInterface {
                override fun failed() {
                }

            }
            failure.failed()
        }
    }

    suspend fun getForecastByLatLong(lat: String, long: String, cnt: String, units: String, appId: String) {
        val result = openWeatherMapInterface.getForecastByLatLong(lat, long, cnt, units, appId)
        if (result.body() != null) {
            forecastsLiveData.postValue(result.body())
        } else {
            val failure: ForecasteFailureInterface = object :ForecasteFailureInterface{
                override fun failed() {
                }

            }
            failure.failed()
        }
    }

}