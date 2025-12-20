package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.R
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.components.cards.EmergencyRequestCard
import com.yareu.redconnect.ui.components.cards.PersonalInfoCard
import com.yareu.redconnect.ui.components.cards.StatusToggleCard
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePendonorScreen(
    onNavigate: (String) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
    ) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val isAvailable = userProfile?.isAvailable ?: true

    // Sample data
    val emergencyRequests = listOf(
        EmergencyRequest(
            bloodType = "A+",
            facilityName = "RSUP Dr. Sardjito",
            distance = "2.5 km",
            timeAgo = "15 menit lalu"
        ),
        EmergencyRequest(
            bloodType = "A+",
            facilityName = "Klinik PMI Yogyakarta",
            distance = "4.1 km",
            timeAgo = "45 menit lalu"
        )
    )

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
                    IconButton(onClick = { /* TODO: Open notifications */ }) {
                        BadgedBox(
                            badge = { Badge(containerColor = PinkAccent) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = DarkText
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            PendonorBottomNavigationBar(
                currentRoute = "home_pendonor",
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item {
                    // 4. Ganti "Budi" jadi nama dari database
                    Text(
                        text = "Halo, ${userProfile?.name ?: "Pahlawan"}!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    StatusToggleCard(
                        isAvailable = isAvailable,
                        onToggleChange = { newValue ->
                            authViewModel.updateAvailability(newValue) // Simpan ke DB
                        }
                    )
                }
                item {
                    PersonalInfoCard()
                }
                item {
                    Text(
                        text = "Permintaan Darurat Terdekat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(emergencyRequests) { request ->
                    EmergencyRequestCard(
                        requesterName = request.requesterName,
                        bloodType = request.bloodType,
                        facilityName = request.facilityName,
                        distance = "Terdekat", // Bisa diupdate dengan LocationUtils nanti
                        timeAgo = com.yareu.redconnect.utils.DateUtils.getTimeAgo(request.createdAt),
                        onDetailClick = {
                            onNavigate(com.yareu.redconnect.navigations.Screen.DetailPermintaan.createRoute(request.id))
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
            if (!isAvailable) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable(enabled = false) { }, // Agar klik tidak tembus ke bawah
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text("😴", fontSize = 80.sp)
                        Text(
                            "Anda Sedang Istirahat",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            "Status Anda tidak aktif. Aktifkan 'Siap Donor' untuk melihat permintaan darah.",
                            color = White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = {
                                authViewModel.updateAvailability(true)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                        ) {
                            Text("Aktifkan Sekarang")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePendonorScreenPreview() {
    RedConnectTheme {
        HomePendonorScreen()
    }
}