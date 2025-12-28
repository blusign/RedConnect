package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.components.navigation.AdminBottomNavigationBar
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.ErrorRed
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilAdminScreen(
    authViewModel: AuthViewModel = viewModel(), // Gunakan AuthViewModel
    onLogoutClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val adminProfile by authViewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Admin", color = DarkText, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(
                selectedTab = 2,
                onTabSelected = { index ->
                    when (index) {
                        0 -> onNavigate("home_admin") // Navigasi ke Beranda Admin
                        1 -> onNavigate("admin_history") // Navigasi ke Riwayat Admin
                    }
                }
            )
        },
        containerColor = LightGray
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Foto Profil
            Image(
                painter = painterResource(id = R.drawable.redconnect_logo),
                contentDescription = "Foto Profil Admin",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(White),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            // Nama & Email Admin
            Text(
                text = adminProfile?.name ?: "Admin RedConnect",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = adminProfile?.email ?: "admin@redconnect.com",
                fontSize = 14.sp,
                color = Gray
            )

            Spacer(Modifier.weight(1f)) // Mendorong tombol ke bawah

            // Tombol Logout
            OutlinedButton(
                onClick = {
                    authViewModel.logout()
                    onLogoutClick() // Memicu navigasi di MainActivity
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ErrorRed),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
            ) {
                Icon(Icons.Default.Logout, contentDescription = "Logout")
                Spacer(Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfilAdminScreenPreview() {
    RedConnectTheme {
        ProfilAdminScreen()
    }
}
