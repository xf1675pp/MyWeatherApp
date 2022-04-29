package com.xf1675pp.myweather

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xf1675pp.myweather.recycler.DayForecastAdapter
import com.xf1675pp.myweather.repo.ForecasteFailureInterface
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import com.xf1675pp.myweather.viewmodels.ForecastFragmentViewModel
import com.xf1675pp.myweather.viewmodels.ForecastFragmentViewModelFactory


class ForecastFragment : Fragment(), ForecasteFailureInterface {

    private lateinit var dayForecastAdapter: DayForecastAdapter
    private lateinit var forecastFragmentViewModel: ForecastFragmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var zipString: String = ""
    private var latString: String = ""
    private var longString: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forecast, container, false)

        zipString = requireArguments().getString("zipcode") as String
        latString = requireArguments().getString("lat") as String
        longString = requireArguments().getString("long") as String

        recyclerView = view.findViewById(R.id.forecastList)
        progressBar = view.findViewById(R.id.forecast_progressbar)

        val openWeatherMapInterface = OpenWeatherMapInterface.create()
        val repo = OpenWeatherMapRepo(openWeatherMapInterface)
        forecastFragmentViewModel = ViewModelProvider(this, ForecastFragmentViewModelFactory(repo)).get(ForecastFragmentViewModel::class.java)

        if (zipString.isNotEmpty()) {
            forecastFragmentViewModel.makeForecastCall(zipString)
        }
        else
        {
            forecastFragmentViewModel.makeForecastCallByLatLong(latString, longString)
        }

        forecastFragmentViewModel.forecast.observe(requireActivity(), Observer {
            progressBar.visibility = View.GONE
            dayForecastAdapter = DayForecastAdapter(it)
            recyclerView.adapter = dayForecastAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        })

        return view
    }

    override fun failed() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Failed to get Data from API")
            .setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.create()
        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(),"Failed to get data from API!", Toast.LENGTH_SHORT).show()

    }

}