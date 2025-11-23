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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.model.MissionWithProgress
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.viewmodel.ClaimAchievementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimAchievementScreen(
    authenticatedNavController: NavController,
    missionId: String?,
    viewModel: ClaimAchievementViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Load mission when screen opens
    LaunchedEffect(missionId) {
        missionId?.let { viewModel.loadMission(it) }
    }

    // Show success dialog and navigate back when reward is claimed
    state.claimedPoints?.let { points ->
        LaunchedEffect(points) {
            authenticatedNavController.navigate(
                AuthenticatedNavObj.SuccessAchievement.createRoute(points)
            ) {
                popUpTo(AuthenticatedNavObj.AchievementScreen.route) {
                    inclusive = false
                }
            }
            viewModel.clearClaimedPoints()
        }
    }

    // Show error dialog if any
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = {
                Text(
                    text = "Error",
                    style = GrowthTypography.HeadingM.textStyle
                )
            },
            text = {
                Text(
                    text = error,
                    style = GrowthTypography.BodyM.textStyle
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }

    val mission = state.mission
    val isCompleted = mission != null && mission.isCompleted && !mission.isClaimed

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
            if (mission != null) {
                ClaimButton(
                    isCompleted = isCompleted,
                    isLoading = state.isClaiming
                ) {
                    viewModel.claimReward()
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isMissionLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(32.dp),
                        color = GrowthScheme.Primary.color
                    )
                }
                mission != null -> {
                    AchievementItem(
                        missionWithProgress = mission,
                        onClick = {}
                    )

                    DescriptionCard(missionWithProgress = mission)
                }
                else -> {
                    Text(
                        text = "Mission not found",
                        style = GrowthTypography.BodyM.textStyle,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionCard(missionWithProgress: MissionWithProgress) {
    val mission = missionWithProgress.mission
    val progress = missionWithProgress.progress

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
                color = GrowthScheme.Black.color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mission.description,
                style = GrowthTypography.BodyM.textStyle,
                color = GrowthScheme.Black2.color
            )

            // Show progress
            if (progress != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Progress Anda:",
                    style = GrowthTypography.LabelL.textStyle,
                    color = GrowthScheme.Black.color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${progress.progressValue} / ${progress.targetValue} Kg",
                    style = GrowthTypography.BodyM.textStyle,
                    color = GrowthScheme.Primary.color
                )
            }

            // Show status
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Status:",
                style = GrowthTypography.LabelL.textStyle,
                color = GrowthScheme.Black.color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when {
                    missionWithProgress.isClaimed -> "Sudah diklaim"
                    missionWithProgress.isCompleted -> "Selesai - Siap diklaim!"
                    else -> "Dalam progress"
                },
                style = GrowthTypography.BodyM.textStyle,
                color = when {
                    missionWithProgress.isClaimed -> Color(0xFF4CAF50)
                    missionWithProgress.isCompleted -> GrowthScheme.Fourth.color
                    else -> GrowthScheme.Black2.color
                },
                fontWeight = if (missionWithProgress.isCompleted && !missionWithProgress.isClaimed) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ClaimButton(
    isCompleted: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isCompleted && !isLoading,
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
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(24.dp),
                color = GrowthScheme.White.color,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Klaim",
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

