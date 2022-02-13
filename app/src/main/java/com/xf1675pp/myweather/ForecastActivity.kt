package com.xf1675pp.myweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.data.Forecast
import com.xf1675pp.myweather.recycler.DayForecastAdapter
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastActivity : AppCompatActivity() {

    private lateinit var dayForecastAdapter: DayForecastAdapter

    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        supportActionBar!!.title = "Forecast"
        recyclerView = findViewById(R.id.forecastList)

        getForecast()
    }

    private fun getForecast(){
        val lat = "35"
        val lon = "139"
        val appid = getString(R.string.API_KEY)
        val cnt = "16"
        val units = "metric"

        val apiInterface = OpenWeatherMapInterface.create().getForecast(lat, lon, cnt, units, appid)

        apiInterface.enqueue(object : Callback<Forecast> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Forecast>?,
                response: Response<Forecast>?
            ) {
                Log.e("response", response.toString())
                if (response?.body() != null) {
                    val data = response.body()
                    // Set the data to the adapter
                    dayForecastAdapter = DayForecastAdapter(data!!)
                    recyclerView!!.adapter = dayForecastAdapter
                    recyclerView!!.layoutManager = LinearLayoutManager(this@ForecastActivity)
                    
                }
            }
            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                Log.e("exception", t.toString())
            }
        })
    }
}