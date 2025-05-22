package com.example.trainymvp.ui.item

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.trainymvp.data.ExerciseImage
import com.example.trainymvp.data.ExerciseImageRepository
import com.example.trainymvp.data.Item
import com.example.trainymvp.data.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class WPEditViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val exerciseImageRepository: ExerciseImageRepository
) : AndroidViewModel(application) {
    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    var imagesUiState by mutableStateOf(ImagesUiState())
        private set

    val itemId: Int = checkNotNull(savedStateHandle[WPEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }

        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            imagesUiState = exerciseImageRepository
                .getExerciseImageByItemId(itemId)
                .filterNotNull()
                .toImageUiState(context)
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

    fun addImagesToImagesUiState(imagesToAdd: MutableList<@JvmSuppressWildcards Uri>) {
        imagesToAdd.addAll(imagesUiState.images)

        imagesUiState =
            ImagesUiState(images = imagesToAdd)
    }

    fun updateImagesUiState(images: MutableList<@JvmSuppressWildcards Uri>) {
        imagesUiState = ImagesUiState(images = images)
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



        if (imagesUiState.images.isNotEmpty()) {
            exerciseImageRepository.resetTable(itemId)
            imagesUiState.images.forEachIndexed { index, _ ->
                exerciseImageRepository.insertExerciseImage(
                    imagesUiState.toImageDetailes(context = context, index = index, itemId = itemId).toExerciseImage()
                )
            }
        }
    }
}

suspend fun Flow<List<ExerciseImage>>.toImageUiState(context: Context): ImagesUiState {
    val imageUris = this.first().mapIndexed { index, exerciseImage ->
        val file = File(context.cacheDir, "image_$index.jpg")
        file.writeBytes(exerciseImage.imageData)
        file.toUri() // returns a proper file:// URI
    }.toMutableList()

    return ImagesUiState(images = imageUris)
}
