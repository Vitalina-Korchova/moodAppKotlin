package com.example.moodapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.repository.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryMoodViewModel : ViewModel() {

    //State
    data class HistoryState(
        val allEntries: List<MoodEntry> = emptyList(),
        val filteredEntries: List<MoodEntry> = emptyList(),
        val searchText: String = "",
        val selectedMood: String = "All",
        val moodOptions: List<String> = listOf("All", "Happy", "Good", "Neutral", "Sad", "Bad"),
        val isDropdownExpanded: Boolean = false,
        val areFiltersExpanded: Boolean = false,
        val isLoading: Boolean = true, // Чи завантажуються дані
        val isUpdating: Boolean = false, // Стан оновлення запису
        val selectedEntry: MoodEntry? = null, // Вибраний запис для редагування
        val errorMessage: String? = null // Повідомлення про помилку
    )

    //    визначає взаємодії юзера з UI
    sealed class HistoryEvent {
        data class SearchTextChanged(val text: String) : HistoryEvent()
        data class MoodFilterSelected(val mood: String) : HistoryEvent()
        object ToggleDropdown : HistoryEvent()
        object ToggleFiltersSection : HistoryEvent()
        object ClearFilters : HistoryEvent()
        data class SelectEntry(val entry: MoodEntry) : HistoryEvent() // вибір запису для редагування
        object CancelUpdate : HistoryEvent() // скасування редагування
        data class UpdateEntry(val updatedEntry: MoodEntry) : HistoryEvent() // оновлення запису
        data class DeleteEntry(val entryId: String) : HistoryEvent() // видалення запису
        object RefreshData : HistoryEvent() // оновлення даних з API
    }

    //внутрішній стан, який можна змінювати
    private val _state = MutableStateFlow(HistoryState())

    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadMoodEntries()
        observeRepositoryState()
    }

    private fun observeRepositoryState() {
        viewModelScope.launch {
            //спостереження за станом завантаження
            MoodRepository.isLoading.collectLatest { isLoading ->
                _state.value = _state.value.copy(isLoading = isLoading)
            }
        }

        viewModelScope.launch {
            // помилками
            MoodRepository.error.collectLatest { error ->
                _state.value = _state.value.copy(errorMessage = error)
            }
        }
        //список усіх записів  та оновлення allEntries
        viewModelScope.launch {

            MoodRepository.moods.collectLatest { entries ->
                _state.value = _state.value.copy(
                    allEntries = entries,
                    filteredEntries = applyFilters(entries, _state.value.searchText, _state.value.selectedMood)
                )
            }
        }
    }

    private fun loadMoodEntries() {
        viewModelScope.launch {
            try {
                //запит даних з API
                MoodRepository.fetchMoodsFromApi()
            } catch (e: Exception) {
                Log.e("HistoryMoodViewModel", "Error loading data", e)
                _state.value = _state.value.copy(
                    errorMessage = "Помилка завантаження даних: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    //  Actions
    private fun setSearchText(text: String) {
        _state.value = _state.value.copy(
            searchText = text,
            filteredEntries = applyFilters(_state.value.allEntries, text, _state.value.selectedMood)
        )
    }

    private fun setMoodFilter(mood: String) {
        _state.value = _state.value.copy(
            selectedMood = mood,
            isDropdownExpanded = false,
            filteredEntries = applyFilters(_state.value.allEntries, _state.value.searchText, mood)
        )
    }

    private fun toggleDropdown() {
        _state.value = _state.value.copy(
            isDropdownExpanded = !_state.value.isDropdownExpanded
        )
    }

    private fun toggleFiltersSection() {
        _state.value = _state.value.copy(
            areFiltersExpanded = !_state.value.areFiltersExpanded
        )
    }

    private fun clearFilters() {
        _state.value = _state.value.copy(
            searchText = "",
            selectedMood = "All",
            filteredEntries = _state.value.allEntries
        )
    }

    // вибір запису для редагування
    private fun selectEntry(entry: MoodEntry) {
        _state.value = _state.value.copy(
            selectedEntry = entry
        )
    }

    // скасування редагування
    private fun cancelUpdate() {
        _state.value = _state.value.copy(
            selectedEntry = null
        )
    }

    // оновлення запису настрою
    private fun updateEntry(updatedEntry: MoodEntry) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true)

            try {
                MoodRepository.updateMoodEntry(updatedEntry)

                _state.value = _state.value.copy(
                    selectedEntry = null,
                    isUpdating = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUpdating = false,
                    errorMessage = "Помилка оновлення запису: ${e.message}"
                )
            }
        }
    }

    // видалення запису настрою
    private fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            try {
                MoodRepository.deleteMoodEntry(entryId)
            } catch (e: Exception) {
                Log.e("HistoryMoodViewModel", "Error deleting mood entry", e)
                _state.value = _state.value.copy(
                    errorMessage = "Помилка видалення запису: ${e.message}"
                )
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {

                val currentEntries = _state.value.allEntries.filter { it.moodImageResId.startsWith("local:") }

                MoodRepository.fetchMoodsFromApi()

            } catch (e: Exception) {
                Log.e("HistoryMoodViewModel", "Error refreshing data", e)
                _state.value = _state.value.copy(
                    errorMessage = "Помилка оновлення даних: ${e.message}"
                )
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.SearchTextChanged -> setSearchText(event.text)
            is HistoryEvent.MoodFilterSelected -> setMoodFilter(event.mood)
            is HistoryEvent.ToggleDropdown -> toggleDropdown()
            is HistoryEvent.ToggleFiltersSection -> toggleFiltersSection()
            is HistoryEvent.ClearFilters -> clearFilters()
            is HistoryEvent.SelectEntry -> selectEntry(event.entry)
            is HistoryEvent.CancelUpdate -> cancelUpdate()
            is HistoryEvent.UpdateEntry -> updateEntry(event.updatedEntry)
            is HistoryEvent.DeleteEntry -> deleteEntry(event.entryId)
            is HistoryEvent.RefreshData -> refreshData()
        }
    }

    // Допоміжна функція для застосування фільтрів
    private fun applyFilters(entries: List<MoodEntry>, searchText: String, selectedMood: String): List<MoodEntry> {
        return entries.filter { entry ->
            val matchesActivity = searchText.isEmpty() ||
                    entry.activities.any { it.contains(searchText, ignoreCase = true) }
            val matchesMood = selectedMood.isEmpty() || selectedMood == "All" ||
                    entry.mood == selectedMood

            matchesActivity && matchesMood
        }
    }
}