package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodapp.repository.MoodRepository
import com.example.moodapp.utils.MoodDatabase

class ChooseActivitiesMoodViewModelFactory(
    private val database: MoodDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChooseActivitiesMoodViewModel::class.java)) {
            // Create the repository first
            val repository = MoodRepository(database)

            // Then create the ViewModel with the repository
            return ChooseActivitiesMoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}