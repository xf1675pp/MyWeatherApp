package com.xf1675pp.myweather.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xf1675pp.myweather.R
import com.xf1675pp.myweather.data.DayForecast
import com.xf1675pp.myweather.data.Forecast
import java.util.*

class DayForecastAdapter(private val dataSet: Forecast) :
    RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.item_date)
        val temp: TextView = view.findViewById(R.id.item_temp)
        val high: TextView = view.findViewById(R.id.item_high)
        val low: TextView = view.findViewById(R.id.item_low)
        val sunrise: TextView = view.findViewById(R.id.item_sunrise)
        val sunset: TextView = view.findViewById(R.id.item_sunset)
        val image: AppCompatImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_row_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataSet.list[position]

        // Timestamp to Month Day ex: (Jan 31)
        val date = Date(data.dt * 1000L)
        val sdf = java.text.SimpleDateFormat("MMM d")
        val dateString = sdf.format(date)

        // Timestamp to Time ex: (12:00 PM)
        val sunrise = Date(data.sunrise * 1000L)
        val parseSunrise = java.text.SimpleDateFormat("h:mm a")
        val sunriseString = parseSunrise.format(sunrise)

        val sunset = Date(data.sunset * 1000L)
        val parseSunset = java.text.SimpleDateFormat("h:mm a")
        val sunsetString = parseSunset.format(sunset)
        
        // Bind data to view
        holder.date.text = dateString.toString()
        holder.temp.text = "Temp: ${data.temp.day}°"
        holder.high.text = "High: ${data.temp.max}°"
        holder.low.text = "Low: ${data.temp.min}°"
        holder.sunrise.text = sunriseString
        holder.sunset.text = sunsetString

        // Use Glide to load the image
        val icon = data.weather[0].icon
        val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"
        Glide.with(holder.image.context).load(iconUrl).into(holder.image)
    }

    override fun getItemCount(): Int {
        return dataSet.list.size
    }
}