package ru.kpfu.itis.carwash.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.ViewModelFactory
import ru.kpfu.itis.carwash.databinding.FragmentMapsBinding
import ru.kpfu.itis.data.repository.LocationRepositoryImpl
import ru.kpfu.itis.data.api.places.ApiFactory
import ru.kpfu.itis.data.api.places.Place
import ru.kpfu.itis.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.domain.AuthInteractor

class MapsFragment : Fragment(){

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var viewModel: MapsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        viewModel = ViewModelProvider(this, initFactory()).get(MapsViewModel::class.java)
        initMap()
        return binding.root
    }

    private fun initMap(){
        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync {
            map = it ?: return@getMapAsync
            map.setOnMyLocationButtonClickListener {
                false
            }
            initSubscribes()
            checkPermissions()
        }
    }

    private fun initFactory() = ViewModelFactory(
        ApiFactory.placesService,
        LocationRepositoryImpl(
            client = LocationServices.getFusedLocationProviderClient(requireActivity())
        ),
        AuthInteractor(
            AuthRepositoryImpl(FirebaseAuth.getInstance())
        )
    )

    private fun initSubscribes(){
        with(viewModel){
            location().observe(viewLifecycleOwner, {
                try {
                    it.getOrThrow().apply {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 12f))
                    }
                }catch (throwable : Throwable){
                    Log.e("loc", "error")
                }
            })

            carWashes().observe(viewLifecycleOwner, {
                it.getOrNull()?.listIterator()?.apply {
                    showMarkers(this)
                }
            })
        }
    }

    private fun showMarkers(iterator: ListIterator<Place>){
        for (place in iterator){
            map.run {
                addMarker(
                    MarkerOptions()
                        .position(place.geometry.location.latLng)
                        .title(place.name)
                )
            }
        }
    }

    private fun checkPermissions(){
        Dexter.withContext(activity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(activity?.let {
                                        ContextCompat.checkSelfPermission(
                                                it,
                                                Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    }
                                    == PackageManager.PERMISSION_GRANTED){
                                map.isMyLocationEnabled = true
                                viewModel.showNearbyCarWashes()
                            }
                        }

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {}

                    override fun onPermissionRationaleShouldBeShown(response: PermissionRequest?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }

                })
                .check()
    }

}