package com.yareu.redconnect.ui.auth

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yareu.redconnect.data.User
import com.yareu.redconnect.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserData(currentUser.uid)
        }
    }

    private fun fetchUserData(uid: String) {
        firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null && snapshot.exists()) {
                    _userProfile.value = snapshot.toObject(User::class.java)
                }
            }
    }

    // Fungsi untuk mendaftarkan pengguna baru
    fun registerUser(
        email: String, // memakai email untuk login
        password: String,
        name: String,
        role: UserRole,
        bloodType: String,
        phoneNumber: String,
        address: String,
        ktpNumber: String,
        onSuccess: (uid: String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // membuat user di Firebase Authentication
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid

                if (uid != null) {
                    // membuat objek User untuk disimpan di Firestore
                    val userProfile = User(
                        id = uid,
                        name = name,
                        email = email,
                        role = role,
                        bloodType = if (role == UserRole.PENDONOR) bloodType else "",
                        phoneNumber = phoneNumber,
                        address = address,
                        isAvailable = true, // Default untuk pendonor
                        ktpNumber = ktpNumber
                    )

                    // menyimpan profil user ke Firestore di koleksi "users" dengan dokumen ID = UID
                    firestore.collection("users").document(uid).set(userProfile).await()

                    // memanggil callback sukses
                    onSuccess(uid)
                } else {
                    onError("Gagal mendapatkan User ID.")
                }
            } catch (e: Exception) {
                // Tangani error (misal: email sudah terdaftar, password lemah, dll)
                onError(e.message ?: "Terjadi error tidak diketahui")
            }
        }
    }

    fun loginUser(
        email: String,
        password: String,
        expectedRole: UserRole,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid
                if (uid != null) {
                    val doc = firestore.collection("users").document(uid).get().await()
                    val user = doc.toObject(User::class.java)

                    if (user != null) {
                        if (user.role == expectedRole) {
                            _userProfile.value = user
                            onSuccess(user)
                        } else {
                            auth.signOut() // Paksa logout karena role tidak sesuai tab yang dipilih
                            onError("Maaf, akun Anda tidak terdaftar di bagian ini.")                        }
                    } else {
                        onError("Data user tidak ditemukan")
                    }
                }
            } catch (e: Exception) {
                val friendlyMessage = when {
                    e.message?.contains("invalid-credential") == true -> "Email atau password salah. Jika belum punya akun, silakan daftar."
                    e.message?.contains("network") == true -> "Koneksi internet bermasalah."
                    e.message?.contains("user-not-found") == true -> "Akun tidak ditemukan. Silakan daftar terlebih dahulu."
                    else -> "Terjadi kesalahan sistem, silakan coba lagi nanti."
                }
                onError(friendlyMessage)
            }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Gagal kirim email") }
    }

    fun logout() {
        auth.signOut()
        _userProfile.value = null // Bersihkan data di aplikasi
    }

    fun updateAvailability(isAvailable: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                firestore.collection("users").document(uid)
                    .update("isAvailable", isAvailable).await()
                // Update state lokal juga agar UI berubah
                _userProfile.value = _userProfile.value?.copy(isAvailable = isAvailable)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateLocationToGps(context: android.content.Context, lat: Double, lng: Double, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                // Proses Geocoding: Ubah angka jadi nama alamat
                val geocoder = Geocoder(context, java.util.Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lng, 1)

                // Ambil alamat lengkap, jika gagal pakai fallback teks GPS
                val addressName = if (!addresses.isNullOrEmpty()) {
                    addresses[0].getAddressLine(0)
                } else {
                    "Lokasi GPS ($lat, $lng)"
                }

                firestore.collection("users").document(uid)
                    .update(mapOf(
                        "latitude" to lat,
                        "longitude" to lng,
                        "address" to addressName
                    )).await()

                _userProfile.value = _userProfile.value?.copy(
                    latitude = lat,
                    longitude = lng,
                    address = addressName
                )
                onSuccess()
            } catch (e: Exception) { }
        }
    }

    fun updateProfile(newName: String, newPhone: String, newAddress: String, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                firestore.collection("users").document(uid).update(mapOf(
                    "name" to newName,
                    "phoneNumber" to newPhone,
                    "address" to newAddress
                )).await()

                // Refresh data lokal
                _userProfile.value = _userProfile.value?.copy(
                    name = newName,
                    phoneNumber = newPhone,
                    address = newAddress
                )
                onSuccess()
            } catch (e: Exception) { }
        }
    }

}
    