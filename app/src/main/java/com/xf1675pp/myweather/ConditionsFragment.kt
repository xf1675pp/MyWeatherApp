package com.xf1675pp.myweather

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.xf1675pp.myweather.data.BitmapObj
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.viewmodels.ConditionsViewModel
import com.xf1675pp.myweather.viewmodels.ConditionsViewModelFactory


class ConditionsFragment : Fragment() {


    private lateinit var currentConditions: CurrentConditions
    private lateinit var bitmapObj: BitmapObj
    private lateinit var image: AppCompatImageView
    private lateinit var title: TextView
    private lateinit var temperature: TextView
    private lateinit var feelsLike: TextView
    private lateinit var low: TextView
    private lateinit var high: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView
    private lateinit var forecast: Button

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController


    lateinit var conditionsViewModel: ConditionsViewModel

    private var zipString: String = "55101"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_conditions, container, false)

        conditionsViewModel =  ViewModelProvider(this, ConditionsViewModelFactory()).get(ConditionsViewModel::class.java)



        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        image = view.findViewById(R.id.image_weather)
        title = view.findViewById(R.id.title)
        temperature = view.findViewById(R.id.temperature)
        feelsLike = view.findViewById(R.id.feels_like)
        low = view.findViewById(R.id.low)
        high = view.findViewById(R.id.high)
        humidity = view.findViewById(R.id.humidity)
        pressure = view.findViewById(R.id.pressure)
        forecast = view.findViewById(R.id.forecast)


        currentConditions = requireArguments().get("currentconditions") as CurrentConditions

        conditionsViewModel.initConditions(currentConditions)
        bitmapObj = requireArguments().get("image") as BitmapObj
        zipString = requireArguments().getString("zipcode") as String

        if (this::bitmapObj.isInitialized)
        {
            val drawable = BitmapDrawable(resources, BitmapFactory.decodeByteArray(bitmapObj.byteArray, 0, bitmapObj.byteArray.size))
            conditionsViewModel.initImage(drawable)
        }


        conditionsViewModel.conditions.observe(requireActivity(), Observer {
            title.text = it.name
            temperature.text = it.main.temp.toString()
            feelsLike.text = "Feels Like: " + it.main.feels_like.toString() + "º"
            low.text = "Low: " + it.main.temp_min + "º"
            high.text = "High: " + it.main.temp_max + "º"
            humidity.text = "Humidity: " + it.main.humidity + "º"
            pressure.text = "Pressure: " + it.main.pressure + " hPa"
        })

        conditionsViewModel.image.observe(requireActivity(), Observer {
            image.setImageDrawable(it)
        })

        forecast.setOnClickListener {
            val action = ConditionsFragmentDirections.actionConditionsFragmentToForecastFragment(zipString)
            navController.navigate(action)
        }

        return view
    }

}