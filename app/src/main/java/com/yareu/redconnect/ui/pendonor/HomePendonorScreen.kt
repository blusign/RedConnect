package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.navigations.Screen
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.components.cards.EmergencyRequestCard
import com.yareu.redconnect.ui.components.cards.PersonalInfoCard
import com.yareu.redconnect.ui.components.cards.StatusToggleCard
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePendonorScreen(
    onNavigate: (String) -> Unit = {},
    authViewModel: AuthViewModel = viewModel(),
    sosViewModel: SOSViewModel = viewModel()
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val isAvailable = userProfile?.isAvailable ?: true
    val currentStatus = userProfile?.isAvailable ?: true

    // Ambil data dari Firestore lewat ViewModel
    val allRequests by sosViewModel.emergencyRequests.collectAsState()
    val activeRequest = allRequests.find {
        it.requesterId == userProfile?.id && it.status != RequestStatus.COMPLETED
    }

    // Ambil data setiap kali layar dibuka
    LaunchedEffect(Unit) {
        sosViewModel.fetchEmergencyRequests()
    }

    // Hanya ambil yang goldarnya sama dan statusnya WAITING
    val matchingRequests = allRequests.filter {
        val isCompatible = it.bloodType == userProfile?.bloodType &&
                it.status == com.yareu.redconnect.data.RequestStatus.WAITING

        val distance = com.yareu.redconnect.utils.LocationUtils.calculateDistance(
            userProfile?.latitude ?: 0.0,
            userProfile?.longitude ?: 0.0,
            it.latitude,
            it.longitude
        )

        isCompatible && distance <= 5000
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
                    // Ganti "Budi" jadi nama dari database
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
                        isAvailable = currentStatus, // menggunakan data dari database
                        onToggleChange = { newValue ->
                            authViewModel.updateAvailability(newValue) // Update ke database
                        }
                    )
                }
                item {
                    PersonalInfoCard(
                        bloodType = userProfile?.bloodType ?: "-",
                        totalDonations = 0,
                        points = 0,
                        status = if (isAvailable) "Siap Donor" else "Sedang Istirahat"
                    )
                }

                val acceptedRequest = allRequests.find { req ->
                    req.status == com.yareu.redconnect.data.RequestStatus.ACCEPTED &&
                            req.respondingDonors.any { it.donorId == userProfile?.id }
                }

                if (acceptedRequest != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = BlueAccent.copy(alpha = 0.1f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BlueAccent)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("🚑", fontSize = 24.sp)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text("Tugas Aktif: Permintaan dari ${acceptedRequest.requesterName}", fontWeight = FontWeight.Bold)
                                    Text("Faskes: ${acceptedRequest.facilityName}", fontSize = 12.sp, color = Gray)
                                }
                                Text(
                                    text = "LIHAT",
                                    color = BlueAccent,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        onNavigate(Screen.DetailPermintaan.createRoute(acceptedRequest.id))
                                    }
                                )
                            }
                        }
                    }
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

                if (matchingRequests.isEmpty()) {
                    item {
                        Text(
                            text = "Belum ada permintaan yang cocok dengan golongan darah Anda.",
                            color = Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp)
                        )
                    }
                } else {
                    items(matchingRequests) { request ->
                        // Simulasi koordinat (Nanti diambil dari GPS/Profile)
                        val donorLat = userProfile?.latitude ?: -7.7956 // Contoh Jogja
                        val donorLng = userProfile?.longitude ?: 110.3695

                        // Asumsikan EmergencyRequest punya field lat/lng rumah sakit
                        val distanceInMeters = com.yareu.redconnect.utils.LocationUtils.calculateDistance(
                            donorLat, donorLng,
                            request.latitude, request.longitude // Pastikan field ini ada di data class
                        )
                        val formattedDistance = com.yareu.redconnect.utils.LocationUtils.formatDistance(distanceInMeters)

                        EmergencyRequestCard(
                            requesterName = request.requesterName,
                            bloodType = request.bloodType,
                            facilityName = request.facilityName,
                            distance = formattedDistance, // Jarak Dinamis
                            timeAgo = com.yareu.redconnect.utils.DateUtils.getTimeAgo(request.createdAt),
                            onDetailClick = {
                                onNavigate(com.yareu.redconnect.navigations.Screen.DetailPermintaan.createRoute(request.id))
                            }
                        )
                    }
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