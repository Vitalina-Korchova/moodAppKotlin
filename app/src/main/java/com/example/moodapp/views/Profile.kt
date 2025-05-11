// ProfileScreen.kt
package com.example.moodapp.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.R
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import com.example.moodapp.viewModel.ProfileViewModel
import com.example.moodapp.viewModel.SettingsViewModel
import com.example.moodapp.viewModel.SettingsViewModelFactory



@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Profile(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    profileViewModel: ProfileViewModel = viewModel(),
    context: Context = LocalContext.current
) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )
    val isWideScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    val scrollState = rememberScrollState()
    val profileState by profileViewModel.uiState.collectAsState()
    val settingsState by settingsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(
                horizontal = if (isWideScreen) 36.dp else 16.dp,
                vertical = if (isWideScreen) 20.dp else 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profileState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(if (isWideScreen) 48.dp else 40.dp)
            )
        }

        profileState.errorMessage?.let { error ->
            AlertDialog(
                onDismissRequest = { profileViewModel.clearError() },
                title = { Text("Error", fontSize = if (isWideScreen) 18.sp else 16.sp) },
                text = { Text(error, fontSize = if (isWideScreen) 16.sp else 14.sp) },
                confirmButton = {
                    Button(onClick = { profileViewModel.clearError() }) {
                        Text("OK", fontSize = if (isWideScreen) 16.sp else 14.sp)
                    }
                }
            )
        }

        // Profile Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = if (isWideScreen) 28.dp else 24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(if (isWideScreen) 110.dp else 80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(if (isWideScreen) 10.dp else 8.dp))

            Text(
                text = profileState.profileData.username,
                fontSize = if (isWideScreen) 30.sp else 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Date of Birth: ${profileState.profileData.dateOfBirth}",
                fontSize = if (isWideScreen) 20.sp else 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // App Settings Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (isWideScreen) 20.dp else 16.dp)
        ) {
            Text(
                text = "App Settings",
                fontSize = if (isWideScreen) 22.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = if (isWideScreen) 20.dp else 8.dp)
            )

            // Notification Setting
            SettingSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                isChecked = settingsState.notificationsEnabled,
                onCheckedChange = { settingsViewModel.toggleNotifications(it) },
                isWideScreen = isWideScreen
            )

            // Sounds Setting
            SettingSwitchItem(
                icon = Icons.Default.Phone,
                title = "Sounds",
                isChecked = settingsState.soundsEnabled,
                onCheckedChange = { settingsViewModel.toggleSounds(it) },
                isWideScreen = isWideScreen
            )

            // Language Setting
            SettingItem(
                icon = Icons.Default.LocationOn,
                title = "Language",
                value = when(settingsState.appLanguage) {
                    "uk" -> "Українська"
                    else -> "English"
                },
                onClick = {
                    val newLanguage = if (settingsState.appLanguage == "en") "uk" else "en"
                    settingsViewModel.setAppLanguage(context, newLanguage)
                },
                isWideScreen = isWideScreen
            )
        }

        Spacer(modifier = Modifier.height(if (isWideScreen) 28.dp else 15.dp))

        // Logout Button
        Button(
            onClick = {
                profileViewModel.logout()
                navController.navigate("signin_screen")
            },
            modifier = Modifier
                .let {
                    if (isWideScreen) it.width(320.dp) else it.fillMaxWidth()
                }
                .padding(vertical = if (isWideScreen) 10.dp else 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(if (isWideScreen) 24.dp else 20.dp)
            )
            Text(
                text = "Log Out",
                fontSize = if (isWideScreen) 20.sp else 16.sp
            )
        }
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit,
    isWideScreen: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isWideScreen) 14.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(if (isWideScreen) 28.dp else 24.dp)
        )

        Spacer(modifier = Modifier.width(if (isWideScreen) 20.dp else 16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = if (isWideScreen) 18.sp else 16.sp
            )
            Text(
                text = value,
                fontSize = if (isWideScreen) 16.sp else 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Change $title",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(if (isWideScreen) 28.dp else 24.dp)
            )
        }
    }
}

@Composable
private fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isWideScreen: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (isWideScreen) 14.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(if (isWideScreen) 28.dp else 24.dp)
        )

        Spacer(modifier = Modifier.width(if (isWideScreen) 20.dp else 16.dp))

        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = if (isWideScreen) 18.sp else 16.sp,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}