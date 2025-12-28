package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.SuccessGreen
import com.yareu.redconnect.ui.theme.White
import com.yareu.redconnect.utils.DateUtils
import com.yareu.redconnect.utils.Constants
import java.util.Date

@Composable
fun SelesaiDonorScreen(
    donorName: String, // Terima dari navigasi
    points: Int,      // Terima dari navigasi
    onFinish: () -> Unit = {}
) {
    // Menggunakan Scaffold untuk konsistensi background
    Scaffold(
        containerColor = LightGray // Menggunakan warna background abu-abu
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎉", fontSize = 80.sp)

            Spacer(Modifier.height(16.dp))

            Text(
                "Verifikasi Berhasil!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Poin telah ditambahkan ke akun pendonor:",
                fontSize = 14.sp,
                color = Gray,
                textAlign = TextAlign.Center
            )
            Text(
                donorName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // Kartu ringkasan
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ringkasan Donor Selesai", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    HorizontalDivider(color = LightGray)
                    Spacer(Modifier.height(4.dp))

                    val nextDateMillis = System.currentTimeMillis() + (Constants.DONATION_COOLDOWN_DAYS * 24L * 60L * 60L * 1000L)
                    val nextDonationDate = DateUtils.formatDate(nextDateMillis)

                    InfoRow("Poin Diberikan", "+$points Poin")
                    InfoRow("Masa Istirahat", "${Constants.DONATION_COOLDOWN_DAYS} Hari")
                    InfoRow("Dapat Donor Kembali", nextDonationDate)
                }
            }

            Spacer(Modifier.weight(1f)) // Mendorong tombol ke bawah

            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kembali ke Beranda", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SelesaiDonorScreenPreview() {
    RedConnectTheme {
        SelesaiDonorScreen(
            donorName = "Ahmad",
            points = 100,
            onFinish = {}
        )
    }
}
