package com.yareu.redconnect.ui.onboardingScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
    onNavigateToNext: () -> Unit = {}
) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        // Animasi progress bar
        while (progress < 1f) {
            delay(30)
            progress += 0.01f
        }
        // Auto navigate setelah selesai
        delay(500)
        onNavigateToNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF5A0E24), // BurgundyPrimary
                        Color(0xFFBF124D)  // PinkAccent
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo RedConnect
            Text(
                text = "RedConnect",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Illustration Card
            Box(
                modifier = Modifier
                    .size(400.dp, 350.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8DCC4)),
                contentAlignment = Alignment.Center
            ) {
                // Ganti dengan gambar ilustrasi kamu
                // Image(
                //     painter = painterResource(id = R.drawable.onboarding_illustration),
                //     contentDescription = "Emergency Blood Donation",
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Fit
                // )

                // Placeholder text (hapus ini kalo udah ada gambar)
                Text(
                    text = "🩸",
                    fontSize = 120.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Main Heading
            Text(
                text = "Darurat Butuh\nDarah?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Kami Siap Membantu",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Progress Indicator
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}