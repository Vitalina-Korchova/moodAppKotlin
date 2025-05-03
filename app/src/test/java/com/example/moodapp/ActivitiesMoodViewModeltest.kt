package com.example.moodapp

import com.example.moodapp.repository.MoodRepository
import com.example.moodapp.viewModel.ActivitiesMoodViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.flow.first
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class ActivitiesMoodViewModelTest {

    private lateinit var viewModel: ActivitiesMoodViewModel

    @Before
    fun setup() {
        viewModel = ActivitiesMoodViewModel()
    }

    @Test
    fun activityToggledShouldAddActivityWhenNotAlreadySelected() = runTest {
        val activity = "Reading"

        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(activity))

        val state = viewModel.state.value
        assertTrue(state.selectedActivities.contains(activity))
    }

    @Test
    fun activityToggledShouldRemoveActivityWhenAlreadySelected() = runTest {
        val activity = "Reading"


        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(activity))
        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(activity))

        val state = viewModel.state.value
        assertFalse(state.selectedActivities.contains(activity))
    }

    @Test
    fun activityToggledShouldNotAddMoreThanMaxSelectableActivities() = runTest {
        val activities = listOf("Reading", "Movie", "Sport", "Family", "Friends")


        activities.forEach {
            viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(it))
        }

        val state = viewModel.state.value
        assertEquals(viewModel.state.value.maxSelectableActivities, state.selectedActivities.size)
    }

    @Test
    fun saveButtonClickedShouldSaveActivitiesAndTriggerNavigation() = runTest {
        val activities = listOf("Reading", "Movie")

        activities.forEach {
            viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(it))
        }
        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.SaveButtonClicked)

        val state = viewModel.state.value
        assertTrue(state.isNavigateToHistory)
    }

    @Test
    fun navigationHandledShouldResetNavigationFlag() = runTest {

        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.SaveButtonClicked)
        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.NavigationHandled)

        val state = viewModel.state.value
        assertFalse(state.isNavigateToHistory)
    }
}
