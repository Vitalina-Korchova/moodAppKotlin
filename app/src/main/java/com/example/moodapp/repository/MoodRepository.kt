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

    // Отримання всіх записів з бази даних
    val moods: Flow<List<MoodEntry>> = database.moodEntryDao().getAll()

    // Створення і збереження запису настрою повністю
    suspend fun saveMoodEntry(mood: String, moodImageResId: Int, activities: List<String>) {
        _isLoading.value = true
        _error.value = null

        try {
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val newEntry = MoodEntry(
                id = UUID.randomUUID().toString(),
                date = currentDate,
                mood = mood,
                moodImageResId = "local:$moodImageResId",
                activities = activities
            )

            withContext(Dispatchers.IO) {
                database.moodEntryDao().insert(newEntry)
                Log.d("MoodRepository", "Entry saved to DB: $newEntry")
            }
        } catch (e: Exception) {
            Log.e("MoodRepository", "Error saving mood entry", e)
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    // Отримання запису настрою за ID
    suspend fun getMoodEntryById(id: String): MoodEntry? {
        return withContext(Dispatchers.IO) {
            database.moodEntryDao().getById(id)
        }
    }

    // Видалення запису настрою
    suspend fun deleteMoodEntry(moodEntry: MoodEntry) {
        _isLoading.value = true
        try {
            withContext(Dispatchers.IO) {
                database.moodEntryDao().delete(moodEntry)
                Log.d(TAG, "Mood entry deleted from database: ${moodEntry.id}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting mood entry", e)
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    // Оновлення запису настрою
    suspend fun updateMoodEntry(updatedEntry: MoodEntry) {
        _isLoading.value = true
        try {
            withContext(Dispatchers.IO) {
                database.moodEntryDao().update(updatedEntry)
                Log.d(TAG, "Mood entry updated in database: ${updatedEntry.id}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating mood entry", e)
            _error.value = "Error updating mood entry: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
}