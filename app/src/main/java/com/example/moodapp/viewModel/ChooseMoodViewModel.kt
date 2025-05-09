package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.R
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.repository.MoodRepository
import com.example.moodapp.utils.MoodDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChooseMoodViewModel(private val database: MoodDatabase) : ViewModel() {
    private val repository = MoodRepository(database)

    data class MoodState(
        val currentDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
        val selectedMood: String? = null,
        val moodOptions: Map<String, Int> = mapOf(
            "Happy" to R.drawable.icon_happy_mood,
            "Good" to R.drawable.icon_good_mood,
            "Neutral" to R.drawable.icon_neutral_mood,
            "Sad" to R.drawable.icon_sad_mood,
            "Bad" to R.drawable.icon_bad_mood
        ),
        val isNavigateToActivities: Boolean = false,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class MoodEvent {
        data class MoodSelected(val mood: String) : MoodEvent()
        object SaveButtonClicked : MoodEvent()
        object NavigationHandled : MoodEvent()
    }

    private val _state = MutableStateFlow(MoodState())
    val state: StateFlow<MoodState> = _state.asStateFlow()

    fun onEvent(event: MoodEvent) {
        when (event) {
            is MoodEvent.MoodSelected -> {
                val moodImageResId = state.value.moodOptions[event.mood] ?: R.drawable.icon_neutral_mood
                repository.saveMood(event.mood, moodImageResId)
                _state.value = _state.value.copy(
                    selectedMood = event.mood,
                    errorMessage = null
                )
            }

            is MoodEvent.SaveButtonClicked -> {
                val currentMood = _state.value.selectedMood
                if (currentMood != null) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true)
                        try {
                            repository.finalizeMoodEntry()
                            _state.value = _state.value.copy(
                                isNavigateToActivities = true,
                                isLoading = false,
                                errorMessage = null
                            )
                        } catch (e: Exception) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = "Failed to save mood: ${e.message}"
                            )
                        }
                    }
                } else {
                    _state.value = _state.value.copy(
                        errorMessage = "Please select a mood first"
                    )
                }
            }

            is MoodEvent.NavigationHandled -> {
                _state.value = _state.value.copy(
                    isNavigateToActivities = false,
                    selectedMood = null
                )
            }
        }
    }
}