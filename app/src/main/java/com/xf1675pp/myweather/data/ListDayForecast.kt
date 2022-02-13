package com.xf1675pp.myweather.data

import android.content.res.Resources
import java.util.*

fun ListDayForecast(resources: Resources): List<DayForecast> {

    fun generateTimestampNextDays(nextDay: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +nextDay)
        return calendar.timeInMillis
    }

    return listOf(
        DayForecast(generateTimestampNextDays(0), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(1), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(2), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(3), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(4), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(5), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(6), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(7), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(8), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(9), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(10), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(11), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(12), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(13), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(14), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
        DayForecast(generateTimestampNextDays(15), 8, 9, ForecastTemp(1f, 60f, 72f), 1023f, 100),
    )
}