package com.example.moodapp

import com.example.moodapp.repository.UserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

//1 тест
//тестування репо і методів

class TestBusinessLogic {

    @Before
    fun resetUser() {
        // скидається до дефолтного юзера перед кожним тестом
        UserRepository.registerUser(0, "vit", "2005-01-15", "1234")

    }

    @Test
    fun registerUserReturnsTrueForValidData() {
        val result = UserRepository.registerUser(1, "test", "2000-05-10", "pass")
        assertTrue(result)
        val user = UserRepository.getUser()
        assertNotNull(user)
        assertEquals("test", user?.login)
    }

    @Test
    fun registerUserReturnsFalseForEmptyFields() {
        val result = UserRepository.registerUser(2, "", "", "")
        assertFalse(result)
    }

    @Test
    fun loginUserReturnsTrueForDefaultUser() {
        val result = UserRepository.loginUser("vit", "1234")
        assertTrue(result)
    }

    @Test
    fun loginUserReturnsTrueForNewlyRegisteredUser() {
        UserRepository.registerUser(3, "newUser", "1999-12-31", "newpass")
        val result = UserRepository.loginUser("newUser", "newpass")
        assertTrue(result)
    }

    @Test
    fun loginUserReturnsFalseForWrongCredentials() {
        val result1 = UserRepository.loginUser("wrong", "1234")
        val result2 = UserRepository.loginUser("vit", "wrongpass")
        assertFalse(result1)
        assertFalse(result2)
    }

    @Test
    fun getUserReturnsCurrentUser() {
        val user = UserRepository.getUser()
        assertNotNull(user)
        assertEquals("vit", user?.login)
    }
}