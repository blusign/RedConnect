package com.yareu.redconnect.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.R
import com.yareu.redconnect.data.UserRole
import com.yareu.redconnect.ui.components.buttons.PrimaryButton
import com.yareu.redconnect.ui.components.inputs.DropdownBloodType
import com.yareu.redconnect.ui.components.inputs.TextFieldStandard
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkGray
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.ErrorRed
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = viewModel(), // MEMBUAT INSTANCE VIEWMODEL
    onSecretAdminLogin: () -> Unit = {},
    onLoginClick: (UserRole) -> Unit = {},
    onRegisterClick: (UserRole) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf(UserRole.PEMOHON) } // Default ke Pemohon
    var selectedTab by remember { mutableIntStateOf(0) } // 0 untuk Daftar, 1 untuk Masuk

    var logoClickCount by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

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
                modifier = Modifier
                    .height(100.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        logoClickCount++
                        if (logoClickCount == 5) {
                            onSecretAdminLogin() // Menjalankan fungsi navigasi ke login admin
                            logoClickCount = 0 // Reset hitungan
                        }
                        // Reset hitungan jika user berhenti mengklik selama 2 detik
                        scope.launch {
                            delay(2000)
                            logoClickCount = 0
                        }
                    },
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
                isSelected = selectedRole == UserRole.PENDONOR,
                onClick = { selectedRole = UserRole.PENDONOR },
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
                RegisterForm(
                    role = selectedRole,
                    authViewModel = authViewModel, // PASS VIEWMODEL
                    onRegisterSuccess = { onRegisterClick(selectedRole) } // PASS CALLBACK
                )
            } else { // Form Masuk
                LoginForm(
                    role = selectedRole,
                    authViewModel = authViewModel,
                    onLoginSuccess = { role ->
                        onLoginClick(role)
                    }
                )
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}

@Composable
fun RegisterForm(
    role: UserRole,
    authViewModel: AuthViewModel, // TERIMA VIEWMODEL
    onRegisterSuccess: () -> Unit // TERIMA CALLBACK
) {
    // State untuk form
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("Pilih Golongan Darah") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var ktpNumber by remember { mutableStateOf("") }

    // State untuk loading dan error
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    Column {
        // Tampilkan pesan error jika ada
        AnimatedVisibility(visible = errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = ErrorRed,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }


        Column {
            SectionTitle("Nama Lengkap")
            TextFieldStandard(value = name, onValueChange = { name = it }, label = "Masukkan nama lengkap", enabled = !isLoading)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Email
        Column {
            SectionTitle("Email")
            TextFieldStandard(value = email, onValueChange = { email = it }, label = "Masukkan alamat email", keyboardType = KeyboardType.Email, enabled = !isLoading)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Golongan Darah (Hanya untuk Pendonor)
        AnimatedVisibility(visible = role == UserRole.PENDONOR) {
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
            TextFieldStandard(value = phone, onValueChange = { phone = it }, label = "Contoh: 081234567890", keyboardType = KeyboardType.Phone, enabled = !isLoading)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Alamat
        Column {
            SectionTitle("Alamat Rumah (Sesuai KTP)")
            TextFieldStandard(value = address, onValueChange = { address = it }, label = "Masukkan alamat lengkap rumah", enabled = !isLoading)
        }

        Spacer(Modifier.height(16.dp))

        // Grup Kata Sandi
        Column {
            SectionTitle("Buat Kata Sandi")
            TextFieldStandard(
                value = password,
                onValueChange = {            // CEGAH SPASI, filter agar tidak ada spasi yang masuk
                    if (!it.contains(" ")) password = it
                },
                label = "Password",
                placeholder = "Minimal 6 karakter",
                isPassword = true,
                enabled = !isLoading,
                // Tambahkan info di bawah field
                isError = password.isNotEmpty() && password.length < 6,
                errorMessage = "Gunakan minimal 6 karakter tanpa spasi"
            )
        }

        Spacer(Modifier.height(16.dp))

        // Grup Upload KTP (Hanya untuk Pendonor)
        AnimatedVisibility(visible = role == UserRole.PENDONOR) {
            Column {
                Text(
                    "Informasi Identitas",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextFieldStandard(
                    value = ktpNumber,
                    onValueChange = { if (it.length <= 16) ktpNumber = it },
                    label = "Nomor KTP (16 Digit)",
                    placeholder = "Masukkan nomor sesuai KTP",
                    keyboardType = KeyboardType.Number,
                    enabled = !isLoading
                )
                Text(
                    "Catatan: Data KTP akan divalidasi oleh petugas saat proses donor di lokasi.",
                    fontSize = 11.sp,
                    color = Gray,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = if (isLoading) "Memproses..." else "Daftar Akun",
            onClick = {
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || (role == UserRole.PENDONOR && bloodType == "Pilih Golongan Darah")) {
                    errorMessage = "Harap isi semua data dengan lengkap!"
                    return@PrimaryButton
                }
                isLoading = true
                errorMessage = null
                authViewModel.registerUser(
                    email = email,
                    password = password,
                    name = name,
                    role = role,
                    bloodType = bloodType,
                    phoneNumber = phone,
                    address = address,
                    ktpNumber = ktpNumber,
                    onSuccess = {
                        isLoading = false
                        onRegisterSuccess() // Panggil navigasi jika sukses
                    },
                    onError = { errorMsg ->
                        isLoading = false
                        errorMessage = errorMsg // Tampilkan pesan error
                    }
                )
            },
            enabled = !isLoading // Tombol dinonaktifkan saat loading
        )
    }
}

@Composable
fun LoginForm(
    role: UserRole,
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (UserRole) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column {
        if (errorMessage != null) {
            Text(errorMessage!!, color = ErrorRed, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }

        SectionTitle("Email")
        TextFieldStandard(value = email, onValueChange = { email = it }, label = "Masukkan email", keyboardType = KeyboardType.Email)

        Spacer(Modifier.height(16.dp))

        SectionTitle("Kata Sandi")
        TextFieldStandard(value = password, onValueChange = { password = it }, label = "Masukkan kata sandi", isPassword = true)

        // FITUR LUPA PASSWORD
        Text(
            text = "Lupa Password?",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    if (email.isNotEmpty()) {
                        authViewModel.resetPassword(email,
                            onSuccess = { /* Bisa tambah Toast "Email Terkirim" */ },
                            onError = { errorMessage = it }
                        )
                    } else {
                        errorMessage = "Masukkan email dulu untuk reset password"
                    }
                },
            color = BurgundyPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        PrimaryButton(
            text = if (isLoading) "Memproses..." else "Masuk",
            onClick = {
                isLoading = true
                errorMessage = null // Reset error sebelum mencoba

                authViewModel.loginUser(
                    email = email,
                    password = password,
                    expectedRole = role,
                    onSuccess = { user ->
                        isLoading = false
                        onLoginSuccess(user.role)
                    },
                    onError = { errorMsg ->
                        isLoading = false
                        errorMessage = errorMsg
                    }
                )
            },
            enabled = !isLoading
        )
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
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
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
                Text("Klik untuk upload file", fontSize = 14.sp, color = Gray)
            }
        }
    }
}


@Preview(name="Auth Screen", showSystemUi = true, showBackground = true)
@Composable
private fun AuthScreenPreview() {
    RedConnectTheme {
        AuthScreen()
    }
}
    