package com.example.trainymvp.ui.start

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trainymvp.R
import com.example.trainymvp.navigation.NavigationDestination
import com.example.trainymvp.ui.AppViewModelProvider

object WPEditDestination : NavigationDestination {
    override val route = "wp_start"
    override val titleRes = R.string.wp_start_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun WPStartScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: WPStartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

}