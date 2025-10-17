package com.example.stockputawayapp.navigation

// Interface to describe the navigation destination for the app

interface NavigationDestination {

    val route: String

    // String resource id to that contains title to be displayed for the screen.
    val titleRes: Int
}