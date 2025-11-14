package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.design_system.Theme
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj

@Composable
fun SuccessAchievementScreen(
    authenticatedNavController: NavController,
    points: Int? // Menerima jumlah poin yang didapat
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Primary.color),
        contentAlignment = Alignment.Center
    ) {
        // Background pattern (optional)
//        Image(
//            painter = painterResource(id = R.drawable.ic_leaf_pattern), // Ganti dengan drawable pattern daun Anda
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxSize()
//                .alpha(0.1f)
//        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Achievement",
                color = Color.White,
                style = GrowthTypography.LabelL.textStyle
            )

            Spacer(modifier = Modifier.height(64.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_achievement_medal), // Ganti dengan drawable medal Anda
                contentDescription = "Achievement Medal",
                modifier = Modifier.size(128.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Berhasil!",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Achievement berhasil diklaim!\nSilahkan cek poin mu!",
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_coin), // Ganti dengan drawable koin Anda
                    contentDescription = "Points",
                    modifier = Modifier.size(20.dp),
                    tint = GrowthScheme.Fourth.color
                )
                Text(
                    text = points?.toString() ?: "0",
                    color = GrowthScheme.White.color,
                    style = GrowthTypography.BodyM.textStyle
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Spacer untuk mendorong tombol ke bawah

            // Tombol Kembali
            Button(
                onClick = {
                    // Kembali ke daftar achievement, hapus ClaimScreen dari backstack
                    authenticatedNavController.navigate(AuthenticatedNavObj.AchievementScreen.route) {
                        popUpTo(AuthenticatedNavObj.AchievementScreen.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = GrowthScheme.Primary.color
                )
            ) {
                Text(
                    "Kembali",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = GrowthTypography.BodyM.textStyle
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol Tukar Poin
            OutlinedButton(
                onClick = {
                    // Navigasi ke halaman tukar poin
                    authenticatedNavController.navigate(AuthenticatedNavObj.ExchangePoint.route)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(
                    "Tukar Poin",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = GrowthTypography.BodyM.textStyle
                )
            }
        }
    }
}

