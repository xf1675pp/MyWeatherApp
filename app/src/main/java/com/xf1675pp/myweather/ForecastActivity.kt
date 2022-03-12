package com.xf1675pp.myweather

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xf1675pp.myweather.databinding.ActivityForecastBinding
import com.xf1675pp.myweather.recycler.DayForecastAdapter
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import com.xf1675pp.myweather.viewmodels.ForecastViewModel
import com.xf1675pp.myweather.viewmodels.ForecastViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {

    private lateinit var dayForecastAdapter: DayForecastAdapter
    private lateinit var forecastViewModel: ForecastViewModel
    private lateinit var binding: ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast)

        supportActionBar!!.title = "Forecast"


        val openWeatherMapInterface = OpenWeatherMapInterface.create()

        val repo = OpenWeatherMapRepo(openWeatherMapInterface)


        forecastViewModel = ViewModelProvider(this, ForecastViewModelFactory(repo)).get(ForecastViewModel::class.java)

        forecastViewModel.forecast.observe(this, Observer {
            binding.forecastProgressbar.visibility = View.GONE
            dayForecastAdapter = DayForecastAdapter(it)
            binding.forecastList.adapter = dayForecastAdapter
            binding.forecastList.layoutManager = LinearLayoutManager(this@ForecastActivity)
        })
    }

}