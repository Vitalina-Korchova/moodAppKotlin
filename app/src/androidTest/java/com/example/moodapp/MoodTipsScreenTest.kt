package com.example.moodapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.compose.MoodAppTheme
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.viewModel.ActivitiesMoodViewModel
import com.example.moodapp.viewModel.HistoryMoodViewModel
import com.example.moodapp.views.ActivitiesMood
import com.example.moodapp.views.HistoryMood
import com.example.moodapp.views.MoodTips
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoodTipsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val moodTips = listOf(
        "Try meditation",
        "Take a walk outside",
        "Talk to a friend",
        "Listen to your favorite music",
        "Write down your thoughts"
    )

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setupComposeContent(isWideScreen: Boolean = false) {
        composeTestRule.setContent {
            val windowSize = if (isWideScreen) DpSize(840.dp, 1200.dp) else DpSize(360.dp, 640.dp)
            val windowSizeClass = WindowSizeClass.calculateFromSize(windowSize)

            MaterialTheme {
                MoodTips(windowSizeClass)
            }
        }
    }

    @Test
    fun compactScreen_shouldShowListInitially() {
        setupComposeContent(isWideScreen = false)

        // Verify title
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()

        // Verify all tips are shown
        moodTips.forEach { tip ->
            composeTestRule.onNodeWithText(tip).assertIsDisplayed()
        }
    }

    @Test
    fun compactScreen_shouldShowDetailWhenTipSelected() {
        setupComposeContent(isWideScreen = false)

        // Select first tip
        composeTestRule.onNodeWithText(moodTips[0]).performClick()

        // Verify back button appears
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

        // Verify detail content appears
        composeTestRule.onNodeWithText("Details:").assertIsDisplayed()
    }

    @Test
    fun compactScreen_shouldReturnToListWhenBackClicked() {
        setupComposeContent(isWideScreen = false)

        // Select and then go back
        composeTestRule.onNodeWithText(moodTips[0]).performClick()
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify list is shown again
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()
    }

    @Test
    fun wideScreen_shouldShowBothListAndDetailInitially() {
        setupComposeContent(isWideScreen = true)

        // Verify list is shown
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()

        // Verify default detail message
        composeTestRule.onNodeWithText("Select a tip from the list").assertIsDisplayed()
    }

    @Test
    fun wideScreen_shouldUpdateDetailWhenTipSelected() {
        setupComposeContent(isWideScreen = true)

        // Select first tip
        composeTestRule.onNodeWithText(moodTips[0]).performClick()

        // Verify detail updates (we can't verify exact content since it's not exposed)
        composeTestRule.onNodeWithText("Details:").assertIsDisplayed()

        // Verify no back button in wide layout
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }




    @Test
    fun shouldDisplayCorrectTipDetails() {
        setupComposeContent(isWideScreen = false)

        // Test each tip's detail view
        moodTips.forEachIndexed { index, tip ->
            // Select the tip
            composeTestRule.onNodeWithText(tip).performClick()

            // Verify detail header
            composeTestRule.onNodeWithText("Details:").assertIsDisplayed()

            // Go back to list
            composeTestRule.onNodeWithContentDescription("Back").performClick()

            // Small delay between tests
            Thread.sleep(100)
        }
    }

    @Test
    fun compactScreen_initialState_shouldNotShowDetail() {
        setupComposeContent(isWideScreen = false)

        // Verify detail placeholder isn't shown in compact mode
        composeTestRule.onNodeWithText("Select a tip from the list").assertDoesNotExist()
    }
}