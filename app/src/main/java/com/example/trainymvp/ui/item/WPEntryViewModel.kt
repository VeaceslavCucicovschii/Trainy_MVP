package com.example.trainymvp.ui.item

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.example.trainymvp.data.ExerciseImage
import com.example.trainymvp.data.Item
import com.example.trainymvp.data.ItemsRepository

class WPEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
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
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    fun updateImagesUiState(images: List<@JvmSuppressWildcards Uri>) {
        imagesUiState =
            ImagesUiState(images = images)
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank()
        }
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    fun convertUrisToBytes(context: Context, uris: List<@JvmSuppressWildcards Uri>): List<ByteArray?> {
        var imageBytesList: List<ByteArray?> = mutableListOf()

        uris.forEach() { element ->
            val imageBytes = context.contentResolver.openInputStream(element)?.use {
                it.readBytes()
            }

            imageBytesList += imageBytes
        }

        return imageBytesList
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
    val images: List<@JvmSuppressWildcards Uri> = mutableListOf(),
    val isEntryValid: Boolean = false
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