package com.ahargunyllib.growth.presentation.view.authenticated

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.model.TPS
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.viewmodel.MapsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MapsScreen(
    authenticatedNavController: NavController, mapsViewModel: MapsViewModel = viewModel()
) {

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    LaunchedEffect(Unit) {
        locationPermissionsState.launchMultiplePermissionRequest()
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetVisible by remember { mutableStateOf(false) }

    val tpsList by mapsViewModel.tpsList.collectAsState()
    val polylinePoints by mapsViewModel.polylinePoints.collectAsState()

    val defaultLocation = LatLng(-7.954222, 112.616611)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            try {
                val lastKnownLocation = locationClient.lastLocation.await()
                if (lastKnownLocation != null) {
                    val myLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                    userLocation = myLocation
                    mapsViewModel.calculateDistances(myLocation)
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngZoom(myLocation, 15f),
                        durationMs = 1000
                    )
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Peta Mitra",
                        style = GrowthTypography.HeadingM.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = GrowthScheme.Primary.color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { authenticatedNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GrowthScheme.Primary.color
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(GrowthScheme.Background.color)
        ) {
            if (locationPermissionsState.allPermissionsGranted) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = false,
                        zoomControlsEnabled = false,
                        zoomGesturesEnabled = true
                    )
                ) {
                    val customMarkerIcon = bitmapDescriptorFromVector(
                        context,
                        R.drawable.ic_tps_marker,
                        GrowthScheme.Primary.color.toArgb()
                    )

                    tpsList.forEach { tps ->
                        MarkerInfoWindow(
                            state = MarkerState(position = tps.location),
                            icon = customMarkerIcon,
                        ) { _ ->
                            TPSInfoWindow(tps = tps)
                        }
                    }
                    if (polylinePoints.isNotEmpty()) {
                        Polyline(points = polylinePoints, color = GrowthScheme.Primary.color, width = 8f)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Izin Lokasi Diperlukan", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Aplikasi ini membutuhkan izin lokasi untuk menampilkan peta.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }

            AnimatedVisibility(visible = !isSheetVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ElevatedButton(
                        onClick = { isSheetVisible = true },
                        shape = RoundedCornerShape(30.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = GrowthScheme.White.color,
                            contentColor = GrowthScheme.Primary.color
                        )
                    ) {
                        Icon(Icons.Default.List, contentDescription = "List")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Show list",
                            style = GrowthTypography.LabelL.textStyle
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = !isSheetVisible,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-20).dp, y = (-120).dp)
            ) {
                IconButton(
                    onClick = {
                        if (locationPermissionsState.allPermissionsGranted) {
                            coroutineScope.launch {
                                try {
                                    val lastKnownLocation = locationClient.lastLocation.await()
                                    if (lastKnownLocation != null) {
                                        val myLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(myLocation, 15f),
                                            durationMs = 1000
                                        )
                                    }
                                } catch (e: Exception) {
                                    // Handle exception
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape)
                        .background(color = GrowthScheme.White.color, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "My location",
                        tint = GrowthScheme.Primary.color
                    )
                }
            }

            if (isSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = { isSheetVisible = false },
                    sheetState = sheetState,
                    containerColor = GrowthScheme.White.color,
                    dragHandle = { BottomSheetDefaults.DragHandle() }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Lokasi TPS (${tpsList.size})",
                            style = GrowthTypography.HeadingM.textStyle,
                            modifier = Modifier.padding(bottom = 12.dp),
                            color = GrowthScheme.Primary.color
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 30.dp)
                        ) {
                            items(tpsList) { tps ->
                                TPSCard(tps = tps) {
                                    isSheetVisible = false
                                    coroutineScope.launch {
                                        val apiKey = context.packageManager
                                            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                                            .metaData.getString("com.google.android.geo.API_KEY")

                                        if (apiKey != null && userLocation != null) {
                                            mapsViewModel.getDirections(apiKey, userLocation!!, tps.location)
                                        }

                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(tps.location, 15f),
                                            durationMs = 1000
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TPSCard(tps: TPS, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = GrowthScheme.White.color
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        GrowthScheme.Secondary.color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Place,
                    contentDescription = null,
                    tint = GrowthScheme.Primary.color
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tps.name,
                    style = GrowthTypography.LabelL.textStyle,
                    color = GrowthScheme.Black2.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tps.address,
                    style = GrowthTypography.BodyM.textStyle,
                    color = GrowthScheme.Disabled.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tps.distance,
                    style = GrowthTypography.LabelL.textStyle,
                    color = GrowthScheme.Primary.color
                )
            }
        }
    }
}

@Composable
fun TPSInfoWindow(tps: TPS) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tps.name,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tps.address,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (tps.distance.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Distance: ${tps.distance}",
                    fontWeight = FontWeight.Bold,
                    color = GrowthScheme.Primary.color
                )
            }
        }
    }
}

fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int, tintColor: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        val wrappedDrawable = DrawableCompat.wrap(this)
        DrawableCompat.setTint(wrappedDrawable, tintColor)
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        wrappedDrawable.draw(canvas)
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}