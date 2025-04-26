package com.example.moodapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moodapp.repository.UserRepository
import com.example.moodapp.viewModel.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var mockUserRepository: UserRepository
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // <-- додаємо обов'язково
        mockUserRepository = mock(UserRepository::class.java)
        viewModel = RegistrationViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // <-- повертаємо Main
    }

    @Test
    fun shouldUpdateLoginStateWhenLoginChanges() = testScope.runTest {
        val testLogin = "testUser"
        viewModel.onLoginChanged(testLogin)
        assertEquals(testLogin, viewModel.login.first())
    }

    @Test
    fun shouldUpdatePasswordStateWhenPasswordChanges() = testScope.runTest {
        val testPassword = "secure123"
        viewModel.onPasswordChanged(testPassword)
        assertEquals(testPassword, viewModel.password.first())
    }

    @Test
    fun shouldUpdateDateOfBirthStateWhenDateChanges() = testScope.runTest {
        val testDate = "1990-01-01"
        viewModel.onDateOfBirthChanged(testDate)
        assertEquals(testDate, viewModel.dateOfBirth.first())
    }

    @Test
    fun shouldUpdatePrivacyAcceptedStateWhenCheckboxChanges() = testScope.runTest {
        val testValue = true
        viewModel.onPrivacyAcceptedChanged(testValue)
        assertEquals(testValue, viewModel.isPrivacyAccepted.first())
    }

    @Test
    fun shouldNotRegisterUserWhenFieldsAreEmpty() = testScope.runTest {
        viewModel.registerUser()
        advanceUntilIdle() // чекаємо всі корутини
        assertFalse(viewModel.isRegistered.first())
    }

    @Test
    fun shouldNotRegisterUserWhenPrivacyNotAccepted() = testScope.runTest {
        viewModel.onLoginChanged("user")
        viewModel.onPasswordChanged("pass")
        viewModel.onDateOfBirthChanged("2000-01-01")
        viewModel.onPrivacyAcceptedChanged(false)

        viewModel.registerUser()
        advanceUntilIdle()
        assertFalse(viewModel.isRegistered.first())
    }

    @Test
    fun shouldSetRegisteredToTrueWhenRegistrationSucceeds() = testScope.runTest {
        viewModel.onLoginChanged("user")
        viewModel.onPasswordChanged("pass")
        viewModel.onDateOfBirthChanged("2000-01-01")
        viewModel.onPrivacyAcceptedChanged(true)

        viewModel.registerUser()
        advanceUntilIdle()
        assertTrue(viewModel.isRegistered.first())
    }

    @Test
    fun shouldSetRegisteredToFalseWhenRegistrationFails() = testScope.runTest {
        viewModel.onLoginChanged("")
        viewModel.onPasswordChanged("")
        viewModel.onDateOfBirthChanged("")
        viewModel.onPrivacyAcceptedChanged(false)

        viewModel.registerUser()
        advanceUntilIdle()
        assertFalse(viewModel.isRegistered.first())
    }
}


