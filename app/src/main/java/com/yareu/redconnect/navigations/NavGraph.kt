package com.yareu.redconnect.navigations

// Sealed class untuk routes
sealed class Screen(val route: String) {
    // Auth
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object RoleSelector : Screen("role_selector")
    object Login : Screen("login")
    object Register : Screen("register")

    // Donor
    object HomeDonor : Screen("home_donor")
    object RiwayatDonor : Screen("riwayat_donor")
    object DetailPermintaan : Screen("detail_permintaan/{id}") {
        fun createRoute(id: String) = "detail_permintaan/$id"
    }
    object ProfilDonor : Screen("profil_donor")

    // Pemohon
    object HomePemohon : Screen("home_pemohon")
    object SOSForm : Screen("sos_form")
    object ProgressDonor : Screen("progress_donor")
    object PilihDonor : Screen("pilih_donor/{requestId}") {
        fun createRoute(requestId: String) = "pilih_donor/$requestId"
    }

    // Admin
    object HomeAdmin : Screen("home_admin")
    object VerifikasiPendonor : Screen("verifikasi_pendonor/{id}") {
        fun createRoute(id: String) = "verifikasi_pendonor/$id"
    }
    object KonfirmasiDonor : Screen("konfirmasi_donor/{id}") {
        fun createRoute(id: String) = "konfirmasi_donor/$id"
    }
}