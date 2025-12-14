package com.yareu.redconnect.ui.pemohon

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.ui.components.navigation.PemohonBottomNavigationBar
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.yareu.redconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePemohonScreen(
    onSOSClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onTrackDonorClick: () -> Unit = {}
) {
    // Sample: Ada permintaan aktif atau tidak
    val hasActiveRequest = true // dari ViewModel

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.redconnect_logo),
                        contentDescription = "RedConnect Logo",
                        modifier = Modifier.height(200.dp),
                        colorFilter = ColorFilter.tint(BurgundyPrimary)
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
            PemohonBottomNavigationBar(
                currentRoute = "home_pemohon",
                onNavigate = onNavigate
            )
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
                ActiveRequestCard(onDetailClick = onTrackDonorClick)
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
            Text("Pendonor ditemukan!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BlueAccent)
            Spacer(Modifier.height(8.dp))
            Text("• Budi Santoso (O+) sedang dalam perjalanan.", fontSize = 13.sp, color = Gray, fontWeight = FontWeight.Medium)
            Text("• Membawa 2 kantong ke RS Harapan Kita.", fontSize = 13.sp, color = Gray)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onDetailClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
            ) {
                Text("LACAK PENDONOR")
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