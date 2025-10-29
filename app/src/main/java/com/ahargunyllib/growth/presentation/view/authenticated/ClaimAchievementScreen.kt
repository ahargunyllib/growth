package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ahargunyllib.growth.model.Mission
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimAchievementScreen(
    authenticatedNavController: NavController,
    missionId: String? // ID misi akan diterima dari layar sebelumnya
) {
    // Di aplikasi nyata, Anda akan menggunakan missionId untuk mengambil data dari ViewModel.
    // Untuk sekarang, kita akan menggunakan data dummy berdasarkan ID.
    val mission = dummyMissions.find { it.id == missionId } ?: dummyMissions.first()
    val isCompleted = mission.progress >= 1.0f

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Achievement",
                        style = GrowthTypography.LabelL.textStyle,
                        color = GrowthScheme.Secondary.color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { authenticatedNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GrowthScheme.Secondary.color
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            ClaimButton(isCompleted = isCompleted) {
                val route = AuthenticatedNavObj.SuccessAchievement.createRoute(mission.points)
                authenticatedNavController.navigate(route)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AchievementItem(
                icon = mission.icon,
                category = mission.category,
                description = mission.description,
                points = mission.points,
                progress = mission.progress,
                onClick = {}
            )

            DescriptionCard(mission = mission)
        }
    }
}

@Composable
fun DescriptionCard(mission: Mission) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Deskripsi:",
                style = GrowthTypography.BodyM.textStyle,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Setor sampah hingga total ${mission.description.substringAfter("hingga ")} dan dapatkan ${mission.points} koin.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Tampilkan progress hanya jika misi belum selesai dan progress > 0
            if (mission.progress < 1.0f && mission.progress > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                // Asumsi: "10Kg" bisa diekstrak dari deskripsi, dan kita hitung nilai sebenarnya.
                val targetKg = mission.description.substringAfter("hingga ").filter { it.isDigit() }.toInt()
                val currentKg = (mission.progress * targetKg).toInt()
                Text(
                    text = "Progress: ${(mission.progress * 100).toInt()}% ($currentKg Kg)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ClaimButton(isCompleted: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isCompleted,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GrowthScheme.Fourth.color,
            contentColor = GrowthScheme.White.color,
            disabledContainerColor = GrowthScheme.Disabled.color,
            disabledContentColor = GrowthScheme.White.color
        )
    ) {
        Text(
            text = "Klaim",
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Data dummy untuk pratinjau
private val dummyMissions = listOf(
    Mission(
        id = "1",
        icon = Icons.Default.Recycling,
        category = "Setor Sampah",
        description = "Setor sampah hingga 1Kg",
        points = 150,
        progress = 1.0f
    ),
    Mission(
        id = "2",
        icon = Icons.Default.Recycling,
        category = "Setor Sampah",
        description = "Setor sampah hingga 10Kg",
        points = 400,
        progress = 0.4f
    )
)

