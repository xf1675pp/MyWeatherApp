package com.xf1675pp.myweather.viewmodels

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragmentViewModel @Inject
constructor(private val repo: OpenWeatherMapRepo, @ApplicationContext context: Context) :
    ViewModel() {

    private val appId = "67018da1a6b55ea20a1477b28970da76"
    private val units = "metric"

    init {
        repo.giveContext(context)
    }

    val image: LiveData<Drawable>
        get() = repo.image

    val conditions: LiveData<CurrentConditions>
        get() = repo.conditions

    val conditionsForService: LiveData<CurrentConditions>
        get() = repo.conditionsForService


    fun makeCall(zip: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrentConditionsByZip(zip, units, appId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repo.downloadImage()
        }
    }

    fun makeCallByLatLong(lat: String, long: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrentConditionsByLatLong(lat, long, units, appId)
        }
        viewModelScope.launch(Dispatchers.IO) {
            repo.downloadImage()
        }

    }


    fun makeCallByLatLongForService(lat: String, long: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrentConditionsByLatLongForService(lat, long, units, appId)
        }
    }


    fun checkPermissions(context: Context): Boolean {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                return (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
            }
        }
        return false
    }


    fun showPermissionsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("App needs location permission to get your lat/long values to show weather forecasts according to your location.")
            .setTitle("Permissions Needed").setPositiveButton("OK",
                DialogInterface.OnClickListener { _, _ ->
                })
        builder.create()
        builder.show()
    }

    fun locationNotFoundDialog(context: Context)
    {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("There was a problem fetching your location, please turn on location services and try again.")
            .setTitle("Last Known Location Not Found").setPositiveButton("OK",
                DialogInterface.OnClickListener { _, _ ->
                })
        builder.create()
        builder.show()
    }



}