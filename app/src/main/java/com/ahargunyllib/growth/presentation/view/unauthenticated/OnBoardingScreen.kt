package com.ahargunyllib.growth.presentation.view.unauthenticated

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.UnauthenticatedNavObj
import kotlinx.coroutines.delay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PageSize


private data class Copy(@DrawableRes val imageRes: Int, val title: String, val desc: String)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    unauthenticatedNavController: NavController,
) {
    val pages = listOf(
        Copy(
            imageRes = R.drawable.onboarding_image,
            title = "Setor dengan mudah",
            desc = "Antar sampahmu ke mitra terdekat dan mulailah berkontribusi untuk lingkungan. Bersama, kita bisa membuat perbedaan nyata."
        ),
        Copy(
            imageRes = R.drawable.onboarding_image_2,
            title = "Dapatkan Poin",
            desc = "Setiap setoran sampah yang dilakukan akan memberikan poin yang bisa ditukarkan dengan uang tunai."
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(Unit) {
        while(true) {
            delay(3500)

            val nextPage = (pagerState.currentPage + 1) % pages.size

            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Primary.color)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                painterResource(id = R.drawable.growth),
                contentDescription = "Growth Logo",
                modifier = Modifier
                    .width(150.dp)
                    .height(60.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Crossfade(
                    targetState = pagerState.currentPage,
                    animationSpec = tween(500),
                    label = "Onboarding Image Crossfade"
                ) { pageIndex ->
                    Image(
                        painter = painterResource(id = pages[pageIndex].imageRes),
                        contentDescription = "Onboarding Illustration",
                        modifier = Modifier.size(360.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                PageIndicator(
                    currentIndex = pagerState.currentPage,
                    count = pages.size,
                    activeColor = Color.White,
                    inactiveColor = Color.White.copy(alpha = 0.5f)
                )

                HorizontalPager(
                    state = pagerState,
                    pageSize = PageSize.Fill,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val item = pages[page]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = item.title,
                            style = GrowthTypography.HeadingL.textStyle,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = item.desc,
                            style = GrowthTypography.BodyL.textStyle,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { unauthenticatedNavController.navigate(UnauthenticatedNavObj.Login.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GrowthScheme.Fourth.color)
                ) {
                    Text(
                        text = "Login",
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = GrowthScheme.White.color,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                OutlinedButton(
                    onClick = { unauthenticatedNavController.navigate(UnauthenticatedNavObj.Register.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(60.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = ButtonDefaults.outlinedButtonBorder.brush,
                        width = 2.dp
                    )
                ) {
                    Text(
                        text = "Register",
                        style = GrowthTypography.BodyL.textStyle.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PageIndicator(
    currentIndex: Int,
    count: Int,
    activeColor: Color,
    inactiveColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { i ->
            val isActive = i == currentIndex
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .width(if (isActive) 28.dp else 12.dp)
                    .clip(RoundedCornerShape(100))
                    .background(if (isActive) activeColor else inactiveColor)
            )
        }
    }
}

