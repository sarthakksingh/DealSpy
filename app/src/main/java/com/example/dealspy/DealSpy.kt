package com.example.dealspy

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DealSpy: Application() {
    override fun onCreate() {
        super.onCreate()

        // 🔹 INITIALIZE FIREBASE FIRST
        FirebaseApp.initializeApp(this)

        // 🔹 JUST LOG THE AUTH STATE - DON'T INTERFERE
        try {
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                Log.d("DealSpy", "✅ User logged in: ${currentUser.email}")
            } else {
                Log.d("DealSpy", "ℹ️ No user logged in")
            }
        } catch (e: Exception) {
            Log.e("DealSpy", "🔥 Firebase Auth error: ${e.message}")
            // Let the AppNavigation handle the error
        }
    }
}
