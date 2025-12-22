package com.yareu.redconnect.ui.pemohon

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Whatsapp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.data.DonorResponse
import com.yareu.redconnect.data.DonorResponseStatus
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkGray
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.SuccessGreen
import com.yareu.redconnect.ui.theme.White

@Composable
fun LacakPendonorScreen(
    requestId: String,
    sosViewModel: SOSViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    // Ambil semua request dari ViewModel
    val allRequests by sosViewModel.emergencyRequests.collectAsState()

    // Cari request yang spesifik berdasarkan ID
    val request = allRequests.find { it.id == requestId }

    // Ambil pendonor yang sudah mengonfirmasi (ACCEPTED/ON_WAY)
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
            requestId = "dummy_id"
        )
    }
}

