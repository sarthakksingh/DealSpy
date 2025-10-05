package com.example.dealspy.view.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dealspy.BuildConfig
import com.example.dealspy.R
import com.example.dealspy.ui.state.UiState
import com.example.dealspy.vm.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    val loginState by loginViewModel.loginState.collectAsState()
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
            val userCurr = FirebaseAuth.getInstance().currentUser

            userCurr?.getIdToken(true)?.addOnSuccessListener { tokenResult ->
                val firebaseIdToken = tokenResult.token
                if (firebaseIdToken != null) {
                    Log.d("Google Auth", "Firebase Token: $firebaseIdToken")

                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val fcmToken = task.result
                                Log.d("FCM", "FCM Token: $fcmToken")

                                loginViewModel.loginWithGoogle(firebaseIdToken)
                            } else {
                                Log.e("FCM", "Fetching FCM token failed", task.exception)
                                Toast.makeText(context, "Failed to get notification token", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Login Failed!", Toast.LENGTH_LONG).show()
                }
            }?.addOnFailureListener {
                Toast.makeText(context, "Failed to fetch Firebase token!", Toast.LENGTH_LONG).show()
            }
        },
        onAuthError = {
            user = null
            Toast.makeText(context, "Login Failed!", Toast.LENGTH_LONG).show()
        }
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = loginState !is UiState.Loading) {
                val token = BuildConfig.GOOGLE_WEB_CLIENT_ID
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)

                googleSignInClient.signOut().addOnCompleteListener {
                    googleSignInClient.revokeAccess().addOnCompleteListener {
                        Log.d("SignInButton", "Signed out and access revoked.")
                        launcher.launch(googleSignInClient.signInIntent)
                    }
                }
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (loginState is UiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.Gray,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Continue with Google",
                    color = Color.Gray.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
