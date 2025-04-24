package com.example.moodapp
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
import junit.framework.TestCase.assertEquals
import org.junit.Before
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
    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))

            MoodAppTheme {
                viewModel = HistoryMoodViewModel()
                viewModel.setTestData(testEntries)

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
    }

    @Test
    fun verifyMainElementsDisplayed() {
        composeTestRule.onNodeWithText("Your Mood History").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search Filters").assertIsDisplayed()
        composeTestRule.onNodeWithText("24.04.2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("23.04.2024").assertIsDisplayed()
    }

    @Test
    fun testFilterFunctionality() {
        // 1. Відкриваємо фільтри
        composeTestRule.onNodeWithText("Search Filters").performClick()

        // 2. Фільтруємо по активності "Reading"
        composeTestRule.onNodeWithText("Search activities")
            .performTextInput("Reading")



        // 5. Очищуємо пошук
        composeTestRule.onNodeWithContentDescription("Clear").performClick()

        // 6. Фільтруємо по настрою "Neutral"
        composeTestRule.onNodeWithText("Select mood").performClick()
        composeTestRule.onNodeWithText("Neutral").performClick()



        // 9. Очищуємо всі фільтри
        composeTestRule.onNodeWithText("Clear Filters").performClick()


    }

    @Test
    fun testEntryActions() {
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
        viewModel.clearEntries()
        composeTestRule.waitUntil(2000) {
            viewModel.state.value.allEntries.isEmpty()
        }
        composeTestRule.onNodeWithText("No entries found").assertIsDisplayed()
        composeTestRule.onNodeWithText("Try adjusting your search filters").assertIsDisplayed()
    }


}