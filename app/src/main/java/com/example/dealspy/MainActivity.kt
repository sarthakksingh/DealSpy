package com.example.dealspy

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.ui.theme.ThemeSelection
import com.example.dealspy.view.navigation.AppNavigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {

        // Install and IMMEDIATELY dismiss system splash
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }

        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.BLACK
        setContent {
            DealSpyTheme(theme = ThemeSelection.Option2) {
                AppNavigation()
            }
        }
    }
}
