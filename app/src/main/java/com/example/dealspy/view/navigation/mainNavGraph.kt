package com.example.dealspy.view.navigation
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.rememberNavController
//import com.example.dealspy.view.screens.SearchScreen
//
//@Composable
//fun MainNavGraph(){
//        val navController = rememberNavController()
//
//        Scaffold(
//            bottomBar = { BottomNavBar(modifier = Modifier,navController, bottomMenu = Unit ) }
//        ) { padding ->
//            NavHost(
//                navController = navController,
//                startDestination = BottomNavItem.Watchlist.route,
//                modifier = Modifier.padding(padding)
//            ) {
//                composable(BottomNavItem.Watchlist.route) {
//                    WatchlistScreen(navController)
//                }
//                composable(BottomNavItem.Search.route) {
//                    SearchScreen(navController)
//                }
//                composable(BottomNavItem.Profile.route) {
//                    ProfileScreen()
//                }
//            }
//        }
//}
