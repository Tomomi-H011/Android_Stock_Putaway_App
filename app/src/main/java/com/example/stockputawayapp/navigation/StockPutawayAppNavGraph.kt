package com.example.stockputawayapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.stockputawayapp.ui.home.HomeDestination
import com.example.stockputawayapp.ui.home.HomeScreen
import com.example.stockputawayapp.ui.settings.InfoScreen
import com.example.stockputawayapp.ui.settings.InfoScreenDestination
import com.example.stockputawayapp.ui.settings.SettingsScreen
import com.example.stockputawayapp.ui.settings.SettingsScreenDestination
import com.example.stockputawayapp.ui.putaway.PutAwayDestination
import com.example.stockputawayapp.ui.putaway.PutawayScreen

// Provides navigation graph for the app.
@Composable
fun StockPutawayNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route) {
            HomeScreen(
                onInfoClick = { navController.navigate(InfoScreenDestination.route) },
                onSettingsClick = { navController.navigate(SettingsScreenDestination.route) },
                onProductClick = { productId ->
                    navController.navigate("${PutAwayDestination.route}/$productId") },
            )
        }
        composable(route = InfoScreenDestination.route) {
            InfoScreen(
                onBackClick = { navController.navigate(HomeDestination.route) },
                onSettingsClick = { navController.navigate(SettingsScreenDestination.route) }
            )
        }
        composable(route = SettingsScreenDestination.route) {
            SettingsScreen(
                onBackClick = { navController.navigate(HomeDestination.route) },
                onInfoClick = { navController.navigate(InfoScreenDestination.route) }
            )
        }
        composable(
            route = PutAwayDestination.routeWithArgs,
            arguments = listOf(navArgument(PutAwayDestination.productIdArg) {
                type = NavType.StringType
            })
        ){ backStackEntry ->
            val productId = backStackEntry.arguments?.getString(PutAwayDestination.productIdArg)
            PutawayScreen(
                productId = productId,
                onBackClick = { navController.navigate(HomeDestination.route) },
                onSettingsClick = { navController.navigate(SettingsScreenDestination.route) },
                onInfoClick = { navController.navigate(InfoScreenDestination.route) }
            )
        }
    }
}