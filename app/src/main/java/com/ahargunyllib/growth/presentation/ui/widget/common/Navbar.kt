package com.ahargunyllib.growth.presentation.ui.widget.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthScheme
import com.ahargunyllib.growth.presentation.ui.design_system.GrowthTypography
import com.ahargunyllib.growth.presentation.ui.navigation.nav_obj.AuthenticatedNavObj
import com.ahargunyllib.growth.presentation.viewmodel.NavbarViewModel

data class NavItem(
    val navObj: AuthenticatedNavObj,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun Navbar(
    authenticatedNavController: NavController, navbarViewModel: NavbarViewModel = hiltViewModel()
) {
    val currentPage by navbarViewModel.pageState.collectAsState()

    val items = listOf(
        NavItem(
            navObj = AuthenticatedNavObj.HomeScreen,
            label = "Home",
            selectedIcon = Icons.Outlined.Home,
            unselectedIcon = Icons.Outlined.Home,
        ), NavItem(
            navObj = AuthenticatedNavObj.AchievementScreen,
            label = "Achievement",
            selectedIcon = Icons.Outlined.EmojiEvents,
            unselectedIcon = Icons.Outlined.EmojiEvents,
        ), NavItem(
            navObj = AuthenticatedNavObj.ProfileScreen,
            label = "Profile",
            selectedIcon = Icons.Outlined.Person,
            unselectedIcon = Icons.Outlined.Person,
        )
    )

    Row(
        modifier = Modifier
            .background(color = GrowthScheme.White.color)
            .border(
                width = 1.dp,
                color = GrowthScheme.Disabled.color,
            )
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items.forEachIndexed { index, item ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                if (currentPage == index) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .background(color = GrowthScheme.Primary.color)
                            .fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                navbarViewModel.setPageState(index)
                                authenticatedNavController.navigate(item.navObj.route)
                            })
                        .padding(top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (currentPage == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (currentPage == index) GrowthScheme.Primary.color else GrowthScheme.Disabled.color,
                        modifier = Modifier.size(24.dp)
                    )
                    (if (currentPage == index) Text(
                        text = item.label, style = GrowthTypography.BodyM.textStyle.copy(
                            color = GrowthScheme.Primary.color,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        ), modifier = Modifier.padding(start = 4.dp)
                    ) else null)
                }
            }
        }
    }
}