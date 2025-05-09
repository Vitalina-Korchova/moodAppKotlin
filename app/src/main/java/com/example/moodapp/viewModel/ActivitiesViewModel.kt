package com.example.moodapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivitiesMoodViewModel(
    private val repository: MoodRepository
) : ViewModel() {

    data class ActivitiesState(
        val allActivities: List<String> = listOf(
            "Reading", "Movie", "Sport", "Family",
            "Friends", "Studying", "Date", "Sleeping",
            "Shopping", "Relax", "Games", "Cleaning",
            "Work", "Cooking", "Walking"
        ),
        val selectedActivities: List<String> = emptyList(),
        val maxSelectableActivities: Int = 4,
        val isNavigateToHistory: Boolean = false,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class ActivitiesEvent {
        data class ActivityToggled(val activity: String) : ActivitiesEvent()
        object SaveButtonClicked : ActivitiesEvent()
        object NavigationHandled : ActivitiesEvent()
        data class InitWithActivities(val activities: List<String>) : ActivitiesEvent()
    }

    private val _state = MutableStateFlow(ActivitiesState())
    val state: StateFlow<ActivitiesState> = _state.asStateFlow()

    init {
        // Check if we have any activities from current mood entry
        repository.getCurrentMoodEntry()?.let { entry ->
            if (entry.activities.isNotEmpty()) {
                _state.value = _state.value.copy(selectedActivities = entry.activities)
            }
        }
    }

    fun onEvent(event: ActivitiesEvent) {
        when (event) {
            is ActivitiesEvent.ActivityToggled -> {
                val currentSelected = _state.value.selectedActivities
                val newSelected = if (currentSelected.contains(event.activity)) {
                    currentSelected - event.activity
                } else if (currentSelected.size < _state.value.maxSelectableActivities) {
                    currentSelected + event.activity
                } else {
                    currentSelected
                }
                _state.value = _state.value.copy(selectedActivities = newSelected)
                Log.d("ActivitiesMoodViewModel", "Activities after toggle: ${_state.value.selectedActivities}")
            }

            is ActivitiesEvent.SaveButtonClicked -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    try {
                        Log.d("ActivitiesMoodViewModel", "Saving activities: ${_state.value.selectedActivities}")

                        // Save the selected activities to the repository
                        repository.saveActivities(_state.value.selectedActivities)

                        // Finalize the mood entry
                        repository.finalizeMoodEntry()

                        _state.value = _state.value.copy(
                            isNavigateToHistory = true,
                            isLoading = false,
                            errorMessage = null
                        )

                        // Clear the current mood entry after successful save
                        repository.clearCurrentMoodEntry()
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = "Помилка збереження: ${e.message}"
                        )
                    }
                }
            }

            is ActivitiesEvent.NavigationHandled -> {
                _state.value = _state.value.copy(
                    isNavigateToHistory = false
                )
            }

            is ActivitiesEvent.InitWithActivities -> {
                _state.value = _state.value.copy(selectedActivities = event.activities)
                Log.d("ActivitiesMoodViewModel", "Initialized with activities: ${event.activities}")
            }
        }
    }
}