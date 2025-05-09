package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodapp.utils.MoodDatabase

class ChooseMoodViewModelFactory(private val database: MoodDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChooseMoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChooseMoodViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}