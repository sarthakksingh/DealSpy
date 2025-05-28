package com.example.dealspy.view.navigation

import androidx.navigation.NavController

//Todo//

sealed class BottomNavOptions (
    val route: String,
    val labelOfIcon: String,
    val unselectedIcon: Int,
    val selectedIcon: Int,
    val onOptionClicked: (NavController) -> Unit,
){
}