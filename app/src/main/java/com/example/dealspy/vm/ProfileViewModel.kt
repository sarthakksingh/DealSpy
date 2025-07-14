package com.example.dealspy.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dealspy.BuildConfig
import com.example.dealspy.data.model.Product
import com.google.ai.client.generativeai.type.Content
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {
    fun onDeleteFromWishlist(product : Product){

    }
    fun onClearWatchlist() {

    }
    fun signOut(context: Context, onComplete:()-> Unit){
        Firebase.auth.signOut()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        googleSignInClient.signOut().addOnCompleteListener {
            onComplete()
        }
    }

}