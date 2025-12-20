package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.components.navigation.AdminBottomNavigationBar
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

// Data dummy HANYA untuk preview di file ini
private val dummyHistoryForPreview = listOf(
    EmergencyRequest(id = "2", requesterName = "Citra W.", bloodType = "O-", bloodBags = 1, facilityName = "RS Sehat Selalu", status = RequestStatus.COMPLETED, timeAgo = "1 hari lalu"),
    EmergencyRequest(id = "3", requesterName = "Adi P.", bloodType = "B+", bloodBags = 3, facilityName = "RS Medika Utama", status = RequestStatus.CANCELLED, timeAgo = "2 hari lalu"),
    EmergencyRequest(id = "4", requesterName = "Dewi K.", bloodType = "AB+", bloodBags = 2, facilityName = "Klinik Harapan Bunda", status = RequestStatus.COMPLETED, timeAgo = "3 hari lalu")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatVerifikasiScreen(
    // Terima daftar riwayat dari NavGraph/ViewModel
    historyRequests: List<EmergencyRequest>,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {}
) {
    // State untuk mengelola tab filter yang dipilih
    var selectedFilterTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Semua", "Berhasil", "Ditolak")

    // Logika untuk memfilter daftar riwayat berdasarkan tab yang aktif
    val filteredHistory = when (selectedFilterTab) {
        1 -> historyRequests.filter { it.status == RequestStatus.COMPLETED || it.status == RequestStatus.ACCEPTED }
        2 -> historyRequests.filter { it.status == RequestStatus.CANCELLED }
        else -> historyRequests // Tab 0 (Semua)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Verifikasi", color = DarkText, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(
                selectedTab = 1,
                onTabSelected = { index ->
                    when (index) {
                        0 -> onNavigate("admin_home")
                        2 -> onNavigate("admin_profile")
                    }
                }
            )
        },
        containerColor = LightGray
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // TabRow untuk filter: Semua, Berhasil, Ditolak
            TabRow(
                selectedTabIndex = selectedFilterTab,
                containerColor = White,
                contentColor = BurgundyPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedFilterTab]),
                        color = BurgundyPrimary
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedFilterTab == index,
                        onClick = { selectedFilterTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Konten Riwayat (LazyColumn)
            if (filteredHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada riwayat untuk kategori ini.", color = Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredHistory) { request ->
                        AdminRequestCard(
                            request = request,
                            showActions = false
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RiwayatVerifikasiScreenPreview() {
    RedConnectTheme {
        
        RiwayatVerifikasiScreen(historyRequests = dummyHistoryForPreview)
    }
}
