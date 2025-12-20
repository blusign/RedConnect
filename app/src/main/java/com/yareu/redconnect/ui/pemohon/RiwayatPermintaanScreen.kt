package com.yareu.redconnect.ui.pemohon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.components.navigation.PemohonBottomNavigationBar
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.ErrorRed
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.SuccessGreen
import com.yareu.redconnect.ui.theme.White

// Data dummy untuk preview
private val dummyRequestHistory = listOf(
    EmergencyRequest(id = "1", facilityName = "RS Harapan Kita", timeAgo = "3 hari lalu", status = RequestStatus.COMPLETED, bloodType = "A+", bloodBags = 2),
    EmergencyRequest(id = "2", facilityName = "Klinik Medika", timeAgo = "1 minggu lalu", status = RequestStatus.CANCELLED, bloodType = "O-", bloodBags = 1),
    EmergencyRequest(id = "3", facilityName = "RS Sehat Selalu", timeAgo = "2 minggu lalu", status = RequestStatus.COMPLETED, bloodType = "B+", bloodBags = 3)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPermintaanScreen(
    requests: List<EmergencyRequest> = dummyRequestHistory,
    onNavigate: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Filter riwayat berdasarkan tab yang dipilih
    val filteredRequests = when (selectedTab) {
        1 -> requests.filter { it.status == RequestStatus.COMPLETED }
        2 -> requests.filter { it.status == RequestStatus.CANCELLED }
        else -> requests
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Permintaan", fontWeight = FontWeight.Bold, color = DarkText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            PemohonBottomNavigationBar(
                currentRoute = "riwayat_pemohon",
                onNavigate = onNavigate
            )
        },
        containerColor = LightGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = White,
                contentColor = BurgundyPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BurgundyPrimary
                    )
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Semua") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Selesai") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Dibatalkan") })
            }

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredRequests) { request ->
                    RequestHistoryCard(request = request)
                }
            }
        }
    }
}

@Composable
private fun RequestHistoryCard(request: EmergencyRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = request.facilityName,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = when (request.status) {
                        RequestStatus.COMPLETED -> "Selesai"
                        RequestStatus.CANCELLED -> "Dibatalkan"
                        else -> "Dalam Proses"
                    },
                    color = when (request.status) {
                        RequestStatus.COMPLETED -> SuccessGreen
                        RequestStatus.CANCELLED -> ErrorRed
                        else -> BlueAccent
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("Butuh ${request.bloodBags} kantong gol. ${request.bloodType}", fontSize = 13.sp, color = Gray)
            Text(request.timeAgo, fontSize = 13.sp, color = Gray)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RiwayatPermintaanScreenPreview() {
    RedConnectTheme {
        RiwayatPermintaanScreen()
    }
}
