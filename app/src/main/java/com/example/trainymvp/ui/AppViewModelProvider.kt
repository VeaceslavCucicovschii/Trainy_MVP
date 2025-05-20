package com.example.trainymvp.ui

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trainymvp.TrainyApplication
import com.example.trainymvp.ui.home.HomeViewModel
import com.example.trainymvp.ui.item.WPEntryViewModel
import com.example.trainymvp.ui.item.WPEditViewModel
import com.example.trainymvp.ui.start.WPStartViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(trainyApplication().container.itemsRepository)
        }

        // Initializer for WPEntryViewModel
        initializer {
            WPEntryViewModel(trainyApplication().container.itemsRepository, trainyApplication().container.exerciseImageRepository)
        }

        // Initializer for WPEditViewModel
        initializer {
            val savedStateHandle = createSavedStateHandle() // <-- this is the correct one
            val application = this.trainyApplication()
            WPEditViewModel(
                savedStateHandle = savedStateHandle,
                itemsRepository = trainyApplication().container.itemsRepository,
                exerciseImageRepository = trainyApplication().container.exerciseImageRepository,
                application = application
            )
        }

        // Initializer for WPStartViewModel
        initializer {
            val savedStateHandle = createSavedStateHandle() // <-- this is the correct one
            val application = this.trainyApplication()
            WPStartViewModel(
                savedStateHandle = savedStateHandle,
                itemsRepository = trainyApplication().container.itemsRepository,
                exerciseImageRepository = trainyApplication().container.exerciseImageRepository,
                application = application
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [TrainyApplication].
 */
fun CreationExtras.trainyApplication(): TrainyApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrainyApplication)
