package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.ui.components.cards.EmergencyRequestCard
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermintaanDaruratScreen(
    onNavigate: (String) -> Unit = {},
    onDetailClick: (String) -> Unit = {},
    sosViewModel: SOSViewModel = viewModel()
) {
    // Ambil data dari Firestore saat layar dibuka
    LaunchedEffect(Unit) {
        sosViewModel.fetchEmergencyRequests()
    }

    // Observasi data dari StateFlow
    val requests by sosViewModel.emergencyRequests.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Permintaan Darurat", fontWeight = FontWeight.Bold, color = DarkText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            PendonorBottomNavigationBar(
                currentRoute = "permintaan_donor",
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(requests) { request ->
                EmergencyRequestCard(
                    requesterName = request.requesterName,
                    bloodType = request.bloodType,
                    facilityName = request.facilityName,
                    distance = "Terdekat",
                    timeAgo = com.yareu.redconnect.utils.DateUtils.getTimeAgo(request.createdAt),
                    onDetailClick = {
                        onNavigate(com.yareu.redconnect.navigations.Screen.DetailPermintaan.createRoute(request.id))
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PermintaanDaruratScreenPreview() {
    RedConnectTheme {
        PermintaanDaruratScreen()
    }
}