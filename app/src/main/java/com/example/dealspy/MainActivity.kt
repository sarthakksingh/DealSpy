package com.example.dealspy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.view.navigation.AppNavigation
import com.example.dealspy.vm.ThemeViewModel
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
            val themeVm: ThemeViewModel = hiltViewModel()
            val currentTheme by themeVm.theme.collectAsState()

            DealSpyTheme(theme = currentTheme)
            {
                AppNavigation()
            }
        }
    }
}
