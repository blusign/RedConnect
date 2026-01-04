package com.yareu.redconnect.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import com.yareu.redconnect.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _adminRequests = MutableStateFlow<List<EmergencyRequest>>(emptyList())
    val adminRequests: StateFlow<List<EmergencyRequest>> = _adminRequests

    // Ambil data real-time untuk admin
    fun fetchAllRequests() {
        firestore.collection("emergency_requests")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    _adminRequests.value = value.toObjects(EmergencyRequest::class.java)
                }
            }
    }

    // Selesaikan Donor & Berikan Reward
    fun finalizeDonation(
        request: EmergencyRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val donorId = request.respondingDonors.firstOrNull()?.donorId ?: return

        viewModelScope.launch {
            try {
                val requestRef = firestore.collection("emergency_requests").document(request.id)
                val donorRef = firestore.collection("users").document(donorId)

                // Menggunakan TRANSACTION agar jika satu gagal, semua batal (menjaga integritas data)
                firestore.runTransaction { transaction ->
                    transaction.update(requestRef, "status", RequestStatus.COMPLETED)
                    transaction.update(donorRef, "points", FieldValue.increment(100))
                    transaction.update(donorRef, "totalDonations", FieldValue.increment(1))
                    transaction.update(donorRef, "isAvailable", false) // Otomatis istirahat
                    transaction.update(donorRef, "lastDonationDate", System.currentTimeMillis())
                    null // Return null untuk menandakan transaksi sukses
                }.await()

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Gagal memverifikasi donor")
            }
        }
    }

    fun rejectRequest(
        requestId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {        try {
            // Ubah status menjadi CANCELLED agar masuk ke tab Riwayat
            firestore.collection("emergency_requests").document(requestId)
                .update("status", RequestStatus.CANCELLED)
                .await()
            onSuccess()
        } catch (e: Exception) {
            onError(e.message ?: "Gagal menolak permintaan")
        }
        }
    }

}

