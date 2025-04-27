package com.example.moodapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
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
import com.example.moodapp.viewModel.AuthorizationViewModel
import com.example.moodapp.views.Authorization
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever


@RunWith(AndroidJUnit4::class)
class AuthorizationScreenTest {

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

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1000.dp, 1000.dp))

            MoodAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "authorization"
                ) {
                    composable("authorization") {
                        Authorization(navController, windowSizeClass)
                    }
                    composable("choose_mood") { /* Mock screen */ }
                    composable("signup_screen") { /* Mock screen */ }
                }
            }
        }
    }

    @Test
    fun authorizationScreen_verifyAllElementsVisible() {

        composeTestRule.onNodeWithText("Authorization").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Login Image").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign up").assertIsDisplayed()
    }

    @Test
    fun authorizationScreen_verifyLoginInput() {

        composeTestRule.onNodeWithText("Login").performTextInput("testuser")
        composeTestRule.onNodeWithText("Login").assertTextContains("testuser")
    }


    @Test
    fun authorizationScreen_loginButtonInitiallyDisabled() {

        composeTestRule.onNodeWithText("Sign In").assertIsNotEnabled()
    }

    @Test
    fun authorizationScreen_loginButtonEnabledWhenFieldsFilled() {

        composeTestRule.onNodeWithText("Login").performTextInput("user")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")
        composeTestRule.onNodeWithText("Sign In").assertIsEnabled()
    }

    @Test
    fun authorizationScreen_navigateToSignUp() {

        composeTestRule.onNodeWithText("Sign up").performClick()
        assertEquals("signup_screen", navController.currentBackStackEntry?.destination?.route)
    }


}