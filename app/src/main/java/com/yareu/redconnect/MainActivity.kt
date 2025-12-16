package com.yareu.redconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.UserRole
import com.yareu.redconnect.navigations.Screen
import com.yareu.redconnect.ui.admin.*
import com.yareu.redconnect.ui.auth.AuthScreen
import com.yareu.redconnect.ui.onboardingScreens.OnboardingScreen
import com.yareu.redconnect.ui.pemohon.*
import com.yareu.redconnect.ui.pendonor.*
import com.yareu.redconnect.ui.theme.RedConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            RedConnectTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Onboarding.route
                ) {

                    // ALUR AWAL
                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onOnboardingFinished = {
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Auth.route) {
                        AuthScreen(
                            onSecretAdminLogin = {
                                navController.navigate(Screen.LoginAdmin.route)
                            },
                            onRegisterClick = { role ->
                                val destination = if (role == UserRole.PEMOHON) Screen.HomePemohon.route else Screen.HomePendonor.route
                                navController.navigate(destination) {
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
                        DetailPermintaanScreen(
                            request = EmergencyRequest(id = requestId),
                            onBackClick = { navController.popBackStack() },
                            onAcceptClick = { /* Logika Terima */ },
                            onRejectClick = { navController.popBackStack() }
                        )
                    }


                    // ALUR ADMIN
                    composable(Screen.LoginAdmin.route) {
                        AdminLoginScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onLoginClick = { id, password ->
                                navController.navigate(Screen.HomeAdmin.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.HomeAdmin.route) {
                        HomeAdminScreen(
                            onNavigate = { route ->
                                // Navigasi dari HomeAdmin ke halaman lain
                                navController.navigate(route)
                            }
                        )
                    }

                    // Kita akan gunakan rute yang ada di NavGraph
                    composable(Screen.DetailVerifikasi.route) {
                        DetailVerifikasiScreen(
                            onBackClick = { navController.popBackStack() },
                            onConfirmClick = { navController.navigate(Screen.SelesaiDonor.route) },
                            onRejectClick = { navController.popBackStack() } // Menolak dan kembali
                        )
                    }

                    composable(Screen.SelesaiDonor.route) {
                        SelesaiDonorScreen(
                            onFinish = {
                                navController.navigate(Screen.HomeAdmin.route) {
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
