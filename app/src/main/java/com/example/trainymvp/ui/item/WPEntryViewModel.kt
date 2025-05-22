package com.example.trainymvp.ui.item

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.trainymvp.data.ExerciseImage
import com.example.trainymvp.data.ExerciseImageRepository
import com.example.trainymvp.data.Item
import com.example.trainymvp.data.ItemsRepository
import kotlinx.coroutines.flow.first

class WPEntryViewModel(private val itemsRepository: ItemsRepository, private val exerciseImageRepository: ExerciseImageRepository) : ViewModel() {
    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    var imagesUiState by mutableStateOf(ImagesUiState())
        private set

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
            itemsRepository.insertItem(
                itemUiState.itemDetails.toItem()
            )
        }

        val currentId = itemsRepository
            .getItemStreamByTitle(itemUiState.itemDetails.title)
            .first()!!
            .toItemUiState()
            .itemDetails
            .id

        if (imagesUiState.images.isNotEmpty()) {
            imagesUiState.images.forEachIndexed { index, _ ->
                Log.d("Image", "index = ${index}, currentId = ${currentId}")
                exerciseImageRepository.insertExerciseImage(
                    imagesUiState.toImageDetailes(context = context, index = index, itemId = currentId).toExerciseImage()
                )
            }
        }
    }
}

/**
 * Item UI State
 */

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
)

/**
 * Extension function to convert [ItemUiState] to [Item]
 */
fun ItemDetails.toItem(): Item = Item(
    itemId = id,
    title = title,
    description = description
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = itemId,
    title = title,
    description = description
)

/**
 * Image UI State
 */

data class ImagesUiState(
    val images: MutableList<@JvmSuppressWildcards Uri> = mutableListOf()
)

data class ImageDetailes(
    val id: Int = 0,
    val imageData: ByteArray = byteArrayOf(),
    val order: Int = 0,
    val itemId: Int = 0
)

/**
 * Extension function to convert [ImageDetailes] to [ExerciseImage]
 */

fun ImageDetailes.toExerciseImage(): ExerciseImage = ExerciseImage(
    exerciseImageId = id,
    imageData = imageData,
    order = order,
    itemId = itemId
)

/**
 * Extension function to convert image from [ImagesUiState] with index [index] to [ImageDetailes]
 */

fun ImagesUiState.toImageDetailes(index: Int, itemId: Int, context: Context): ImageDetailes {
    return ImageDetailes(
        imageData = convertUriToByte(context = context, images[index]),
        order = index,
        itemId = itemId
    )
}

fun convertUriToByte(context: Context, uri: @JvmSuppressWildcards Uri): ByteArray {
    val imageBytes = context.contentResolver.openInputStream(uri)?.use {
        it.readBytes()
    }
    Log.d("Image", "imageBytes = $imageBytes")
    return imageBytes!!
}