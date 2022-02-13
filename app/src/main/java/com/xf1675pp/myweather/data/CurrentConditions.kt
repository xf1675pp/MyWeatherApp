package com.xf1675pp.myweather.data

data class CurrentConditions(
    val name : String, val main: Main, val sys: SysData
)

data class SysData(
    val sunrise: Long,
    val sunset: Long
)

data class Main(
    val temp: Float,
    val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Int,
    val humidity: Int
)

data class Forecast(val list: List<DayForecast>)

data class DayForecast(
    val dt: Long,
    val sunrise: Long,
    val weather: List<Weather>,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int
)

data class ForecastTemp(val day: Float, val min: Float, val max: Float)

data class Weather(val icon: String)
