package com.example.moodapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
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
import com.example.moodapp.viewModel.RegistrationViewModel
import com.example.moodapp.views.Registration
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock


@RunWith(AndroidJUnit4::class)
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Before
    fun setUp() {
        composeTestRule.setContent {
            // Initialize TestNavHostController
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1000.dp, 1000.dp))

            MoodAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = "registration"
                ) {
                    composable("registration") {
                        Registration(navController, windowSizeClass)
                    }
                    composable("signin_screen") { /* Mock screen */ }
                }
            }
        }
    }

    @Test
    fun registrationScreen_verifyAllElementsVisible() {
        // Check all main elements are displayed
        composeTestRule.onNodeWithText("Registration").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Login Image").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth").assertIsDisplayed()
        composeTestRule.onNodeWithText("I accept the Privacy Policy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
    }

    @Test
    fun registrationScreen_signUpButtonInitiallyDisabled() {
        // Button should be initially disabled
        composeTestRule.onNodeWithText("Sign Up").assertIsNotEnabled()
    }

    @Test
    fun registrationScreen_signUpButtonEnabledWhenAllFieldsFilled() {
        // Fill all required fields
        composeTestRule.onNodeWithText("Login").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Date of Birth").performTextInput("01.01.2000")

        // Use the SemanticsMatcher for Checkbox
        composeTestRule.onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .performClick()

        // Wait for UI to update
        composeTestRule.waitForIdle()

        // Check if button is enabled
        composeTestRule.onNodeWithText("Sign Up").assertIsEnabled()
    }

    @Test
    fun registrationScreen_privacyPolicyToggle() {
        // Заповнення всіх обов'язкових полів
        composeTestRule.onNodeWithText("Login").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Date of Birth").performTextInput("01.01.2000")

        // Перевірка: кнопка має бути вимкнена
        composeTestRule.onNodeWithText("Sign Up").assertIsNotEnabled()

        // Натискаємо саме на чекбокс
        composeTestRule
            .onNode(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .performClick()

        // Дочекаймося оновлення
        composeTestRule.waitForIdle()

        // Перевірка: тепер кнопка має бути увімкнена
        composeTestRule.onNodeWithText("Sign Up").assertIsEnabled()
    }


}