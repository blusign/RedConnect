package com.yareu.redconnect.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    private val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))

    /**
     * Format timestamp ke "17 Agustus 2024"
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    /**
     * Format timestamp ke "15 menit lalu", "2 jam lalu", "3 hari lalu"
     */
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "Baru saja"
            minutes < 60 -> "$minutes menit lalu"
            hours < 24 -> "$hours jam lalu"
            days < 7 -> "$days hari lalu"
            days < 30 -> "${days / 7} minggu lalu"
            else -> formatDate(timestamp)
        }
    }

    /**
     * Hitung berapa hari sejak tanggal tertentu
     */
    fun daysSince(dateString: String): Int {
        return try {
            val date = dateFormat.parse(dateString)
            val diff = System.currentTimeMillis() - (date?.time ?: 0)
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Cek apakah sudah boleh donor lagi (90 hari)
     */
    fun canDonateAgain(lastDonationDate: String): Boolean {
        return daysSince(lastDonationDate) >= Constants.DONATION_COOLDOWN_DAYS
    }
}