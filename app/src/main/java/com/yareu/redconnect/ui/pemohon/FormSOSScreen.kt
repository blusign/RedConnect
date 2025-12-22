package com.yareu.redconnect.ui.pemohon

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.components.inputs.DropdownBloodType
import com.yareu.redconnect.ui.components.inputs.TextFieldStandard
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkGray
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White
import com.yareu.redconnect.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormSOSScreen(
    onBackClick: () -> Unit = {},
    onSubmit: (String) -> Unit = {},
    sosViewModel: SOSViewModel = viewModel()
) {
    var selectedBloodType by remember { mutableStateOf("") }
    var bloodBags by remember { mutableIntStateOf(1) }
    var facilityLocation by remember { mutableStateOf("") }
    var patientName by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var urgencyLevel by remember { mutableStateOf("Tinggi") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val fusedLocationClient = remember {
        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    }

    val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            android.widget.Toast.makeText(context, "Izin diberikan, silakan klik Kirim lagi", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            android.widget.Toast.makeText(context, "Aplikasi butuh izin lokasi untuk akurasi", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBack(title = "Formulir Permintaan Darurat", onBackClick = onBackClick)
        },
        containerColor = LightGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Grup Pilih Tipe Darah
            Column {
                SectionTitle("Golongan Darah Pasien")
                DropdownBloodType(
                    selectedBloodType = selectedBloodType,
                    onBloodTypeSelected = { selectedBloodType = it },
                    label = "Pilih Golongan Darah"
                )
            }
            Spacer(Modifier.height(16.dp))

            // Grup Jumlah Kantong
            Column {
                SectionTitle("Jumlah Kantong Dibutuhkan")
                BloodBagCounter(
                    bloodBags = bloodBags,
                    onBagsChange = { bloodBags = it }
                )
            }
            Spacer(Modifier.height(16.dp))

            // Grup Nama Pasien
            Column {
                SectionTitle("Nama Pasien")
                TextFieldStandard(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = "Masukkan nama lengkap pasien"
                )
            }
            Spacer(Modifier.height(16.dp))

            // Grup Lokasi
            Column {
                SectionTitle("Lokasi Fasilitas Kesehatan")
                TextFieldStandard(
                    value = facilityLocation,
                    onValueChange = { facilityLocation = it },
                    label = "Masukkan nama atau alamat faskes"
                )
            }
            Spacer(Modifier.height(16.dp))

            // Grup Hubungan
            Column {
                SectionTitle("Hubungan Anda dengan Pasien")
                TextFieldStandard(
                    value = relationship,
                    onValueChange = { relationship = it },
                    label = "cth: Keluarga, Teman, Diri Sendiri"
                )
            }
            Spacer(Modifier.height(16.dp))

            // Grup Tingkat Urgensi
            Column {
                SectionTitle("Tingkat Urgensi")
                UrgencyChips(
                    urgencyLevel = urgencyLevel,
                    onUrgencyChange = { urgencyLevel = it }
                )
            }
            Spacer(Modifier.height(16.dp))

            // Grup Upload
            Column {
                SectionTitle("Unggah Surat Keterangan (Opsional)")
                UploadField()
            }

            // Spacer untuk mendorong tombol ke bawah
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(24.dp))

            // Tombol Kirim SOS
            Button(
                onClick = {
                    val fineLocationPermission = ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.ACCESS_FINE_LOCATION
                    )

                    if (fineLocationPermission == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        isLoading = true
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            // Jika lokasi null (GPS mati), kita kirim 0.0 atau koordinat default
                            val lat = location?.latitude ?: 0.0
                            val lng = location?.longitude ?: 0.0

                            sosViewModel.sendSOSRequest(
                                patientName = patientName,
                                bloodType = selectedBloodType,
                                bloodBags = bloodBags,
                                facilityName = facilityLocation,
                                note = relationship,
                                lat = lat,
                                lng = lng,
                                // AMBIL NOMOR HP DARI USER PROFILE YANG SEDANG LOGIN
                                requesterPhone = com.google.firebase.auth.FirebaseAuth.getInstance()
                                    .currentUser?.phoneNumber ?: "",
                                onSuccess = { requestId ->
                                    isLoading = false
                                    onSubmit(requestId)
                                },
                                onError = { error ->
                                    isLoading = false
                                    android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    } else {
                        // Meminta izin jika belum ada
                        locationPermissionLauncher.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkAccent),
                shape = RoundedCornerShape(12.dp),
                // Tombol mati jika sedang loading
                enabled = !isLoading && selectedBloodType.isNotEmpty() && facilityLocation.isNotEmpty() && patientName.isNotEmpty()
            ) {
                Text(
                    text = if (isLoading) "Mengirim..." else "Kirim Permintaan SOS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = DarkText,
        modifier = Modifier.padding(bottom = 8.dp) // Jarak antara judul dan field
    )
}

// Composable untuk counter kantong darah
@Composable
private fun BloodBagCounter(bloodBags: Int, onBagsChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Beri tinggi yang sama dengan text field
            .background(White, RoundedCornerShape(12.dp))
            .border(1.dp, Gray, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🩸", fontSize = 24.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { if (bloodBags > 1) onBagsChange(bloodBags - 1) },
                enabled = bloodBags > 1
            ) {
                Icon(Icons.Default.Remove, "Kurangi", tint = if (bloodBags > 1) BurgundyPrimary else Gray)
            }

            Text(
                text = bloodBags.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(
                onClick = { if (bloodBags < Constants.BLOOD_BAGS_OPTIONS.last()) onBagsChange(bloodBags + 1) },
                enabled = bloodBags < Constants.BLOOD_BAGS_OPTIONS.last()
            ) {
                Icon(Icons.Default.Add, "Tambah", tint = if (bloodBags < Constants.BLOOD_BAGS_OPTIONS.last()) PinkAccent else Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UrgencyChips(urgencyLevel: String, onUrgencyChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Tinggi", "Sedang", "Terencana").forEach { level ->
            FilterChip(
                selected = urgencyLevel == level,
                onClick = { onUrgencyChange(level) },
                label = { Text(level) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PinkAccent,
                    selectedLabelColor = White,
                    containerColor = White,
                    labelColor = DarkGray
                ),
            )
        }
    }
}

@Composable
private fun UploadField() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(painterResource(id = R.drawable.ic_upload), contentDescription = null, tint = Gray)
            Spacer(Modifier.height(8.dp))
            Text("Ketuk untuk mengunggah foto", fontSize = 12.sp, color = Gray)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FormSOSScreenPreview() {
    RedConnectTheme { FormSOSScreen() }
}
