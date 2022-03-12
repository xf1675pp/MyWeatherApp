package com.xf1675pp.myweather

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xf1675pp.myweather.databinding.ActivityMainBinding
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import com.xf1675pp.myweather.viewmodels.MainViewModel
import com.xf1675pp.myweather.viewmodels.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.forecast.setOnClickListener {
            val intent = Intent(this, ForecastActivity::class.java)
            startActivity(intent)
        }

        val openWeatherMapInterface = OpenWeatherMapInterface.create()

        val repo = OpenWeatherMapRepo(openWeatherMapInterface)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repo, this@MainActivity)).get(MainViewModel::class.java)


        mainViewModel.conditions.observe(this, Observer {
            binding.title.text = it.name
            binding.temperature.text = it.main.temp.toString()
            binding.feelsLike.text = "Feels Like: " + it.main.feels_like.toString() + "º"
            binding.low.text = "Low: " + it.main.temp_min + "º"
            binding.high.text = "High: " + it.main.temp_max + "º"
            binding.humidity.text = "Humidity: " + it.main.humidity + "º"
            binding.pressure.text = "Pressure: " + it.main.pressure + " hPa"
            binding.mainProgressbar.visibility = View.GONE
        })

        mainViewModel.image.observe(this, Observer {
            binding.imageWeather.setImageDrawable(it)
        })

    }



}