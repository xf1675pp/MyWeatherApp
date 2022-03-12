package com.xf1675pp.myweather.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo

class ForecastViewModelFactory(private val repo: OpenWeatherMapRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastViewModel(repo) as T
    }
}