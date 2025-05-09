package com.example.moodapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.repository.MoodRepository
import com.example.moodapp.utils.MoodDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryMoodViewModel(private val database: MoodDatabase) : ViewModel() {
    private val repository = MoodRepository(database)

    // State
    data class HistoryState(
        val allEntries: List<MoodEntry> = emptyList(),
        val filteredEntries: List<MoodEntry> = emptyList(),
        val searchText: String = "",
        val selectedMood: String = "All",
        val moodOptions: List<String> = listOf("All", "Happy", "Good", "Neutral", "Sad", "Bad"),
        val isDropdownExpanded: Boolean = false,
        val areFiltersExpanded: Boolean = false,
        val isLoading: Boolean = false,
        val isUpdating: Boolean = false,
        val selectedEntry: MoodEntry? = null,
        val errorMessage: String? = null
    )

    // Events
    sealed class HistoryEvent {
        data class SearchTextChanged(val text: String) : HistoryEvent()
        data class MoodFilterSelected(val mood: String) : HistoryEvent()
        object ToggleDropdown : HistoryEvent()
        object ToggleFiltersSection : HistoryEvent()
        object ClearFilters : HistoryEvent()
        data class SelectEntry(val entry: MoodEntry) : HistoryEvent()
        object CancelUpdate : HistoryEvent()
        data class UpdateEntry(val updatedEntry: MoodEntry) : HistoryEvent()
        data class DeleteEntry(val entryId: String) : HistoryEvent()
    }

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadMoodEntries()
        observeRepository()
    }

    private fun observeRepository() {
        viewModelScope.launch {
            repository.moods.collectLatest { entries ->
                _state.value = _state.value.copy(
                    allEntries = entries,
                    filteredEntries = applyFilters(entries, _state.value.searchText, _state.value.selectedMood)
                )
            }
        }

        viewModelScope.launch {
            repository.error.collectLatest { error ->
                _state.value = _state.value.copy(errorMessage = error)
            }
        }

        viewModelScope.launch {
            repository.isLoading.collectLatest { isLoading ->
                _state.value = _state.value.copy(isLoading = isLoading)
            }
        }
    }

    private fun loadMoodEntries() {
        // Дані автоматично оновлюються через Flow з бази даних
    }

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

    private fun selectEntry(entry: MoodEntry) {
        _state.value = _state.value.copy(
            selectedEntry = entry
        )
    }

    private fun cancelUpdate() {
        _state.value = _state.value.copy(
            selectedEntry = null
        )
    }

    private fun updateEntry(updatedEntry: MoodEntry) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true)
            repository.updateMoodEntry(updatedEntry)
            _state.value = _state.value.copy(
                selectedEntry = null,
                isUpdating = false
            )
        }
    }

    private fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            val entry = _state.value.allEntries.find { it.id == entryId }
            entry?.let {
                repository.deleteMoodEntry(it)
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
        }
    }

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