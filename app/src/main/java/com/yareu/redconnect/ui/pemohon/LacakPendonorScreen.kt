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
import androidx.compose.material.icons.rounded.Whatsapp

@Composable
fun LacakPendonorScreen(
    request: EmergencyRequest?,
    onBackClick: () -> Unit = {}
) {
    val confirmedDonor = request?.respondingDonors?.firstOrNull {
        it.status == DonorResponseStatus.ON_WAY || it.status == DonorResponseStatus.ARRIVED
    }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Lacak Pendonor",
                onBackClick = onBackClick
            )
        },
        containerColor = LightGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Info "Pendonor Ditemukan!"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlueAccent.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = BlueAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Pendonor Ditemukan!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueAccent
                        )
                        Text(
                            text = "Silakan berkoordinasi via WhatsApp.",
                            fontSize = 13.sp,
                            color = DarkGray
                        )
                    }
                }
            }

            // Kartu donor terpilih
            if (confirmedDonor != null) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    PendonorTerpilihCard(donor = confirmedDonor)
                }
            } else {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("Menunggu konfirmasi pendonor...", color = Gray)
                }
            }

            Spacer(Modifier.weight(1f)) // Mendorong kartu status ke bawah

            // Kartu status sebagai pengganti tombol konfirmasi
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Apa selanjutnya?",
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Setelah proses donor selesai, Admin akan memverifikasi dan menutup permintaan ini. Anda akan menerima notifikasi.",
                        fontSize = 13.sp,
                        color = Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PendonorTerpilihCard(donor: DonorResponse) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp),
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "📍 ${donor.distance} dari lokasi",
                    fontSize = 13.sp,
                    color = Gray
                )
            }

            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://wa.me/62${donor.phoneNumber.removePrefix("0")}")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(SuccessGreen, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Whatsapp,
                    contentDescription = "Chat via WhatsApp",
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LacakPendonorScreenPreview() {
    RedConnectTheme {
        LacakPendonorScreen(
            request = EmergencyRequest(
                respondingDonors = listOf(
                    DonorResponse(
                        id = "1",
                        donorName = "Budi Santoso",
                        distance = "1.5 km",
                        phoneNumber = "081234567890",
                        status = DonorResponseStatus.ON_WAY
                    )
                )
            )
        )
    }
}
