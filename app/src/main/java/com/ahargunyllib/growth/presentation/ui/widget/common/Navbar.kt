package com.ahargunyllib.growth.presentation.ui.widget.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
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
    authenticatedNavController: NavController,
    navbarViewModel: NavbarViewModel = hiltViewModel()
) {
    val currentPage by navbarViewModel.pageState.collectAsState()

    val items = listOf(
        NavItem(
            navObj = AuthenticatedNavObj.HomeScreen,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavItem(
            navObj = AuthenticatedNavObj.MapsScreen,
            label = "Maps",
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map,
        ),
        NavItem(
            navObj = AuthenticatedNavObj.AchievementScreen,
            label = "Achievement",
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map,
        ),
        NavItem(
            navObj = AuthenticatedNavObj.ProfileScreen,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
        )
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentPage == index,
                onClick = {
                    navbarViewModel.setPageState(index);
                    authenticatedNavController.navigate(item.navObj.route)
                },
                icon = {
                    Icon(
                        imageVector = if (currentPage == index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}