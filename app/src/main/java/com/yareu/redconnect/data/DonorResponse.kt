package com.yareu.redconnect.data

data class DonorResponse(
    val id: String = "",
    val requestId: String = "",  // ID permintaan yang direspond
    val donorId: String = "",
    val donorName: String = "",
    val bloodType: String = "",
    val distance: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
    val totalDonations: Int = 0,
    val responseTime: Long = System.currentTimeMillis(),
    val status: DonorResponseStatus = DonorResponseStatus.READY
)

enum class DonorResponseStatus {
    READY,      // Siap donor
    ON_WAY,     // Dalam perjalanan
    ARRIVED,    // Sudah sampai
    COMPLETED,  // Donor selesai
    CANCELLED   // Batal
}