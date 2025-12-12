package com.yareu.redconnect.ui.pemohon

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePemohonScreen(
    onSOSClick: () -> Unit = {},
    onDetailRequestClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Sample: Ada permintaan aktif atau tidak
    val hasActiveRequest = true // Nanti dari ViewModel

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RedConnect",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        BadgedBox(
                            badge = { Badge(containerColor = PinkAccent) }
                        ) {
                            Icon(Icons.Default.Notifications, "Notifikasi", tint = DarkText)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, "Beranda") },
                    label = { Text("Beranda", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BurgundyPrimary,
                        selectedTextColor = BurgundyPrimary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Gray,
                        unselectedTextColor = Gray
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.History, "Riwayat") },
                    label = { Text("Riwayat", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BurgundyPrimary,
                        selectedTextColor = BurgundyPrimary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Gray,
                        unselectedTextColor = Gray
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Person, "Profil") },
                    label = { Text("Profil", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BurgundyPrimary,
                        selectedTextColor = BurgundyPrimary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Gray,
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGray)
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tombol SOS dengan animasi pulse
            SOSButton(onClick = onSOSClick)

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Tekan tombol ini untuk mencari pendonor terdekat",
                fontSize = 14.sp,
                color = Gray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(48.dp))

            // Status Permintaan
            Text(
                text = "Status Permintaan Anda",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(Modifier.height(16.dp))

            if (hasActiveRequest) {
                ActiveRequestCard(onDetailClick = onDetailRequestClick)
            } else {
                NoRequestCard()
            }
        }
    }
}

@Composable
fun SOSButton(onClick: () -> Unit) {
    // Animasi pulse
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(200.dp)
            .scale(scale),
        colors = ButtonDefaults.buttonColors(containerColor = PinkAccent),
        shape = CircleShape
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "🩸", fontSize = 48.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "BUTUH DARAH\nDARURAT?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActiveRequestCard(onDetailClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("✅ Ada permintaan aktif", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
            Spacer(Modifier.height(8.dp))
            Text("• O+ • 2 kantong", fontSize = 13.sp, color = Gray)
            Text("• 3 pendonor siap", fontSize = 13.sp, color = Gray)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onDetailClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
            ) {
                Text("LIHAT DETAIL")
            }
        }
    }
}

@Composable
fun NoRequestCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("⭕ Belum ada permintaan aktif", fontSize = 14.sp, color = Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePemohonScreenPreview() {
    MaterialTheme { HomePemohonScreen() }
}