package com.xf1675pp.myweather.viewmodels

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragmentViewModel @Inject
constructor(private val repo: OpenWeatherMapRepo, @ApplicationContext context: Context): ViewModel() {

    init {
        repo.giveContext(context)
    }

    val image: LiveData<Drawable>
        get() = repo.image

    val conditions: LiveData<CurrentConditions>
        get() = repo.conditions

    fun makeCall(zip: String)
    {
        val appid = "67018da1a6b55ea20a1477b28970da76"
        val units = "metric"
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrentConditionsByZip(zip, units, appid)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repo.downloadImage()
        }
    }
}