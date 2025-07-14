package com.example.dealspy.view.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dealspy.view.screens.PriceCompareScreen
import com.example.dealspy.view.screens.ProfileScreen
import com.example.dealspy.view.screens.SearchScreen
import com.example.dealspy.view.screens.SplashScreen
import com.example.dealspy.view.screens.WatchlistScreen

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val navItemList = listOf(
        NavItems("watchList", Icons.Default.Home, Icons.Outlined.Home),
        NavItems("Search", Icons.Default.Search, Icons.Outlined.Search),
        NavItems("Profile", Icons.Default.AccountCircle, Icons.Outlined.AccountCircle)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        DealSpyScreens.WatchListScreen.routes,
        DealSpyScreens.SearchScreen.routes,
        DealSpyScreens.ProfileScreen.routes
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    navItemList.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                navController.navigate(item.title + "_screen") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selectedIndex == index) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = DealSpyScreens.SplashScreen.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(DealSpyScreens.WatchListScreen.routes) {
                WatchlistScreen(navController = navController)
            }
            composable(DealSpyScreens.ProfileScreen.routes) {
                ProfileScreen(navController = navController)
            }
            composable(DealSpyScreens.SearchScreen.routes) {
                SearchScreen(navController = navController)
            }
            composable(route = DealSpyScreens.SplashScreen.routes) {
                SplashScreen(navController = navController)
            }
            composable(
                route = "${DealSpyScreens.PriceCompareScreen.routes}/{product}",
                arguments = listOf(navArgument("product") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("product")?.let { productName ->
                    PriceCompareScreen(productName = productName, navController = navController)
                }
            }
        }
    }
}
