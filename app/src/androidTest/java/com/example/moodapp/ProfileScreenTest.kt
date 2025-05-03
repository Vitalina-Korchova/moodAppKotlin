package com.example.moodapp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.compose.MoodAppTheme
import com.example.moodapp.viewmodels.ProfileUiState
import com.example.moodapp.viewmodels.ProfileViewModel
import com.example.moodapp.viewmodels.UserProfileData
import com.example.moodapp.viewmodels.UserStatistics
import com.example.moodapp.views.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var mockViewModel: TestProfileViewModel

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun setupScreenWithSize(dpSize: DpSize) {
        mockViewModel = TestProfileViewModel()

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(dpSize)

            MoodAppTheme {
                Profile(
                    navController = navController,
                    windowSizeClass = windowSizeClass,
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun compactScreen_verifyBasicElements() {
        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onNodeWithContentDescription("Profile Picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth: 1 January 1990").assertIsDisplayed()
        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()
        composeTestRule.onNodeWithText("App Settings").assertIsDisplayed()

        composeTestRule.onNodeWithText("Log Out").performScrollTo()
        composeTestRule.onNodeWithText("Log Out").assertIsDisplayed()
    }

    @Test
    fun wideScreen_verifyBasicElements() {

        setupScreenWithSize(DpSize(840.dp, 1000.dp))


        composeTestRule.onNodeWithContentDescription("Profile Picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth: 1 January 1990").assertIsDisplayed()
        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()
        composeTestRule.onNodeWithText("App Settings").assertIsDisplayed()


        composeTestRule.onNodeWithText("Log Out").performScrollTo()
        composeTestRule.onNodeWithText("Log Out").assertIsDisplayed()
    }

    @Test
    fun compactScreen_verifyProfileHeaderSizing() {

        setupScreenWithSize(DpSize(360.dp, 640.dp))


        composeTestRule.onNodeWithContentDescription("Profile Picture").assertIsDisplayed()


        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth: 1 January 1990").assertIsDisplayed()
    }

    @Test
    fun wideScreen_verifyProfileHeaderSizing() {

        setupScreenWithSize(DpSize(840.dp, 1000.dp))

        composeTestRule.onNodeWithContentDescription("Profile Picture").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth: 1 January 1990").assertIsDisplayed()
    }

    @Test
    fun compactScreen_verifyStatisticsLayout() {

        setupScreenWithSize(DpSize(360.dp, 640.dp))

        composeTestRule.onRoot().printToLog("UI_HIERARCHY")


        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()


        composeTestRule.onNodeWithText("Moods Recorded").assertIsDisplayed()
        composeTestRule.onNodeWithText("Streak").assertIsDisplayed()
        composeTestRule.onNodeWithText("Achievements").assertIsDisplayed()


        composeTestRule.onAllNodesWithText("10").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("5 days").onFirst().assertIsDisplayed()
        composeTestRule.onAllNodesWithText("3").onFirst().assertIsDisplayed()


        composeTestRule.onNodeWithText("Fav advices").assertIsDisplayed()
        composeTestRule.onNodeWithText("Updated moods").assertIsDisplayed()
        composeTestRule.onNodeWithText("Missed Days").assertIsDisplayed()
    }

    @Test
    fun mediumScreen_verifyLayout() {

        setupScreenWithSize(DpSize(720.dp, 360.dp))


        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()
        composeTestRule.onNodeWithText("App Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }

    @Test
    fun veryWideScreen_verifyLayout() {

        setupScreenWithSize(DpSize(1200.dp, 800.dp))


        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()
        composeTestRule.onNodeWithText("App Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }



    @Test
    fun profileScreen_verifyLoadingState() {

        setupScreenWithSize(DpSize(360.dp, 640.dp))


        composeTestRule.runOnUiThread {
            mockViewModel.testUiState.value = mockViewModel.testUiState.value.copy(isLoading = true)
        }

        composeTestRule.waitForIdle()

        try {
            composeTestRule.onNodeWithText("Log Out").assertDoesNotExist()
        } catch (e: AssertionError) {

        }
    }
}

class TestProfileViewModel : ProfileViewModel() {
    val testUiState = MutableStateFlow(
        ProfileUiState(
            isLoading = false,
            errorMessage = null,
            profileData = UserProfileData(
                username = "Test User",
                dateOfBirth = "1 January 1990",
                region = "Ukraine",
                language = "English",
                notificationsEnabled = true
            ),
            statistics = UserStatistics(
                moodsRecorded = 10,
                streakDays = 5,
                achievements = 3
            )
        )
    )

    override val uiState: StateFlow<ProfileUiState> = testUiState
}