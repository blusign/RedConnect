package com.yareu.redconnect.data

data class EmergencyRequest(
    val id: String = "",
    val requesterId: String = "",
    val requesterName: String = "",
    val bloodType: String = "",
    val bloodBags: Int = 1,
    val facilityName: String = "",
    val facilityAddress: String = "",
    val distance: String = "",
    val note: String = "",
    val status: RequestStatus = RequestStatus.WAITING,
    val timeAgo: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val respondingDonors: List<DonorResponse> = emptyList()
)

enum class RequestStatus {
    WAITING,        // Menunggu pendonor
    ACCEPTED,       // Ada pendonor yang terima
    IN_PROGRESS,    // Donor sedang berlangsung
    COMPLETED,      // Donor selesai
    CANCELLED       // Dibatalkan
}