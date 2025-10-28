package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.SolidColor
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj

@Composable
fun SuccessDepositScreen(
    authenticatedNavController: NavController,
    points: Int,
    weight: Int
) {
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
                    text = "Voucher",
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
                text = "Terima kasih telah menyetor sampah mu! Cek Riwayat setormu untuk melihat detail setoranmu.",
                style = GrowthTypography.BodyL.textStyle,
                color = GrowthScheme.White.color,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    icon = {
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
                                contentDescription = "Poin Icon",
                                tint = Color(0xFFF9C23C),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    },
                    text = points.toString()
                )

                InfoItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_trash),
                            contentDescription = "Trash Icon",
                            tint = GrowthScheme.Fourth.color,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = "$weight kg"
                )
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
                        text = "Kembali",
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = GrowthScheme.Primary.color,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = SolidColor(GrowthScheme.White.color)
                    )
                ) {
                    Text(
                        text = "Riwayat Setor",
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = GrowthScheme.White.color,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(icon: @Composable () -> Unit, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()

        Text(
            text = text,
            style = GrowthTypography.BodyL.textStyle,
            color = GrowthScheme.White.color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessDepositScreenPreview() {
    SuccessDepositScreen(
        authenticatedNavController = rememberNavController(),
        points = 20,
        weight = 2
    )
}
