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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {
    fun getGoogleSignInClient() = googleSignInClient

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val loginState = _loginState.asStateFlow()

    private val _verifyTokenState = MutableStateFlow<UiState<CustomResponse<Unit>>>(UiState.Loading)
    val verifyTokenState = _verifyTokenState.asStateFlow()

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                _loginState.value = UiState.Loading
                val loginSuccess = authRepo.signInWithGoogleCredential(idToken)
                if (loginSuccess) {
                    val token = authRepo.getIdToken()
                    val verifyResponse = authRepo.verifyToken(token.removePrefix("Bearer "))
                    _verifyTokenState.value = UiState.Success(verifyResponse)
                    _loginState.value = UiState.Success(Unit)
                } else {
                    _loginState.value = UiState.Failed("Google Sign-In Failed")
                }
            } catch (e: Exception) {
                _loginState.value = UiState.Failed(e.message ?: "Unknown error during login")
            }
        }
    }
}
