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

class DayForecastAdapter(private val dataSet: Forecast, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val date: TextView = view.findViewById(R.id.item_date)
        private val temp: TextView = view.findViewById(R.id.item_temp)
        private val high: TextView = view.findViewById(R.id.item_high)
        private val low: TextView = view.findViewById(R.id.item_low)
        private val sunrise: TextView = view.findViewById(R.id.item_sunrise)
        private val sunset: TextView = view.findViewById(R.id.item_sunset)
        private val image: AppCompatImageView = view.findViewById(R.id.item_image)

        fun bind(dayForecast: DayForecast, clickListener: OnItemClickListener)
        {
            val date1 = Date(dayForecast.dt * 1000L)
            val sdf = java.text.SimpleDateFormat("MMM d")
            val dateString = sdf.format(date1)

            val sunrise1 = Date(dayForecast.sunrise * 1000L)
            val parseSunrise = java.text.SimpleDateFormat("h:mm a")
            val sunriseString = parseSunrise.format(sunrise1)

            val sunset1 = Date(dayForecast.sunset * 1000L)
            val parseSunset = java.text.SimpleDateFormat("h:mm a")
            val sunsetString = parseSunset.format(sunset1)

            date.text = dateString.toString()
            temp.text = "Temp: ${dayForecast.temp.day}°"
            high.text = "High: ${dayForecast.temp.max}°"
            low.text = "Low: ${dayForecast.temp.min}°"
            sunrise.text = sunriseString
            sunset.text = sunsetString

            val icon = dayForecast.weather[0].icon
            val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"
            Glide.with(image.context).load(iconUrl).into(image)

            itemView.setOnClickListener {
                clickListener.onItemClicked(dayForecast)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_row_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataSet.list[position]

//        val date = Date(data.dt * 1000L)
//        val sdf = java.text.SimpleDateFormat("MMM d")
//        val dateString = sdf.format(date)
//
//        val sunrise = Date(data.sunrise * 1000L)
//        val parseSunrise = java.text.SimpleDateFormat("h:mm a")
//        val sunriseString = parseSunrise.format(sunrise)
//
//        val sunset = Date(data.sunset * 1000L)
//        val parseSunset = java.text.SimpleDateFormat("h:mm a")
//        val sunsetString = parseSunset.format(sunset)
//
//        holder.date.text = dateString.toString()
//        holder.temp.text = "Temp: ${data.temp.day}°"
//        holder.high.text = "High: ${data.temp.max}°"
//        holder.low.text = "Low: ${data.temp.min}°"
//        holder.sunrise.text = sunriseString
//        holder.sunset.text = sunsetString
//
//        val icon = data.weather[0].icon
//        val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"
//        Glide.with(holder.image.context).load(iconUrl).into(holder.image)
        holder.bind(data,itemClickListener)
    }

    override fun getItemCount(): Int {
        return dataSet.list.size
    }

}

interface OnItemClickListener{
    fun onItemClicked(dayForecast: DayForecast)
}