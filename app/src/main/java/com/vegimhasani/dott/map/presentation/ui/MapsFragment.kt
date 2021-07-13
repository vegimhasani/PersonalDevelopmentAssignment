package com.vegimhasani.dott.map.presentation.ui

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.afollestad.assent.AssentResult
import com.afollestad.assent.Permission
import com.afollestad.assent.coroutines.awaitPermissionsResult
import com.afollestad.assent.isAllGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.vegimhasani.dott.R
import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.Restaurant
import com.vegimhasani.dott.map.presentation.ui.state.MapUiState
import com.vegimhasani.dott.map.presentation.viewmodel.MapsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
@SuppressLint("MissingPermission")
class MapsFragment : Fragment(), OnMapReadyCallback {

    val markers: MutableList<Marker> = mutableListOf()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var googleMap: GoogleMap

    private fun requestNewLocationData(latLng: LatLng) {
        mapsViewModel.onLocationRetrieved(latLng, googleMap.projection.visibleRegion.latLngBounds)
    }


    private fun setMapInteractionListeners() {
        //initialise the variables before the camera move started
        var cameraPositionBeforeCameraMoveStarted = googleMap.cameraPosition
        var cameraChangeReason = REASON_DEVELOPER_ANIMATION
        var isZoomInStarted = false
        var isZoomOutStarted = false

        googleMap.setOnCameraMoveStartedListener { reason ->
            cameraChangeReason = reason
            if (reason == REASON_GESTURE) {
                cameraPositionBeforeCameraMoveStarted = googleMap.cameraPosition
            }
        }

        googleMap.setOnCameraMoveListener {

            // track when the user starts zooming in
            if (googleMap.cameraPosition.zoom > cameraPositionBeforeCameraMoveStarted.zoom && !isZoomInStarted) {
                isZoomInStarted = true
            } else if (googleMap.cameraPosition.zoom < cameraPositionBeforeCameraMoveStarted.zoom && !isZoomOutStarted) {
                // track when the user starts zooming out
                isZoomOutStarted = true
            }
        }

        googleMap.setOnCameraIdleListener {
            // track the camera change only if it is moved by the user
            if (cameraChangeReason == REASON_GESTURE) {

                val isZoomInOrOutStarted = isZoomInStarted || isZoomOutStarted

                // tracking when the user pans the map view and if the panning is not happening during the zoom
                if (googleMap.cameraPosition.bearing == cameraPositionBeforeCameraMoveStarted.bearing && googleMap.cameraPosition.target != cameraPositionBeforeCameraMoveStarted.target
                    && !isZoomInOrOutStarted
                ) {
                    requestNewLocationData(googleMap.cameraPosition.target)
                }

                // track when the user ends the zoom in
                if (googleMap.cameraPosition.zoom > cameraPositionBeforeCameraMoveStarted.zoom && isZoomInStarted) {
                    isZoomInStarted = false
                    requestNewLocationData(googleMap.cameraPosition.target)
                }

                // track when the user ends the zoom out
                if (googleMap.cameraPosition.zoom < cameraPositionBeforeCameraMoveStarted.zoom && isZoomOutStarted) {
                    isZoomOutStarted = false
                    requestNewLocationData(googleMap.cameraPosition.target)
                }

                //reinitialise the  @cameraPosition and  @cameraChangeReason when the camera is idle
                cameraPositionBeforeCameraMoveStarted = googleMap.cameraPosition
                cameraChangeReason = REASON_DEVELOPER_ANIMATION
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun checkUserPermissions() {
        // Set to true if one or more permissions are all granted
        val permissionsGranted: Boolean = isAllGranted(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_COARSE_LOCATION)

        // Requests one or more permissions, sending the result to a callback
        if (!permissionsGranted) {
            lifecycleScope.launchWhenCreated {
                val result: AssentResult = awaitPermissionsResult(
                    Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION
                )
                val permissionGranted: Boolean = result.isAllGranted(Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION)
                if (permissionGranted) {
                    getUserLocation()
                }
            }
        } else {
            getUserLocation()
        }
    }

    private fun getUserLocation() {
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    requestNewLocationData(latLng)
                }
            }
    }

    private fun init() {
        observeViewModel()
        checkUserPermissions()
        setMapInteractionListeners()
    }

    private fun observeViewModel() {
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launchWhenStarted {
            mapsViewModel.uiState.collect { uiState ->
                // New value received
                when (uiState) {
                    is MapUiState.Loading -> Toast.makeText(requireContext(), R.string.loading_message, Toast.LENGTH_SHORT).show()
                    is MapUiState.Error -> Toast.makeText(requireContext(), R.string.error_message, Toast.LENGTH_SHORT).show()
                    is MapUiState.LocationRetrieved -> {
                        handleLocation(uiState)
                    }
                }
            }
        }
    }

    private fun handleLocation(uiState: MapUiState.LocationRetrieved) {
        if (uiState.restaurants.isNotEmpty()) {
            addMarkers(uiState.restaurants)
        } else {
            displayUserLocation(uiState.latitudeLongitude)
        }
    }

    private fun addMarkers(restaurants: List<Restaurant>) {
        markers.clear()
        restaurants.forEach { restaurant ->
            val restaurantLatLng = LatLng(restaurant.latitudeLongitude.lat, restaurant.latitudeLongitude.lng)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(restaurantLatLng)
                    .title(restaurant.name)
            )
            if (marker != null) {
                marker.tag = restaurant.id
                markers.add(marker)
            }
        }
        val builder = LatLngBounds.Builder()
        markers.forEach {
            builder.include(it.position)
        }
        val bounds = builder.build()
        val padding = 0 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cu)
    }

    private fun displayUserLocation(latitudeLongitude: LatitudeLongitude) {
        val builder = LatLngBounds.Builder()
        val userLatLng = LatLng(latitudeLongitude.lat, latitudeLongitude.lng)
        builder.include(userLatLng)
        val padding = 0 // offset from edges of the map in pixels
        val cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding)
        googleMap.animateCamera(cu)
    }


    private fun navigateToDetails(id: String) {
        val action = MapsFragmentDirections.navigateToDetailsAction(id)
        Navigation.findNavController(this.requireView()).navigate(action)
    }

    override fun onMapReady(map: GoogleMap) {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        googleMap = map
        googleMap.setOnInfoWindowClickListener {
            navigateToDetails(it.tag as String)
        }
        init()
    }
}
