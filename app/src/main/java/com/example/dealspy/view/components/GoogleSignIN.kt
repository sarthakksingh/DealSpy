package com.example.dealspy.view.components

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dealspy.BuildConfig
import com.example.dealspy.view.navigation.DealSpyScreens
import com.example.dealspy.vm.FirebaseAuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
@Composable
fun SignInButton(
    modifier: Modifier,
    navController: NavController,
    viewModel: FirebaseAuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val idToken = account.idToken
            if (idToken != null) {
                // pass to VM
                viewModel.signInWithGoogleCredential(
                    idToken = idToken,
                    onSuccess = {
                        // optionally also verify with backend here:
                        viewModel.getIdToken { firebaseIdToken ->
                            if (firebaseIdToken != null) {
                                // send firebaseIdToken to backend
                                // verifyWithBackend(firebaseIdToken)
                                android.util.Log.d("FIREBASE_ID_TOKEN", "Firebase ID Token: $firebaseIdToken")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Could not get Firebase ID token",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        Toast.makeText(
                            context,
                            "Sign in Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.navigate(DealSpyScreens.WatchListScreen.routes) {
                            popUpTo(DealSpyScreens.SplashScreen.routes) {
                                inclusive = true
                            }
                        }
                    },
                    onFailure = { error ->
                        Toast.makeText(context, "Sign in Failed: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(context, "Google ID token missing", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Google Sign in Failed: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            SignInButton(context).apply {
                setSize(SignInButton.SIZE_WIDE)
                setOnClickListener {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            }
        }
    )
}
