package com.example.trainymvp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trainymvp.ui.home.HomeDestination
import com.example.trainymvp.ui.home.HomeScreen
import com.example.trainymvp.ui.item.WPEntryDestination
import com.example.trainymvp.ui.item.WPEntryScreen

@Composable
fun TrainyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToWPEntry = { navController.navigate(WPEntryDestination.route) }
            )
        }
        composable(route = WPEntryDestination.route) {
            WPEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
