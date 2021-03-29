package ru.kpfu.itis.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import ru.kpfu.itis.carwash.api.PlacesApiFactory


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val REQUEST_CHECK_SETTINGS = 43
    }
    private val api = PlacesApiFactory.placesService

    private lateinit var mMap: GoogleMap

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)



    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //initMap()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CHECK_SETTINGS)
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        val sydney = LatLng(55.7887, 49.1221)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
        showNearbyPlaces(sydney)
    }


    private fun showNearbyPlaces(location: LatLng){
        lifecycleScope.launch {
            val places = api.nearbyPlaces(
                "${location.latitude},${location.longitude}",
                3000,
                "car_wash")
                .results
            Log.e("places", places.size.toString())
            for (place in places ){
                mMap.run {
                    addMarker(
                        MarkerOptions()
                            .position(place.geometry.location.latLng)
                            .title(place.name)
                    )
                }
            }
        }
    }

}