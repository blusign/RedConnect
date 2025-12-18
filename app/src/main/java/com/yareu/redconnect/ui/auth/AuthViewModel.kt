package com.yareu.redconnect.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yareu.redconnect.data.User
import com.yareu.redconnect.data.UserRole
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

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
                        bloodType = if (role == UserRole.DONOR) bloodType else "",
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
}
    