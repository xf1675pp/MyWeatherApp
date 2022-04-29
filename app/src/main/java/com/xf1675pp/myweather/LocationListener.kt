package com.xf1675pp.myweather

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.*
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import com.xf1675pp.myweather.viewmodels.SearchFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LocationListener : LifecycleService() {

    private val openWeatherMapInterface = OpenWeatherMapInterface.create()
    val repo = OpenWeatherMapRepo(openWeatherMapInterface)
    private lateinit var searchFragmentViewModel: SearchFragmentViewModel
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    override fun onCreate() {

        searchFragmentViewModel = SearchFragmentViewModel(repo, this)

        createNotificationChannel()
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.sun_icon)

        val builder = NotificationCompat.Builder(this, "location")
            .setContentTitle("Notification Title")
            .setContentText("Notification Description")
            .setSmallIcon(R.drawable.sun_icon)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)

        val notification: Notification = builder.build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        startForeground(1, notification)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationRequest = create().apply {
            interval = 1800000
            fastestInterval = 1800000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            maxWaitTime = 1810000
        }



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                if (locationResult.locations.isNotEmpty()) {
                    var location = locationResult.lastLocation
                    var lat = location.latitude
                    var long = location.longitude
                    lifecycleScope.launch(Dispatchers.IO) {
                        searchFragmentViewModel.makeCallByLatLongForService(lat.toString(), long.toString())
                    }

                } else {
                    Toast.makeText(this@LocationListener, "Some problem occurred while fetching location!", Toast.LENGTH_SHORT).show()
                }
            }
        }



        if (this::fusedLocationClient.isInitialized) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        var lat = location.latitude.toString()
                        var long = location.longitude.toString()
                        lifecycleScope.launch(Dispatchers.IO) {
                            searchFragmentViewModel.makeCallByLatLongForService(lat, long)
                        }

                    }
                }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }


        searchFragmentViewModel.conditionsForService.observe(this, Observer {
                if (it != null)
                {
                    builder.setContentTitle("Current Temperature : " + it.main.temp.toString() + "º")
                    builder.setContentText(it.name)
                    manager.notify(1, builder.build())
                }
        })

        super.onCreate()
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "location",
                "location service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)

        }
    }
}