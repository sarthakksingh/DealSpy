package com.example.dealspy.vm

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirebaseAuthViewModel @Inject constructor() : ViewModel() {
    private val firebaseAuth = Firebase.auth

    fun signInWithGoogleCredential(
        idToken: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun getIdToken(onToken: (String?) -> Unit) {
        firebaseAuth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener { tokenTask ->
                if (tokenTask.isSuccessful) {
                    onToken(tokenTask.result?.token)
                } else {
                    onToken(null)
                }
            }
    }

    fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun signOut(onComplete: () -> Unit) {
        firebaseAuth.signOut()
        onComplete()
    }
}
