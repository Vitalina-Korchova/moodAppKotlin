package com.example.moodapp.repository

import com.example.moodapp.R
import com.example.moodapp.model.MoodEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

object MoodRepository {
    private val _moods = MutableStateFlow(
        listOf(
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "02.03.2025",
                mood = "Happy",
                moodImageResId = R.drawable.icon_happy_mood,
                activities = listOf("Reading", "Friends", "Walking", "Cooking")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "03.03.2025",
                mood = "Sad",
                moodImageResId = R.drawable.icon_sad_mood,
                activities = listOf("Sleeping", "Movie")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "07.03.2025",
                mood = "Happy",
                moodImageResId = R.drawable.icon_happy_mood,
                activities = listOf("Sleeping")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "08.03.2025",
                mood = "Neutral",
                moodImageResId = R.drawable.icon_neutral_mood,
                activities = listOf("Cleaning", "Work", "Studying")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "09.03.2025",
                mood = "Bad",
                moodImageResId = R.drawable.icon_bad_mood,
                activities = listOf("Studying")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "10.03.2025",
                mood = "Good",
                moodImageResId = R.drawable.icon_good_mood,
                activities = listOf("Movie", "Sport")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "12.03.2025",
                mood = "Sad",
                moodImageResId = R.drawable.icon_sad_mood,
                activities = listOf("Work", "Cleaning")
            ),
            MoodEntry(
                id = UUID.randomUUID().toString(),
                date = "15.03.2025",
                mood = "Good",
                moodImageResId = R.drawable.icon_good_mood,
                activities = listOf("Sport", "Movie")
            )
        )
    )

    //список MoodEntry, загорнутий у MutableStateFlow, щоб дозволяти оновлення в реальному часі.
    val moods: StateFlow<List<MoodEntry>> = _moods

    private var selectedMood: String? = null
    private var selectedMoodImageResId: Int? = null
    private var selectedActivities: List<String> = emptyList()

    // збереження настрою
    fun saveMood(mood: String, moodImageResId: Int) {
        selectedMood = mood
        selectedMoodImageResId = moodImageResId
    }

    //  збереження активностей
    fun saveActivities(activities: List<String>) {
        selectedActivities = activities
    }

    // створення MoodEntry та його збереження
    fun finalizeMoodEntry() {
        val mood = selectedMood
        val moodImageResId = selectedMoodImageResId

        if (mood != null && moodImageResId != null) {
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val newMoodEntry = MoodEntry(
                id = UUID.randomUUID().toString(),
                date = currentDate,
                mood = mood,
                moodImageResId = moodImageResId,
                activities = selectedActivities
            )
            _moods.value = _moods.value + newMoodEntry

            // очищення тимчасових змінних
            selectedMood = null
            selectedMoodImageResId = null
            selectedActivities = emptyList()
        }
    }

    fun updateMoodEntry(updatedEntry: MoodEntry) {
        val currentEntries = _moods.value.toMutableList()
        val index = currentEntries.indexOfFirst { it.id == updatedEntry.id }

        if (index != -1) {
            currentEntries[index] = updatedEntry
            _moods.value = currentEntries
        }
    }


    fun deleteMoodEntry(entryId: String) {
        val updatedEntries = _moods.value.filter { it.id != entryId }
        _moods.value = updatedEntries
    }

   
    fun getMoodEntryById(entryId: String): MoodEntry? {
        return _moods.value.find { it.id == entryId }
    }
}
