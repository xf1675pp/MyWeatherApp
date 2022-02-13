package com.xf1675pp.myweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xf1675pp.myweather.data.ListDayForecast
import com.xf1675pp.myweather.recycler.DayForecastAdapter

class ForecastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        supportActionBar!!.title = "Forecast"

        val recyclerView: RecyclerView = findViewById(R.id.forecastList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DayForecastAdapter(ListDayForecast(resources))
    }
}