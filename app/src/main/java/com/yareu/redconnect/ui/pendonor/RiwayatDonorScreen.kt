package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.data.DonorHistory
import com.yareu.redconnect.data.HistoryStatus
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatDonorScreen(
    onNavigate: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    val allHistory = listOf(
        DonorHistory(
            id = "1",
            date = "17 Agustus 2024",
            facilityName = "Puskesmas Sehat Selalu",
            bloodType = "A+",
            status = HistoryStatus.COMPLETED,
            points = 100
        ),
        DonorHistory(
            id = "2",
            date = "05 Juni 2024",
            facilityName = "Klinik Medika Utama",
            bloodType = "A+",
            status = HistoryStatus.CANCELLED,
            points = 0
        ),
        DonorHistory(
            id = "3",
            date = "12 April 2024",
            facilityName = "Rumah Sakit Harapan Bangsa",
            bloodType = "A+",
            status = HistoryStatus.COMPLETED,
            points = 100
        )
    )

    val filteredHistory = when (selectedTab) {
        1 -> allHistory.filter { it.status == HistoryStatus.COMPLETED }
        2 -> allHistory.filter { it.status == HistoryStatus.CANCELLED }
        else -> allHistory
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

                items(filteredHistory) { history ->
                    HistoryCard(history = history)
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
fun HistoryCard(history: DonorHistory) {
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tanggal: ${history.date}",
                    fontSize = 14.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )

                if (history.status == HistoryStatus.COMPLETED) {
                    TextButton(onClick = { /* TODO: Show detail */ }) {
                        Text(
                            text = "Selesai ✓",
                            fontSize = 12.sp,
                            color = BlueAccent
                        )
                    }
                } else {
                    Text(
                        text = "Dibatalkan",
                        fontSize = 12.sp,
                        color = Gray
                    )
                }
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

            if (history.status == HistoryStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Poin Didapat: +${history.points} poin",
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