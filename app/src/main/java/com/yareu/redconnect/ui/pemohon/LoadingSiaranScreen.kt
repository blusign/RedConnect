package com.yareu.redconnect.ui.pemohon

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.ui.theme.*
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import kotlinx.coroutines.delay

@Composable
fun LoadingSiaranScreen(
    request: EmergencyRequest? = null,  // Terima data request
    onDonorsFound: () -> Unit = {}
) {
    // Ambil jumlah pendonor dari request
    val donorCount = request?.respondingDonors?.size ?: 0

    // Simulasi, Monitor perubahan jumlah pendonor
    LaunchedEffect(donorCount) {
        if (donorCount > 0) {
            delay(2000) // Kasih waktu user baca
            onDonorsFound() // Auto navigate
        }
    }

    // Animasi ripple
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale1"
    )
    val scale2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale2"
    )
    val scale3 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, delayMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scale3"
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ripple Animation
            Box(
                modifier = Modifier.size(300.dp),
                contentAlignment = Alignment.Center
            ) {
                // Ripple circles
                Canvas(Modifier.fillMaxSize()) {
                    val maxRadius = size.minDimension / 2

                    drawCircle(
                        color = PinkAccent.copy(alpha = (1f - scale1 / 2).coerceIn(0f, 0.3f)),
                        radius = maxRadius * scale1,
                        style = Stroke(width = 4.dp.toPx())
                    )
                    drawCircle(
                        color = PinkAccent.copy(alpha = (1f - scale2 / 2).coerceIn(0f, 0.3f)),
                        radius = maxRadius * scale2,
                        style = Stroke(width = 4.dp.toPx())
                    )
                    drawCircle(
                        color = PinkAccent.copy(alpha = (1f - scale3 / 2).coerceIn(0f, 0.3f)),
                        radius = maxRadius * scale3,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }

                // Center circle
                Surface(
                    modifier = Modifier.size(150.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = PinkAccent
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🩸", fontSize = 48.sp)
                    }
                }
            }

            Spacer(Modifier.height(48.dp))

            Text(
                text = "Permintaan Darurat\nSedang Disiarkan",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Sistem kami sedang mencari dan menghubungi\npara pendonor yang cocok dan tersedia di sekitar Anda",
                fontSize = 14.sp,
                color = Gray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // ✅ Progress berdasarkan data real
            if (donorCount > 0) {
                Text(
                    text = "✅ $donorCount pendonor siap membantu!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            } else {
                CircularProgressIndicator(color = PinkAccent)
                Spacer(Modifier.height(8.dp))
                Text("Mencari pendonor terdekat...", fontSize = 14.sp, color = Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingSiaranScreenPreview() {
    MaterialTheme {
        LoadingSiaranScreen(
            request = EmergencyRequest(
                bloodType = "O+",
                bloodBags = 2,
                status = RequestStatus.WAITING
            )
        )
    }
}