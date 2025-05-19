package com.example.trainymvp.ui.item

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainymvp.data.ExerciseImage
import com.example.trainymvp.data.ExerciseImageRepository
import com.example.trainymvp.data.Item
import com.example.trainymvp.data.ItemsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Base64
import kotlinx.coroutines.flow.Flow

class WPEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val exerciseImageRepository: ExerciseImageRepository
) : ViewModel() {
    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    var imagesUiState by mutableStateOf(ImagesUiState())
        private set

    val itemId: Int = checkNotNull(savedStateHandle[WPEditDestination.itemIdArg]).also {
        Log.d("ViewModel", "Retrieved itemId = $it")
    } as Int

    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }

        viewModelScope.launch {
            imagesUiState = exerciseImageRepository.getExerciseImageByItemId(itemId)
                .filterNotNull()
                .toImageUiState()
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values
     */
    fun updateItemUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateItemInput(itemDetails))
    }

    fun updateImagesUiState(imagesToAdd: MutableList<@JvmSuppressWildcards Uri>) {
        imagesToAdd.addAll(imagesUiState.images)

        imagesUiState =
            ImagesUiState(images = imagesToAdd)
    }

    private fun validateItemInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem(context: Context) {
        if (validateItemInput()) {
            itemsRepository.updateItem(
                itemUiState.itemDetails.toItem()
            )
        }

        var currentId = 0
        itemsRepository.getItemStreamByTitle(itemUiState.itemDetails.title)
            .collect {
                currentId = it!!.toItemUiState().itemDetails.id
            }

        if (imagesUiState.images.isNotEmpty()) {
            imagesUiState.images.forEachIndexed { index, _ ->
                Log.d("Image", "index = ${index}, currentId = ${currentId}")
                exerciseImageRepository.updateExerciseImage(
                    imagesUiState.toImageDetailes(context = context, index = index, itemId = currentId).toExerciseImage()
                )
            }
        }
    }
}

suspend fun Flow<List<ExerciseImage>>.toImageUiState(): ImagesUiState {
    val images = this.first().map { exerciseImage ->
        val base64 = Base64.encodeToString(exerciseImage.imageData, Base64.DEFAULT)
        Uri.parse("data:image/jpeg;base64,$base64")
    }.toMutableList()

    return ImagesUiState(images)
}