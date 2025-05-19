package com.example.trainymvp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trainymvp.ui.item.WPEditDestination
import com.example.trainymvp.ui.item.WPEditScreen
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
                navigateToWPEntry = { navController.navigate(WPEntryDestination.route) },
                navigateToWPEdit = {
                    Log.d("Navigation", "Navigating to edit with itemId = $it")
                    navController.navigate("${WPEditDestination.route}/${it}")
                }
            )
        }
        composable(route = WPEntryDestination.route) {
            WPEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = WPEditDestination.routeWithArgs,
            arguments = listOf(navArgument(WPEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            WPEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
