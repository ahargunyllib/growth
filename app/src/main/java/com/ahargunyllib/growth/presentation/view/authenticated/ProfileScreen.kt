package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.design_system.AppIcons
import androidx.compose.ui.draw.shadow

@Composable
fun ProfileScreen(
    authenticatedNavController: NavController,
    rootNavController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Primary.color)
    ) {
        // HEADER BAGIAN ATAS (HIJAU)
        Spacer(modifier = Modifier.height(48.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Bagas Anugrah",
                style = GrowthTypography.HeadingL.textStyle,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "bagas@gmail.com",
                style = GrowthTypography.BodyM.textStyle,
                color = Color.White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        // CARD PUTIH MENU PROFIL
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .wrapContentHeight()
                .shadow(4.dp, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // CARD PUTIH
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .shadow(4.dp, RoundedCornerShape(24.dp))
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        MenuItem(icon = AppIcons.History, text = "Riwayat Setor")
                        MenuItem(icon = AppIcons.Edit, text = "Edit Profile")
                        MenuItem(icon = AppIcons.Settings, text = "Setting Account")
                        MenuItem(icon = AppIcons.Info, text = "About App")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // LOGOUT DI LUAR SURFACE
                LogoutButton()
            }
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrowthScheme.Primary.color,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = GrowthTypography.BodyL.textStyle,
            color = GrowthScheme.Primary.color,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = AppIcons.ArrowRight,
            contentDescription = null,
            tint = GrowthScheme.Primary.color,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun LogoutButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GrowthScheme.Error.color)
            .clickable { }
            .padding(vertical = 14.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = AppIcons.Logout,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Keluar",
                style = GrowthTypography.BodyL.textStyle,
                color = Color.White
            )
        }
    }

}
