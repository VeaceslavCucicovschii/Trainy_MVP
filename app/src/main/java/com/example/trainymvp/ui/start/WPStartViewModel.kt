package com.example.trainymvp.ui.start

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trainymvp.data.ExerciseImageRepository
import com.example.trainymvp.data.ItemsRepository
import com.example.trainymvp.ui.item.ImagesUiState
import com.example.trainymvp.ui.item.ItemUiState
import com.example.trainymvp.ui.item.WPEditDestination
import com.example.trainymvp.ui.item.toImageUiState
import com.example.trainymvp.ui.item.toItemUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WPStartViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val exerciseImageRepository: ExerciseImageRepository
) : AndroidViewModel(application) {
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

    var timerUiState by mutableStateOf(TimerUiState())
        private set

    fun resetExercise() {
        timerUiState = timerUiState.copy(timeLeft = 45, timeRest = 25, isPaused = false)
    }

    fun resetWP() {
        timerUiState = TimerUiState()
    }
}

data class TimerUiState(
    var imageIndex: Int = 0,
    var timeLeft: Int = 45,
    var timeRest: Int = 25,
    var isPaused: Boolean = false
)