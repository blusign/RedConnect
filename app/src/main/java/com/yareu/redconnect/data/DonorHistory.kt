package com.yareu.redconnect.data

data class DonorHistory(
    val id: String = "",
    val donorId: String = "",
    val date: String = "",
    val facilityName: String = "",
    val bloodType: String = "",
    val status: HistoryStatus = HistoryStatus.COMPLETED,
    val points: Int = 100
)

enum class HistoryStatus {
    COMPLETED,      // Selesai
    CANCELLED       // Dibatalkan
}