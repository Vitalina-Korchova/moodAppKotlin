package com.example.moodapp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.compose.MoodAppTheme
import com.example.moodapp.views.BottomNavigation
import com.example.moodapp.views.Screen
import com.example.moodapp.views.SideNavigation
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.navigatorProvider.addNavigator(ComposeNavigator())
    }

    private fun setBottomNavContent() {
        composeTestRule.setContent {
            MoodAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.HistoryMood.route
                ) {
                    composable(Screen.HistoryMood.route) {}
                    composable(Screen.Profile.route) {}
                    composable(Screen.MoodTips.route) {}
                }

                BottomNavigation(navController = navController)
            }
        }
    }

    private fun setSideNavContent() {
        composeTestRule.setContent {
            MoodAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.HistoryMood.route
                ) {
                    composable(Screen.HistoryMood.route) {}
                    composable(Screen.Profile.route) {}
                    composable(Screen.MoodTips.route) {}
                }

                SideNavigation(navController = navController)
            }
        }
    }

    @Test
    fun bottomNavigation_initialState_historyMoodSelected() {
        setBottomNavContent()
        assertEquals(Screen.HistoryMood.route, navController.currentDestination?.route)
    }

    @Test
    fun bottomNavigation_clickProfile_navigatesToProfile() {
        setBottomNavContent()
        composeTestRule.onNodeWithContentDescription(Screen.Profile.label).performClick()
        assertEquals(Screen.Profile.route, navController.currentDestination?.route)
    }

    @Test
    fun bottomNavigation_clickMoodTips_navigatesToMoodTips() {
        setBottomNavContent()
        composeTestRule.onNodeWithContentDescription(Screen.MoodTips.label).performClick()
        assertEquals(Screen.MoodTips.route, navController.currentDestination?.route)
    }

    @Test
    fun bottomNavigation_backToHistoryMood_restoresState() {
        setBottomNavContent()
        composeTestRule.onNodeWithContentDescription(Screen.Profile.label).performClick()
        composeTestRule.onNodeWithContentDescription(Screen.HistoryMood.label).performClick()
        assertEquals(Screen.HistoryMood.route, navController.currentDestination?.route)
    }

    @Test
    fun sideNavigation_clickItems_navigatesCorrectly() {
        setSideNavContent()

        composeTestRule.onNodeWithContentDescription(Screen.Profile.label).performClick()
        assertEquals(Screen.Profile.route, navController.currentDestination?.route)

        composeTestRule.onNodeWithContentDescription(Screen.MoodTips.label).performClick()
        assertEquals(Screen.MoodTips.route, navController.currentDestination?.route)
    }
}
