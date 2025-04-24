package com.example.moodapp
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
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
import com.example.moodapp.viewmodels.ProfileUiState
import com.example.moodapp.viewmodels.ProfileViewModel
import com.example.moodapp.viewmodels.UserProfileData
import com.example.moodapp.viewmodels.UserStatistics
import com.example.moodapp.views.ActivitiesMood
import com.example.moodapp.views.Profile
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController


    class TestProfileViewModel : ProfileViewModel() {
        private val _uiState = MutableStateFlow(
            ProfileUiState(
                profileData = UserProfileData(
                    username = "TestUser",
                    dateOfBirth = "01.01.1990",
                    region = "Ukraine",
                    language = "English",
                    notificationsEnabled = true
                ),
                statistics = UserStatistics(
                    moodsRecorded = 42,
                    streakDays = 7,
                    achievements = 3
                ),
                isLoading = false
            )
        )

        override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

        fun setTestState(newState: ProfileUiState) {
            _uiState.value = newState
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Before
    fun setUp() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))

            MoodAppTheme {
                val viewModel = TestProfileViewModel()
                Profile(navController, windowSizeClass, viewModel)
            }
        }
    }

    @Test
    fun verifyProfileHeader() {
        composeTestRule.onNodeWithText("TestUser").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date of Birth: 01.01.1990").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Profile Picture").assertIsDisplayed()
    }

    @Test
    fun verifyStatisticsSection() {
        composeTestRule.onNodeWithText("Statistics").assertIsDisplayed()
        composeTestRule.onNodeWithText("42").assertIsDisplayed()
        composeTestRule.onNodeWithText("Moods Recorded").assertIsDisplayed()
        composeTestRule.onNodeWithText("7 days").assertIsDisplayed()
        composeTestRule.onNodeWithText("Streak").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Achievements").assertIsDisplayed()
    }

    @Test
    fun verifySettingsSection() {
        composeTestRule.onNodeWithText("App Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Region").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ukraine").assertIsDisplayed()
        composeTestRule.onNodeWithText("Language").assertIsDisplayed()
        composeTestRule.onNodeWithText("English").assertIsDisplayed()
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("On").assertIsDisplayed()
    }

    @Test
    fun verifyLogoutButton() {
        composeTestRule.onNodeWithText("Log Out").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Logout").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun testLoadingState() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))

            MoodAppTheme {
                val viewModel = TestProfileViewModel().apply {
                    setTestState(
                        ProfileUiState(
                            isLoading = true,
                            profileData = UserProfileData(),
                            statistics = UserStatistics()
                        )
                    )
                }
                Profile(navController, windowSizeClass, viewModel)
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun testErrorDialog() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

            val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))

            MoodAppTheme {
                val viewModel = TestProfileViewModel().apply {
                    setTestState(
                        ProfileUiState(
                            errorMessage = "Test error message",
                            profileData = UserProfileData(),
                            statistics = UserStatistics()
                        )
                    )
                }
                Profile(navController, windowSizeClass, viewModel)
            }
        }

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test error message").assertIsDisplayed()
        composeTestRule.onNodeWithText("OK").assertIsDisplayed().performClick()
    }
}