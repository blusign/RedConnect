package com.yareu.redconnect.ui.pemohon

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.yareu.redconnect.R
import com.yareu.redconnect.navigations.Screen
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.components.navigation.PemohonBottomNavigationBar
import com.yareu.redconnect.ui.theme.BlueAccent
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.ErrorRed
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilPemohonScreen(
    onNavigate: (String) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", fontWeight = FontWeight.Bold, color = DarkText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            PemohonBottomNavigationBar(
                currentRoute = "profil_pemohon",
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
                name = userProfile?.name ?: "Memuat...",
                email = userProfile?.email ?: "",
                onEditClick = { /*TODO*/ }
            )
            Spacer(Modifier.height(24.dp))

            // Container untuk sisa konten
            Column(Modifier.padding(horizontal = 16.dp)) {
                SectionCard(title = "Data Diri") {
                    InfoRow(label = "Nama", value = userProfile?.name ?: "-")
                    InfoRow(label = "Nomor HP", value = userProfile?.phoneNumber ?: "-")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Alamat", fontSize = 14.sp, color = Gray)
                            Text(userProfile?.address ?: "-", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
                        }

                        IconButton(
                            onClick = {
                                val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                if (permission == PackageManager.PERMISSION_GRANTED) {
                                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                        location?.let {
                                            authViewModel.updateLocationToGps(context, it.latitude, it.longitude) {
                                                Toast.makeText(context, "Alamat rumah diperbarui!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                } else {
                                    // Minta izin lokasi
                                }
                            },
                            modifier = Modifier.background(BlueAccent.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, tint = BlueAccent, modifier = Modifier.size(20.dp))
                        }
                    }
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
            }

            Spacer(Modifier.weight(1f)) // Mendorong tombol logout ke bawah
            Spacer(Modifier.height(32.dp))

            // Tombol Logout
            OutlinedButton(
                onClick = {
                    authViewModel.logout() // Panggil fungsi logout di ViewModel
                    onNavigate(Screen.Auth.route) // Navigasi ke layar login
                },
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

// Menggunakan kembali komponen-komponen yang sama dengan profil pendonor
@Composable
private fun ProfileHeader(name: String, email: String, onEditClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.redconnect_logo), // Ganti dengan foto profil
                contentDescription = "Foto Profil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6E6FA)) // Warna beda untuk pemohon
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
private fun ProfilPemohonScreenPreview() {
    RedConnectTheme {
        ProfilPemohonScreen()
    }
}
