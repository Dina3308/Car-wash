package ru.kpfu.itis.carwash.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.FragmentMapsBinding
import ru.kpfu.itis.carwash.map.model.CarWashMarker
import javax.inject.Inject

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    @Inject
    lateinit var viewModel: MapsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        initMap()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as App).appComponent.mapsComponentFactory()
            .create(this)
            .inject(this)
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync {
            map = it ?: return@getMapAsync
            map.setOnMyLocationButtonClickListener {
                false
            }
            initSubscribes()
            checkPermissions()
        }
    }

    private fun initSubscribes() {
        with(viewModel) {
            location().observe(
                viewLifecycleOwner,
                {
                    try {
                        it.getOrThrow().apply {
                            map.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        latitude,
                                        longitude
                                    ),
                                    12f
                                )
                            )
                        }
                    } catch (throwable: Throwable) {
                        Log.e("loc", "error")
                    }
                }
            )

            carWashes().observe(
                viewLifecycleOwner,
                {
                    it.getOrNull()?.listIterator()?.apply {
                        showMarkers(this)
                    }
                }
            )
        }
    }

    private fun showMarkers(carWashes: ListIterator<CarWashMarker>) {
        for (carWash in carWashes) {
            map.run {
                addMarker(
                    MarkerOptions()
                        .position(carWash.latLng)
                        .title(carWash.title)
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkPermissions() {
        Dexter.withContext(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(
                object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        map.isMyLocationEnabled = true
                        viewModel.showNearbyCarWashes()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {}

                    override fun onPermissionRationaleShouldBeShown(
                        response: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }
            )
            .check()
    }
}
