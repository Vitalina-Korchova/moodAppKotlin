package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.moodapp.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActivitiesMoodViewModel : ViewModel() {

    data class ActivitiesState(
        val allActivities: List<String> = listOf(
            "Reading", "Movie", "Sport", "Family",
            "Friends", "Studying", "Date", "Sleeping",
            "Shopping", "Relax", "Games", "Cleaning",
            "Work", "Cooking", "Walking"
        ),
        val selectedActivities: List<String> = emptyList(),
        val maxSelectableActivities: Int = 4,
        val isNavigateToHistory: Boolean = false
    )

    sealed class ActivitiesEvent {
        data class ActivityToggled(val activity: String) : ActivitiesEvent()
        object SaveButtonClicked : ActivitiesEvent()
        object NavigationHandled : ActivitiesEvent()
    }

    private val _state = MutableStateFlow(ActivitiesState())

    val state: StateFlow<ActivitiesState> = _state.asStateFlow()

    // Event
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
            }

            is ActivitiesEvent.SaveButtonClicked -> {

                MoodRepository.saveActivities(_state.value.selectedActivities)


                MoodRepository.finalizeMoodEntry()


                _state.value = _state.value.copy(isNavigateToHistory = true)
            }

            is ActivitiesEvent.NavigationHandled -> {

                _state.value = _state.value.copy(isNavigateToHistory = false)
            }
        }
    }
}