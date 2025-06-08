package com.example.dealspy.view.navigation

sealed class DealSpyScreens(val routes: String) {
    object ProfileScreen : DealSpyScreens("profile_screen")
    object SplashScreen: DealSpyScreens("splash_screen")
    object SearchScreen: DealSpyScreens("search_screen")
    object PriceCompareScreen: DealSpyScreens("price_compare_screen")
    object WatchListScreen: DealSpyScreens("watchlist_screen")
}