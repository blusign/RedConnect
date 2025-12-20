package com.yareu.redconnect.ui.sos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yareu.redconnect.data.EmergencyRequest
import com.yareu.redconnect.data.RequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SOSViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _emergencyRequests = MutableStateFlow<List<EmergencyRequest>>(emptyList())
    val emergencyRequests: StateFlow<List<EmergencyRequest>> = _emergencyRequests

    // Fungsi Kirim SOS
    fun sendSOSRequest(
        patientName: String,
        bloodType: String,
        bloodBags: Int,
        facilityName: String,
        note: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val docRef = firestore.collection("emergency_requests").document()
                val request = EmergencyRequest(
                    id = docRef.id,
                    requesterId = uid,
                    requesterName = patientName,
                    bloodType = bloodType,
                    bloodBags = bloodBags,
                    facilityName = facilityName,
                    note = note,
                    status = RequestStatus.WAITING,
                    createdAt = System.currentTimeMillis()
                )
                docRef.set(request).await()
                onSuccess(docRef.id)
            } catch (e: Exception) {
                onError(e.message ?: "Gagal mengirim SOS")
            }
        }
    }

    // Fungsi Ambil List SOS (Real-time)
    fun fetchEmergencyRequests() {
        firestore.collection("emergency_requests")
            .whereEqualTo("status", "WAITING")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    _emergencyRequests.value = value.toObjects(EmergencyRequest::class.java)
                }
            }
    }
}
