package com.xf1675pp.myweather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xf1675pp.myweather.data.Forecast
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastFragmentViewModel(private val repo: OpenWeatherMapRepo): ViewModel() {

    private val cnt = "16"
    private val appId = "67018da1a6b55ea20a1477b28970da76"
    private val units = "metric"
    val forecast: LiveData<Forecast>
        get() = repo.forecasts

    fun makeForecastCall(zip: String)
    {

        viewModelScope.launch(Dispatchers.IO) {
            repo.getForecastByZip(zip, cnt, units, appId)
        }
    }

    fun makeForecastCallByLatLong(lat: String, long: String)
    {

        viewModelScope.launch(Dispatchers.IO) {
            repo.getForecastByLatLong(lat, long, cnt, units, appId)
        }
    }
}