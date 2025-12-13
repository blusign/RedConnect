package com.yareu.redconnect.ui.pendonor

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.R
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.theme.*
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.rounded.Whatsapp

@Composable
fun DetailPermintaanScreen(
    request: EmergencyRequest, // Data permintaan akan dikirim dari layar sebelumnya
    onBackClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    val context = LocalContext.current

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
                        InfoRow(label = "Nama Penerima", value = request.requesterName)
                        InfoRow(label = "Nomor HP", value = "+6281234567890") // Ganti dengan nomor HP asli dari request
                        InfoRow(label = "Lokasi Tujuan", value = request.facilityName)
                        InfoRow(label = "Alamat", value = request.facilityAddress, isVertical = true)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol Tolak
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

                // Tombol Terima
                Button(
                    onClick = onAcceptClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
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
    // Data dummy untuk preview
    val dummyRequest = EmergencyRequest(
        id = "req_123",
        requesterName = "Joko Susilo",
        facilityName = "Puskesmas Mergangsan",
        facilityAddress = "Jl. Sisingamangaraja No.45, Brontokusuman, Kec. Mergangsan, Kota Yogyakarta, Daerah Istimewa Yogyakarta 55153"
        // bisa ditambah yaw
    )

    RedConnectTheme {
        DetailPermintaanScreen(
            request = dummyRequest,
            onBackClick = {},
            onAcceptClick = {},
            onRejectClick = {}
        )
    }
}
