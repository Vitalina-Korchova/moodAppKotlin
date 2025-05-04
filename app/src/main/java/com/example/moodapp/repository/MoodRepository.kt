package com.example.moodapp.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.moodapp.R
import com.example.moodapp.RetrofitInstance
import com.example.moodapp.model.MoodEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

object MoodRepository {
    private val _moods = MutableStateFlow<List<MoodEntry>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // список MoodEntry, загорнутий у StateFlow, щоб дозволяти оновлення в реальному часі
    val moods: StateFlow<List<MoodEntry>> = _moods
    val isLoading: StateFlow<Boolean> = _isLoading
    val error: StateFlow<String?> = _error

    private var selectedMood: String? = null
    private var selectedMoodImageResId: Int? = null
    private var selectedActivities: List<String> = emptyList()

    // Функція для завантаження даних з API
    suspend fun fetchMoodsFromApi() {
        _isLoading.value = true
        _error.value = null

        try {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "Fetching data from API")
                val apiMoods = RetrofitInstance.api.getData()

                // Convert API response to your model
                val moodEntries = apiMoods.map { apiMood ->
                    MoodEntry(
                        id = apiMood.id,
                        date = apiMood.date,
                        mood = apiMood.mood,
                        moodImageResId = apiMood.moodImageResId, // This should be the URL from API
                        activities = apiMood.activities
                    )
                }

                // Combine API entries with any local entries
                // This ensures we don't lose local entries when refreshing from API
                val localEntries = _moods.value.filter {
                    it.moodImageResId.startsWith("local:")
                }

                // Combine entries but avoid duplicates
                val combinedEntries = (moodEntries + localEntries).distinctBy { it.id }

                _moods.value = combinedEntries
                Log.d(TAG, "Data successfully loaded: ${combinedEntries.size} entries")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data: ${e.message}", e)
            _error.value = "Error loading data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }


    // збереження настрою
    fun saveMood(mood: String, moodImageResId: Int) {
        selectedMood = mood
        selectedMoodImageResId = moodImageResId
    }

    // збереження активностей
    fun saveActivities(activities: List<String>) {
        selectedActivities = activities
    }

    // створення MoodEntry та його збереження
    suspend fun finalizeMoodEntry() {
        val mood = selectedMood
        val moodImageResId = selectedMoodImageResId

        if (mood != null && moodImageResId != null) {
            withContext(Dispatchers.IO) {
                val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                val newMoodEntry = MoodEntry(
                    id = UUID.randomUUID().toString(),
                    date = currentDate,
                    mood = mood,
                    moodImageResId = "local:$moodImageResId", // Mark as local resource
                    activities = selectedActivities
                )

                // Update the StateFlow with the new entry
                _moods.value = _moods.value + newMoodEntry

                Log.d(TAG, "New mood entry created and added to state: ${newMoodEntry.id}")

                // Optional: Try to persist this entry to your backend
                try {
                    // You would implement this API call if needed
                    // RetrofitInstance.api.saveMoodEntry(newMoodEntry)
                } catch (e: Exception) {
                    Log.e(TAG, "Error saving mood entry to API: ${e.message}", e)
                    // Still keep the entry locally even if API save fails
                }

                // Clear temporary variables
                selectedMood = null
                selectedMoodImageResId = null
                selectedActivities = emptyList()
            }
        } else {
            Log.e(TAG, "Cannot finalize mood entry: mood or moodImageResId is null")
            _error.value = "Cannot create mood entry: mood or moodImageResId is null"
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