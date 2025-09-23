package com.example.dealspy.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.model.CustomResponse
import com.example.dealspy.data.repo.AuthRepo
import com.example.dealspy.ui.state.UiState
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    fun getGoogleSignInClient() = googleSignInClient

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _verifyTokenState = MutableStateFlow<UiState<CustomResponse<Unit>>>(UiState.Idle)
    val verifyTokenState = _verifyTokenState.asStateFlow()

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                _loginState.value = UiState.Loading
                Log.d("LoginViewModel", "Starting Google login process...")

                // Step 1: Authenticate with Firebase
                val loginSuccess = authRepo.signInWithGoogleCredential(idToken)

                if (loginSuccess) {
                    Log.d("LoginViewModel", "Firebase authentication successful")

                    // Step 2: Get Firebase ID Token
                    val firebaseToken = authRepo.getIdToken()
                    val cleanToken = firebaseToken.removePrefix("Bearer ")

                    // Step 3: Get FCM Token
                    val fcmToken = authRepo.getFCMToken()
                    Log.d("LoginViewModel", "FCM Token retrieved: $fcmToken")

                    // Step 4: Verify token + Save user data with FCM
                    Log.d("LoginViewModel", "Calling /verify endpoint with FCM token...")
                    val verifyResponse = authRepo.verifyTokenWithFCM(cleanToken, fcmToken)

                    if (verifyResponse.success == true) {
                        Log.d("LoginViewModel", "User verified and data saved successfully!")
                        _verifyTokenState.value = UiState.Success(verifyResponse)
                        _loginState.value = UiState.Success(Unit)
                    } else {
                        _loginState.value =
                            UiState.Error("Backend verification failed: ${verifyResponse.message}")
                    }

                } else {
                    _loginState.value =
                        UiState.Error("Google authentication failed. Please try again.")
                }
            } catch (e: UnknownHostException) {
                // Network error
                Log.e("LoginViewModel", "Network error during login", e)
                _loginState.value = UiState.NoInternet
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login process failed: ${e.message}", e)
                _loginState.value = UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun resetStates() {
        _loginState.value = UiState.Idle
        _verifyTokenState.value = UiState.Idle
        Log.d("LoginViewModel", "States reset to Idle")
    }
}
