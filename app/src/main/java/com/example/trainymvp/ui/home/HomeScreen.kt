package com.example.trainymvp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.trainymvp.R
import com.example.trainymvp.TrainyTopAppBar
import com.example.trainymvp.data.Item
import com.example.trainymvp.data.TimePreset
import com.example.trainymvp.navigation.NavigationDestination
import com.example.trainymvp.ui.item.WPItem

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
//    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
//    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
//    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TrainyTopAppBar(
                title = stringResource(R.string.homeScreenTitle),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.item_entry_title),
                    modifier = Modifier.size(30.dp)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
//            itemList = homeUiState.itemList,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
//    itemList: List<Item>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
//        if (itemList.isEmpty()) {
//            Text(
//                text = stringResource(R.string.no_item_description),
//                textAlign = TextAlign.Center,
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(contentPadding),
//            )
//        } else {
//            InventoryList(
//                itemList = itemList,
//                onItemClick = { onItemClick(it.id) },
//                contentPadding = contentPadding,
//                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
//            )
//        }
        InventoryList(
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_large))
        )
    }
}

@Composable
private fun InventoryList(
//    itemList: List<Item>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        contentPadding = contentPadding
    ) {
        items(3) { index ->
            WPItem(
                Item(
                    1,
                    "Title",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit.",
                    TimePreset(0, 45, 30)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            navigateToItemEntry = { /*TODO*/ }
        )
    }
}