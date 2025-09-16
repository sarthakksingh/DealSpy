package com.example.dealspy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


enum class ThemeSelection {
    Option1, // Cool & Professional
    Option2, // Warm & Inviting
    Option3, // Modern & Vibrant
    Option4, // Elegant & Minimalist
    Option5, // Deep Forest Green
    Option6  // Retro Sunset Dark
}


private val option1ColorScheme = darkColorScheme(
    background = opt1_Primary_Background,
    surface = opt1_Card_Background,
    onBackground = opt1_Text_Primary,
    onSurface = opt1_Text_Primary,
    primary = opt1_Price_Highlight,
    onPrimary = opt1_Card_Background,
    secondary = opt1_BuyNow_Button,
    onSecondary = opt1_Card_Background,
    onSurfaceVariant = opt1_Text_Secondary_Discount,
    errorContainer = opt1_Timer_Background,
    onErrorContainer = opt1_Timer_Text
)


private val option2ColorScheme = darkColorScheme(
    background = opt2_Primary_Background,
    surface = opt2_Card_Background,
    onBackground = opt2_Text_Primary,
    onSurface = opt2_Text_Primary,
    primary = opt2_Price_Highlight,
    onPrimary = opt2_Card_Background,
    secondary = opt2_BuyNow_Button,
    onSecondary = opt2_Card_Background,
    onSurfaceVariant = opt2_Text_Secondary_Discount,
    errorContainer = opt2_Timer_Background,
    onErrorContainer = opt2_Timer_Text
)


private val option3ColorScheme = darkColorScheme(
    background = opt3_Primary_Background,
    surface = opt3_Card_Background,
    onBackground = opt3_Text_Primary,
    onSurface = opt3_Text_Primary,
    primary = opt3_Price_Highlight,
    onPrimary = opt3_Card_Background,
    secondary = opt3_BuyNow_Button,
    onSecondary = opt3_Card_Background,
    onSurfaceVariant = opt3_Text_Secondary_Discount,
    errorContainer = opt3_Timer_Background,
    onErrorContainer = opt3_Timer_Text
)


private val option4ColorScheme = darkColorScheme(
    background = opt4_Primary_Background,
    surface = opt4_Card_Background,
    onBackground = opt4_Text_Primary,
    onSurface = opt4_Text_Primary,
    primary = opt4_Price_Highlight,
    onPrimary = opt4_Card_Background,
    secondary = opt4_BuyNow_Button,
    onSecondary = opt4_Card_Background,
    onSurfaceVariant = opt4_Text_Secondary_Discount,
    errorContainer = opt4_Timer_Background,
    onErrorContainer = opt4_Timer_Text
)


private val option5ColorScheme = darkColorScheme(
    background = opt5_Primary_Background,
    surface = opt5_Card_Background,
    onBackground = opt5_Text_Primary,
    onSurface = opt5_Text_Primary,
    primary = opt5_Price_Highlight,
    onPrimary = opt5_Card_Background,
    secondary = opt5_BuyNow_Button,
    onSecondary = opt5_Card_Background,
    onSurfaceVariant = opt5_Text_Secondary_Discount,
    errorContainer = opt5_Timer_Background,
    onErrorContainer = opt5_Timer_Text
)


private val option6ColorScheme = darkColorScheme(
    background = opt6_Primary_Background,
    surface = opt6_Card_Background,
    onBackground = opt6_Text_Primary,
    onSurface = opt6_Text_Primary,
    primary = opt6_Price_Highlight,
    onPrimary = opt6_Card_Background,
    secondary = opt6_BuyNow_Button,
    onSecondary = opt6_Card_Background,
    onSurfaceVariant = opt6_Text_Secondary_Discount,
    errorContainer = opt6_Timer_Background,
    onErrorContainer = opt6_Timer_Text
)

@Composable
fun DealSpyTheme(
    theme: ThemeSelection = ThemeSelection.Option1,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        ThemeSelection.Option1 -> option1ColorScheme
        ThemeSelection.Option2 -> option2ColorScheme
        ThemeSelection.Option3 -> option3ColorScheme
        ThemeSelection.Option4 -> option4ColorScheme
        ThemeSelection.Option5 -> option5ColorScheme
        ThemeSelection.Option6 -> option6ColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}