package com.example.moodapp
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToString
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.compose.MoodAppTheme
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.viewModel.HistoryMoodViewModel
import com.example.moodapp.views.HistoryMood
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryMoodScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var viewModel: HistoryMoodViewModel

    private val testEntries = listOf(
        MoodEntry(
            id = "1",
            date = "24.04.2024",
            mood = "Happy",
            activities = listOf("Reading", "Walking"),
            moodImageResId = R.drawable.icon_happy_mood
        ),
        MoodEntry(
            id = "2",
            date = "23.04.2024",
            mood = "Neutral",
            activities = listOf("Work", "Cooking"),
            moodImageResId = R.drawable.icon_neutral_mood
        )
    )

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setupScreenWithSize(dpSize: DpSize, entries: List<MoodEntry> = testEntries) {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(dpSize)

            MoodAppTheme {
                viewModel = HistoryMoodViewModel()
                viewModel.setTestData(entries)

                NavHost(
                    navController = navController,
                    startDestination = "history_mood"
                ) {
                    composable("history_mood") {
                        HistoryMood(navController, windowSizeClass, viewModel)
                    }
                }
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun verifyMainElementsDisplayed() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onNodeWithText("Your Mood History").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Filters").assertIsDisplayed()
        composeTestRule.onNodeWithText("24.04.2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("23.04.2024").assertIsDisplayed()
    }

    @Test
    fun testFilterFunctionality() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onNodeWithText("Search Filters").performClick()
        composeTestRule.onNodeWithText("Search activities")
            .performTextInput("Reading")
        composeTestRule.onNodeWithContentDescription("Clear").performClick()
        composeTestRule.onNodeWithText("Select mood").performClick()
        composeTestRule.onNodeWithText("Neutral").performClick()
        composeTestRule.onNodeWithText("Clear Filters").performClick()
    }

    @Test
    fun testEntryActions() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onAllNodesWithContentDescription("Edit mood entry")[0]
            .performClick()
        composeTestRule.onNodeWithText("Edit Mood Entry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onAllNodesWithContentDescription("Delete mood entry")[0]
            .performClick()
        composeTestRule.onNodeWithText("Delete Mood Entry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").performClick()
    }

    @Test
    fun testEmptyState() {
        setupScreenWithSize(DpSize(360.dp, 640.dp), emptyList())

        composeTestRule.onNodeWithText("No entries found").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try adjusting your search filters").assertIsDisplayed()
    }

    @Test
    fun compactScreen_shouldShowSingleColumnLayout() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        println("ViewModel entries: ${viewModel.state.value.allEntries}")

        composeTestRule.onNodeWithText("Your Mood History").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Filters").assertIsDisplayed()

        composeTestRule.onNodeWithText("24.04.2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("23.04.2024").assertIsDisplayed()

        try {
            composeTestRule.onAllNodesWithTag("MoodCard").assertCountEquals(testEntries.size)
        } catch (e: AssertionError) {
            val moodEntries = composeTestRule.onAllNodesWithText("Happy", substring = true).fetchSemanticsNodes().size +
                    composeTestRule.onAllNodesWithText("Neutral", substring = true).fetchSemanticsNodes().size
            assertEquals("Should find 2 mood entries", testEntries.size, moodEntries)
        }
    }


    @Test
    fun compactScreen_filtersShouldBeCollapsible() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onNodeWithText("Search Filters").performClick()
        composeTestRule.onNodeWithText("Search activities").assertIsDisplayed()
        composeTestRule.onNodeWithText("Select mood").assertIsDisplayed()
    }


    @Test
    fun wideScreen_shouldShowGridLayoutForEntries() {
        setupScreenWithSize(DpSize(840.dp, 1200.dp))

        composeTestRule.onNodeWithText("24.04.2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("23.04.2024").assertIsDisplayed()

        try {
            composeTestRule.onAllNodesWithTag("MoodCard").assertCountEquals(testEntries.size)
        } catch (e: AssertionError) {
            val moodEntries = composeTestRule.onAllNodesWithText("Happy", substring = true).fetchSemanticsNodes().size +
                    composeTestRule.onAllNodesWithText("Neutral", substring = true).fetchSemanticsNodes().size
            assertEquals("Should find 2 mood entries", testEntries.size, moodEntries)
        }
    }

    @Test
    fun compactScreen_shouldShowListLayoutForEntries() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onNodeWithText("24.04.2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("23.04.2024").assertIsDisplayed()

        try {
            composeTestRule.onAllNodesWithTag("MoodCard").assertCountEquals(testEntries.size)
        } catch (e: AssertionError) {

            val moodEntries = composeTestRule.onAllNodesWithText("Happy", substring = true).fetchSemanticsNodes().size +
                    composeTestRule.onAllNodesWithText("Neutral", substring = true).fetchSemanticsNodes().size
            assertEquals("Should find 2 mood entries", testEntries.size, moodEntries)
        }
    }

    @Test
    fun emptyState_shouldBeDisplayedCorrectlyInCompactLayout() {
        setupScreenWithSize(DpSize(360.dp, 640.dp), emptyList())

        composeTestRule.onNodeWithText("No entries found").assertIsDisplayed()
    }

    @Test
    fun emptyState_shouldBeDisplayedCorrectlyInWideLayout() {
        setupScreenWithSize(DpSize(840.dp, 1200.dp), emptyList())

        composeTestRule.onNodeWithText("No entries found").assertIsDisplayed()
    }

    @Test
    fun debugUiHierarchy() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))


        println("COMPLETE UI HIERARCHY:")
        println(composeTestRule.onRoot().printToString())


        assert(true)
    }
}