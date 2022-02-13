package com.xf1675pp.myweather.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xf1675pp.myweather.R
import com.xf1675pp.myweather.data.DayForecast
import java.util.*

class DayForecastAdapter(private val dataSet: List<DayForecast>) :
    RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.item_date)
        val temp: TextView = view.findViewById(R.id.item_temp)
        val high: TextView = view.findViewById(R.id.item_high)
        val low: TextView = view.findViewById(R.id.item_low)
        val sunrise: TextView = view.findViewById(R.id.item_sunrise)
        val sunset: TextView = view.findViewById(R.id.item_sunset)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_row_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = dataSet[position]

        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(data.date)
        val dayOfWeekString =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        holder.date.text = dayOfWeekString!! + " " + day
        holder.temp.text = "Temp: " + data.temp.day.toString() + "º"
        holder.high.text = "High: " + data.temp.max.toString() + "º"
        holder.low.text = "Low: " + data.temp.min.toString() + "º"
        holder.sunrise.text = "Sunrise:" + data.sunrise.toString()
        holder.sunset.text = "Sunset: " + data.sunset.toString()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}