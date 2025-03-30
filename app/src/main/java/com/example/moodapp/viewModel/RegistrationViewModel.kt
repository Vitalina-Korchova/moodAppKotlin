package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _dateOfBirth = MutableStateFlow("")
    val dateOfBirth: StateFlow<String> = _dateOfBirth

    private val _isPrivacyAccepted = MutableStateFlow(false)
    val isPrivacyAccepted: StateFlow<Boolean> = _isPrivacyAccepted

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    fun onLoginChanged(value: String) {
        _login.value = value
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
    }

    fun onDateOfBirthChanged(value: String) {
        _dateOfBirth.value = value
    }

    fun onPrivacyAcceptedChanged(value: Boolean) {
        _isPrivacyAccepted.value = value
    }

    fun registerUser() {
        if (_login.value.isNotEmpty() && _password.value.isNotEmpty() && _dateOfBirth.value.isNotEmpty() && _isPrivacyAccepted.value) {
            viewModelScope.launch {
                val success = UserRepository.registerUser(1, _login.value, _dateOfBirth.value, _password.value)
                _isRegistered.value = success
            }
        }
    }
}
