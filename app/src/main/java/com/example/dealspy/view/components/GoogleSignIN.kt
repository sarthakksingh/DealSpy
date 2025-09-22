// Update your SignInButton component
package com.example.dealspy.view.components

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.dealspy.BuildConfig
import com.example.dealspy.vm.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current

    // 🔹 GOOGLE SIGN-IN LAUNCHER WITH FCM TOKEN INTEGRATION
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        task.addOnCompleteListener { signInTask ->
            if (signInTask.isSuccessful) {
                val account = signInTask.result
                val idToken = account?.idToken

                if (idToken != null) {
                    Log.d("SignInButton", "Google sign-in successful, ID token: $idToken")

                    // 🔹 AUTOMATICALLY GET FCM TOKEN AND CALL LOGIN
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener { fcmTask ->
                            if (fcmTask.isSuccessful) {
                                val fcmToken = fcmTask.result
                                Log.d("SignInButton", "FCM Token: $fcmToken")

                                // Call your LoginViewModel method that handles both tokens
                                loginViewModel.loginWithGoogle(idToken)
                            } else {
                                Log.e(
                                    "SignInButton",
                                    "Fetching FCM token failed",
                                    fcmTask.exception
                                )
                                // Still try login without FCM token as fallback
                                loginViewModel.loginWithGoogle(idToken)
                            }
                        }
                } else {
                    Log.e("SignInButton", "ID token is null")
                }
            } else {
                Log.e("SignInButton", "Google sign-in failed", signInTask.exception)
            }
        }
    }

    Button(
        onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .requestProfile()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            // Sign out and revoke access for clean login
            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInClient.revokeAccess().addOnCompleteListener {
                    Log.d("SignInButton", "Signed out and access revoked.")
                    launcher.launch(googleSignInClient.signInIntent)
                }
            }
        },
        modifier = modifier
    ) {
        Text("Sign in with Google")
    }
}
