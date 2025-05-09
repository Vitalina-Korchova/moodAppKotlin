package com.example.moodapp.repository


import android.content.ContentValues.TAG
import android.util.Log
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.utils.MoodDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class MoodRepository(private val database: MoodDatabase) {
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val isLoading: StateFlow<Boolean> = _isLoading
    val error: StateFlow<String?> = _error

    // Поточний запис настрою, який формується
    private var currentMoodEntry: MoodEntry? = null

    // Отримання всіх записів з бази даних
    val moods: Flow<List<MoodEntry>> = database.moodEntryDao().getAll()

    // Збереження базових даних настрою
    fun saveMood(mood: String, moodImageResId: Int) {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        currentMoodEntry = MoodEntry(
            id = UUID.randomUUID().toString(),
            date = currentDate,
            mood = mood,
            moodImageResId = "local:$moodImageResId",
            activities = emptyList() // Спочатку без активностей
        )
        Log.d("MoodRepository", "Mood saved: $mood")
    }

    // Збереження вибраних активностей
    fun saveActivities(activities: List<String>) {
        Log.d("MoodRepository", "Before update activities: Current entry = $currentMoodEntry")

        if (currentMoodEntry == null) {
            Log.e("MoodRepository", "ERROR: Cannot save activities - no current mood entry!")
            return
        }

        // Make a new copy with updated activities
        currentMoodEntry = currentMoodEntry?.copy(activities = activities)

        Log.d("MoodRepository", "Activities saved to current entry: $activities")
        Log.d("MoodRepository", "After update activities: Current entry = $currentMoodEntry")
    }

    // Фінальне збереження запису в базу даних
    suspend fun finalizeMoodEntry() {
        _isLoading.value = true
        _error.value = null

        try {
            currentMoodEntry?.let { entry ->
                withContext(Dispatchers.IO) {
                    database.moodEntryDao().insert(entry)
                    Log.d("MoodRepository", "Entry saved to DB: $entry")
                }
            } ?: run {
                throw IllegalStateException("No mood data to save")
            }
        } catch (e: Exception) {
            Log.e("MoodRepository", "Error saving mood entry", e)
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
            // Don't set currentMoodEntry to null here, in case we need to access it later
        }
    }

    // Getter for the current mood entry
    fun getCurrentMoodEntry(): MoodEntry? {
        return currentMoodEntry
    }

    // Setter for the current mood entry (useful for edit mode)
    fun setCurrentMoodEntry(entry: MoodEntry) {
        currentMoodEntry = entry
    }

    // Clear the current mood entry
    fun clearCurrentMoodEntry() {
        currentMoodEntry = null
    }

    // Інші методи репозиторію...
    suspend fun getMoodEntryById(id: String): MoodEntry? {
        return withContext(Dispatchers.IO) {
            database.moodEntryDao().getById(id)
        }
    }

    suspend fun deleteMoodEntry(moodEntry: MoodEntry) {
        withContext(Dispatchers.IO) {
            database.moodEntryDao().delete(moodEntry)
        }
    }

    suspend fun updateMoodEntry(updatedEntry: MoodEntry) {
        withContext(Dispatchers.IO) {
            try {
                database.moodEntryDao().update(updatedEntry) // Using the new update method
                Log.d(TAG, "Mood entry updated in database: ${updatedEntry.id}")
            } catch (e: Exception) {
                Log.e(TAG, "Error updating mood entry", e)
                _error.value = "Error updating mood entry: ${e.message}"
            }
        }
    }
}
