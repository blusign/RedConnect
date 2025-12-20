package com.yareu.redconnect.navigations

// Sealed class untuk rute type-safe
sealed class Screen(val route: String) {

    // 1. Alur Awal
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth") // Menggantikan RoleSelector, Login, Register menjadi satu layar

    // 2. Alur Pemohon
    object HomePemohon : Screen("home_pemohon")
    object RiwayatPemohon : Screen("riwayat_pemohon")
    object ProfilPemohon : Screen("profil_pemohon")
    object FormSOS : Screen("form_sos")
    object LoadingSiaran : Screen("loading_siaran/{requestId}") {
        fun createRoute(requestId: String) = "loading_siaran/$requestId"
    }
    object LacakPendonor : Screen("lacak_pendonor/{requestId}") {
        fun createRoute(requestId: String) = "lacak_pendonor/$requestId"
    }

    // 3. Alur Pendonor
    object HomePendonor : Screen("home_pendonor")
    object PermintaanDarurat : Screen("permintaan_donor")
    object RiwayatDonor : Screen("riwayat_donor")
    object ProfilPendonor : Screen("profil_donor")
    object DetailPermintaan : Screen("detail_permintaan/{requestId}") {
        fun createRoute(requestId: String) = "detail_permintaan/$requestId"
    }

    // 4. Alur Admin
    object HomeAdmin : Screen("home_admin")
    // membuat halaman Login Admin terpisah (mungkin)
    object LoginAdmin : Screen("login_admin")
    object DetailVerifikasi : Screen("detail_verifikasi/{requestId}") {
        fun createRoute(requestId: String) = "detail_verifikasi/$requestId"
    }
    object SelesaiDonor : Screen("selesai_donor")

}
