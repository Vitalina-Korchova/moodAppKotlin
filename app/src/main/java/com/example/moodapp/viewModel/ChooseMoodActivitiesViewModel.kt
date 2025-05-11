package com.example.moodapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.moodapp.R

class ChooseActivitiesMoodViewModel(
    private val repository: MoodRepository,
    private val moodStringResources: Map<String, Int>,
    private val activityStringResources: Map<String, Int>
) : ViewModel() {

    // State for the combined screen
    private val _state = MutableStateFlow(CombinedState())
    val state: StateFlow<CombinedState> = _state.asStateFlow()

    init {
        // Set current date
        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        _state.update { it.copy(currentDate = currentDate) }

        // Initialize activities
        val activities = listOf(
            "Work", "Study", "Exercise", "Reading",
            "Family", "Cooking", "Gaming", "Meditation", "Shopping",
            "Cleaning", "Sleeping", "Watching TV", "Traveling",
            "Music", "Art","Sports"
        )
        _state.update { it.copy(allActivities = activities) }

        // Initialize mood options
        val moodOptions = mapOf(
            "Happy" to R.drawable.icon_happy_mood,
            "Good" to R.drawable.icon_good_mood,
            "Neutral" to R.drawable.icon_neutral_mood,
            "Sad" to R.drawable.icon_sad_mood,
            "Bad" to R.drawable.icon_bad_mood
        )
        _state.update { it.copy(moodOptions = moodOptions) }
    }

    // Events that can be triggered in the UI
    sealed class CombinedEvent {
        data class MoodSelected(val mood: String) : CombinedEvent()
        data class ActivityToggled(val activity: String) : CombinedEvent()
        data class InitWithExistingData(val mood: String, val activities: List<String>) : CombinedEvent()
        object SaveButtonClicked : CombinedEvent()
        object NavigationHandled : CombinedEvent()
    }

    // State for the screen
    data class CombinedState(
        val currentDate: String = "",
        val selectedMood: String = "",
        val moodOptions: Map<String, Int> = emptyMap(),
        val allActivities: List<String> = emptyList(),
        val selectedActivities: List<String> = emptyList(),
        val isNavigateToHistory: Boolean = false,
        val isSaving: Boolean = false,
        val error: String? = null
    )


    // Handle events
    fun onEvent(event: CombinedEvent) {
        when (event) {
            is CombinedEvent.MoodSelected -> {
                _state.update { it.copy(selectedMood = event.mood) }
                Log.d("ChooseActivitiesMoodViewModel", "Selected mood: ${event.mood}")
            }

            is CombinedEvent.ActivityToggled -> {
                val currentSelectedActivities = _state.value.selectedActivities.toMutableList()
                if (currentSelectedActivities.contains(event.activity)) {
                    currentSelectedActivities.remove(event.activity)
                } else {
                    currentSelectedActivities.add(event.activity)
                }
                _state.update { it.copy(selectedActivities = currentSelectedActivities) }
            }

            is CombinedEvent.InitWithExistingData -> {
                _state.update {
                    it.copy(
                        selectedMood = event.mood,
                        selectedActivities = event.activities
                    )
                }
            }

            is CombinedEvent.SaveButtonClicked -> {
                saveMoodEntry()
            }

            is CombinedEvent.NavigationHandled -> {
                _state.update { it.copy(isNavigateToHistory = false) }
            }
        }
    }

    private fun saveMoodEntry() {
        viewModelScope.launch {
            try {
                val selectedMood = _state.value.selectedMood
                val selectedActivities = _state.value.selectedActivities

                // Update UI state to show saving in progress
                _state.update { it.copy(isSaving = true, error = null) }

                // Validate data
                if (selectedMood.isBlank()) {
                    _state.update { it.copy(isSaving = false, error = "No mood selected") }
                    Log.e("ChooseActivitiesMoodViewModel", "Cannot save: No mood selected")
                    return@launch
                }

                // Get the corresponding image resource ID from the mood options
                val moodImageResId = _state.value.moodOptions[selectedMood] ?: 0
                if (moodImageResId == 0) {
                    _state.update { it.copy(isSaving = false, error = "Invalid mood selection") }
                    Log.e("ChooseActivitiesMoodViewModel", "Invalid mood selection")
                    return@launch
                }

                // Save everything in one go with the new repository method
                repository.saveMoodEntry(selectedMood, moodImageResId, selectedActivities)
                Log.d("ChooseActivitiesMoodViewModel", "Mood entry saved with mood: $selectedMood and activities: $selectedActivities")

                // Navigate to history screen
                _state.update { it.copy(isSaving = false, isNavigateToHistory = true) }
                Log.d("ChooseActivitiesMoodViewModel", "Mood entry saved successfully")

            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = e.message) }
                Log.e("ChooseActivitiesMoodViewModel", "Error saving mood entry: ${e.message}")
            }
        }
    }

    fun getMoodResource(mood: String): Int = moodStringResources[mood] ?: 0
    fun getActivityResource(activity: String): Int = activityStringResources[activity] ?: 0
}