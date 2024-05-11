package com.example.offroad.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.offroad.R
import com.example.offroad.util.LoadingContent
import com.example.offroad.util.MapTopAppBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val location by viewModel.currentLocation.collectAsState()
    val zoom by viewModel.zoom.collectAsState()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    LaunchedEffect(true) {
        locationPermissions.launchMultiplePermissionRequest()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MapTopAppBar(
                openDrawer = openDrawer,
                onMapReset = viewModel::resetLocation
            )
        },
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.forceUpdateLocation()
            }) {
                Icon(
                    Icons.Outlined.MyLocation,
                    stringResource(id = R.string.center_map),
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        MapContent(
            location = location,
            zoom = zoom,
            onRefresh = {}, // TODO: fix this
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun MapContent(
    location: MapLocation?,
    zoom: Float,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val map = rememberMapViewWithLifecycle()
    val coroutineScope = MainScope()

    LoadingContent(
        loading = false,
        onRefresh = onRefresh
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { map },
                update = { mapView ->
                    coroutineScope.launch {
                        val libreMap = mapView.awaitMap()
                        libreMap.setStyle("https://demotiles.maplibre.org/style.json")
                        location?.let {
                            libreMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(it.lat.toDouble(), it.lng.toDouble()), zoom.toDouble()
                                )
                            )
                        }
                    }
                }
            )
        }
    }
}
