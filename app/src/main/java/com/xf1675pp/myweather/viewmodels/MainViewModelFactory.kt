package com.xf1675pp.myweather.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo

class MainViewModelFactory(private val repo: OpenWeatherMapRepo, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repo, context) as T
    }
}