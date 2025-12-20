package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.data.DonorResponse
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.theme.DarkGray
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.ErrorRed
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.SuccessGreen
import com.yareu.redconnect.ui.theme.WarningYellow
import com.yareu.redconnect.ui.theme.White

@Composable
fun DetailVerifikasiScreen(
    // ViewModel menyediakan data ini berdasarkan requestId
    request: EmergencyRequest = dummyDetailRequest,
    donorResponse: DonorResponse = dummyDetailDonor,
    isEligible: Boolean = false, // Hasil pengecekan riwayat donor
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onRejectClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Detail Verifikasi Donor",
                onBackClick = onBackClick
            )
        },
        containerColor = LightGray // Warna background abu-abu terang
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Agar bisa di-scroll jika konten panjang
        ) {
            Spacer(Modifier.height(16.dp))

            // 1. Kartu Detail Permintaan
            SectionCard(title = "Detail Permintaan Darurat") {
                InfoRow(label = "Pemohon", value = request.requesterName)
                InfoRow(label = "Fasilitas Kesehatan", value = request.facilityName)
                InfoRow(label = "Golongan Darah", value = request.bloodType)
                InfoRow(label = "Jumlah Kantong", value = "${request.bloodBags} kantong")
            }

            Spacer(Modifier.height(16.dp))

            // 2. Kartu Detail Pendonor
            SectionCard(title = "Pendonor yang Merespons") {
                InfoRow(label = "Nama Pendonor", value = donorResponse.donorName)
                InfoRow(label = "Golongan Darah", value = donorResponse.bloodType)
                InfoRow(label = "Total Donor", value = "${donorResponse.totalDonations} kali")
                InfoRow(label = "No. Telepon", value = donorResponse.phoneNumber)
            }

            Spacer(Modifier.height(16.dp))

            // 3. Kartu Hasil Verifikasi
            VerificationResultCard(isEligible = isEligible)

            Spacer(Modifier.weight(1f)) // Mendorong tombol ke bawah

            // 4. Tombol Aksi
            Column(modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)) {
                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp),
                    // Tombol hanya aktif jika pendonor memenuhi syarat
                    enabled = isEligible
                ) {
                    Text("Konfirmasi & Selesaikan Donor", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    border = BorderStroke(1.dp, ErrorRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        // Teks tombol berubah sesuai kondisi
                        text = if (isEligible) "Tolak Permintaan (Alasan Lain)" else "Tolak (Tidak Memenuhi Syarat)",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Composable untuk kartu-kartu bagian
@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = LightGray)
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                content()
            }
        }
    }
}

// Composable untuk kartu hasil verifikasi
@Composable
private fun VerificationResultCard(isEligible: Boolean) {
    val backgroundColor = if (isEligible) SuccessGreen.copy(0.1f) else WarningYellow.copy(0.15f)
    val outlineColor = if (isEligible) SuccessGreen else WarningYellow
    val iconColor = if (isEligible) SuccessGreen else WarningYellow
    val titleText = if (isEligible) "Pendonor Memenuhi Syarat" else "Perhatian: Pendonor Belum Memenuhi Syarat"
    val descriptionText = if (isEligible) "Pendonor dapat melanjutkan proses donor darah." else "Pendonor masih dalam masa jeda atau memiliki riwayat yang perlu diperiksa lebih lanjut."

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, outlineColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(titleText, fontWeight = FontWeight.Bold, color = DarkText)
                Text(descriptionText, fontSize = 13.sp, color = DarkGray, lineHeight = 18.sp)
            }
        }
    }
}

// Composable untuk baris info (Label: Value)
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText, textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}

// Data Dummy untuk Preview
private val dummyDetailRequest = EmergencyRequest(
    id = "REQ123",
    requesterName = "Keluarga Bpk. Budi",
    facilityName = "RS Harapan Kita",
    bloodType = "A+",
    bloodBags = 2
)
private val dummyDetailDonor = DonorResponse(
    donorName = "Citra Lestari",
    bloodType = "A+",
    totalDonations = 8,
    phoneNumber = "0812-3456-7890"
)

@Preview(name = "Pendonor Memenuhi Syarat", showBackground = true, showSystemUi = true)
@Composable
private fun DetailVerifikasiScreenEligiblePreview() {
    RedConnectTheme {
        DetailVerifikasiScreen(isEligible = true)
    }
}

@Preview(name = "Pendonor Tidak Memenuhi Syarat", showBackground = true, showSystemUi = true)
@Composable
private fun DetailVerifikasiScreenNotEligiblePreview() {
    RedConnectTheme {
        DetailVerifikasiScreen(isEligible = false)
    }
}
