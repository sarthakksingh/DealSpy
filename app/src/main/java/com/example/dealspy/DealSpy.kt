package com.example.dealspy

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class DealSpy: Application() {
    override fun onCreate() {
        super.onCreate()

        // 🔹 AUTOMATIC KEYSTORE CORRUPTION RECOVERY
        recoverFromKeystoreCorruption()
    }

    private fun recoverFromKeystoreCorruption() {
        Thread {
            try {
                // Test Firebase keystore access
                val currentUser = Firebase.auth.currentUser
                Log.d("DealSpy", "Firebase user check: ${currentUser?.email ?: "None"}")

                // Force refresh to trigger any keystore errors
                currentUser?.getIdToken(false)?.addOnFailureListener { error ->
                    Log.e("DealSpy", "🔧 Token refresh failed - keystore corrupt: ${error.message}")
                    clearCorruptedState()
                }

            } catch (e: Exception) {
                Log.e("DealSpy", "🔧 Keystore corruption detected: ${e.message}")
                clearCorruptedState()
            }
        }.start()
    }

    private fun clearCorruptedState() {
        try {
            // Sign out from Firebase
            Firebase.auth.signOut()

            // Clear all app preferences
            val prefs = getSharedPreferences("dealspy_auth", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            // Clear Firebase internal cache
            val firebasePrefs = getSharedPreferences("com.google.firebase.auth", Context.MODE_PRIVATE)
            firebasePrefs.edit().clear().apply()

            Log.d("DealSpy", "✅ Cleared corrupted Firebase state")

        } catch (e: Exception) {
            Log.e("DealSpy", "Failed to clear corruption: ${e.message}")
        }
    }
}
