package com.xf1675pp.myweather.data

import javax.inject.Inject

data class CurrentConditions @Inject constructor(val name : String, val main: Main, val sys: SysData)

data class SysData @Inject constructor(
    val sunrise: Long,
    val sunset: Long
)

data class Main @Inject constructor(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int
)

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

data class ForecastTemp @Inject constructor(val day: Float, val min: Float, val max: Float)

data class Weather @Inject constructor(val icon: String)
