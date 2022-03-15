package com.xf1675pp.myweather.viewmodels

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xf1675pp.myweather.data.CurrentConditions

class ConditionsViewModel: ViewModel() {

    private val currentConditionsLiveData = MutableLiveData<CurrentConditions>()
    private val imageLiveData = MutableLiveData<Drawable>()

    val conditions: LiveData<CurrentConditions>
        get() = currentConditionsLiveData

    val image: LiveData<Drawable>
        get() = imageLiveData

    fun initConditions(conditions: CurrentConditions)
    {
        currentConditionsLiveData.postValue(conditions)
    }

    fun initImage(drawable: Drawable)
    {
        imageLiveData.postValue(drawable)
    }


}