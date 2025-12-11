package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.components.cards.EmergencyRequestCard
import com.yareu.redconnect.data.EmergencyRequest

@Composable
fun PermintaanDaruratScreen(
    onBackClick: () -> Unit = {},
    onDetailClick: (String) -> Unit = {}
) {
    val requests = listOf(
        EmergencyRequest(
            id = "1",
            bloodType = "A+",
            facilityName = "RSUP Dr. Sardjito",
            distance = "2.5 km",
            timeAgo = "15 menit lalu"
        ),
        EmergencyRequest(
            id = "2",
            bloodType = "A+",
            facilityName = "Klinik PMI Yogyakarta",
            distance = "4.1 km",
            timeAgo = "45 menit lalu"
        ),
        EmergencyRequest(
            id = "3",
            bloodType = "A+",
            facilityName = "RS Panti Rapih",
            distance = "5.2 km",
            timeAgo = "1 jam lalu"
        ),
        EmergencyRequest(
            id = "4",
            bloodType = "A+",
            facilityName = "RS Bethesda",
            distance = "6.8 km",
            timeAgo = "2 jam lalu"
        ),
        EmergencyRequest(
            id = "5",
            bloodType = "A+",
            facilityName = "RSU Queen Latifa",
            distance = "8.1 km",
            timeAgo = "3 jam lalu"
        )
    )

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Permintaan Darurat",
                onBackClick = onBackClick
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
                    bloodType = request.bloodType,
                    facilityName = request.facilityName,
                    distance = request.distance,
                    timeAgo = request.timeAgo,
                    onDetailClick = { onDetailClick(request.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PermintaanDaruratScreenPreview() {
    MaterialTheme {
        PermintaanDaruratScreen()
    }
}