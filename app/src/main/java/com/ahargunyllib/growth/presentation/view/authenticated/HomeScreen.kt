package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    authenticatedNavController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeState.collectAsState()

    // Refresh data when returning to this screen
    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ================= HEADER =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GrowthScheme.Primary.color)
                .height(170.dp)
        ) {
            // === Daun ===
            Image(
                painter = painterResource(id = R.drawable.bunga),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(250.dp)
                    .offset(x = (-30).dp, y = (20).dp)
            )

            // === Nama user ===
            Column(
                modifier = Modifier
                    .padding(start = 24.dp, top = 48.dp)
            ) {
                Text(
                    text = "Selamat datang,",
                    color = GrowthScheme.White.color,
                    style = GrowthTypography.BodyM.textStyle
                )
                Text(
                    text = homeState.user?.name ?: "Pengguna",
                    color = GrowthScheme.White.color,
                    style = GrowthTypography.HeadingM.textStyle
                )
            }

            // === Ikon notifikasi ===
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 24.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(GrowthScheme.White.color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = GrowthScheme.Primary.color
                )
            }
        }

        // ================= CARD POIN =================
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .offset(y = (-35).dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GrowthScheme.White.color),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // === Coin & poin ===
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CoinIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Poinmu",
                            color = GrowthScheme.Black2.color,
                            style = GrowthTypography.BodyM.textStyle.copy(fontSize = 12.sp)
                        )
                        if (homeState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = GrowthScheme.Primary.color,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "${homeState.pointAccount?.balance ?: 0}",
                                color = GrowthScheme.Black.color,
                                style = GrowthTypography.HeadingL.textStyle
                            )
                        }
                    }
                }

                // === Tukar poin kanan ===
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                    authenticatedNavController.navigate(AuthenticatedNavObj.ExchangePoint.route)
                }) {
                    Text(
                        text = "Tukar Poin",
                        color = GrowthScheme.Primary.color,
                        style = GrowthTypography.LabelL.textStyle
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = GrowthScheme.Primary.color,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // ================= CARD GROWTH =================
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .offset(y = (-20).dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = GrowthScheme.Secondary.color),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Growth",
                        color = GrowthScheme.White.color,
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Aplikasi inovatif yang mengubah sampah menjadi nilai nyata. Setor, kumpulkan dan kontribusi.",
                        color = GrowthScheme.White.color,
                        style = GrowthTypography.BodyM.textStyle.copy(
                            fontSize = 12.sp,
                            lineHeight = 14.sp
                        )
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(110.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ================= MENU SETOR & MITRA =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-10).dp),
            horizontalArrangement = Arrangement.Center
        ) {
            MenuBox(icon = Icons.Default.Upload, title = "Setor", onClick = {
                authenticatedNavController.navigate(AuthenticatedNavObj.ScanQR.route)
            })
            Spacer(modifier = Modifier.width(20.dp))
            MenuBox(icon = Icons.Default.Handshake, title = "Mitra",
                onClick = {
                    // added onclick to Test MapsScreen
                    authenticatedNavController.navigate(AuthenticatedNavObj.MapsScreen.route)
                })
        }

        // ================= MISI =================
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .offset(y = (-4).dp)
        ) {
            Text(
                text = "Misi",
                color = GrowthScheme.Black.color,
                style = GrowthTypography.LabelL.textStyle
            )
            Spacer(modifier = Modifier.height(6.dp))
            MissionItem(target = "1 Kg", desc = "Setor sampah harianmu!", points = "150")
            MissionItem(target = "10 Kg", desc = "Setor sampah harianmu!", points = "500")
        }
    }
}

@Composable
fun CoinIcon() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFE5A33D), Color(0xFFD4942D))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Recycling,
            contentDescription = null,
            tint = Color(0xFFF9C23C),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun MenuBox(icon: ImageVector, title: String, onClick: () -> Unit = {})  {
    Card(
        modifier = Modifier
            .width(130.dp)
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthScheme.White.color),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = GrowthScheme.Primary.color)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = GrowthScheme.Black2.color,
                style = GrowthTypography.LabelL.textStyle
            )
        }
    }
}

@Composable
fun MissionItem(target: String, desc: String, points: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthScheme.White.color),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.TrackChanges,
                contentDescription = null,
                tint = GrowthScheme.Primary.color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = target,
                    color = GrowthScheme.Black.color,
                    style = GrowthTypography.LabelL.textStyle.copy(fontSize = 13.sp)
                )
                Text(
                    text = desc,
                    color = GrowthScheme.Black2.color,
                    style = GrowthTypography.BodyM.textStyle.copy(fontSize = 10.sp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFE5A33D), Color(0xFFD4942D))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Recycling,
                        contentDescription = null,
                        tint = Color(0xFFF9C23C),
                        modifier = Modifier.size(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = points,
                    color = GrowthScheme.Black2.color,
                    style = GrowthTypography.BodyM.textStyle.copy(fontSize = 12.sp)
                )
            }
        }
    }
}