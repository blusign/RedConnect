package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History // <-- GANTI IKON
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdminScreen(
    onValidateClick: (String) -> Unit = {},
    onRejectClick: (String) -> Unit = {},
    onDetailClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // permintaan aktif dan riwayat
    val allRequests = remember {
        listOf(
            EmergencyRequest(id = "1", requesterName = "Budi S.", bloodType = "A+", bloodBags = 2, facilityName = "RS Harapan Kita", status = RequestStatus.WAITING, timeAgo = "5 menit lalu"),
            EmergencyRequest(id = "2", requesterName = "Citra W.", bloodType = "O-", bloodBags = 1, facilityName = "RS Sehat Selalu", status = RequestStatus.ACCEPTED, timeAgo = "15 menit lalu"),
            EmergencyRequest(id = "3", requesterName = "Adi P.", bloodType = "B+", bloodBags = 3, facilityName = "RS Medika Utama", status = RequestStatus.CANCELLED, timeAgo = "1 jam lalu")
        )
    }
    // Filter data untuk masing-masing tab
    val activeRequests = allRequests.filter { it.status == RequestStatus.WAITING }
    val historyRequests = allRequests.filter { it.status != RequestStatus.WAITING }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RedConnect Admin", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText) },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        BadgedBox(badge = { Badge(containerColor = PinkAccent) }) {
                            Icon(Icons.Default.Notifications, "Notifikasi", tint = DarkText)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = White, tonalElevation = 0.dp) {
                // TAB BERANDA
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, "Beranda") },
                    label = { Text("Beranda", fontSize = 12.sp) },
                    colors = navigationBarColors()
                )

                // TAB RIWAYAT
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.History, "Riwayat") }, // <-- Ikon baru
                    label = { Text("Riwayat", fontSize = 12.sp) },      // <-- Teks baru
                    colors = navigationBarColors()
                )

                // TAB PROFIL
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Person, "Profil") },
                    label = { Text("Profil", fontSize = 12.sp) },
                    colors = navigationBarColors()
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> AdminBerandaContent(
                requests = allRequests,
                onValidateClick = onValidateClick,
                onRejectClick = onRejectClick,
                modifier = Modifier.padding(padding)
            )
            1 -> AdminRiwayatContent(
                requests = historyRequests,
                onDetailClick = onDetailClick,
                modifier = Modifier.padding(padding)
            )
            2 -> ProfilAdminScreen(
                // data ini akan diambil dari ViewModel
                adminName = "Budi Santoso",
                adminEmail = "budi.santoso@redconnect.admin",
                onLogoutClick = { /* TODO: Implementasi logika logout */ },
                modifier = Modifier.padding(padding)
            )
        }
    }
}


// Composable untuk konten tab Beranda
@Composable
fun AdminBerandaContent(
    requests: List<EmergencyRequest>,
    onValidateClick: (String) -> Unit,
    onRejectClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp)
    ) {
        Text("Permintaan Darurat (SOS)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(requests) { request ->
                AdminRequestCard(
                    request = request,
                    onValidateClick = { onValidateClick(request.id) },
                    onRejectClick = { onRejectClick(request.id) },
                    onDetailClick = { onValidateClick(request.id) }
                )
            }
        }
    }
}

// Composable baru untuk konten tab Riwayat
@Composable
fun AdminRiwayatContent(
    requests: List<EmergencyRequest>,
    onDetailClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp)
    ) {
        Text("Riwayat Verifikasi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(requests) { request ->
                // Menggunakan kembali AdminRequestCard
                AdminRequestCard(
                    request = request,
                    onValidateClick = {}, // Tidak ada aksi
                    onRejectClick = {},   // Tidak ada aksi
                    onDetailClick = { onDetailClick(request.id) }
                )
            }
        }
    }
}


@Composable
fun AdminRequestCard(
    request: EmergencyRequest,
    onValidateClick: () -> Unit,
    onRejectClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "${request.requesterName} - Golongan ${request.bloodType}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(request.facilityName, fontSize = 13.sp, color = Gray)
                    Text("Butuh ${request.bloodBags} kantong", fontSize = 13.sp, color = Gray)
                }
                StatusChip(status = request.status)
            }

            Spacer(Modifier.height(12.dp))

            when (request.status) {
                RequestStatus.WAITING -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onValidateClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)) {
                            Icon(Icons.Default.Check, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Validasi")
                        }

                        OutlinedButton(onClick = onRejectClick, modifier = Modifier.weight(1f), border = BorderStroke(1.dp, ErrorRed), colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)) {
                            Icon(Icons.Default.Close, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Tolak")
                        }
                    }
                }
                else -> {
                    OutlinedButton(onClick = onDetailClick, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, BurgundyPrimary)) {
                        Text("Lihat Detail")
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: RequestStatus) {
    val (text, color) = when (status) {
        RequestStatus.WAITING -> "Konfirmasi" to WarningYellow
        RequestStatus.ACCEPTED -> "Berhasil" to SuccessGreen
        RequestStatus.CANCELLED -> "Dibatalkan" to ErrorRed // Warna disesuaikan agar lebih konsisten
        else -> "Proses" to BlueAccent
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = color)
    }
}

// Helper untuk warna Navigasi
@Composable
private fun navigationBarColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = BurgundyPrimary,
    selectedTextColor = BurgundyPrimary,
    indicatorColor = Color.Transparent,
    unselectedIconColor = Gray,
    unselectedTextColor = Gray
)

@Preview(showBackground = true)
@Composable
fun HomeAdminScreenPreview() {
    RedConnectTheme {
        HomeAdminScreen()
    }
}
