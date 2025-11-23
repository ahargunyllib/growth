package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SuccessExchangeScreen(
    authenticatedNavController: NavController,
    points: Int,
    amount: Int
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GrowthScheme.Primary.color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = "Tukar Poin",
                    style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                    color = GrowthScheme.White.color,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_success_medal),
                contentDescription = "Success Medal",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sukses!",
                style = GrowthTypography.HeadingL.textStyle.copy(fontWeight = FontWeight.Bold),
                color = GrowthScheme.White.color
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Penukaran poin berhasil diproses! Dana akan ditransfer ke rekening Anda dalam 1-3 hari kerja.",
                style = GrowthTypography.BodyL.textStyle,
                color = GrowthScheme.White.color,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Exchange details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GrowthScheme.White.color.copy(alpha = 0.15f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Poin Ditukar",
                            style = GrowthTypography.BodyM.textStyle,
                            color = GrowthScheme.White.color.copy(alpha = 0.8f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
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
                                    contentDescription = "Poin Icon",
                                    tint = Color(0xFFF9C23C),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Text(
                                text = "$points",
                                style = GrowthTypography.BodyM.textStyle.copy(fontWeight = FontWeight.Bold),
                                color = GrowthScheme.White.color
                            )
                        }
                    }

                    Divider(
                        color = GrowthScheme.White.color.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jumlah Diterima",
                            style = GrowthTypography.BodyM.textStyle,
                            color = GrowthScheme.White.color.copy(alpha = 0.8f)
                        )
                        Text(
                            text = currencyFormat.format(amount),
                            style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                            color = GrowthScheme.Fourth.color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        authenticatedNavController.navigate(AuthenticatedNavObj.HomeScreen.route) {
                            popUpTo(authenticatedNavController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GrowthScheme.White.color)
                ) {
                    Text(
                        text = "Kembali ke Beranda",
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = GrowthScheme.Primary.color,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        authenticatedNavController.navigate(AuthenticatedNavObj.ExchangePoint.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(GrowthScheme.White.color)
                    )
                ) {
                    Text(
                        text = "Tukar Lagi",
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = GrowthScheme.White.color,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
