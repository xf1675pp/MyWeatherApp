package com.xf1675pp.myweather

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.xf1675pp.myweather.data.BitmapObj
import com.xf1675pp.myweather.data.CurrentConditions
import com.xf1675pp.myweather.repo.FailInterface
import com.xf1675pp.myweather.repo.OpenWeatherMapRepo
import com.xf1675pp.myweather.retrofit.OpenWeatherMapInterface
import com.xf1675pp.myweather.viewmodels.SearchFragmentViewModel
import com.xf1675pp.myweather.viewmodels.SearchFragmentViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.ByteArrayOutputStream


class SearchFragment : Fragment(), FailInterface{


    lateinit var search: Button
    lateinit var zipcode: EditText
    var length = 0
    lateinit var searchFragmentViewModel: SearchFragmentViewModel
    private var currentConditions: CurrentConditions? = null
    private var bitmapObj: BitmapObj? = null
    lateinit var navHostFragment:NavHostFragment
    lateinit var navController: NavController
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        progressBar = view.findViewById(R.id.main_progressbar)

        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val openWeatherMapInterface = OpenWeatherMapInterface.create()
        val repo = OpenWeatherMapRepo(openWeatherMapInterface)



        searchFragmentViewModel = ViewModelProvider(this, SearchFragmentViewModelFactory(repo, requireContext())).get(SearchFragmentViewModel::class.java)



        zipcode = view.findViewById(R.id.zipcode_edittext)

        zipcode.doAfterTextChanged {
            length = zipcode.text.toString().length
            if (length < 5)
            {
                search.isEnabled = false
            }
            else{
                if (length == 5)
                {
                    search.isEnabled = true
                }
            }
        }

        search = view.findViewById(R.id.search_button)
        search.setOnClickListener {
            var zipString = ""
            if (zipcode_edittext.text.toString().isNotEmpty() && zipcode_edittext.text.toString().length == 5)
            {
                zipString = zipcode_edittext.text.toString()
                zipString = "$zipString,us"
                searchFragmentViewModel.makeCall(zipString)
            }
            progressBar.visibility = View.VISIBLE
            searchFragmentViewModel.conditions.observe(requireActivity(), Observer {

                currentConditions = it
                if (currentConditions != null  && bitmapObj != null)
                {
                    zipcode.text.clear()
                    var action:NavDirections?  = SearchFragmentDirections.actionSearchFragmentToConditionsFragment(currentConditions!!, bitmapObj!!, zipString)
                    navController.navigate(action!!)
                    searchFragmentViewModel.conditions.removeObservers(requireActivity())
                    currentConditions = null
                    bitmapObj = null
                    progressBar.visibility = View.GONE
                    action = null
                }
            })

            searchFragmentViewModel.image.observe(requireActivity(), Observer {
                var image = it
                if (image != null)
                {
                    var bitmap = (image as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b: ByteArray = baos.toByteArray()
                    bitmapObj = BitmapObj(b)
                    if (currentConditions != null  && bitmapObj != null)
                    {
                        zipcode.text.clear()
                        val action = SearchFragmentDirections.actionSearchFragmentToConditionsFragment(currentConditions!!, bitmapObj!!, zipString)
                        navController.navigate(action)
                        searchFragmentViewModel.image.removeObservers(requireActivity())
                        currentConditions = null
                        bitmapObj = null
                        progressBar.visibility = View.GONE

                    }
                }
            })

        }

        return view
    }

    override fun onResume() {
        if (this::searchFragmentViewModel.isInitialized)
        {
            if (searchFragmentViewModel.conditions.hasActiveObservers())
            {
                searchFragmentViewModel.conditions.removeObservers(requireActivity())
            }

            if (searchFragmentViewModel.image.hasActiveObservers())
            {
                searchFragmentViewModel.image.removeObservers(requireActivity())
            }
        }
        super.onResume()
    }

    override fun failed() {
        Log.d("FAILED", "========================interfaceworking")
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Failed to get Data from API")
            .setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, id ->
                    // START THE GAME!
                })
        builder.create()
        progressBar.visibility = View.GONE
        searchFragmentViewModel.image.removeObservers(requireActivity())
        searchFragmentViewModel.conditions.removeObservers(requireActivity())
        zipcode.text.clear()
        Toast.makeText(requireContext(),"Failed to get data from API!", Toast.LENGTH_SHORT).show()
    }


}

