package com.example.offroad.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offroad.data.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val defLocation = MapLocation(37.0902f, -95.7129f)
    private val defZoom = 2f

    private val _currentLocation = MutableStateFlow<MapLocation?>(defLocation)
    val currentLocation = _currentLocation.asStateFlow()
    private val _zoom = MutableStateFlow(defZoom)
    val zoom = _zoom.asStateFlow()

    fun forceUpdateLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val currLocation = locationTracker.getCurrentLocation()
            currLocation?.let {
                _currentLocation.value = MapLocation(it.latitude.toFloat(), it.longitude.toFloat())
                _zoom.value = 11f
            }
        }
    }

    fun resetLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _currentLocation.value = defLocation
            _zoom.value = defZoom
        }
    }
}

data class MapLocation(
    val lat: Float,
    val lng: Float
)