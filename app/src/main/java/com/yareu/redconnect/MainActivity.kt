package com.yareu.redconnect

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yareu.redconnect.data.UserRole
import com.yareu.redconnect.navigations.Screen
import com.yareu.redconnect.ui.admin.AdminLoginScreen
import com.yareu.redconnect.ui.admin.DetailVerifikasiScreen
import com.yareu.redconnect.ui.admin.HomeAdminScreen
import com.yareu.redconnect.ui.admin.SelesaiDonorScreen
import com.yareu.redconnect.ui.auth.AuthScreen
import com.yareu.redconnect.ui.auth.AuthViewModel
import com.yareu.redconnect.ui.onboardingScreens.OnboardingScreen
import com.yareu.redconnect.ui.pemohon.FormSOSScreen
import com.yareu.redconnect.ui.pemohon.HomePemohonScreen
import com.yareu.redconnect.ui.pemohon.LacakPendonorScreen
import com.yareu.redconnect.ui.pemohon.LoadingSiaranScreen
import com.yareu.redconnect.ui.pemohon.ProfilPemohonScreen
import com.yareu.redconnect.ui.pemohon.RiwayatPermintaanScreen
import com.yareu.redconnect.ui.pendonor.DetailPermintaanScreen
import com.yareu.redconnect.ui.pendonor.HomePendonorScreen
import com.yareu.redconnect.ui.pendonor.PermintaanDaruratScreen
import com.yareu.redconnect.ui.pendonor.ProfilPendonorScreen
import com.yareu.redconnect.ui.pendonor.RiwayatDonorScreen
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
                            onSecretAdminLogin = { navController.navigate(Screen.LoginAdmin.route) },
                            onRegisterClick = { role ->
                                val destination = if (role == UserRole.PEMOHON) Screen.HomePemohon.route else Screen.HomePendonor.route
                                navController.navigate(destination) { popUpTo(Screen.Auth.route) { inclusive = true } }
                            },
                            onLoginClick = { roleFromDb ->
                                // navigasi berdasarkan data ASLI di Firebase
                                val destination = if (roleFromDb == UserRole.PEMOHON) Screen.HomePemohon.route else Screen.HomePendonor.route
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
                            onTrackDonorClick = { requestId ->
                                navController.navigate(Screen.LacakPendonor.createRoute(requestId))
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
                            onSubmit = { requestId ->
                                navController.navigate(Screen.LoadingSiaran.createRoute(requestId))
                            }
                        )
                    }
                    composable(Screen.LoadingSiaran.route) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getString("requestId") ?: ""

                        val sosViewModel: com.yareu.redconnect.ui.sos.SOSViewModel =
                            viewModel()

                        LoadingSiaranScreen(
                            requestId = requestId, // Pastikan parameter ini diterima di LoadingSiaranScreen
                            sosViewModel = sosViewModel,
                            onDonorsFound = {
                                // Jika ditemukan pendonor, pindah ke layar Lacak
                                navController.navigate(Screen.LacakPendonor.createRoute(requestId)) {
                                    // Hapus loading dari tumpukan agar tidak bisa back ke loading lagi
                                    popUpTo(Screen.LoadingSiaran.route) { inclusive = true }
                                }
                            },
                            onCancelClick = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.LacakPendonor.route) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
                        LacakPendonorScreen(
                            requestId = requestId,
                            onBackClick = { navController.popBackStack() }
                        )
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
                            requestId = requestId, // Kirim ID saja
                            onBackClick = { navController.popBackStack() },
                            onAcceptClick = {
                                // Setelah terima, balik ke beranda atau daftar permintaan
                                navController.navigate(Screen.HomePendonor.route) {
                                    // Hapus tumpukan detail agar tidak bisa back ke detail lagi
                                    popUpTo(Screen.HomePendonor.route) { inclusive = true }
                                }
                            },
                            onRejectClick = { navController.popBackStack() }
                        )
                    }


                    // ALUR ADMIN
                    composable(Screen.LoginAdmin.route) {
                        val authViewModel: AuthViewModel = viewModel()
                        val context = androidx.compose.ui.platform.LocalContext.current // Tambahkan ini

                        AdminLoginScreen(
                            onBackClick = { navController.popBackStack() },
                            onLoginClick = { id, password ->
                                authViewModel.loginUser(
                                    email = id,
                                    password = password,
                                    expectedRole = UserRole.ADMIN,
                                    onSuccess = {
                                        navController.navigate(Screen.HomeAdmin.route) {
                                            popUpTo(Screen.Auth.route) { inclusive = true }
                                        }
                                    },
                                    onError = { error ->
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        )
                    }

                    composable(Screen.HomeAdmin.route) {
                        HomeAdminScreen(
                            onNavigate = { route ->
                                if (route == "login_admin") {
                                    // Jika route yang dikirim adalah login_admin (dari tombol logout)
                                    navController.navigate(Screen.LoginAdmin.route) {
                                        // Hapus semua history admin agar tidak bisa di-back balik ke dashboard
                                        popUpTo(Screen.HomeAdmin.route) { inclusive = true }
                                    }
                                } else {
                                    navController.navigate(route)
                                }
                            }
                        )
                    }

                    // Kita akan gunakan rute yang ada di NavGraph
                    composable(Screen.DetailVerifikasi.route) { backStackEntry ->
                        val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
                        val context = androidx.compose.ui.platform.LocalContext.current

                        DetailVerifikasiScreen(
                            requestId = requestId,
                            onBackClick = { navController.popBackStack() },
                            onSuccessNav = { dynamicRoute ->
                                navController.navigate(dynamicRoute)
                            },
                            // TAMBAHKAN INI:
                            onRejectClick = {
                                navController.popBackStack()
                                Toast.makeText(context, "Permintaan ditolak", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }


                    composable(
                        route = "selesai_donor/{donorName}/{points}",
                        arguments = listOf(
                            navArgument("donorName") { type = NavType.StringType },
                            navArgument("points") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("donorName") ?: "Pendonor"
                        val pts = backStackEntry.arguments?.getInt("points") ?: 100

                        SelesaiDonorScreen(
                            donorName = name,
                            points = pts,
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
