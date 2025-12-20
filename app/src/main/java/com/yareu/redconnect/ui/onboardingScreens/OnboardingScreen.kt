package com.yareu.redconnect.ui.onboardingScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yareu.redconnect.R
import com.yareu.redconnect.ui.components.buttons.PrimaryButton
import com.yareu.redconnect.ui.theme.BurgundyPrimary
import com.yareu.redconnect.ui.theme.DarkText
import com.yareu.redconnect.ui.theme.Gray
import com.yareu.redconnect.ui.theme.LightGray
import com.yareu.redconnect.ui.theme.PinkAccent
import com.yareu.redconnect.ui.theme.RedConnectTheme
import com.yareu.redconnect.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Model untuk data setiap halaman onboarding
data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onOnboardingFinished: () -> Unit = {}
) {
    // Data untuk halaman 2 dan 3
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboarding_2,
            title = "Notifikasi Real-time untuk Pendonor Terdekat",
            description = "Dapatkan peringatan segera ketika ada kebutuhan darah mendesak di sekitar Anda, dan jadilah pahlawan lokal."
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_3,
            title = "Jadi Pahlawan, Dapatkan Apresiasi",
            description = "Setiap tetes darah Anda sangat berarti. Kami memastikan setiap pahlawan seperti Anda mendapatkan pengakuan yang layak."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size + 1 }) // +1 untuk splash screen
    val scope = rememberCoroutineScope()

    // Efek untuk splash screen
    LaunchedEffect(Unit) {
        delay(2000) // Tampilkan splash screen selama 2 detik
        pagerState.animateScrollToPage(1) // Pindah ke halaman onboarding pertama setelah splash
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Latar belakang putih
            .background(White)
    ) {
        // Pager untuk menggeser halaman
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = pagerState.currentPage != 0 // Nonaktifkan scroll di splash screen
        ) { pageIndex ->
            when (pageIndex) {
                0 -> SplashScreenContent()
                else -> OnboardingPageContent(page = pages[pageIndex - 1]) // -1 karena index pages mulai dari 0
            }
        }

        // Tampilkan navigasi HANYA jika bukan di splash screen
        if (pagerState.currentPage > 0) {
            OnboardingNavigation(
                modifier = Modifier.padding(16.dp),
                pagerState = pagerState,
                onSkip = {
                    onOnboardingFinished() // Langsung ke halaman utama jika 'Lewati' ditekan
                },
                onNext = {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                onFinish = {
                    onOnboardingFinished() // Ke halaman utama jika 'Mulai Sekarang' ditekan
                }
            )
        } else {
            // Beri ruang kosong agar layout konsisten selama splash screen
            Spacer(modifier = Modifier.height(136.dp))
        }
    }
}

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Background putih
            .background(White),
        contentAlignment = Alignment.Center
    ) {
        // Hanya ada logo di tengah
        Image(
            painter = painterResource(id = R.drawable.redconnect_logo),
            contentDescription = "RedConnect Logo",
            modifier = Modifier.size(300.dp), // Ukuran bisa disesuaikan
            // menambahkan ColorFilter untuk memberi warna pada logo jika diperlukan
            colorFilter = ColorFilter.tint(BurgundyPrimary)
        )
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        // Gambar Ilustrasi
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Menjaga gambar tetap proporsional
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Judul
        Text(
            text = page.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Deskripsi
        Text(
            text = page.description,
            fontSize = 14.sp,
            color = Gray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingNavigation(
    modifier: Modifier = Modifier,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit
) {
    // Tombol 'Lewati' hanya muncul di halaman 2 dan 3
    val showSkipButton = pagerState.currentPage == 1 || pagerState.currentPage == 2

    Box(
        modifier = modifier
            .fillMaxWidth()
            // Mengubah tinggi agar sesuai dengan konten navigasi saja
            .height(120.dp)
    ) {
        // Tombol 'Lewati' di atas indikator/tombol utama
        AnimatedVisibility(
            visible = showSkipButton,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            TextButton(onClick = onSkip) {
                Text(text = "Lewati", color = Gray)
            }
        }

        // Wrapper untuk konten bawah
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indikator halaman (dots)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                // Mulai dari 1 untuk skip splash screen dot
                for (i in 1 until pagerState.pageCount) {
                    val isSelected = pagerState.currentPage == i
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PinkAccent else LightGray)
                    )
                }
            }

            // Tombol 'Lanjutkan' atau 'Mulai Sekarang'
            if (pagerState.currentPage == 1) { // Halaman 2
                PrimaryButton(
                    text = "Lanjutkan",
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (pagerState.currentPage == 2) { // Halaman 3
                PrimaryButton(
                    text = "Mulai Sekarang",
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(name = "Splash Screen", showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    RedConnectTheme {
        SplashScreenContent()
    }
}

@Preview(name = "Onboarding Page 2", showBackground = true, showSystemUi = true)
@Composable
fun OnboardingPage2Preview() {
    RedConnectTheme {
        Column(Modifier.background(White)) {
            OnboardingPageContent(
                page = OnboardingPage(
                    R.drawable.onboarding_2,
                    "Notifikasi Real-time untuk Pendonor Terdekat",
                    "Dapatkan peringatan segera ketika ada kebutuhan darah mendesak di sekitar Anda, dan jadilah pahlawan lokal."
                )
            )
        }
    }
}

@Preview(name = "Onboarding Page 3", showBackground = true, showSystemUi = true)
@Composable
fun OnboardingPage3Preview() {
    RedConnectTheme {
        Column(Modifier.background(White)) {// Kita panggil OnboardingPageContent dengan data untuk halaman ke-3
            OnboardingPageContent(
                page = OnboardingPage(
                    R.drawable.onboarding_3, // Pastikan nama file gambar benar
                    "Jadi Pahlawan, Dapatkan Apresiasi",
                    "Setiap tetes darah Anda sangat berarti. Kami memastikan setiap pahlawan seperti Anda mendapatkan pengakuan yang layak."
                )
            )
        }
    }
}

@Preview(name = "Full Onboarding Flow", showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    RedConnectTheme {
        OnboardingScreen()
    }
}
