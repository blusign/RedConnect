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
import androidx.compose.runtime.LaunchedEffect
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
    sosViewModel: SOSViewModel = viewModel(),
    authViewModel: com.yareu.redconnect.ui.auth.AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sosViewModel.fetchEmergencyRequests()
    }

    val requests by sosViewModel.emergencyRequests.collectAsState()
    val userProfile by authViewModel.userProfile.collectAsState()
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
                        // GUNAKAN DATA ASLI DARI FIREBASE
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
                        // AMBIL LAT/LNG DARI OBJECT 'request' (Data Pemohon)
                        val lat = request.latitude
                        val lng = request.longitude
                        if (lat != 0.0 && lng != 0.0) {
                            val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
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
                        val phone = request.requesterPhone // Nomor si Pemohon

                        // Logika konversi otomatis
                        val cleanPhone = phone.replace(Regex("[^0-9]"), "")
                        val formattedPhone = if (cleanPhone.startsWith("0")) {
                            "62" + cleanPhone.substring(1)
                        } else if (cleanPhone.startsWith("8")) {
                            "62" + cleanPhone
                        } else {
                            cleanPhone
                        }

                        val message = "Halo ${request.requesterName}, saya pendonor RedConnect ingin membantu permintaan darah Anda."
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://wa.me/$formattedPhone?text=${Uri.encode(message)}")
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
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, PinkAccent)
                ) {
                    Text("TOLAK", color = PinkAccent, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val donor = userProfile
                        if (donor != null) {
                            sosViewModel.acceptRequest(
                                requestId = requestId,
                                donorProfile = donor,
                                onSuccess = {
                                    // Jika berhasil, navigasi (misal ke halaman Lacak/Sukses)
                                    onAcceptClick()
                                },
                                onError = { error ->
                                    // Tampilkan pesan error (bisa pakai Toast)
                                    android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp),
                    enabled = userProfile != null // Tombol hanya aktif jika data user sudah dimuat
                ) {
                    Text("TERIMA", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, isVertical: Boolean = false) {
    if (isVertical) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(label, fontSize = 13.sp, color = Gray)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = DarkText)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp), // Beri jarak antar baris
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 14.sp, color = Gray, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp)) // Beri jarak horizontal minimal
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold, // Buat lebih tegas
                color = DarkText,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                modifier = Modifier.weight(1.5f) // Beri ruang lebih luas untuk nilai
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

