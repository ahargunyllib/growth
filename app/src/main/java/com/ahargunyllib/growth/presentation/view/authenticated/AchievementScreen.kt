package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.Theme
import com.ahargunyllib.growth.model.Mission // <<--- IMPORT YOUR EXISTING MISSION MODEL
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj.AchievementScreen.route

@Composable
fun AchievementScreen(
    authenticatedNavController: NavController,
    rootNavController: NavController
) {
    val missions: List<Mission> = listOf(
        Mission(id = "1", icon = Icons.Default.Recycling, category = "Setor Sampah", description = "Setor sampah hingga 1Kg", points = 150, progress = 1.0f),
        Mission(id = "2", icon = Icons.Default.Recycling, category = "Setor Sampah", description = "Setor sampah hingga 10Kg", points = 400, progress = 0.4f),
        Mission(id = "3", icon = Icons.Default.Newspaper, category = "Artikel", description = "Membaca 3 artikel", points = 50, progress = 0.3f),
        Mission(id = "4", icon = Icons.Default.CalendarMonth, category = "Event", description = "Ikut 1 event dan selesaikan", points = 100, progress = 1.0f),
        Mission(id = "5", icon = Icons.Default.CalendarMonth, category = "Event", description = "Ikut 5 event dan selesaikan", points = 600, progress = 0.2f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Use background from theme
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AchievementHeader()
        Spacer(modifier = Modifier.height(24.dp))
        AchievementList(
            missions = missions,
            onItemClick = { missionId ->
                val route = AuthenticatedNavObj.ClaimAchievement.createRoute(missionId)
                authenticatedNavController.navigate(route)
            }
        )
    }
}

@Composable
fun AchievementHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthScheme.Primary.color) // Use primary color
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        GrowthScheme.Fourth.color, // Use secondary color
                        shape = RoundedCornerShape(topStartPercent = 60, bottomStartPercent = 100)
                    )
                    .clip(shape = RoundedCornerShape(topStartPercent = 60, bottomStartPercent = 100))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Achievement",
                        color = GrowthScheme.White.color,
                        style = GrowthTypography.LabelL.textStyle
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Setiap misi yang berhasil akan memberikan poin sebagai reward, yang dapat ditukarkan berbagai hadiah menarik.",
                        color = GrowthScheme.White.color, // Text on primary
                        style = GrowthTypography.BodyS.textStyle
                        )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_achievement_medal), // Replace with your medal icon
                    contentDescription = "Achievement Medal",
                    modifier = Modifier.size(104.dp)
                )
            }
        }
    }
}

@Composable
fun AchievementList(
    missions: List<Mission>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(missions) { mission ->
            AchievementItem(
                icon = mission.icon,
                category = mission.category,
                description = mission.description,
                points = mission.points,
                progress = mission.progress,
                onClick = { onItemClick(mission.id) } // Teruskan id misi saat diklik
            )
        }
    }
}

@Composable
fun AchievementItem(
    icon: ImageVector,
    category: String,
    description: String,
    points: Int,
    progress: Float,
    onClick: () -> Unit
) {
    val isCompleted = progress >= 1.0f
    val borderColor = if (isCompleted) GrowthScheme.Fourth.color else GrowthScheme.Disabled.color

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Card background
        border = BorderStroke(2.dp, borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = icon,
                        contentDescription = category,
                        modifier = Modifier.size(24.dp),
                        tint = GrowthScheme.Primary.color // Icon tint
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column() {
                        Text(
                            text = category,
                            style = GrowthTypography.LabelL.textStyle,
                        )
                        Text(
                            text = description,
                            style = GrowthTypography.BodyM.textStyle
                        )
                    }
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
                        text = points.toString(),
                        color = GrowthScheme.Secondary.color,
                        style = GrowthTypography.LabelL.textStyle.copy(fontSize = 12.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GrowthScheme.Primary.color, // Progress bar color
                    trackColor = GrowthScheme.Disabled.color, // Progress bar background
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = GrowthTypography.BodyS.textStyle
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun AchievementScreenPreview() {
    AchievementScreen(
        authenticatedNavController = rememberNavController(),
        rootNavController = rememberNavController()
    )
}