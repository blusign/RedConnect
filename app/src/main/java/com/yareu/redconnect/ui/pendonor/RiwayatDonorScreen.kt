package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.auth.AuthViewModel
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
fun RiwayatDonorScreen(
    onNavigate: (String) -> Unit = {},
    sosViewModel: SOSViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val allRequests by sosViewModel.emergencyRequests.collectAsState()
    val userProfile by authViewModel.userProfile.collectAsState()

    // State untuk Tab
    var selectedTab by remember { mutableIntStateOf(0) }

    // Ambil data asli: Request yang sudah COMPLETED/CANCELLED
    val myHistory = allRequests.filter { req ->
        (req.status == RequestStatus.COMPLETED || req.status == RequestStatus.CANCELLED) &&
                req.respondingDonors.any { it.donorId == userProfile?.id }
    }

    // Filter data berdasarkan Tab yang dipilih
    val filteredHistory = when (selectedTab) {
        1 -> myHistory.filter { it.status == RequestStatus.COMPLETED }
        2 -> myHistory.filter { it.status == RequestStatus.CANCELLED }
        else -> myHistory // Tab Semua
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat Donor",
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        bottomBar = {
            PendonorBottomNavigationBar(
                currentRoute = "riwayat_donor",
                onNavigate = onNavigate
            )
        },
        containerColor = LightGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
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
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Semua", fontSize = 14.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Berhasil", fontSize = 14.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Dibatalkan", fontSize = 14.sp) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                items(filteredHistory) { historyItem -> // Gunakan hasil filter tab
                    HistoryCard(history = historyItem)
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            Button(
                onClick = { /* TODO: Download */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkAccent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Download Semua Sertifikat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HistoryCard(history: com.yareu.redconnect.data.EmergencyRequest) {
    val isSuccess = history.status == RequestStatus.COMPLETED

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    // Format tanggal dari Long (createdAt) ke String
                    text = com.yareu.redconnect.utils.DateUtils.formatDate(history.createdAt),
                    fontSize = 14.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )

                // Label Status
                Text(
                    text = if (isSuccess) "Selesai ✓" else "Dibatalkan",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSuccess) BlueAccent else Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lokasi: ${history.facilityName}",
                fontSize = 13.sp,
                color = Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Golongan Darah: ${history.bloodType}",
                fontSize = 13.sp,
                color = Gray
            )

            // Tampilkan poin hanya jika statusnya Selesai
            if (isSuccess) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Poin Didapat: +100 poin",
                    fontSize = 13.sp,
                    color = BlueAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RiwayatDonorScreenPreview() {
    RedConnectTheme {
        RiwayatDonorScreen()
    }
}