package com.example.dealspy.vm




import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dealspy.data.repo.ThemeRepo
import com.example.dealspy.ui.theme.ThemeSelection as DealSpyTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val repo: ThemeRepo
) : ViewModel() {

    val theme: StateFlow<DealSpyTheme> = repo.themeFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, DealSpyTheme.Theme2)

    fun setTheme(theme: DealSpyTheme) = viewModelScope.launch {
        repo.setTheme(theme)
    }
}
