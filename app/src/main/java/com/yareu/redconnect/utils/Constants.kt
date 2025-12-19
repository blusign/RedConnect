package com.yareu.redconnect.utils

object Constants {
    // Blood Types
    val BLOOD_TYPES = listOf("A", "B", "AB", "O")

    // Donation Settings
    const val DONATION_COOLDOWN_DAYS = 90
    const val MIN_DONATION_AGE = 17
    const val MAX_DONATION_AGE = 65
    const val MIN_WEIGHT_KG = 45

    // Points System
    const val POINTS_PER_DONATION = 100
    const val POINTS_FIRST_TIME_BONUS = 50

    // Search Radius (km)
    val SEARCH_RADIUS_OPTIONS = listOf(5, 10, 15, 20)
    const val DEFAULT_SEARCH_RADIUS = 10

    // Status Text
    const val STATUS_ACTIVE = "Donor Aktif"
    const val STATUS_INACTIVE = "Tidak Aktif"
    const val STATUS_PENDING = "Menunggu Verifikasi"

    // Blood Bags
    val BLOOD_BAGS_OPTIONS = listOf(1, 2, 3, 4, 5)
}