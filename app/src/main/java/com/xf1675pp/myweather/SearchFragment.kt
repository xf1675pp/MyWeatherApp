package com.xf1675pp.myweather

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.xf1675pp.myweather.data.BitmapObj
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.repo.FailInterface
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import com.xf1675pp.myweather.viewmodels.SearchFragmentViewModel
import com.xf1675pp.myweather.viewmodels.SearchFragmentViewModelFactory
import java.io.ByteArrayOutputStream


class SearchFragment : Fragment(), FailInterface {


    lateinit var search: Button
    lateinit var zipcode: EditText
    var length = 0
    private lateinit var searchFragmentViewModel: SearchFragmentViewModel
    private var currentConditions: CurrentConditions? = null
    private var bitmapObj: BitmapObj? = null
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var progressBar: ProgressBar
    lateinit var locationButton: Button
    lateinit var startServiceButton: Button

    private lateinit var locationListenerService:LocationListener

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var lat = ""
    var long = ""
    var zipCodeString = ""
    var reqFlag = 0
    var check = -1

    // we are checking permissions first and then requesting for location
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        progressBar = view.findViewById(R.id.main_progressbar)
        locationButton = view.findViewById(R.id.location_button)
        startServiceButton = view.findViewById(R.id.start_service_button)

        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val openWeatherMapInterface = OpenWeatherMapInterface.create()
        val repo = OpenWeatherMapRepo(openWeatherMapInterface)


        searchFragmentViewModel =
            ViewModelProvider(this, SearchFragmentViewModelFactory(repo, requireContext())).get(
                SearchFragmentViewModel::class.java
            )


        locationListenerService = LocationListener()

        zipcode = view.findViewById(R.id.zipcode_edittext)

        zipcode.doAfterTextChanged {
            length = zipcode.text.toString().length
            if (length < 5) {
                search.isEnabled = false
            } else {
                if (length == 5) {
                    search.isEnabled = true
                }
            }
        }

        search = view.findViewById(R.id.search_button)
        search.setOnClickListener {

            if (reqFlag == 0) {
                if (zipcode.text.toString()
                        .isNotEmpty() && zipcode.text.toString().length == 5
                ) {
                    zipCodeString = zipcode.text.toString()
                    zipCodeString = "$zipCodeString,us"
                    searchFragmentViewModel.makeCall(zipCodeString)
                    setFlags()
                    reqFlag = 1
                    check = 0
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Already fetching current conditions!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        if (!isServiceRunning(locationListenerService.javaClass)) {
            startServiceButton.text = "Start Service"
        }
        else
        {
            startServiceButton.text = "Stop Service"
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        val requestBackgroundLocationPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (!permissions.containsValue(false)) {
                } else {
                    searchFragmentViewModel.showPermissionsDialog(requireContext())
                }
            }

        val requestForegroundLocationPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (!permissions.containsValue(false)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        requestBackgroundLocationPermission.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                        )
                    }
                } else {
                    searchFragmentViewModel.showPermissionsDialog(requireContext())
                }
            }


        if (!searchFragmentViewModel.checkPermissions(requireContext())) {
            requestForegroundLocationPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }

        locationButton.setOnClickListener {

            if (!searchFragmentViewModel.checkPermissions(requireContext())) {

                requestForegroundLocationPermission.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )

            } else {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            lat = location.latitude.toString()
                            long = location.longitude.toString()
                            if (reqFlag == 0) {
                                searchFragmentViewModel.makeCallByLatLong(lat, long)
                                setFlags()
                                reqFlag = 1
                                check = 1
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Already trying to fetch weather conditions!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            searchFragmentViewModel.locationNotFoundDialog(requireContext())
                        }
                    }
            }
        }



        searchFragmentViewModel.conditions.observe(viewLifecycleOwner, Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                currentConditions = it
                if (currentConditions != null && bitmapObj != null) {
                    locationButton.isEnabled = true
                    val action: NavDirections =
                        SearchFragmentDirections.actionSearchFragmentToConditionsFragment(
                            currentConditions!!,
                            bitmapObj!!,
                            zipCodeString,
                            lat,
                            long
                        )
                    navController.navigate(action)
                    searchFragmentViewModel.conditions.removeObservers(requireActivity())
                    currentConditions = null
                    bitmapObj = null
                    progressBar.visibility = View.GONE
                    reqFlag = 0
                    if (check == 0) {
                        zipcode.text.clear()
                    }
                    check = -1
                }
            }
        })

        searchFragmentViewModel.image.observe(viewLifecycleOwner, Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                val image = it
                if (image != null) {
                    val bitmap = (image as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b: ByteArray = baos.toByteArray()
                    bitmapObj = BitmapObj(b)
                    if (currentConditions != null && bitmapObj != null) {
                        zipcode.text.clear()
                        locationButton.isEnabled = true
                        val action =
                            SearchFragmentDirections.actionSearchFragmentToConditionsFragment(
                                currentConditions!!,
                                bitmapObj!!,
                                zipCodeString,
                                lat,
                                long
                            )
                        navController.navigate(action)
                        searchFragmentViewModel.image.removeObservers(requireActivity())
                        currentConditions = null
                        bitmapObj = null
                        progressBar.visibility = View.GONE
                        reqFlag = 0
                        if (check == 0) {
                            zipcode.text.clear()
                        }
                        check = -1

                    }
                }
            }
        })



        startServiceButton.setOnClickListener {
            if (!searchFragmentViewModel.checkPermissions(requireContext())) {

                requestForegroundLocationPermission.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )

                Toast.makeText(
                    requireContext(),
                    "Please allow permissions first!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var intent = Intent(requireContext(), locationListenerService.javaClass)
                if (!isServiceRunning(locationListenerService.javaClass)) {
                    requireActivity().startService(intent)
                    Toast.makeText(
                        requireContext(),
                        "Service started successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startServiceButton.text = "STOP SERVICE"
                } else {
                    requireActivity().stopService(intent)
                    Toast.makeText(
                        requireContext(),
                        "Service stopped successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startServiceButton.text = "START SERVICE"
                }
            }
        }

        return view
    }

    override fun onResume() {
        if (this::searchFragmentViewModel.isInitialized) {
            if (searchFragmentViewModel.conditions.hasActiveObservers()) {
                searchFragmentViewModel.conditions.removeObservers(requireActivity())
            }

            if (searchFragmentViewModel.image.hasActiveObservers()) {
                searchFragmentViewModel.image.removeObservers(requireActivity())
            }
        }
        super.onResume()
    }

    override fun failed() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Failed to get Data from API")
            .setPositiveButton("Ok",
                DialogInterface.OnClickListener { _, _ ->
                })
        builder.create()
        builder.show()
        progressBar.visibility = View.GONE
        searchFragmentViewModel.image.removeObservers(requireActivity())
        searchFragmentViewModel.conditions.removeObservers(requireActivity())
        zipcode.text.clear()
        Toast.makeText(requireContext(), "Failed to get data from API!", Toast.LENGTH_SHORT).show()
    }


    private fun setFlags() {
        progressBar.visibility = View.VISIBLE
        if (check == 1) {
            zipCodeString = ""
        } else {
            lat = ""
            long = ""
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager =
            requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (running in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == running.service.className) {
                return true
            }
        }
        return false
    }


}

