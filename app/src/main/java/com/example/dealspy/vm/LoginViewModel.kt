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

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    fun loginWithGoogle(firebaseIdToken: String) {
        viewModelScope.launch {
            try {
                _loginState.value = UiState.Loading
                Log.d("LoginViewModel", "Firebase already authenticated, calling backend...")

                val fcmToken = authRepo.getFCMToken()
                Log.d("LoginViewModel", "FCM Token: $fcmToken")

                val verifyResponse = authRepo.verifyTokenWithFCM(firebaseIdToken, fcmToken)

                if (verifyResponse.success) {
                    Log.d("LoginViewModel", "Backend verification successful!")
                    _loginState.value = UiState.Success(Unit)
                } else {
                    _loginState.value = UiState.Error("Backend verification failed: ${verifyResponse.message}")
                }

            } catch (e: UnknownHostException) {
                Log.e("LoginViewModel", "Network error", e)
                _loginState.value = UiState.NoInternet
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Backend call failed: ${e.message}", e)
                _loginState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetStates() {
        _loginState.value = UiState.Idle
        Log.d("LoginViewModel", "States reset to Idle")
    }
}
