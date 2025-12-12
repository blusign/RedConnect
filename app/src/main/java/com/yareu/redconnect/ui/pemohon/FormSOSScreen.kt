package com.yareu.redconnect.ui.pemohon

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.components.inputs.DropdownBloodType
import com.yareu.redconnect.ui.components.inputs.TextFieldStandard
import com.yareu.redconnect.utils.Constants

@Composable
fun FormSOSScreen(
    onBackClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var selectedBloodType by remember { mutableStateOf("") }
    var bloodBags by remember { mutableIntStateOf(1) }
    var facilityLocation by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var urgencyLevel by remember { mutableStateOf("Tinggi") }

    Scaffold(
        topBar = {
            TopBarWithBack(title = "SOS Darah", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pilih Tipe Darah
            Text("Pilih Tipe Darah", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            DropdownBloodType(
                selectedBloodType = selectedBloodType,
                onBloodTypeSelected = { selectedBloodType = it },
                label = "A+"
            )

            // Jumlah Kantong Darah
            Text("Jumlah Kantong Darah", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🩸", fontSize = 24.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (bloodBags > 1) bloodBags-- },
                        enabled = bloodBags > 1
                    ) {
                        Icon(Icons.Default.Remove, "Kurangi", tint = if (bloodBags > 1) BurgundyPrimary else Gray)
                    }

                    Text(
                        text = bloodBags.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    IconButton(
                        onClick = { if (bloodBags < 5) bloodBags++ },
                        enabled = bloodBags < 5
                    ) {
                        Icon(Icons.Default.Add, "Tambah", tint = if (bloodBags < 5) PinkAccent else Gray)
                    }
                }
            }

            // Lokasi Klinik/Puskesmas
            Text("Lokasi Klinik / Puskesmas", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            TextFieldStandard(
                value = facilityLocation,
                onValueChange = { facilityLocation = it },
                label = "Lokasi",
                placeholder = "Masukkan nama atau alamat klinik"
            )

            // Hubungan dengan Pasien
            Text("Hubungan dengan Pasien", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            TextFieldStandard(
                value = relationship,
                onValueChange = { relationship = it },
                label = "Hubungan",
                placeholder = "cth: Keluarga, Teman, Diri Sendiri"
            )

            // Tingkat Urgensi
            Text("Tingkat Urgensi", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Tinggi", "Sedang", "Terencana").forEach { level ->
                    FilterChip(
                        selected = urgencyLevel == level,
                        onClick = { urgencyLevel = level },
                        label = { Text(level) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PinkAccent,
                            selectedLabelColor = White
                        )
                    )
                }
            }

            // Upload Foto (Optional)
            Text("Unggah Foto Surat Dokter / Klinik (Opsional)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(containerColor = LightGray)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("📄", fontSize = 32.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Ketuk untuk mengunggah", fontSize = 12.sp, color = Gray)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Tombol Kirim SOS
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkAccent),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedBloodType.isNotEmpty() && facilityLocation.isNotEmpty()
            ) {
                Text("Kirim SOS", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormSOSScreenPreview() {
    MaterialTheme { FormSOSScreen() }
}