package com.yareu.redconnect.ui.pendonor

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.yareu.redconnect.R
import com.yareu.redconnect.navigations.Screen
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.components.cards.PersonalInfoCard
import com.yareu.redconnect.ui.components.inputs.TextFieldStandard
import com.yareu.redconnect.ui.components.navigation.PendonorBottomNavigationBar
import com.yareu.redconnect.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilPendonorScreen(
    onNavigate: (String) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val userProfile by authViewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // State untuk Dialog Edit
    var showEditDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf("") }
    var tempPhone by remember { mutableStateOf("") }
    var tempAddress by remember { mutableStateOf("") }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            Toast.makeText(context, "Izin diberikan!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", fontWeight = FontWeight.Bold, color = DarkText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            PendonorBottomNavigationBar(currentRoute = "profil_donor", onNavigate = onNavigate)
        },
        containerColor = LightGray
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                ProfileHeader(
                    name = userProfile?.name ?: "Memuat...",
                    email = userProfile?.email ?: ""
                )

                Spacer(Modifier.height(24.dp))

                Column(Modifier.padding(horizontal = 16.dp)) {
                    SectionCard(title = "Data Diri") {
                        InfoRow(label = "Nama", value = userProfile?.name ?: "-")
                        InfoRow(label = "Nomor KTP", value = userProfile?.ktpNumber ?: "-")
                        InfoRow(label = "Nomor HP", value = userProfile?.phoneNumber ?: "-")
                        InfoRow(label = "Golongan Darah", value = userProfile?.bloodType ?: "-")

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Alamat", fontSize = 14.sp, color = Gray)
                                Text(userProfile?.address ?: "-", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
                            }
                            IconButton(
                                onClick = {
                                    val fineLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                    if (fineLoc == PackageManager.PERMISSION_GRANTED) {
                                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                            location?.let {
                                                authViewModel.updateLocationToGps(context, it.latitude, it.longitude) {
                                                    Toast.makeText(context, "Alamat GPS Diperbarui!", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    } else {
                                        locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                                    }
                                },
                                modifier = Modifier.background(BlueAccent.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Icon(Icons.Default.MyLocation, null, tint = BlueAccent, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            tempName = userProfile?.name ?: ""
                            tempPhone = userProfile?.phoneNumber ?: ""
                            tempAddress = userProfile?.address ?: ""
                            showEditDialog = true
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        border = BorderStroke(1.dp, BurgundyPrimary)
                    ) {
                        Text("Edit Profil", color = BurgundyPrimary, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(24.dp))

                    Text("Statistik", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText, modifier = Modifier.padding(bottom = 8.dp))
                    PersonalInfoCard(
                        bloodType = userProfile?.bloodType ?: "-",
                        totalDonations = userProfile?.totalDonations ?: 0,
                        points = userProfile?.points ?: 0,
                        status = if (userProfile?.isAvailable == true) "Siap Donor" else "Sedang Istirahat"
                    )

                    Spacer(Modifier.height(32.dp))

                    OutlinedButton(
                        onClick = {
                            authViewModel.logout()
                            onNavigate(Screen.Auth.route)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        border = BorderStroke(1.dp, ErrorRed)
                    ) {
                        Icon(Icons.Default.Logout, null, tint = ErrorRed)
                        Spacer(Modifier.width(8.dp))
                        Text("Keluar Aplikasi", color = ErrorRed, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }

            // --- DIALOG EDIT PROFIL ---
            if (showEditDialog) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showEditDialog = false }) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Update Profil", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            TextFieldStandard(value = tempName, onValueChange = { tempName = it }, label = "Nama Lengkap")
                            TextFieldStandard(value = tempPhone, onValueChange = { tempPhone = it }, label = "Nomor HP Baru")
                            TextFieldStandard(value = tempAddress, onValueChange = { tempAddress = it }, label = "Alamat Lengkap")

                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { showEditDialog = false }) { Text("Batal", color = Gray) }
                                Button(
                                    onClick = {
                                        authViewModel.updateProfile(tempName, tempPhone, tempAddress) {
                                            showEditDialog = false
                                            Toast.makeText(context, "Profil Diperbarui!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
                                ) { Text("Simpan") }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.redconnect_logo),
            contentDescription = null,
            modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0xFFE6E6FA))
        )
        Spacer(Modifier.height(12.dp))
        Text(name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
        Text(email, fontSize = 14.sp, color = Gray)
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText, modifier = Modifier.padding(bottom = 8.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = DarkText)
    }
}
