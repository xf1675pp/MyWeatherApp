package com.xf1675pp.myweather.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Parcelize
data class CurrentConditions @Inject constructor(val name : String, val main: Main, val sys: SysData): Parcelable

@Parcelize
data class SysData @Inject constructor(
    val sunrise: Long,
    val sunset: Long
): Parcelable

@Parcelize
data class Main @Inject constructor(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int
):Parcelable

data class Forecast @Inject constructor(val list: List<DayForecast>)

data class DayForecast @Inject constructor(
    val dt: Long,
    val sunrise: Long,
    val weather: List<Weather>,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int
)

@Parcelize
data class BitmapObj(val byteArray: ByteArray): Parcelable

data class ForecastTemp @Inject constructor(val day: Float, val min: Float, val max: Float)

data class Weather @Inject constructor(val icon: String)
