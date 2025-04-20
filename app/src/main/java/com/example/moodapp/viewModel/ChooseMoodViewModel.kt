package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.moodapp.R
import com.example.moodapp.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class ChooseMoodViewModel : ViewModel() {

    // визначає, які дані зберігає ViewModel для керування станом екрана настрою
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
        val isNavigateToActivities: Boolean = false
    )

    sealed class MoodEvent {
        data class MoodSelected(val mood: String) : MoodEvent() //вибір настрою
        object SaveButtonClicked : MoodEvent()
        object NavigationHandled : MoodEvent()
    }

    private val _state = MutableStateFlow(MoodState())

    val state: StateFlow<MoodState> = _state.asStateFlow() //надає можливітсь зчитувати

    fun onEvent(event: MoodEvent) {
        when (event) {
            is MoodEvent.MoodSelected -> {

                _state.value = _state.value.copy(selectedMood = event.mood)

                val moodImageResId = _state.value.moodOptions[event.mood] ?: R.drawable.icon_neutral_mood
                MoodRepository.saveMood(event.mood, moodImageResId)
            }

            is MoodEvent.SaveButtonClicked -> {

                val currentMood = _state.value.selectedMood
                if (currentMood != null) {

                    _state.value = _state.value.copy(isNavigateToActivities = true)
                }
            }

            is MoodEvent.NavigationHandled -> {

                _state.value = _state.value.copy(isNavigateToActivities = false)
            }
        }
    }
}