package com.yareu.redconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yareu.redconnect.data.EmergencyRequest // Import data class
import com.yareu.redconnect.data.UserRole
import com.yareu.redconnect.navigations.Screen
import com.yareu.redconnect.ui.admin.*
import com.yareu.redconnect.ui.auth.AuthScreen
import com.yareu.redconnect.ui.onboardingScreens.OnboardingScreen
import com.yareu.redconnect.ui.pendonor.*
import com.yareu.redconnect.ui.pemohon.*
import com.yareu.redconnect.ui.theme.RedConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedConnectTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Onboarding.route // Aplikasi dimulai dari Onboarding
                ) {

                    // ALUR AWAL
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onOnboardingFinished = {
                                // Setelah onboarding selesai, hapus dari back stack dan pergi ke Auth
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Auth.route) {
                        AuthScreen(
                            // Logika navigasi setelah login/register
                            onRegisterClick = { role ->
                                // Navigasi berdasarkan peran yang dipilih saat mendaftar
                                val destination = if (role == UserRole.PEMOHON) Screen.HomePemohon.route else Screen.HomePendonor.route
                                navController.navigate(destination) {
                                    // Hapus semua history sebelumnya agar tidak bisa kembali ke halaman login
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            },
                            onLoginClick = { role ->
                                val destination = if (role == UserRole.PEMOHON) Screen.HomePemohon.route else Screen.HomePendonor.route
                                navController.navigate(destination) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // ALUR PEMOHON
                    composable(Screen.HomePemohon.route) {
                        HomePemohonScreen(
                            onNavigate = { route -> navController.navigate(route) },
                            onSOSClick = { navController.navigate(Screen.FormSOS.route) },
                            onTrackDonorClick = {
                                // NOTE: "dummy_req_id" akan diganti dengan ID asli dari ViewModel
                                navController.navigate(Screen.LacakPendonor.createRoute("dummy_req_id"))
                            }
                        )
                    }
                    composable(Screen.RiwayatPemohon.route) {
                        RiwayatPermintaanScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.ProfilPemohon.route) {
                        ProfilPemohonScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.FormSOS.route) {
                        FormSOSScreen(
                            onBackClick = { navController.popBackStack() },
                            onSubmit = {
                                // NOTE: "dummy_req_id" akan diganti dengan ID asli dari ViewModel
                                navController.navigate(Screen.LoadingSiaran.createRoute("dummy_req_id"))
                            }
                        )
                    }
                    composable(Screen.LoadingSiaran.route) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
                        LoadingSiaranScreen(
                            onDonorsFound = { navController.navigate(Screen.LacakPendonor.createRoute(requestId)) },
                            onCancelClick = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.LacakPendonor.route) {
                        // Di sini kita akan mengambil data request dari ViewModel berdasarkan ID
                        LacakPendonorScreen(request = null, onBackClick = { navController.popBackStack() })
                    }


                    // ALUR PENDONOR
                    composable(Screen.HomePendonor.route) {
                        HomePendonorScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.PermintaanDarurat.route) {
                        PermintaanDaruratScreen(
                            onNavigate = { route -> navController.navigate(route) },
                            onDetailClick = { requestId -> navController.navigate(Screen.DetailPermintaan.createRoute(requestId)) }
                        )
                    }
                    composable(Screen.RiwayatDonor.route) {
                        RiwayatDonorScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.ProfilPendonor.route) {
                        ProfilPendonorScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.DetailPermintaan.route) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
                        // Nanti data request diambil dari ViewModel berdasarkan requestId
                        DetailPermintaanScreen(
                            request = EmergencyRequest(id = requestId), // Kirim data dummy dulu
                            onBackClick = { navController.popBackStack() },
                            onAcceptClick = { /* Logika Terima */ },
                            onRejectClick = { navController.popBackStack() }
                        )
                    }


                    // ALUR ADMIN
                    // Rute login admin bisa dibuat terpisah dari Auth user biasa
                    composable(Screen.LoginAdmin.route) { /* TODO: AdminLoginScreen() */ }

                    composable(Screen.HomeAdmin.route) {
                        HomeAdminScreen(
                            onNavigate = { route ->
                                // Navigasi khusus dari dalam HomeAdmin
                                if (route.startsWith("detail_verifikasi")) {
                                    navController.navigate(route)
                                } else if (route == "login") { // Jika logout
                                    navController.navigate(Screen.Auth.route) {
                                        popUpTo(Screen.HomeAdmin.route){ inclusive = true }
                                    }
                                }
                            }
                        )
                    }
                    composable(Screen.DetailVerifikasi.route) {
                        DetailVerifikasiScreen(
                            onBackClick = { navController.popBackStack() },
                            onConfirmClick = { navController.navigate(Screen.SelesaiDonor.route) }
                        )
                    }
                    composable(Screen.SelesaiDonor.route) {
                        SelesaiDonorScreen(
                            onFinish = {
                                navController.navigate(Screen.HomeAdmin.route) {
                                    // Hapus semua backstack sampai HomeAdmin
                                    popUpTo(Screen.HomeAdmin.route) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
