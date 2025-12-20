package com.yareu.redconnect.ui.pendonor

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.rounded.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.SuccessGreen
import com.yareu.redconnect.ui.theme.White

@Composable
fun DetailPermintaanScreen(
    requestId: String,
    onBackClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit,
    sosViewModel: SOSViewModel = viewModel()
) {
    val context = LocalContext.current

    val requests by sosViewModel.emergencyRequests.collectAsState()

    val request = requests.find { it.id == requestId }

    // Jika data belum dimuat atau tidak ditemukan
    if (request == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PinkAccent)
        }
        return
    }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Detail Permintaan",
                onBackClick = onBackClick
            )
        },
        containerColor = LightGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()) // Agar halaman bisa di-scroll
        ) {
            // KARTU DETAIL PERMINTAAN
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    // Gambar Placeholder Peta
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentAlignment = Alignment.Center // Agar tulisan berada di tengah
                    ) {
                        // Gambar Placeholder Peta (sebagai background)
                        Image(
                            painter = painterResource(id = R.drawable.map_placeholder),
                            contentDescription = "Peta Lokasi",
                            modifier = Modifier.fillMaxSize(), // Memenuhi ukuran Box
                            contentScale = ContentScale.Crop
                        )

                        // Lapisan gelap transparan agar tulisan lebih terbaca
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f))
                        )

                        Text(
                            text = "(hanya ilustrasi)",
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    // Detail Teks
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 3. GUNAKAN DATA ASLI DARI FIREBASE
                        InfoRow(label = "Nama Pasien", value = request.requesterName)
                        InfoRow(label = "Golongan Darah", value = request.bloodType)
                        InfoRow(label = "Jumlah Kantong", value = "${request.bloodBags} Kantong")
                        InfoRow(label = "Lokasi Tujuan", value = request.facilityName)
                        // Note: Tambahkan field alamat jika ada di EmergencyRequest
                        InfoRow(label = "Catatan", value = request.note.ifEmpty { "-" }, isVertical = true)
                    }
                }
            }

            // Info pendonor lain
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = White,
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "3 pendonor lain juga siap membantu",
                        fontSize = 13.sp,
                        color = Gray
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // TOMBOL BUKA MAPS & CHAT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol Buka Maps
                Button(
                    onClick = {
                        val gmmIntentUri = Uri.parse("google.navigation:q=${request.facilityAddress}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        // Cek apakah aplikasi Google Maps terinstall
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            // Fallback jika tidak ada, buka di browser
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${request.facilityAddress}"))
                            context.startActivity(browserIntent)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, "Buka Maps")
                    Spacer(Modifier.width(8.dp))
                    Text("BUKA MAPS")
                }

                // Tombol Chat (WhatsApp)
                Button(
                    onClick = {
                        // Ganti nomor HP dengan data dari request
                        val phoneNumber = "6281234567890"
                        val message = "Halo, saya pendonor dari RedConnect dan bersedia membantu permintaan darah Anda."
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Whatsapp,
                        contentDescription = "Chat via WhatsApp",
                        tint = White // Pastikan ikon berwarna putih
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("CHAT")
                }
            }

            Spacer(Modifier.weight(1f)) // Mendorong tombol Terima/Tolak ke bawah

            // TOMBOL TOLAK & TERIMA
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRejectClick,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, PinkAccent)
                ) {
                    Text("TOLAK", color = PinkAccent, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        // Nanti di sini panggil fungsi sosViewModel.acceptRequest(requestId)
                        onAcceptClick()
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("TERIMA", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, isVertical: Boolean = false) {
    Row(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Gray,
            modifier = Modifier.width(120.dp) // Lebar tetap untuk label
        )
        if (isVertical) {
            // Untuk alamat yang panjang
            Text(
                text = value,
                fontSize = 14.sp,
                color = DarkText,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = value,
                fontSize = 14.sp,
                color = DarkText,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetailPermintaanScreenPreview() {
    RedConnectTheme {
        DetailPermintaanScreen(
            requestId = "req_123",
            onBackClick = {},
            onAcceptClick = {},
            onRejectClick = {}
        )
    }
}

