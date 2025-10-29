package com.ahargunyllib.growth.presentation.view.authenticated

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.model.Partner
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.viewmodel.PartnerListViewModel
import com.ahargunyllib.growth.utils.Resource




@Composable
fun PartnerListScreen(
    authenticatedNavController: NavController,
    viewModel: PartnerListViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val partnersState by viewModel.partnersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllPartners()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrowthScheme.Background.color)
    ) {
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

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            placeholder = {
                Text(
                    text = "Cari Mitra",
                    style = GrowthTypography.BodyM.textStyle.copy(fontSize = 14.sp),
                    color = GrowthScheme.Black2.color
                )
            },
            trailingIcon = {
                IconButton(onClick = { keyboardController?.hide() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = GrowthScheme.Black2.color
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
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

        when (partnersState) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GrowthScheme.Primary.color)
                }
            }

            is Resource.Success -> {
                val partners = (partnersState as Resource.Success<List<Partner>>).data ?: emptyList()
                val filteredPartners = partners.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.address.contains(searchQuery, ignoreCase = true)
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

            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (partnersState as Resource.Error).message
                            ?: "Gagal memuat data",
                        color = GrowthScheme.Black2.color
                    )
                }
            }
        }
    }




}




@Composable
fun PartnerCard(partner: Partner) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrowthScheme.White.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = partner.name,
                style = GrowthTypography.BodyL.textStyle.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                color = GrowthScheme.Black.color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = GrowthScheme.Primary.color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = partner.address,
                    style = GrowthTypography.BodyM.textStyle.copy(fontSize = 13.sp),
                    color = GrowthScheme.Black2.color
                )
            }
        }
    }
}

