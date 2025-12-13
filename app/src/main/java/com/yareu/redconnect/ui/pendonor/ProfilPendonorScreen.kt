package com.yareu.redconnect.ui.pendonor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.components.cards.PersonalInfoCard
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilPendonorScreen(
    onNavigate: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", fontWeight = FontWeight.Bold, color = DarkText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            PendonorBottomNavigationBar(
                currentRoute = "profil_donor",
                onNavigate = onNavigate
            )
        },
        containerColor = LightGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bagian Header Profil
            Spacer(Modifier.height(24.dp))
            ProfileHeader(
                name = "Budi Hartono",
                email = "budi.h@email.com",
                onEditClick = { /*TODO*/ }
            )
            Spacer(Modifier.height(24.dp))

            // Container untuk sisa konten
            Column(Modifier.padding(horizontal = 16.dp)) {
                // Kartu Data Diri
                SectionCard(title = "Data Diri") {
                    InfoRow(label = "Nama", value = "Budi Hartono")
                    InfoRow(label = "Nomor HP", value = "+62 812 3456 7890")
                    InfoRow(label = "Golongan Darah", value = "A+")
                    InfoRow(label = "Alamat", value = "Kecamatan Setiabudi")
                }

                Spacer(Modifier.height(16.dp))

                // Tombol Edit Profil
                OutlinedButton(
                    onClick = onEditProfileClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(BurgundyPrimary))
                ) {
                    Text("Edit Profil", color = BurgundyPrimary, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(24.dp))

                // Kartu Statistik menggunakan PersonalInfoCard yang sudah ada
                Text(
                    text = "Statistik",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                PersonalInfoCard(
                    bloodType = "A+",
                    totalDonations = 8,
                    points = 850,
                    status = "Donor Aktif"
                )

                Spacer(Modifier.height(24.dp))

                // Riwayat Kesehatan
                SectionCard(title = "Riwayat Kesehatan") {
                    InfoRow(label = "Cek Kesehatan Terakhir", value = "12 Agustus 2024")
                    InfoRow(label = "Berat Badan", value = "72 kg")
                }
            }

            Spacer(Modifier.weight(1f)) // Mendorong tombol logout ke bawah
            Spacer(Modifier.height(32.dp))

            // Tombol Logout
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(ErrorRed))
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = ErrorRed)
                Spacer(Modifier.width(8.dp))
                Text("Keluar", color = ErrorRed, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String, onEditClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.redconnect_logo),
                contentDescription = "Foto Profil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5E6D3))
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PinkAccent)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Foto", tint = White, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Text(email, fontSize = 14.sp, color = Gray)
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = DarkText,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
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
private fun ProfilPendonorScreenPreview() {
    RedConnectTheme {
        ProfilPendonorScreen()
    }
}
