
package com.example.moodapp.viewModel
import androidx.lifecycle.ViewModel
import com.example.moodapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class AuthorizationViewModel : ViewModel() {
    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun onLoginChanged(value: String) {
        _login.value = value
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
    }

    fun loginUser() {
        _isLoggedIn.value = UserRepository.loginUser(_login.value, _password.value)
    }
}
