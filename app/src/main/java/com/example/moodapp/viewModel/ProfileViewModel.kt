// ProfileViewModel.kt
package com.example.moodapp.viewModel

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
    val region: String = "Ukraine"
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profileData: UserProfileData = UserProfileData()
)

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val userRepository = UserRepository

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val user = userRepository.getUser()

                if (user != null) {
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
                        region = "Ukraine"
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun logout() {
        viewModelScope.launch {
            // Implement logout logic
        }
    }
}