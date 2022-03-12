package com.xf1675pp.myweather

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var forecastButton: Button
    private lateinit var temp : TextView
    private lateinit var feelsLike : TextView
    private lateinit var tempMin : TextView
    private lateinit var tempMax : TextView
    private lateinit var pressure : TextView
    private lateinit var humidity : TextView
    private lateinit var title : TextView
    private lateinit var iconImage : AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastButton = findViewById(R.id.forecast)
        temp = findViewById(R.id.temperature)
        feelsLike = findViewById(R.id.feels_like)
        tempMin = findViewById(R.id.low)
        tempMax = findViewById(R.id.high)
        pressure = findViewById(R.id.pressure)
        humidity = findViewById(R.id.humidity)
        title = findViewById(R.id.title)
        iconImage = findViewById(R.id.image_weather)

        forecastButton.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }

        getCurrentConditions()
    }



    private fun getCurrentConditions() {

        val lat = "35"
        val lon = "139"
        val appid = getString(R.string.API_KEY)
        val units = "metric"

        val apiInterface = OpenWeatherMapInterface.create().getCurrentConditions(lat, lon, units, appid)

        apiInterface.enqueue(object : Callback<CurrentConditions> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<CurrentConditions>?,
                response: Response<CurrentConditions>?
            ) {
                if (response?.body() != null) {
                    val data = response.body()

                    title.text = data!!.name
                    temp.text = data.main.temp.toString() + "º"
                    feelsLike.text = "Feels Like: "+data.main.feels_like.toString()+"º"
                    tempMin.text = "Low: "+data.main.temp_min+"º"
                    tempMax.text = "High: "+data.main.temp_max+"º"
                    humidity.text = "Humidity: "+data.main.humidity+"º"
                    pressure.text = "Pressure: "+data.main.pressure+" hPa"

                    val imageUrl = "https://openweathermap.org/img/wn/10d@2x.png"
                    Glide.with(this@MainActivity).load(imageUrl).centerCrop().placeholder(R.drawable.sun_icon).into(iconImage)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>?, t: Throwable?) {
                Log.e("exception", t.toString())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getCurrentConditions()
    }
}