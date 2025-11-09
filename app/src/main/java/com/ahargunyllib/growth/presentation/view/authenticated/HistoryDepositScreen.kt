package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.model.Collection
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.viewmodel.HistoryDepositViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDepositScreen(
    navController: NavController,
    viewModel: HistoryDepositViewModel = hiltViewModel()
) {
    val historyState by viewModel.historyState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Riwayat Setor",
                        style = GrowthTypography.HeadingM.textStyle.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = GrowthScheme.Primary.color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GrowthScheme.Primary.color
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = GrowthScheme.Background.color
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            FilterDropdown()

            Spacer(modifier = Modifier.height(16.dp))

            when {
                historyState.isLoading -> {
                    LoadingState()
                }

                historyState.error != null -> {
                    ErrorState(
                        errorMessage = historyState.error!!,
                        onRetry = { viewModel.retryLoad() }
                    )
                }

                historyState.isEmpty -> {
                    EmptyState()
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(historyState.collections) { collection ->
                            HistoryItemCard(collection)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = GrowthScheme.Primary.color
            )
            Text(
                text = "Memuat riwayat setoran...",
                style = GrowthTypography.BodyM.textStyle,
                color = GrowthScheme.Black.color
            )
        }
    }
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "❌",
                style = GrowthTypography.HeadingL.textStyle,
                fontSize = 48.sp
            )
            Text(
                text = errorMessage,
                style = GrowthTypography.BodyM.textStyle,
                color = GrowthScheme.Black.color,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GrowthScheme.Primary.color
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Coba Lagi",
                    style = GrowthTypography.BodyM.textStyle,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingBag,
                contentDescription = "Empty",
                tint = GrowthScheme.Primary.color.copy(alpha = 0.5f),
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = "Belum Ada Riwayat Setoran",
                style = GrowthTypography.LabelL.textStyle.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = GrowthScheme.Black.color
            )
            Text(
                text = "Mulai setor sampah untuk mendapatkan poin",
                style = GrowthTypography.BodyM.textStyle,
                color = GrowthScheme.Black.color.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FilterDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Tanggal") }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFCACACA),
                contentColor = GrowthScheme.Black.color
            ),
            modifier = Modifier.width(140.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedFilter,
                    style = GrowthTypography.BodyM.textStyle,
                    color = GrowthScheme.Black.color
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = GrowthScheme.Black.color,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Tanggal",
                        modifier = if (selectedFilter == "Tanggal") {
                            Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        } else {
                            Modifier
                        }
                    )
                },
                onClick = {
                    selectedFilter = "Tanggal"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Poin Tertinggi",
                        modifier = if (selectedFilter == "Poin Tertinggi") {
                            Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFEFEF), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        } else {
                            Modifier
                        }
                    )
                },
                onClick = {
                    selectedFilter = "Poin Tertinggi"
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun HistoryItemCard(collection: Collection) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    val formattedDate = try {
        collection.createdAt.toLongOrNull()?.let { timestamp ->
            dateFormat.format(Date(timestamp))
        } ?: collection.createdAt
    } catch (e: Exception) {
        collection.createdAt
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = formattedDate,
                style = GrowthTypography.BodyM.textStyle,
                color = GrowthScheme.Black.color
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GrowthScheme.Primary.color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = "Deposit Icon",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Hasil Setoran",
                        style = GrowthTypography.LabelL.textStyle.copy(
                            fontWeight = FontWeight.Bold,
                            color = GrowthScheme.Black.color
                        )
                    )

                    val weightLabel = if (collection.totalWeightKg % 1.0 == 0.0) {
                        "${collection.totalWeightKg.toLong()} Kg"
                    } else {
                        String.format(Locale("id", "ID"), "%.2f Kg", collection.totalWeightKg)
                    }
                    Text(
                        text = weightLabel,
                        style = GrowthTypography.BodyM.textStyle,
                        color = GrowthScheme.Black.color
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5A33D)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Recycling,
                                contentDescription = "Points Icon",
                                tint = Color(0xFFF9C23C),
                                modifier = Modifier.size(10.dp)
                            )
                        }

                        Text(
                            text = "${collection.receivedPoints}",
                            style = GrowthTypography.BodyM.textStyle,
                            color = GrowthScheme.Black.color
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE8F5F4))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = GrowthScheme.Primary.color,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Partner ID: ${collection.partnerId}",
                            style = GrowthTypography.BodyM.textStyle.copy(
                                fontSize = 12.sp
                            ),
                            color = GrowthScheme.Primary.color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = collection.status.label,
                style = GrowthTypography.BodyM.textStyle.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = when (collection.status.value) {
                    "success" -> GrowthScheme.Secondary.color
                    "failed" -> Color.Red
                    else -> Color.Gray
                }
            )
        }
    }
}