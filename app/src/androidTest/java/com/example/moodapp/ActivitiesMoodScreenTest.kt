package com.example.moodapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.compose.MoodAppTheme
import com.example.moodapp.viewModel.ActivitiesMoodViewModel
import com.example.moodapp.views.ActivitiesMood
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivitiesMoodScreenTest {

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
                    startDestination = "activities_mood"
                ) {
                    composable("activities_mood") {
                        val viewModel = ActivitiesMoodViewModel()
                        ActivitiesMood(navController, windowSizeClass, viewModel)
                    }
                    composable("history_mood") { /* Mock screen */ }
                }
            }
        }
    }

    @Test
    fun verifyMainElementsDisplayed() {

        composeTestRule.onNodeWithText("What have you been up to?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun verifyAllActivitiesDisplayed() {

        val testActivities = listOf(
            "Reading", "Movie", "Sport", "Family",
            "Friends", "Studying", "Date", "Sleeping",
            "Shopping", "Relax", "Games", "Cleaning",
            "Work", "Cooking", "Walking"
        )

        testActivities.forEach { activity ->
            composeTestRule.onNodeWithText(activity).assertIsDisplayed()
        }
    }

    @Test
    fun testActivitySelection() {

        composeTestRule.onNodeWithText("Reading").performClick()
        composeTestRule.onNodeWithText("Sport").performClick()


        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    @Test
    fun testMaxActivitiesSelection() {
        //  максимальна кількість активностей 4
        composeTestRule.onNodeWithText("Reading").performClick()
        composeTestRule.onNodeWithText("Movie").performClick()
        composeTestRule.onNodeWithText("Sport").performClick()
        composeTestRule.onNodeWithText("Family").performClick()


        composeTestRule.onNodeWithText("Friends").performClick()


    }

    @Test
    fun testSaveButtonNavigation() {

        composeTestRule.onNodeWithText("Reading").performClick()
        composeTestRule.onNodeWithText("Sport").performClick()


        composeTestRule.onNodeWithText("Save").performClick()


        composeTestRule.waitUntil(3000) {
            navController.currentBackStackEntry?.destination?.route == "history_mood"
        }


        assertEquals("history_mood", navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun testInitialState() {

        composeTestRule.onNodeWithText("Save").assertIsEnabled()

    }
}