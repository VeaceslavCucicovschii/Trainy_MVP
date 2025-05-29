package com.example.trainymvp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trainymvp.R
import com.example.trainymvp.TrainyTopAppBar
import com.example.trainymvp.data.Item
import com.example.trainymvp.navigation.NavigationDestination
import com.example.trainymvp.ui.AppViewModelProvider
import com.example.trainymvp.ui.item.WPCard

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToWPEdit: (Int) -> Unit,
    navigateToWPStart: (Int) -> Unit,
    navigateToWPEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrainyTopAppBar(
                title = stringResource(R.string.home_screen_title),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToWPEntry,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.wp_entry_title),
                    modifier = Modifier.size(30.dp)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            navigateToWPEdit = navigateToWPEdit,
            navigateToWPStart = navigateToWPStart,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
            // contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Item>,
    navigateToWPEdit: (Int) -> Unit,
    navigateToWPStart: (Int) -> Unit,
    modifier: Modifier = Modifier,
    // contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                // modifier = Modifier.padding(contentPadding),
            )
        } else {
            InventoryList(
                itemList = itemList,
                navigateToWPEdit = { navigateToWPEdit(it.itemId) },
                navigateToWPStart = { navigateToWPStart(it.itemId) },
                // contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Item>,
    navigateToWPEdit: (Item) -> Unit,
    navigateToWPStart: (Item) -> Unit,
    // contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        // contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.itemId }) { item ->
            WPCard(
                navigateToWPEdit = { navigateToWPEdit(item) },
                navigateToWPStart = { navigateToWPStart(item) },
                item = item
            )
        }
    }
}