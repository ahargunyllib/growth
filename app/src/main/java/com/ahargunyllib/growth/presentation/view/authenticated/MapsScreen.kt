package com.ahargunyllib.growth.presentation.view.authenticated

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.model.TPS
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.design_system.Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission") // Ini aman karena kita mengecek izin sebelum menggunakannya
@Composable
fun MapsScreen(authenticatedNavController: NavController) {

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

    val tpsList = listOf(
        TPS("Tempat Pembuangan Sampah Dinoyo", "Jl. Mertojoyo, Merjosari, Kec. Lowokwaru, Kota Malang, Jawa Timur 65144", "1 Km", LatLng(-7.946083, 112.603944)),
        TPS("TPS sumbersari kota malang", "Jl. Bendungan Sutami No.54SumbersariKec, Kec. Lowokwaru, Kota Malang, Jawa Timur 65145", "2 Km", LatLng(-7.960843, 112.613567)),
        TPS("TPS Doro - Karngbesuki", "Jl. Raya Candi V No.757, Doro, Karangbesuki, Kec. Sukun, Kabupaten Malang, Jawa Timur 65149", "3 Km", LatLng(-7.954151, 112.595981)),
        )

    val karawang = LatLng(-7.954222, 112.616611)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(karawang, 12f)
    }

    // Inisialisasi variabel untuk Fused Location Provider dan Coroutine Scope
    val context = LocalContext.current
    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
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
                    tpsList.forEach { tps ->
                        Marker(
                            state = MarkerState(position = tps.location),
                            title = tps.name,
                            snippet = tps.address
                        )
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

            // Sisa UI lainnya (Floating buttons dan Bottom Sheet) tetap di sini
            AnimatedVisibility(visible = !isSheetVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp), // <-- Sesuaikan padding jika perlu
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
                    .offset(x = (-20).dp, y = (-120).dp) // <-- Sesuaikan padding jika perlu
            ) {
                IconButton(
                    onClick = {
                        if (locationPermissionsState.allPermissionsGranted) {
                            coroutineScope.launch {
                                try {
                                    val lastKnownLocation = locationClient.lastLocation.await()
                                    if (lastKnownLocation != null) {
                                        val myLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

                                        // Sekarang `animate` dipanggil dari dalam coroutine scope yang benar
                                        cameraPositionState.animate(
                                            update = CameraUpdateFactory.newLatLngZoom(myLocation, 15f),
                                            durationMs = 1000
                                        )
                                    } else {
                                        // Handle kasus di mana lokasi tidak tersedia
                                        // (misalnya, lokasi dimatikan di perangkat)
                                        // Anda bisa menampilkan Toast atau Snackbar di sini
                                    }
                                } catch (e: Exception) {
                                    // Handle kemungkinan exception saat mengambil lokasi
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
                                TPSCard(tps)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TPSCard(tps: TPS) {
    ElevatedCard(
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
            Column {
                Text(
                    text = tps.name,
                    style = GrowthTypography.HeadingM.textStyle,
                    color = GrowthScheme.Black2.color
                )
                Text(
                    text = tps.address,
                    style = GrowthTypography.BodyM.textStyle,
                    color = GrowthScheme.Disabled.color
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
