package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.theme.*

@Composable
fun ProfilAdminScreen(
    adminName: String = "Budi Santoso",
    adminEmail: String = "budi.santoso@redconnect.admin",
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Profil Admin",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        // Foto Profil (Placeholder)
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.redconnect_logo),
            contentDescription = "Foto Profil Admin",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(White),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        // Nama Admin
        Text(
            text = adminName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        // Email Admin
        Text(
            text = adminEmail,
            fontSize = 14.sp,
            color = Gray
        )

        Spacer(Modifier.weight(1f)) // Mendorong tombol ke bawah

        // Tombol Logout
        OutlinedButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfilAdminScreenPreview() {
    RedConnectTheme {
        ProfilAdminScreen()
    }
}
