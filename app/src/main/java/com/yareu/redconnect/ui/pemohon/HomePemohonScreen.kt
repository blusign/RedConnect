package com.yareu.redconnect.ui.pemohon

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.R
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.components.navigation.PemohonBottomNavigationBar
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePemohonScreen(
    onSOSClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onTrackDonorClick: (String) -> Unit = {},
    sosViewModel: SOSViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val allRequests by sosViewModel.emergencyRequests.collectAsState()

    // Cari request milik user ini yang statusnya BELUM selesai/batal
    val activeRequest = allRequests.find {
        it.requesterId == userProfile?.id &&
                it.status != RequestStatus.COMPLETED &&
                it.status != RequestStatus.CANCELLED
    }

    LaunchedEffect(Unit) {
        sosViewModel.fetchEmergencyRequests()
    }

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

            if (activeRequest != null) {
                ActiveRequestCard(
                    // Ambil nama pendonor pertama yang ACC / ON_WAY
                    donorName = activeRequest.respondingDonors.firstOrNull()?.donorName ?: "Mencari...",
                    onDetailClick = { onTrackDonorClick(activeRequest.id) }
                )
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
fun ActiveRequestCard(donorName: String, onDetailClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Pendonor ditemukan!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BlueAccent)
            Spacer(Modifier.height(8.dp))
            // Gunakan $donorName agar dinamis
            Text("• $donorName bersedia membantu.", fontSize = 13.sp, color = Gray, fontWeight = FontWeight.Medium)
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