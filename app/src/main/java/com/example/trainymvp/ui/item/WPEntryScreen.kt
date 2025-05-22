package com.example.trainymvp.ui.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.compose.AppTheme
import com.example.trainymvp.R
import com.example.trainymvp.TrainyTopAppBar
import com.example.trainymvp.navigation.NavigationDestination
import com.example.trainymvp.ui.AppViewModelProvider
import kotlinx.coroutines.launch

object WPEntryDestination : NavigationDestination {
    override val route = "wp_entry"
    override val titleRes = R.string.wp_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WPEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: WPEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TrainyTopAppBar(
                title = stringResource(WPEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            imagesUiState = viewModel.imagesUiState,
            onItemValueChange = viewModel::updateItemUiState,
            onImagesValueAdd = viewModel::addImagesToImagesUiState,
            onImagesValueChange = viewModel::updateImagesUiState,

            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.saveItem(context)
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )

    }
}

@Composable
fun ItemEntryBody(
    itemUiState: ItemUiState,
    imagesUiState: ImagesUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onImagesValueAdd: (MutableList<@JvmSuppressWildcards Uri>) -> Unit,
    onImagesValueChange: (MutableList<@JvmSuppressWildcards Uri>) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            imagesList = imagesUiState.images,
            onItemValueChange = onItemValueChange,
            onImagesValueAdd = onImagesValueAdd,
            onImagesValueChange = onImagesValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.create_action))
        }
    }
}

@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    imagesList: MutableList<@JvmSuppressWildcards Uri> = mutableListOf(),
    onItemValueChange: (ItemDetails) -> Unit = {},
    onImagesValueAdd: (MutableList<@JvmSuppressWildcards Uri>) -> Unit = {},
    onImagesValueChange: (MutableList<@JvmSuppressWildcards Uri>) -> Unit = {},

    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        OutlinedTextField(
            value = itemDetails.title,
            onValueChange = { onItemValueChange(itemDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.wp_title_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = itemDetails.description,
            onValueChange = { onItemValueChange(itemDetails.copy(description = it)) },
            label = { Text(stringResource(R.string.wp_description_req)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )
        ImageInputForm(
            imagesList = imagesList,
            onValueAdd = onImagesValueAdd,
            onValueChange = onImagesValueChange
        )
    }
}

@Composable
fun ImageInputForm(
    imagesList: MutableList<@JvmSuppressWildcards Uri>,
    onValueAdd: (MutableList<@JvmSuppressWildcards Uri>) -> Unit = {},
    onValueChange: (MutableList<@JvmSuppressWildcards Uri>) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Registers a photo picker activity launcher in multi-select mode.
    val pickMultipleImages = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        // Callback is invoked after the user selects media items or closes the photo picker.
        if (uris.isNotEmpty()) {
            onValueAdd(uris.toMutableList())
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.heightIn(max = 4000.dp)
    ) {
        itemsIndexed(imagesList) { index, _ ->
            ImageContainer(
                index = index,
                imagesList = imagesList,
                onRemove = {
                    val newList = imagesList.toMutableList()
                    newList.removeAt(index)
                    onValueChange(newList)
                }
            )
        }
    }
    
    Button(
        onClick = {
            pickMultipleImages.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Gray,
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth()
            .height(205.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(id = R.string.add_exercise_image),
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun ImageContainer(
    index: Int,
    imagesList: MutableList<@JvmSuppressWildcards Uri>,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
    ) {
        Image(
            painter = rememberAsyncImagePainter(imagesList[index]),
            contentDescription = null,
            modifier.clickable { expanded = !expanded }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Remove") },
                onClick = {
                    expanded = false
                    onRemove()
                }
            )
        }
    }
}

@Preview
@Composable
fun WPEntryScreenPreview() {
    AppTheme {
        ItemInputForm(
            itemDetails = ItemDetails(
                0,
                "",
                ""
            ),
        )
    }
}