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

class ThemeRepository(private val context: Context) {
    val themeFlow: Flow<DealSpyTheme> = context.dataStore.data.map { prefs ->
        when (prefs[THEME_KEY]) {
            "Theme1" -> DealSpyTheme.Theme1
            "Theme2" -> DealSpyTheme.Theme2
            "Theme3" -> DealSpyTheme.Theme3
            "Theme4" -> DealSpyTheme.Theme4
            "Theme5" -> DealSpyTheme.Theme5
            "Theme6" -> DealSpyTheme.Theme6
            else -> DealSpyTheme.Theme2 // default, aligns with current Option2
        }
    }

    suspend fun setTheme(theme: DealSpyTheme) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }
}
