package com.example.moodapp.repository

import com.example.moodapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object UserRepository {
    private val _userState = MutableStateFlow<User?>(null)


    // Дефолтний юзер
    private val defaultUser = User(
        id = 0,
        login = "vit",
        dateOfBirth = "2005-01-15",
        password = "1234"
    )

    init {
        _userState.value = defaultUser
    }

    // Реєстрація нового юзера
    fun registerUser(id: Int, login: String, dateOfBirth: String, password: String): Boolean {
        if (login.isNotEmpty() && password.isNotEmpty() && dateOfBirth.isNotEmpty()) {
            _userState.value = User(id, login, dateOfBirth, password)
            return true
        }
        return false
    }

    // Авторизація перевіряє і дефолтного, і зареєстрованого юзера
    fun loginUser(login: String, password: String): Boolean {
        val currentUser = _userState.value
        return currentUser != null && currentUser.login == login && currentUser.password == password
    }


    fun getUser(): User? = _userState.value
}
