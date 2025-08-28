package com.example.dealspy.view.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

sealed class BottomNavOptions(
    val route: String,
    val labelOfIcon: String,
    val icon: ImageVector,
    val onOptionClicked: (NavController) -> Unit,
) {
    data object WatchlistOption : BottomNavOptions(
        route = DealSpyScreens.WatchListScreen.routes,
        labelOfIcon = "Watchlist",
        icon = Icons.Default.Home,
        onOptionClicked = { navController ->
            navigateWithDebounce(navController, DealSpyScreens.WatchListScreen.routes)
        }
    )

    data object SearchOption : BottomNavOptions(
        route = DealSpyScreens.SearchScreen.routes,
        labelOfIcon = "Search",
        icon = Icons.Default.Search,
        onOptionClicked = { navController ->
            navigateWithDebounce(navController, DealSpyScreens.SearchScreen.routes)
        }
    )

    data object ProfileOption : BottomNavOptions(
        route = DealSpyScreens.ProfileScreen.routes,
        labelOfIcon = "Profile",
        icon = Icons.Default.AccountCircle,
        onOptionClicked = { navController ->
            navigateWithDebounce(navController, DealSpyScreens.ProfileScreen.routes)
        }
    )

    companion object {
        val bottomNavOptions = listOf(
            WatchlistOption,
            SearchOption,
            ProfileOption
        )

        private var lastNavigationTime = 0L
        private const val NAVIGATION_DEBOUNCE_TIME = 500L

        private fun navigateWithDebounce(navController: NavController, route: String) {
            val currentTime = System.currentTimeMillis()

            if (navController.currentDestination?.route == route) return
            if (currentTime - lastNavigationTime < NAVIGATION_DEBOUNCE_TIME) return

            lastNavigationTime = currentTime

            try {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
