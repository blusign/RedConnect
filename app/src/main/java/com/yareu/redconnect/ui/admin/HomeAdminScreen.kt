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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.yareu.redconnect.ui.components.navigation.AdminBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdminScreen(
    onNavigate: (route: String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Data dummy
    val allRequests = remember {
        listOf(
            EmergencyRequest(id = "1", requesterName = "Budi S.", bloodType = "A+", bloodBags = 2, facilityName = "RS Harapan Kita", status = RequestStatus.WAITING, timeAgo = "5 menit lalu"),
            EmergencyRequest(id = "2", requesterName = "Citra W.", bloodType = "O-", bloodBags = 1, facilityName = "RS Sehat Selalu", status = RequestStatus.COMPLETED, timeAgo = "1 hari lalu"),
            EmergencyRequest(id = "3", requesterName = "Adi P.", bloodType = "B+", bloodBags = 3, facilityName = "RS Medika Utama", status = RequestStatus.CANCELLED, timeAgo = "2 hari lalu")
        )
    }
    // Pisahkan data untuk setiap tab
    val activeRequests = allRequests.filter { it.status == RequestStatus.WAITING }
    val historyRequests = allRequests.filter { it.status != RequestStatus.WAITING }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            1 -> "Riwayat Verifikasi"
                            2 -> "Profil Admin"
                            else -> "RedConnect Admin"
                        },
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notifikasi */ }) {
                        BadgedBox(badge = { Badge(containerColor = PinkAccent) }) {
                            Icon(Icons.Default.Notifications, "Notifikasi", tint = DarkText)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        }
    ) { padding ->
        // Konten berganti berdasarkan tab yang dipilih
        when (selectedTab) {
            0 -> AdminBerandaContent(
                requests = activeRequests,
                onValidateClick = { requestId -> onNavigate("detail_verifikasi/$requestId") },
                onRejectClick = { /* TODO: Implementasi logika tolak */ },
                modifier = Modifier.padding(padding)
            )
            1 -> AdminRiwayatContent(
                requests = historyRequests,
                modifier = Modifier.padding(padding)
            )
            2 -> ProfilAdminScreen(
                onLogoutClick = { onNavigate("login") }, // Contoh navigasi ke halaman login
                modifier = Modifier.padding(padding)
            )
        }
    }
}

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
        if (requests.isNotEmpty()) {
            Text("Permintaan Darurat (SOS)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
            Spacer(Modifier.height(16.dp))
        }

        if (requests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tidak ada permintaan yang perlu divalidasi.", color = Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(requests) { request ->
                    AdminRequestCard(
                        request = request,
                        showActions = true, // Tampilkan tombol di Beranda
                        onValidateClick = { onValidateClick(request.id) },
                        onRejectClick = { onRejectClick(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminRiwayatContent(
    requests: List<EmergencyRequest>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp)
    ) {
        if (requests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada riwayat verifikasi.", color = Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(requests) { request ->
                    // showActions = false, sehingga tidak ada tombol yang muncul
                    AdminRequestCard(
                        request = request,
                        showActions = false
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestCard(
    request: EmergencyRequest,
    showActions: Boolean, // Parameter krusial untuk mengontrol tampilan tombol
    onValidateClick: () -> Unit = {},
    onRejectClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
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
                        fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkText
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(request.facilityName, fontSize = 13.sp, color = Gray)
                    Text("Butuh ${request.bloodBags} kantong • ${request.timeAgo}", fontSize = 13.sp, color = Gray)
                }
                StatusChip(status = request.status)
            }

            // Tampilkan tombol HANYA jika showActions bernilai true
            if (showActions) {
                Spacer(Modifier.height(16.dp))
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
        }
    }
}

@Composable
fun StatusChip(status: RequestStatus) {
    val (text, color) = when (status) {
        RequestStatus.WAITING -> "Perlu Validasi" to WarningYellow
        RequestStatus.ACCEPTED, RequestStatus.COMPLETED -> "Berhasil" to BlueAccent
        RequestStatus.CANCELLED -> "Ditolak" to ErrorRed
        else -> "Proses" to BlueAccent
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = color)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeAdminScreenPreview() {
    RedConnectTheme {
        HomeAdminScreen()
    }
}
