package com.example.moodapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class UserProfileData(
    val username: String = "",
    val dateOfBirth: String = "",
    val region: String = "Ukraine",
    val language: String = "English",
    val notificationsEnabled: Boolean = true
)

data class UserStatistics(
    val moodsRecorded: Int = 0,
    val streakDays: Int = 0,
    val achievements: Int = 0
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profileData: UserProfileData = UserProfileData(),
    val statistics: UserStatistics = UserStatistics()
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val userRepository = UserRepository

    init {
        loadUserProfile()
        loadUserStatistics()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Fetch user from repository
                val user = userRepository.getUser()

                if (user != null) {
                    // Format date of birth from repository format (YYYY-MM-DD) to more readable format
                    val formattedDate = try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                        val date = inputFormat.parse(user.dateOfBirth)
                        date?.let { outputFormat.format(it) } ?: user.dateOfBirth
                    } catch (e: Exception) {
                        user.dateOfBirth
                    }

                    val profileData = UserProfileData(
                        username = user.login,
                        dateOfBirth = formattedDate,
                        region = "Ukraine", // Default or could be stored in User model
                        language = "English", // Default or could be stored in User model
                        notificationsEnabled = true // Default or could be stored in User model
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profileData = profileData
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "User not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load profile"
                )
            }
        }
    }

    private fun loadUserStatistics() {
        viewModelScope.launch {
            try {

                val stats = UserStatistics(
                    moodsRecorded = 8,
                    streakDays = 8,
                    achievements = 5
                )

                _uiState.value = _uiState.value.copy(statistics = stats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load statistics"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun logout() {
        viewModelScope.launch {

        }
    }
}