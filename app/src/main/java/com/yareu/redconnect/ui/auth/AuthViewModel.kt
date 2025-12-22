package com.yareu.redconnect.ui.auth

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
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                _userProfile.value = doc.toObject(User::class.java)
            } catch (e: Exception) {
                _userProfile.value = null
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
                        isAvailable = true // Default untuk pendonor
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
                            onError("Akun ini terdaftar sebagai ${user.role}, bukan $expectedRole")
                        }
                    } else {
                        onError("Data user tidak ditemukan di database")
                    }
                }
            } catch (e: Exception) {
                val friendlyMessage = when {
                    e.message?.contains("invalid-credential") == true -> "Email atau password salah. Jika belum punya akun, silakan daftar."
                    e.message?.contains("user-not-found") == true -> "Akun tidak ditemukan. Silakan daftar terlebih dahulu."
                    else -> "Terjadi kesalahan. Pastikan koneksi internet stabil."
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

    fun updateLocationToGps(lat: Double, lng: Double, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                firestore.collection("users").document(uid)
                    .update(mapOf(
                        "latitude" to lat,
                        "longitude" to lng
                    )).await()

                // Update local state
                _userProfile.value = _userProfile.value?.copy(latitude = lat, longitude = lng)
                onSuccess()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
    