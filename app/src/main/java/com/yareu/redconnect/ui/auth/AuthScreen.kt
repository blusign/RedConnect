package com.yareu.redconnect.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.components.buttons.PrimaryButton
import com.yareu.redconnect.ui.components.inputs.DropdownBloodType
import com.yareu.redconnect.ui.components.inputs.TextFieldStandard
import com.yareu.redconnect.ui.theme.*
import androidx.compose.ui.graphics.Color

// Menggunakan enum dari User.kt agar konsisten
import com.yareu.redconnect.data.UserRole

@Composable
fun AuthScreen(
    onLoginClick: (UserRole) -> Unit = {},
    onRegisterClick: (UserRole) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf(UserRole.PEMOHON) } // Default ke Pemohon
    var selectedTab by remember { mutableIntStateOf(0) } // 0 untuk Daftar, 1 untuk Masuk

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(top = 48.dp)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.redconnect_logo),
                contentDescription = "RedConnect Logo",
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(BurgundyPrimary)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Satu Tetes Darah, Sejuta Harapan",
                fontSize = 16.sp,
                color = DarkText,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(32.dp))

        // Role Selector
        Text(
            "Pilih Peran Anda",
            modifier = Modifier.padding(horizontal = 24.dp),
            fontSize = 14.sp,
            color = Gray
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RoleCard(
                role = "Pendonor",
                icon = R.drawable.ic_donor,
                isSelected = selectedRole == UserRole.DONOR,
                onClick = { selectedRole = UserRole.DONOR },
                modifier = Modifier.weight(1f)
            )
            RoleCard(
                role = "Pemohon",
                icon = R.drawable.ic_requester,
                isSelected = selectedRole == UserRole.PEMOHON,
                onClick = { selectedRole = UserRole.PEMOHON },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Tabs (Daftar / Masuk)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = White,
            contentColor = BurgundyPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = BurgundyPrimary,
                    height = 3.dp
                )
            },
            divider = { HorizontalDivider(color = LightGray) }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Daftar") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Masuk") }
            )
        }

        // Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            if (selectedTab == 0) { // Form Daftar
                RegisterForm(role = selectedRole, onRegisterClick = { onRegisterClick(selectedRole) })
            } else { // Form Masuk
                LoginForm(onLoginClick = { onLoginClick(selectedRole) })
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun RegisterForm(role: UserRole, onRegisterClick: () -> Unit) {
    // State untuk form
    var name by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("Pilih Golongan Darah") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        Column {
            SectionTitle("Nama Lengkap")
            TextFieldStandard(value = name, onValueChange = { name = it }, label = "Masukkan nama lengkap")
        }

        Spacer(Modifier.height(16.dp)) // Jarak antar grup

        // Grup Golongan Darah (Hanya untuk Pendonor)
        AnimatedVisibility(visible = role == UserRole.DONOR) {
            Column {
                SectionTitle("Golongan Darah")
                DropdownBloodType(
                    selectedBloodType = bloodType,
                    onBloodTypeSelected = { bloodType = it }
                )
                Spacer(Modifier.height(16.dp)) // Jarak jika grup ini muncul
            }
        }
        // Grup Nomor HP
        Column {
            SectionTitle("Nomor HP")
            TextFieldStandard(value = phone, onValueChange = { phone = it }, label = "Contoh: 081234567890", keyboardType = KeyboardType.Phone)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Alamat
        Column {
            SectionTitle("Alamat/Lokasi")
            TextFieldStandard(value = address, onValueChange = { address = it }, label = "Masukkan alamat Anda")
        }

        Spacer(Modifier.height(16.dp))

        // Grup Kata Sandi
        Column {
            SectionTitle("Buat Kata Sandi")
            TextFieldStandard(value = password, onValueChange = { password = it }, label = "Minimal 8 karakter", isPassword = true)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Upload KTP (Hanya untuk Pendonor)
        AnimatedVisibility(visible = role == UserRole.DONOR) {
            Column {
                SectionTitle("Upload KTP")
                UploadKtpField()
                Spacer(Modifier.height(16.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
        PrimaryButton(text = "Daftar Akun", onClick = onRegisterClick)
    }
}

@Composable
fun LoginForm(onLoginClick: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {
        // Grup Nomor HP
        Column {
            SectionTitle("Nomor HP")
            TextFieldStandard(value = phone, onValueChange = { phone = it }, label = "Masukkan nomor HP terdaftar", keyboardType = KeyboardType.Phone)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Kata Sandi
        Column {
            SectionTitle("Kata Sandi")
            TextFieldStandard(value = password, onValueChange = { password = it }, label = "Masukkan kata sandi Anda", isPassword = true)
        }

        Spacer(Modifier.height(24.dp)) // Jarak lebih besar sebelum tombol
        PrimaryButton(text = "Masuk", onClick = onLoginClick)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = DarkText,
        modifier = Modifier.padding(bottom = 1.dp)
    )
}

@Composable
fun RoleCard(
    role: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) PinkAccent.copy(alpha = 0.1f) else White
    val borderColor = if (isSelected) PinkAccent else LightGray

    Card(
        modifier = modifier.height(100.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = role,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) PinkAccent else DarkGray
            )
            Spacer(Modifier.height(8.dp))
            Text(
                role, // Teks langsung dari parameter
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) PinkAccent else DarkGray
            )
        }
    }
}


@Composable
fun UploadKtpField() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { /* TODO: Implementasi logika pilih file */ }
                .background(LightGray.copy(alpha = 0.5f))
                .border(
                    width = 1.dp,
                    color = Gray,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = null,
                    tint = Gray
                )
                Text("Klik untuk upload file", fontSize = 12.sp, color = Gray)
                Text("PNG, JPG (MAX. 2MB)", fontSize = 10.sp, color = Gray)
            }
        }
    }
}

@Preview(showBackground = true, name = "Register - Pemohon")
@Composable
fun AuthScreenRequesterRegisterPreview() {
    RedConnectTheme {
        AuthScreen()
    }
}

@Preview(showBackground = true, name = "Login - Pendonor")
@Composable
fun AuthScreenDonorLoginPreview() {
    RedConnectTheme {
        // Untuk menampilkan preview state yang berbeda
        var selectedRole by remember { mutableStateOf(UserRole.DONOR) }
        var selectedTab by remember { mutableIntStateOf(1) } // 1 = Login

        // Tampilan parsial untuk preview
        Column(Modifier.background(White)) {
            if (selectedTab == 0) {
                RegisterForm(role = selectedRole, onRegisterClick = {})
            } else {
                LoginForm(onLoginClick = {})
            }
        }
    }
}
