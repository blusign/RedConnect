package com.yareu.redconnect.ui.pemohon

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yareu.redconnect.ui.sos.SOSViewModel
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.SuccessGreen

@Composable
fun LoadingSiaranScreen(
    requestId: String,
    sosViewModel: SOSViewModel = viewModel(),
    onDonorsFound: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val allRequests by sosViewModel.emergencyRequests.collectAsState()
    val currentRequest = allRequests.find { it.id == requestId }

    // Ambil jumlah pendonor secara dinamis dari data Firestore
    val donorCount = currentRequest?.respondingDonors?.size ?: 0

    LaunchedEffect(currentRequest?.status) {
        // Jika status berubah jadi ACCEPTED, berarti sudah ada pendonor
        if (currentRequest?.status == com.yareu.redconnect.data.RequestStatus.ACCEPTED) {
            onDonorsFound()
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

    Scaffold (
        containerColor = LightGray
    ) { padding ->
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

            Spacer(Modifier.weight(1f)) // Mendorong tombol ke bawah

            // TOMBOL BATALKAN
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(1.dp, Gray)
            ) {
                Text("Batalkan Pencarian", color = Gray, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingSiaranScreenPreview() {
    com.yareu.redconnect.ui.theme.RedConnectTheme {
        LoadingSiaranScreen(
            requestId = "dummy_id",
            onDonorsFound = {},
            onCancelClick = {}
        )
    }
}
