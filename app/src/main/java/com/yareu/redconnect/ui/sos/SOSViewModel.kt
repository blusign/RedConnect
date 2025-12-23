package com.yareu.redconnect.ui.sos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.yareu.redconnect.data.DonorResponse
import com.yareu.redconnect.data.DonorResponseStatus
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
        lat: Double,
        lng: Double,
        requesterPhone: String,
        urgency: String,
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
                    requesterPhone = requesterPhone,
                    bloodType = bloodType,
                    bloodBags = bloodBags,
                    facilityName = facilityName,
                    note = note,
                    urgency = urgency,
                    status = RequestStatus.WAITING,
                    createdAt = System.currentTimeMillis(),
                    latitude = lat, // Simpan koordinat asli
                    longitude = lng // Simpan koordinat asli
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
        // Jangan di-filter statusnya di query Firestore agar data ACCEPTED tetap terbaca
        firestore.collection("emergency_requests")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    _emergencyRequests.value = value.toObjects(EmergencyRequest::class.java)
                }
            }
    }

    fun acceptRequest(
        requestId: String,
        donorProfile: com.yareu.redconnect.data.User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Ambil data request terbaru dari Firestore
                val docRef = firestore.collection("emergency_requests").document(requestId)
                val snapshot = docRef.get().await()
                val request = snapshot.toObject(EmergencyRequest::class.java) ?: return@launch

                // Cek Kuota berdasarkan Urgensi (Gunakan field 'note' atau tambah field urgency)
                val currentDonors = request.respondingDonors.size
                val maxAllowed = when(request.urgency) {
                    "Tinggi" -> 1
                    "Sedang" -> 3
                    "Terencana" -> 10
                    else -> 1
                }

                if (currentDonors >= maxAllowed) {
                    onError("Maaf, kuota pendonor untuk permintaan ini sudah terpenuhi.")
                    return@launch
                }

                // Buat objek respons
                val response = DonorResponse(
                    id = auth.currentUser?.uid ?: "",
                    requestId = requestId,
                    donorId = auth.currentUser?.uid ?: "",
                    donorName = donorProfile.name,
                    bloodType = donorProfile.bloodType,
                    phoneNumber = donorProfile.phoneNumber,
                    distance = "Terdekat",
                    status = DonorResponseStatus.ON_WAY
                )

                // Update Firestore
                docRef.update(
                    "respondingDonors", FieldValue.arrayUnion(response),
                    "status", RequestStatus.ACCEPTED
                ).await()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Gagal menerima permintaan")
            }
        }
    }

    fun deleteRequest(requestId: String) {    viewModelScope.launch {
        try {
            firestore.collection("emergency_requests").document(requestId).delete().await()
        } catch (e: Exception) {
            // Handle error silent
        }
    }
    }

}
