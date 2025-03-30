package com.example.moodapp.viewModel

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

    // 1. State - представляє стан UI
    data class HistoryState(
        val allEntries: List<MoodEntry> = emptyList(),
        val filteredEntries: List<MoodEntry> = emptyList(),
        val searchText: String = "",
        val selectedMood: String = "All",
        val moodOptions: List<String> = listOf("All", "Happy", "Good", "Neutral", "Sad", "Bad"),
        val isDropdownExpanded: Boolean = false,
        val areFiltersExpanded: Boolean = false,
        val isLoading: Boolean = true
    )

    // 2. Events - взаємодії користувача
    sealed class HistoryEvent {
        data class SearchTextChanged(val text: String) : HistoryEvent()
        data class MoodFilterSelected(val mood: String) : HistoryEvent()
        object ToggleDropdown : HistoryEvent()
        object ToggleFiltersSection : HistoryEvent()
        object ClearFilters : HistoryEvent()
    }

    // Внутрішній змінний стан
    private val _state = MutableStateFlow(HistoryState())

    // Відкритий незмінний стан
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        // Завантаження записів про настрій з репозиторію під час створення ViewModel
        loadMoodEntries()
    }

    private fun loadMoodEntries() {
        viewModelScope.launch {
            MoodRepository.moods.collectLatest { entries ->
                _state.value = _state.value.copy(
                    allEntries = entries,
                    filteredEntries = applyFilters(entries, _state.value.searchText, _state.value.selectedMood),
                    isLoading = false
                )
            }
        }
    }

    // 3. Actions - функції, що виконують зміни стану
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

    // Обробник подій
    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.SearchTextChanged -> setSearchText(event.text)
            is HistoryEvent.MoodFilterSelected -> setMoodFilter(event.mood)
            is HistoryEvent.ToggleDropdown -> toggleDropdown()
            is HistoryEvent.ToggleFiltersSection -> toggleFiltersSection()
            is HistoryEvent.ClearFilters -> clearFilters()
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
