package com.yareu.redconnect.ui.pemohon

import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.google.android.gms.location.LocationServices
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Launcher Izin tetap ada untuk tombol GPS
    val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            Toast.makeText(context, "Izin diberikan, silakan klik ikon 📍", Toast.LENGTH_SHORT).show()
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextFieldStandard(
                        value = facilityLocation,
                        onValueChange = { facilityLocation = it },
                        label = "Ketik nama RS atau Alamat",
                        modifier = Modifier.weight(1f) // Memberi ruang untuk tombol di samping
                    )

                    Spacer(Modifier.width(8.dp))

                    // TOMBOL GPS (Gunakan Lokasi Saat Ini)
                    IconButton(
                        onClick = {
                            val permission = ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                            )
                            if (permission == PackageManager.PERMISSION_GRANTED) {
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    location?.let {
                                        // Gunakan Geocoder untuk mengubah koordinat jadi teks alamat di field
                                        scope.launch(Dispatchers.IO) {
                                            try {
                                                val geocoder = Geocoder(context, java.util.Locale.getDefault())
                                                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                                                if (!addresses.isNullOrEmpty()) {
                                                    withContext(Dispatchers.Main) {
                                                        facilityLocation = addresses[0].getAddressLine(0)
                                                        Toast.makeText(context, "Lokasi saat ini diambil", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } catch (e: Exception) { }
                                        }
                                    }
                                }
                            } else {
                                locationPermissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION))
                            }
                        },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(PinkAccent.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Gunakan GPS",
                            tint = PinkAccent
                        )
                    }
                }
                Text(
                    "Tips: Ketik nama RS (contoh: RS Sardjito) atau klik ikon 📍",
                    fontSize = 11.sp,
                    color = Gray,
                    modifier = Modifier.padding(top = 4.dp)
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

            // Tombol Kirim Permintaan SOS
            Button(
                onClick = {
                    if (facilityLocation.isEmpty()) {
                        Toast.makeText(context, "Mohon isi lokasi faskes", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    scope.launch(Dispatchers.IO) {
                        try {
                            val geocoder = Geocoder(context)
                            // Mencari koordinat berdasarkan teks yang ada di facilityLocation
                            // (Baik itu hasil ketik manual atau hasil klik tombol GPS tadi)
                            val addresses = geocoder.getFromLocationName(facilityLocation, 1)

                            if (!addresses.isNullOrEmpty()) {
                                val targetLat = addresses[0].latitude
                                val targetLng = addresses[0].longitude

                                withContext(Dispatchers.Main) {
                                    sosViewModel.sendSOSRequest(
                                        patientName = patientName,
                                        bloodType = selectedBloodType,
                                        bloodBags = bloodBags,
                                        facilityName = facilityLocation,
                                        note = relationship,
                                        lat = targetLat,
                                        lng = targetLng,
                                        requesterPhone = com.google.firebase.auth.FirebaseAuth.getInstance()
                                            .currentUser?.phoneNumber ?: "",
                                        urgency = urgencyLevel,
                                        onSuccess = { requestId ->
                                            isLoading = false
                                            onSubmit(requestId)
                                        },
                                        onError = { error ->
                                            isLoading = false
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    isLoading = false
                                    Toast.makeText(context, "Lokasi tidak ditemukan. Pastikan nama RS benar.", Toast.LENGTH_LONG).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkAccent),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading && selectedBloodType.isNotEmpty() && facilityLocation.isNotEmpty() && patientName.isNotEmpty()
            ) {
                if (isLoading) {
                    // Lingkaran loading kecil saat proses kirim ke Firebase
                    CircularProgressIndicator(
                        color = White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "KIRIM PERMINTAAN SOS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
            .height(56.dp)
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
