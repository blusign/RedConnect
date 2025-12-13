package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.ui.components.cards.*
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePendonorScreen(onNavigate: (String) -> Unit = {}) {
    var isAvailable by remember { mutableStateOf(true) }

    // Sample data
    val emergencyRequests = listOf(
        EmergencyRequest(
            bloodType = "A+",
            facilityName = "RSUP Dr. Sardjito",
            distance = "2.5 km",
            timeAgo = "15 menit lalu"
        ),
        EmergencyRequest(
            bloodType = "A+",
            facilityName = "Klinik PMI Yogyakarta",
            distance = "4.1 km",
            timeAgo = "45 menit lalu"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Profile Avatar
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8D5C4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF5E6D3))
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Halo, Budi!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    }
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
                currentRoute = "home_donor",
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
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
                StatusToggleCard(
                    isAvailable = isAvailable,
                    onToggleChange = { isAvailable = it }
                )
            }
            item {
                PersonalInfoCard()
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

            items(emergencyRequests) { request ->
                EmergencyRequestCard(
                    bloodType = request.bloodType,
                    facilityName = request.facilityName,
                    distance = request.distance,
                    timeAgo = request.timeAgo,
                    onDetailClick = { /* TODO: Navigate */ }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
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