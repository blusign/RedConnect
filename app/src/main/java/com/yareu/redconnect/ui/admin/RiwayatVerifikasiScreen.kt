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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.components.navigation.AdminBottomNavigationBar
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatVerifikasiScreen(
    adminViewModel: AdminViewModel = viewModel(),
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allRequests by adminViewModel.adminRequests.collectAsState()

    // Filter hanya yang COMPLETED atau CANCELLED
    val historyRequests = allRequests.filter {
        it.status == RequestStatus.COMPLETED || it.status == RequestStatus.CANCELLED
    }

    var selectedFilterTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Semua", "Berhasil", "Ditolak")

    val filteredHistory = when (selectedFilterTab) {
        1 -> historyRequests.filter { it.status == RequestStatus.COMPLETED }
        2 -> historyRequests.filter { it.status == RequestStatus.CANCELLED }
        else -> historyRequests
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
                        0 -> onNavigate("home_admin")
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
    }
}
