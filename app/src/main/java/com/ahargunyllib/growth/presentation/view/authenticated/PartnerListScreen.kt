package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ahargunyllib.growth.R
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography


data class Partner(
    val id: Int,
    val name: String,
    val address: String,
    val distance: String,
    val imageRes: Int
)


@Composable
fun PartnerListScreen(authenticatedNavController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val partnerList = listOf(
        Partner(
            id = 1,
            name = "TPS 3R Baraya Runtah",
            address = "Sukaluyu, Telukjambe Timur",
            distance = "1 Km",
            imageRes = R.drawable.mitra1
        ),
        Partner(
            id = 2,
            name = "Bank Sampah DLHK Karawang",
            address = "Jl. Bypass Tanjungpura, Karawang Barat",
            distance = "2 Km",
            imageRes = R.drawable.mitra2
        ),
        Partner(
            id = 3,
            name = "Bank Sampah Cipta Usaha Mandiri",
            address = "Tegalsawah, Kec. Karawang Timur",
            distance = "4 Km",
            imageRes = R.drawable.mitra3
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Background.color)
    ) {

        // ================= TOP BAR =================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(GrowthScheme.Primary.color, shape = CircleShape)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { authenticatedNavController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = GrowthScheme.White.color,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = "Mitra",
                style = GrowthTypography.HeadingM.textStyle.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = GrowthScheme.Primary.color,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ================= SEARCH BAR =================
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            placeholder = {
                Text(
                    text = "Cari",
                    style = GrowthTypography.BodyM.textStyle.copy(fontSize = 14.sp),
                    color = GrowthScheme.Black2.color
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    keyboardController?.hide()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GrowthScheme.Black2.color
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GrowthScheme.Primary.color,
                unfocusedBorderColor = GrowthScheme.Primary.color,
                focusedContainerColor = GrowthScheme.White.color,
                unfocusedContainerColor = GrowthScheme.White.color
            ),
            textStyle = GrowthTypography.BodyM.textStyle.copy(fontSize = 14.sp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ================= LOCATION CHIP =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = GrowthScheme.Primary.color.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = GrowthScheme.Primary.color,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Malang",
                        style = GrowthTypography.BodyM.textStyle.copy(fontSize = 13.sp),
                        color = GrowthScheme.Primary.color
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ================= PARTNER LIST =================
        val filteredPartners = partnerList.filter { partner ->
            partner.name.contains(searchQuery, ignoreCase = true) ||
                    partner.address.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredPartners) { partner ->
                PartnerCard(partner)
            }
        }
    }
}

// ======================================================
// ================ PARTNER CARD =========================
// ======================================================
@Composable
fun PartnerCard(partner: Partner) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthScheme.White.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = partner.imageRes),
                contentDescription = partner.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = partner.name,
                    style = GrowthTypography.BodyL.textStyle.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    ),
                    color = GrowthScheme.Black.color
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = GrowthScheme.Black2.color,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = partner.address,
                        style = GrowthTypography.BodyM.textStyle.copy(fontSize = 13.sp),
                        color = GrowthScheme.Black2.color
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = partner.distance,
                    style = GrowthTypography.BodyM.textStyle.copy(fontSize = 13.sp),
                    color = GrowthScheme.Black2.color
                )
            }
        }
    }
}

