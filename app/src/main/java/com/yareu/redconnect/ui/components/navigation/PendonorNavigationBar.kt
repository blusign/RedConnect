package com.yareu.redconnect.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*

@Composable
fun PendonorBottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = White,
        tonalElevation = 0.dp
    ) {
        // Beranda
        NavigationBarItem(
            selected = currentRoute == "home_donor",
            onClick = { onNavigate("home_donor") },
            icon = { Icon(Icons.Default.Home, "Beranda") },
            label = { Text("Beranda", fontSize = 12.sp) },
            colors = navigationBarColors()
        )
        // Riwayat
        NavigationBarItem(
            selected = currentRoute == "riwayat_donor",
            onClick = { onNavigate("riwayat_donor") },
            icon = { Icon(Icons.Default.History, "Riwayat") },
            label = { Text("Riwayat", fontSize = 12.sp) },
            colors = navigationBarColors()
        )
        // Permintaan
        NavigationBarItem(
            selected = currentRoute == "permintaan_donor",
            onClick = { onNavigate("permintaan_donor") },
            icon = { Icon(Icons.Default.List, "Permintaan") },
            label = { Text("Permintaan", fontSize = 12.sp) },
            colors = navigationBarColors()
        )
        // Profil
        NavigationBarItem(
            selected = currentRoute == "profil_donor",
            onClick = { onNavigate("profil_donor") },
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
