package com.yareu.redconnect.data

data class EmergencyRequest(
    val id: String = "",
    val requesterId: String = "",
    val requesterName: String = "",
    val requesterPhone: String = "",
    val bloodType: String = "",
    val bloodBags: Int = 1,
    val facilityName: String = "",
    val facilityAddress: String = "",
    val distance: String = "",
    val note: String = "",
    val status: RequestStatus = RequestStatus.WAITING,
    val timeAgo: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val respondingDonors: List<DonorResponse> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

enum class RequestStatus {
    WAITING,        // Menunggu pendonor
    ACCEPTED,       // Ada pendonor yang terima
    IN_PROGRESS,    // Donor sedang berlangsung
    COMPLETED,      // Donor selesai
    CANCELLED       // Dibatalkan
}