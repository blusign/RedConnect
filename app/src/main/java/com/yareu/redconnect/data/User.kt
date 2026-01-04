package com.yareu.redconnect.data

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.PENDONOR,
    val bloodType: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val isAvailable: Boolean = true,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val points: Int = 0,
    val totalDonations: Int = 0,
    val ktpNumber: String = ""
)

enum class UserRole {
    PENDONOR,      // Pendonor
    PEMOHON,    // Penerima/Pasien
    ADMIN       // Petugas Puskesmas
}