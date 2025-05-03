package com.example.moodapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.compose.MoodAppTheme
import com.example.moodapp.viewModel.ChooseMoodViewModel
import com.example.moodapp.views.ChooseMood
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(AndroidJUnit4::class)
class ChooseMoodScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))

            MoodAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "choose_mood"
                ) {
                    composable("choose_mood") {
                        val viewModel = ChooseMoodViewModel()
                        ChooseMood(navController, windowSizeClass, viewModel)
                    }
                    composable("activities_mood") { /**/ }
                }
            }
        }
    }

    @Test
    fun verifyMainElementsDisplayed() {
        composeTestRule.onNodeWithText("How are you today?").assertIsDisplayed()
        composeTestRule.onNodeWithText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun verifyAllMoodOptionsDisplayed() {
        val moods = listOf("Happy", "Good", "Neutral", "Sad", "Bad")
        moods.forEach { mood ->
            composeTestRule.onNodeWithContentDescription("$mood mood").assertIsDisplayed()
            composeTestRule.onNodeWithText(mood).assertIsDisplayed()
        }
    }

    @Test
    fun testMoodSelection() {

        composeTestRule.onNodeWithContentDescription("Happy mood").performClick()
        composeTestRule.onNodeWithText("Happy").assertIsDisplayed()


        composeTestRule.onNodeWithContentDescription("Sad mood").performClick()
        composeTestRule.onNodeWithText("Sad").assertIsDisplayed()
    }

    @Test
    fun testSaveButtonNavigation() {

        composeTestRule.onNodeWithContentDescription("Neutral mood").performClick()


        composeTestRule.onNodeWithText("Save").performClick()


        composeTestRule.waitUntil(3000) {
            navController.currentBackStackEntry?.destination?.route == "activities_mood"
        }


        assertEquals("activities_mood", navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun testInitialState() {

        composeTestRule.onNodeWithText("Save").assertIsEnabled()


        listOf("Happy", "Good", "Neutral", "Sad", "Bad").forEach { mood ->
            composeTestRule.onNodeWithText(mood).assertIsDisplayed()
        }
    }


}