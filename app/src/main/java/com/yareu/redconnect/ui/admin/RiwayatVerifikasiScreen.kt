package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme

@Composable
fun RiwayatVerifikasiScreen(
    historyRequests: List<EmergencyRequest>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Riwayat Verifikasi",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        Spacer(Modifier.height(16.dp))

        // Gunakan LazyColumn untuk menampilkan daftar riwayat
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(historyRequests) { request ->
                // Kita bisa menggunakan kembali AdminRequestCard,
                // karena logikanya sudah bisa menangani status 'COMPLETED' dan 'CANCELLED'
                AdminRequestCard(
                    request = request,
                    onValidateClick = { /* Tidak ada aksi validasi di riwayat */ },
                    onRejectClick = { /* Tidak ada aksi tolak di riwayat */ },
                    onDetailClick = { /* TODO: Navigasi ke detail riwayat (read-only) */ }
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RiwayatVerifikasiScreenPreview() {
    RedConnectTheme {
        val historyRequests = listOf(
            EmergencyRequest(id = "2", requesterName = "Citra W.", bloodType = "O-", bloodBags = 1, facilityName = "RS Sehat Selalu", status = RequestStatus.COMPLETED, timeAgo = "1 hari lalu"),
            EmergencyRequest(id = "3", requesterName = "Adi P.", bloodType = "B+", bloodBags = 3, facilityName = "RS Medika Utama", status = RequestStatus.CANCELLED, timeAgo = "2 hari lalu"),
            EmergencyRequest(id = "4", requesterName = "Dewi K.", bloodType = "AB+", bloodBags = 2, facilityName = "Klinik Harapan Bunda", status = RequestStatus.COMPLETED, timeAgo = "3 hari lalu")
        )
        RiwayatVerifikasiScreen(historyRequests = historyRequests)
    }
}
