package ru.kpfu.itis.carwash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationProviderClient = FusedLocationProviderClient(this)


    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
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