package com.example.moodapp.viewmodel

import com.example.moodapp.model.MoodEntry
import com.example.moodapp.viewModel.HistoryMoodViewModel
import com.example.moodapp.viewModel.HistoryMoodViewModel.HistoryEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HistoryMoodViewModelTest {

    private lateinit var viewModel: HistoryMoodViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {

        Dispatchers.setMain(testDispatcher)
        viewModel = HistoryMoodViewModel()
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }

    @Test
    fun searchTextChangedFiltersEntriesCorrectly() = runTest {
        val moodEntries = listOf(
            MoodEntry(
                id = "1",
                date = "2024-04-26",
                mood = "Happy",
                moodImageResId = 1,
                activities = listOf("Running", "Swimming")
            ),
            MoodEntry(
                id = "2",
                date = "2024-04-25",
                mood = "Sad",
                moodImageResId = 2,
                activities = listOf("Reading", "Sleeping")
            )
        )
        viewModel.setTestData(moodEntries)

        viewModel.onEvent(HistoryEvent.SearchTextChanged("Run"))

        val filtered = viewModel.state.value.filteredEntries
        assertEquals(1, filtered.size)
        assertEquals("Running", filtered.first().activities.first())
    }

    @Test
    fun moodFilterSelectedFiltersEntriesCorrectly() = runTest {
        val moodEntries = listOf(
            MoodEntry(
                id = "1",
                date = "2024-04-26",
                mood = "Happy",
                moodImageResId = 1,
                activities = listOf("Running")
            ),
            MoodEntry(
                id = "2",
                date = "2024-04-25",
                mood = "Sad",
                moodImageResId = 2,
                activities = listOf("Sleeping")
            )
        )
        viewModel.setTestData(moodEntries)

        viewModel.onEvent(HistoryEvent.MoodFilterSelected("Happy"))

        val filtered = viewModel.state.value.filteredEntries
        assertEquals(1, filtered.size)
        assertEquals("Happy", filtered.first().mood)
    }

    @Test
    fun toggleDropdownChangesState() = runTest {
        val initial = viewModel.state.value.isDropdownExpanded

        viewModel.onEvent(HistoryEvent.ToggleDropdown)

        val toggled = viewModel.state.value.isDropdownExpanded
        assertEquals(!initial, toggled)
    }

    @Test
    fun toggleFiltersSectionChangesState() = runTest {
        val initial = viewModel.state.value.areFiltersExpanded

        viewModel.onEvent(HistoryEvent.ToggleFiltersSection)

        val toggled = viewModel.state.value.areFiltersExpanded
        assertEquals(!initial, toggled)
    }

    @Test
    fun clearFiltersResetsSearchTextAndMood() = runTest {
        val moodEntries = listOf(
            MoodEntry(
                id = "1",
                date = "2024-04-26",
                mood = "Happy",
                moodImageResId = 1,
                activities = listOf("Running")
            )
        )
        viewModel.setTestData(moodEntries)

        viewModel.onEvent(HistoryEvent.SearchTextChanged("Run"))
        viewModel.onEvent(HistoryEvent.MoodFilterSelected("Happy"))
        viewModel.onEvent(HistoryEvent.ClearFilters)

        val state = viewModel.state.value
        assertEquals("", state.searchText)
        assertEquals("All", state.selectedMood)
        assertEquals(moodEntries, state.filteredEntries)
    }

    @Test
    fun selectEntryUpdatesSelectedEntry() = runTest {
        val entry = MoodEntry(
            id = "1",
            date = "2024-04-26",
            mood = "Happy",
            moodImageResId = 1,
            activities = listOf("Running")
        )
        viewModel.onEvent(HistoryEvent.SelectEntry(entry))

        val selected = viewModel.state.value.selectedEntry
        assertEquals(entry, selected)
    }

    @Test
    fun cancelUpdateClearsSelectedEntry() = runTest {
        val entry = MoodEntry(
            id = "1",
            date = "2024-04-26",
            mood = "Happy",
            moodImageResId = 1,
            activities = listOf("Running")
        )
        viewModel.onEvent(HistoryEvent.SelectEntry(entry))
        viewModel.onEvent(HistoryEvent.CancelUpdate)

        val selected = viewModel.state.value.selectedEntry
        assertEquals(null, selected)
    }
}