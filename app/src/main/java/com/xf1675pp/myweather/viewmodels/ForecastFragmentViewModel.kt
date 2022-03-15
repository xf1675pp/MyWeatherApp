package com.xf1675pp.myweather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xf1675pp.myweather.data.Forecast
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForecastFragmentViewModel(private val repo: OpenWeatherMapRepo): ViewModel() {

    val forecast: LiveData<Forecast>
        get() = repo.forecasts

    fun makeForecastCall(zip: String)
    {
        val cnt = "16"
        val appid = "67018da1a6b55ea20a1477b28970da76"
        val units = "metric"
        viewModelScope.launch(Dispatchers.IO) {
            repo.getForecastByZip(zip, cnt, units, appid)
        }
    }
}