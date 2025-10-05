package com.example.dealspy.view.navigation

sealed class DealSpyScreens(val routes: String) {
    object ProfileScreen : DealSpyScreens("profile_screen")
    object LoginScreen: DealSpyScreens("login_screen")
    object SearchScreen: DealSpyScreens("search_screen")
    object PriceCompareScreen: DealSpyScreens("price_compare_screen")
    object WatchListScreen: DealSpyScreens("watchlist_screen")

    companion object {
        fun createPriceCompareRoute(productName: String): String {
            return "price_compare_screen/$productName"
        }
    }
}