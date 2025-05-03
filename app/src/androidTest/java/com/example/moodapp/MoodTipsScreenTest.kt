package com.example.moodapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
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
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()
        moodTips.forEach { tip ->
            composeTestRule.onNodeWithText(tip).assertIsDisplayed()
        }
    }

    @Test
    fun compactScreen_shouldShowDetailWhenTipSelected() {
        setupComposeContent(isWideScreen = false)

        composeTestRule.onNodeWithText(moodTips[0]).performClick()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Details:").assertIsDisplayed()
    }

    @Test
    fun compactScreen_shouldReturnToListWhenBackClicked() {
        setupComposeContent(isWideScreen = false)
        composeTestRule.onNodeWithText(moodTips[0]).performClick()
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()
    }

    @Test
    fun wideScreen_shouldShowBothListAndDetailInitially() {
        setupComposeContent(isWideScreen = true)
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()
        composeTestRule.onNodeWithText("Select a tip from the list").assertIsDisplayed()
    }

    @Test
    fun wideScreen_shouldUpdateDetailWhenTipSelected() {
        setupComposeContent(isWideScreen = true)

        composeTestRule.onNodeWithText(moodTips[0]).performClick()
        composeTestRule.onNodeWithText("Details:").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }




    @Test
    fun shouldDisplayCorrectTipDetails() {
        setupComposeContent(isWideScreen = false)


        moodTips.forEachIndexed { index, tip ->

            composeTestRule.onNodeWithText(tip).performClick()
            composeTestRule.onNodeWithText("Details:").assertIsDisplayed()
            composeTestRule.onNodeWithContentDescription("Back").performClick()


            Thread.sleep(100)
        }
    }

    @Test
    fun compactScreen_initialState_shouldNotShowDetail() {
        setupComposeContent(isWideScreen = false)
        composeTestRule.onNodeWithText("Select a tip from the list").assertDoesNotExist()
    }

    @Test
    fun mediumScreen_shouldShowListAndPlaceholderDetailInitially() {
        setupComposeContent(isWideScreen = true)

        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()
        composeTestRule.onNodeWithText("Select a tip from the list").assertIsDisplayed()
    }


    @Test
    fun compactScreen_shouldShowOnlyListInitially() {
        setupComposeContent(isWideScreen = false)

        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()

        composeTestRule.onNodeWithText("Details:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Select a tip from the list").assertDoesNotExist()
    }

    @Test
    fun wideScreen_shouldShowCorrectProportions() {
        setupComposeContent(isWideScreen = true)

        composeTestRule.onAllNodesWithText("Mood Tips")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("Select a tip from the list").assertIsDisplayed()
    }

    @Test
    fun compactScreen_navigationFlow() {
        setupComposeContent(isWideScreen = false)

        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()

        composeTestRule.onNodeWithText(moodTips[2]).performClick()

        composeTestRule.onNodeWithText("Details:").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.onNodeWithText("Mood Tips").assertIsDisplayed()
    }


}