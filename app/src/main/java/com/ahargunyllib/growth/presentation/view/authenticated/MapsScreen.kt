package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.ahargunyllib.growth.presentation.ui.design_system.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(authenticatedNavController: NavController) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isSheetVisible by remember { mutableStateOf(false) }

    val tpsList = listOf(
        TPSData("TPS 3R Baraya Runtah", "Sukaluyu, Telukjambe Timur", "1 Km"),
        TPSData("Bank Sampah DLHK Karawang", "Jl. Bypass Tanjungpura, Karawang Barat", "2 Km"),
        TPSData("TPS 3R Mekar Asih", "Jl. Raya Kosambi, Karawang Barat", "3 Km"),
        TPSData("Bank Sampah Sejahtera", "Cikampek, Karawang Timur", "4 Km"),
        TPSData("TPS 3R Bersih Indah", "Jl. Suroto, Karawang Kota", "5 Km")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Background.color)
    ) {
        // Decorative grid background
        GridBackground()

        // Map markers
        MarkerLayer()

        // Floating buttons (show when bottom sheet hidden)
        AnimatedVisibility(visible = !isSheetVisible) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                ElevatedButton(
                    onClick = {
                        scope.launch {
                            isSheetVisible = true
                            sheetState.show()
                        }
                    },
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

        // Floating "My Location" button
        AnimatedVisibility(
            visible = !isSheetVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-20).dp, y = (-180).dp)
        ) {
            IconButton(
                onClick = { /* Focus map to user location */ },
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

        // Modal bottom sheet
        if (isSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        isSheetVisible = false
                    }
                },
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
                        text = "TPS Locations (${tpsList.size})",
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

@Composable
fun GridBackground(
    lineColor: androidx.compose.ui.graphics.Color = GrowthScheme.Disabled.color.copy(alpha = 0.4f),
    spacing: Dp = 100.dp
) {
    val spacingPx = with(LocalDensity.current) { spacing.toPx() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = lineColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += spacingPx
        }

        var y = 0f
        while (y < size.height) {
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += spacingPx
        }
    }
}

@Composable
fun MarkerLayer() {
    val markerPositions = listOf(
        Offset(0.45f, 0.5f),
        Offset(0.55f, 0.6f),
        Offset(0.52f, 0.4f),
        Offset(0.4f, 0.45f),
        Offset(0.6f, 0.55f)
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        // User location marker
        Box(
            modifier = Modifier
                .offset(
                    x = with(LocalDensity.current) { (width * 0.5f).toDp() - 6.dp },
                    y = with(LocalDensity.current) { (height * 0.55f).toDp() - 6.dp }
                )
                .size(12.dp)
                .background(GrowthScheme.Secondary.color, shape = RoundedCornerShape(6.dp))
        )

        // Partner markers
        markerPositions.forEach { offset ->
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Marker",
                tint = GrowthScheme.Error.color,
                modifier = Modifier
                    .offset(
                        x = with(LocalDensity.current) { (width * offset.x).toDp() },
                        y = with(LocalDensity.current) { (height * offset.y).toDp() }
                    )
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun TPSCard(tps: TPSData) {
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
                    .background(GrowthScheme.Secondary.color.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp)),
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

data class TPSData(
    val name: String,
    val address: String,
    val distance: String
)
