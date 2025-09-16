package com.example.dealspy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import com.example.dealspy.ui.theme.DealSpyTheme
import com.example.dealspy.ui.theme.ThemeSelection
import com.example.dealspy.view.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DealSpyTheme(theme = ThemeSelection.Option1) {
                AppNavigation()
            }
        }
    }
}
