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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography

@Composable
fun ExchangePointScreen(
    authenticatedNavController: NavController,
    rootNavController: NavController,
    points: Int = 0
) {
    val eWallets = listOf(
        PaymentMethod("Dana", "Tanpa potongan admin", R.drawable.ic_dana)
    )
    val banks = listOf(
        PaymentMethod("BRI", "BRI Virtual Account", R.drawable.
        ic_bri),
        PaymentMethod("Mandiri", "Mandiri Virtual Account", R.drawable.ic_mandiri),
        PaymentMethod("BNI", "BNI Virtual Account", R.drawable.ic_bni),
        PaymentMethod("BCA", "BCA Virtual Account", R.drawable.ic_bca)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GrowthScheme.White.color
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
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
                            text = points.toString(),
                            style = GrowthTypography.HeadingL.textStyle.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

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
                                text = "Tukarkan poin yang dikumpulkan dari setoran sampah menjadi berbagai hadiah menarik atau uang.",
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

            item {
                SectionHeader("E-Wallet")
                Spacer(modifier = Modifier.height(16.dp))

            }
            items(eWallets.size) { index ->
                PaymentOptionItem(paymentMethod = eWallets[index])
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader("Bank")
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(banks.size) { index ->
                PaymentOptionItem(paymentMethod = banks[index])
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class PaymentMethod(
    val name: String,
    val description: String,
    val iconResId: Int
)

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
private fun PaymentOptionItem(paymentMethod: PaymentMethod) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(
                width = 1.dp,
                color = GrowthScheme.Disabled.color,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = paymentMethod.iconResId),
            contentDescription = "${paymentMethod.name} Logo",
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = paymentMethod.name,
                style = GrowthTypography.BodyM.textStyle.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = paymentMethod.description,
                style = GrowthTypography.BodyM.textStyle,
                color = GrowthScheme.Black.color
            )
        }
    }
}
