package com.example.dealspy.data.repo



import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.dealspy.ui.theme.ThemeSelection as DealSpyTheme


private val Context.dataStore by preferencesDataStore(name = "DealSpy_settings")
private val THEME_KEY = stringPreferencesKey("DealSpy_theme")

class ThemeRepo(private val context: Context) {
    val themeFlow: Flow<DealSpyTheme> = context.dataStore.data.map { prefs ->
        when (prefs[THEME_KEY]) {
            "Theme1" -> DealSpyTheme.Theme1
            "Theme2" -> DealSpyTheme.Theme2
            "Theme3" -> DealSpyTheme.Theme3
            "Theme4" -> DealSpyTheme.Theme4
            "Theme5" -> DealSpyTheme.Theme5
            "Theme6" -> DealSpyTheme.Theme6
            "Theme7" -> DealSpyTheme.Theme7
            "Theme8" -> DealSpyTheme.Theme8
            "Theme9" -> DealSpyTheme.Theme9
            "Theme10" -> DealSpyTheme.Theme10
            "Theme11" -> DealSpyTheme.Theme11
            "Theme12" -> DealSpyTheme.Theme12
            "Theme13" -> DealSpyTheme.Theme13
            "Theme14" -> DealSpyTheme.Theme14
            "Theme15" -> DealSpyTheme.Theme15
            "Theme16" -> DealSpyTheme.Theme16
            "Theme17" -> DealSpyTheme.Theme17
            "Theme18" -> DealSpyTheme.Theme18
            else -> DealSpyTheme.Theme17 //default theme
        }
    }

    suspend fun setTheme(theme: DealSpyTheme) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }
}
