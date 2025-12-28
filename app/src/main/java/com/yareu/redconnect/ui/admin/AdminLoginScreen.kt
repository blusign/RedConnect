package com.yareu.redconnect.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.components.buttons.PrimaryButton
import com.yareu.redconnect.ui.components.inputs.TextFieldStandard
import com.yareu.redconnect.ui.components.topbars.TopBarWithBack
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.RedConnectTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.ColorFilter
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.R


@Composable
fun AdminLoginScreen(
    onLoginClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var adminId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "Login Petugas",
                onBackClick = onBackClick
            )
        },
        containerColor = LightGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.redconnect_logo),
                contentDescription = "Logo RedConnect",
                modifier = Modifier.size(120.dp),
                colorFilter = ColorFilter.tint(BurgundyPrimary)
            )

            Text(
                "Selamat Datang, Petugas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                "Silakan masuk untuk mengakses dasbor admin.",
                fontSize = 14.sp,
                color = Gray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))

            // Form Login
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextFieldStandard(
                    value = adminId,
                    onValueChange = { adminId = it },
                    label = "ID Petugas"
                )
                TextFieldStandard(
                    value = password,
                    onValueChange = { password = it },
                    label = "Kata Sandi",
                    isPassword = true
                )
            }

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                text = "Masuk",
                onClick = { onLoginClick(adminId, password) },
                modifier = Modifier.fillMaxWidth(),
                // Tombol hanya aktif jika kedua field tidak kosong
                enabled = adminId.isNotBlank() && password.isNotBlank()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AdminLoginScreenPreview() {
    RedConnectTheme {
        AdminLoginScreen(
            onLoginClick = { _, _ -> },
            onBackClick = {}
        )
    }
}
