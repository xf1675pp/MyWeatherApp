package com.xf1675pp.myweather

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.xf1675pp.myweather.recycler.DayForecastAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var forecastButton: Button
    private lateinit var dayForecastAdapter: DayForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastButton = findViewById(R.id.forecast)

        forecastButton.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }
    }
}