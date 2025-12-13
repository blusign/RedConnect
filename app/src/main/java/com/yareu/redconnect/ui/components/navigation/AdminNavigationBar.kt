package com.yareu.redconnect.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*

@Composable
fun AdminBottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = White,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Home, "Beranda") },
            label = { Text("Beranda", fontSize = 12.sp) },
            colors = navigationBarColors()
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.History, "Riwayat") },
            label = { Text("Riwayat", fontSize = 12.sp) },
            colors = navigationBarColors()
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.Person, "Profil") },
            label = { Text("Profil", fontSize = 12.sp) },
            colors = navigationBarColors()
        )
    }
}

@Composable
private fun navigationBarColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = BurgundyPrimary,
    selectedTextColor = BurgundyPrimary,
    indicatorColor = Color.Transparent,
    unselectedIconColor = Gray,
    unselectedTextColor = Gray
)
