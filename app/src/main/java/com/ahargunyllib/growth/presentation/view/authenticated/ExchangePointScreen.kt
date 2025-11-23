package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.model.ExchangeMethod
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.viewmodel.ExchangePointViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExchangePointScreen(
    authenticatedNavController: NavController,
    rootNavController: NavController,
    viewModel: ExchangePointViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate to success screen when exchange is successful
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            authenticatedNavController.navigate(
                AuthenticatedNavObj.SuccessExchange.createRoute(
                    points = state.pointsToExchange.toIntOrNull() ?: 0,
                    amount = state.amountReceived
                )
            )
            viewModel.resetState()
        }
    }

    // Show error snackbar
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    val eWallets = state.exchangeMethods.filter { it.type.category == "E-Wallet" }
    val banks = state.exchangeMethods.filter { it.type.category == "Bank" }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = GrowthScheme.White.color
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Header
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(GrowthScheme.Primary.color)
                                .clickable { authenticatedNavController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = "Back",
                                tint = GrowthScheme.White.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "Tukar Poin",
                            style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                            color = GrowthScheme.Primary.color,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Point balance
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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

                        Column {
                            Text(
                                text = "Poinmu",
                                style = GrowthTypography.BodyL.textStyle,
                                color = GrowthScheme.Black.color
                            )
                            Text(
                                text = "${state.currentPoints}",
                                style = GrowthTypography.HeadingL.textStyle.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Info card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .drawBehind {
                                val width = size.width
                                val height = size.height
                                val greenEndPointXTop = width * 0.7f
                                val greenEndPointXBottom = width * 0.55f

                                val yellowPath = Path().apply {
                                    moveTo(greenEndPointXTop, 0f)
                                    lineTo(width, 0f)
                                    lineTo(width, height)
                                    lineTo(greenEndPointXBottom, height)
                                    close()
                                }
                                drawPath(path = yellowPath, color = GrowthScheme.Fourth.color)

                                val greenPath = Path().apply {
                                    moveTo(0f, 0f)
                                    lineTo(greenEndPointXTop, 0f)
                                    lineTo(greenEndPointXBottom, height)
                                    lineTo(0f, height)
                                    close()
                                }
                                drawPath(path = greenPath, color = GrowthScheme.Primary.color)
                            }
                            .padding(vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.6f)
                                    .padding(start = 20.dp, end = 8.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Tukar Poin",
                                    style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                                    color = GrowthScheme.White.color
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tukarkan poin yang dikumpulkan dari setoran sampah menjadi uang yang dapat ditransfer ke rekening Anda.",
                                    style = GrowthTypography.LabelL.textStyle,
                                    color = GrowthScheme.White.color,
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(0.4f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_wallet),
                                    contentDescription = "Wallet Illustration",
                                    modifier = Modifier.size(110.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Exchange amount input (only show when method is selected)
                if (state.selectedMethod != null) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = "Jumlah Poin",
                                style = GrowthTypography.LabelL.textStyle,
                                color = GrowthScheme.Black.color
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = state.pointsToExchange,
                                onValueChange = { viewModel.updatePointsToExchange(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = GrowthScheme.Disabled.color,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                placeholder = {
                                    Text(
                                        "Masukkan jumlah poin",
                                        style = GrowthTypography.LabelL.textStyle,
                                        color = GrowthScheme.Disabled.color
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                textStyle = GrowthTypography.LabelL.textStyle,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    focusedContainerColor = GrowthScheme.White.color,
                                    unfocusedContainerColor = GrowthScheme.White.color
                                )
                            )

                            state.selectedMethod?.let { method ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Min: ${method.minAmount} poin | Max: ${method.maxAmount} poin",
                                    style = GrowthTypography.LabelL.textStyle,
                                    color = GrowthScheme.Secondary.color
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Account number input
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = "Nomor Rekening/Akun",
                                style = GrowthTypography.LabelL.textStyle,
                                color = GrowthScheme.Black.color
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = state.accountNumber,
                                onValueChange = { viewModel.updateAccountNumber(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = GrowthScheme.Disabled.color,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                placeholder = {
                                    Text(
                                        "Masukkan nomor rekening/akun",
                                        style = GrowthTypography.LabelL.textStyle,
                                        color = GrowthScheme.Disabled.color
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                textStyle = GrowthTypography.LabelL.textStyle,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    focusedContainerColor = GrowthScheme.White.color,
                                    unfocusedContainerColor = GrowthScheme.White.color
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Account name input
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = "Nama Pemilik Rekening/Akun",
                                style = GrowthTypography.LabelL.textStyle,
                                color = GrowthScheme.Black.color
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = state.accountName,
                                onValueChange = { viewModel.updateAccountName(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = GrowthScheme.Disabled.color,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                placeholder = {
                                    Text(
                                        "Masukkan nama pemilik akun",
                                        style = GrowthTypography.LabelL.textStyle,
                                        color = GrowthScheme.Disabled.color
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                textStyle = GrowthTypography.LabelL.textStyle,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    focusedContainerColor = GrowthScheme.White.color,
                                    unfocusedContainerColor = GrowthScheme.White.color
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // Submit button
                    item {
                        Button(
                            onClick = { viewModel.validateAndShowConfirmation() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GrowthScheme.Primary.color
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !state.isProcessing
                        ) {
                            if (state.isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = GrowthScheme.White.color
                                )
                            } else {
                                Text(
                                    text = "Tukar Sekarang",
                                    style = GrowthTypography.BodyM.textStyle.copy(fontWeight = FontWeight.SemiBold),
                                    color = GrowthScheme.White.color
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // E-Wallets
                if (eWallets.isNotEmpty()) {
                    item {
                        SectionHeader("E-Wallet")
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(eWallets.size) { index ->
                        ExchangeMethodItem(
                            method = eWallets[index],
                            isSelected = state.selectedMethod?.id == eWallets[index].id,
                            onClick = { viewModel.selectMethod(eWallets[index]) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Banks
                if (banks.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader("Bank")
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    items(banks.size) { index ->
                        ExchangeMethodItem(
                            method = banks[index],
                            isSelected = state.selectedMethod?.id == banks[index].id,
                            onClick = { viewModel.selectMethod(banks[index]) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        // Loading overlay
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GrowthScheme.Primary.color)
            }
        }
    }

    // Confirmation dialog
    if (state.showConfirmationDialog) {
        val pointsToExchange = state.pointsToExchange.toIntOrNull() ?: 0
        val conversionRate = state.selectedMethod?.conversionRate ?: 100
        val adminFee = state.selectedMethod?.adminFee ?: 0
        val amountReceived = (pointsToExchange * conversionRate) - adminFee

        ExchangeConfirmationDialog(
            method = state.selectedMethod,
            pointsToExchange = pointsToExchange,
            accountNumber = state.accountNumber,
            accountName = state.accountName,
            amountReceived = amountReceived,
            onConfirm = { viewModel.processExchange() },
            onDismiss = { viewModel.hideConfirmationDialog() }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
private fun ExchangeMethodItem(
    method: ExchangeMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) GrowthScheme.Primary.color else GrowthScheme.Disabled.color,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) GrowthScheme.Primary.color.copy(alpha = 0.05f)
                else Color.Transparent
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = method.iconResId),
            contentDescription = "${method.name} Logo",
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = method.name,
                style = GrowthTypography.BodyM.textStyle.copy(fontWeight = FontWeight.SemiBold),
                color = if (isSelected) GrowthScheme.Primary.color else GrowthScheme.Black.color
            )
            Text(
                text = method.description,
                style = GrowthTypography.BodyL.textStyle,
                color = GrowthScheme.Secondary.color
            )
        }
    }
}

@Composable
private fun ExchangeConfirmationDialog(
    method: ExchangeMethod?,
    pointsToExchange: Int,
    accountNumber: String,
    accountName: String,
    amountReceived: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Konfirmasi Penukaran",
                style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                Text(
                    text = "Pastikan data berikut sudah benar:",
                    style = GrowthTypography.BodyM.textStyle
                )
                Spacer(modifier = Modifier.height(16.dp))

                InfoRow("Metode", method?.name ?: "-")
                InfoRow("Poin Ditukar", "$pointsToExchange poin")
                InfoRow("Nomor Rekening", accountNumber)
                InfoRow("Nama Pemilik", accountName)
                InfoRow("Jumlah Diterima", currencyFormat.format(amountReceived))

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Proses penukaran akan diproses dalam 1-3 hari kerja",
                    style = GrowthTypography.BodyL.textStyle,
                    color = GrowthScheme.Secondary.color
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Konfirmasi", color = GrowthScheme.Primary.color)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = GrowthScheme.Secondary.color)
            }
        },
        containerColor = GrowthScheme.White.color
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = GrowthTypography.BodyS.textStyle,
            color = GrowthScheme.Secondary.color
        )
        Text(
            text = value,
            style = GrowthTypography.BodyS.textStyle.copy(fontWeight = FontWeight.SemiBold),
            color = GrowthScheme.Black.color,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}
