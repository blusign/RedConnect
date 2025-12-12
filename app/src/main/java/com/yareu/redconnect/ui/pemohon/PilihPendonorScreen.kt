package com.yareu.redconnect.ui.pemohon

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.DonorResponse
import com.yareu.redconnect.data.DonorResponseStatus
import com.yareu.redconnect.data.RequestStatus

@Composable
fun PilihPendonorScreen(
    request: EmergencyRequest? = null, // terima data request
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    // Ambil list donors dari request
    val donors = request?.respondingDonors ?: emptyList()
    val selectedDonor = donors.firstOrNull { it.status == DonorResponseStatus.COMPLETED }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Pilih Pendonor",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Daftar Pendonor (${donors.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Pilih satu pendonor untuk dikonfirmasi dan melanjutkan proses",
                        fontSize = 12.sp,
                        color = Gray
                    )
                }
            }

            // Detail Permintaan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = BurgundyPrimary),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Detail Permintaan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Spacer(Modifier.height(8.dp))

                    Row {
                        Text("🩸 ", fontSize = 16.sp)
                        Text("Golongan Darah", fontSize = 13.sp, color = White.copy(0.8f))
                        Spacer(Modifier.weight(1f))
                        Text(
                            request?.bloodType ?: "-",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Row {
                        Text("💉 ", fontSize = 16.sp)
                        Text("Kebutuhan", fontSize = 13.sp, color = White.copy(0.8f))
                        Spacer(Modifier.weight(1f))
                        Text(
                            "${request?.bloodBags ?: 0} Kantong",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Row {
                        Text("📍 ", fontSize = 16.sp)
                        Text("Lokasi", fontSize = 13.sp, color = White.copy(0.8f))
                        Spacer(Modifier.weight(1f))
                        Text(
                            request?.facilityName ?: "-",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = White,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Pesan Konfirmasi Terkirim (jika ada donor yang dipilih)
            if (selectedDonor != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlueAccent.copy(alpha = 0.1f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = BlueAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Pesan Konfirmasi Terkirim",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = BlueAccent
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "${selectedDonor.donorName} telah diberi tahu bahwa ia telah dipilih. Silakan lanjutkan koordinasi melalui chat.",
                    fontSize = 12.sp,
                    color = Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))
            }

            // List Donors
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(donors) { donor ->
                    PendonorCard(
                        donor = donor,
                        isSelected = donor.id == selectedDonor?.id
                    )
                }
            }

            // Confirm Button
            if (selectedDonor != null) {
                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Tutup Permintaan Pendonor",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PendonorCard(
    donor: DonorResponse,
    isSelected: Boolean = false
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BlueAccent.copy(alpha = 0.1f) else White
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = donor.donorName.firstOrNull()?.toString() ?: "?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BurgundyPrimary
                )
            }

            Spacer(Modifier.width(12.dp))

            // Info
            Column(Modifier.weight(1f)) {
                Text(
                    donor.donorName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "📍 ${donor.distance}",
                    fontSize = 12.sp,
                    color = Gray
                )
            }

            // Status Check atau Buttons
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Dipilih",
                    tint = BlueAccent,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Column(horizontalAlignment = Alignment.End) {
                    // WhatsApp Button
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/62${donor.phoneNumber.removePrefix("0")}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(SuccessGreen, CircleShape)
                    ) {
                        Text("💬", fontSize = 18.sp)
                    }

                    Spacer(Modifier.height(4.dp))

                    // Call Button
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${donor.phoneNumber}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(BlueAccent, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Telepon",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PilihPendonorScreenPreview() {
    MaterialTheme {
        PilihPendonorScreen(
            request = EmergencyRequest(
                id = "req_123",
                bloodType = "O+",
                bloodBags = 2,
                facilityName = "RSUD Cipto Mangunkusumo",
                status = RequestStatus.ACCEPTED,
                respondingDonors = listOf(
                    DonorResponse(
                        id = "1",
                        donorName = "Budi Santoso",
                        bloodType = "O+",
                        distance = "1.5 km",
                        phoneNumber = "081234567890",
                        status = DonorResponseStatus.READY
                    ),
                    DonorResponse(
                        id = "2",
                        donorName = "Citra Lestari",
                        bloodType = "A+",
                        distance = "2.1 km",
                        phoneNumber = "081234567891",
                        status = DonorResponseStatus.READY
                    ),
                    DonorResponse(
                        id = "3",
                        donorName = "Eko Wijoyo",
                        bloodType = "O+",
                        distance = "3.6 km",
                        phoneNumber = "081234567892",
                        status = DonorResponseStatus.READY
                    )
                )
            )
        )
    }
}