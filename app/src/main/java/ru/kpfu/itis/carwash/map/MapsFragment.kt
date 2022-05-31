package ru.kpfu.itis.carwash.map

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.databinding.FragmentMapsBinding
import ru.kpfu.itis.carwash.map.model.CarWashMarker
import ru.kpfu.itis.data.BuildConfig
import javax.inject.Inject
import com.yandex.mapkit.user_location.UserLocationLayer
import ru.kpfu.itis.carwash.R
import android.graphics.PointF
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.yandex.runtime.image.ImageProvider
import ru.kpfu.itis.carwash.common.getBitmapFromVectorDrawable

class MapsFragment : Fragment(), UserLocationObjectListener {

    private lateinit var binding: FragmentMapsBinding
    private var userLocationLayer: UserLocationLayer? = null

    @Inject
    lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(BuildConfig.API_KEY_YANDEX_MAPS)
        MapKitFactory.initialize(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.mapsComponentFactory()
            .create(this)
            .inject(this)

        initSubscribes()
        checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer?.setAnchor(
            PointF(
                binding.map.width * 0.5f,
                binding.map.height() * 0.5f,
            ),
            PointF(
                binding.map.width * 0.5f,
                binding.map.height() * 0.83f,
            )
        )
    }

    override fun onObjectRemoved(p0: UserLocationView) = Unit

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) = Unit

    private fun initUserLocationLayer() {
        userLocationLayer = MapKitFactory
            .getInstance()
            .createUserLocationLayer(binding.map.mapWindow)
            .apply {
                isVisible = true
                isHeadingEnabled = true
                setObjectListener(this@MapsFragment)
            }
    }

    private fun initSubscribes() {
        with(viewModel) {
            location().observe(
                viewLifecycleOwner
            ) {
                initUserLocationLayer()
                binding.map.map.move(
                    CameraPosition(Point(it.latitude, it.longitude), 12f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 0F),
                    null
                )
            }

            carWashes().observe(
                viewLifecycleOwner
            ) {
                println(it)
                showMarkers(it)
            }

            progress().observe(
                viewLifecycleOwner
            ) {
                binding.progressBar.isVisible = it
            }

            showErrorEvent().observe(
                viewLifecycleOwner
            ) {
                showToast(it.peekContent())
            }

            carWash().observe(
                viewLifecycleOwner
            ) {
                MapsFragmentDirections.actionMapsFragmentToBottomSheetFragment(it).also { nav ->
                    findNavController().navigate(nav)
                }
            }
        }
    }

    private fun showMarkers(carWashes: List<CarWashMarker>) {
        for (carWash in carWashes) {
            val marker = binding.map.map.mapObjects.addPlacemark(
                Point(
                    carWash.latLng.latitude,
                    carWash.latLng.longitude,
                ),
                ImageProvider.fromBitmap(requireContext().getBitmapFromVectorDrawable(R.drawable.ic_location_))
            )
            marker.addTapListener { _, point ->
                viewModel.getCarWash(point.latitude, point.longitude)
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
