package com.example.moodapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.moodapp.viewmodels.ProfileViewModel

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun Profile(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: ProfileViewModel = viewModel()
) {
    val isWideScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

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
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(if (isWideScreen) 48.dp else 40.dp)
            )
        }

        uiState.errorMessage?.let { error ->
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Error", fontSize = if (isWideScreen) 18.sp else 16.sp) },
                text = { Text(error, fontSize = if (isWideScreen) 16.sp else 14.sp) },
                confirmButton = {
                    Button(onClick = { viewModel.clearError() }) {
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

            // Username
            Text(
                text = uiState.profileData.username,
                fontSize = if (isWideScreen) 30.sp else 24.sp,
                fontWeight = FontWeight.Bold
            )

            // Birth Date
            Text(
                text = "Date of Birth: ${uiState.profileData.dateOfBirth}",
                fontSize = if (isWideScreen) 20.sp else 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Main content - adapts based on screen width
        if (isWideScreen) {
            // Wide screen layout - side by side
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Left column - Statistics
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = "Statistics",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Common modifier for both rows
                    val itemModifierT = Modifier.weight(1f)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(modifier = itemModifierT, contentAlignment = Alignment.Center) {
                            StatItem(
                                icon = Icons.Default.CheckCircle,
                                label = "Moods Recorded",
                                value = uiState.statistics.moodsRecorded.toString(),
                                isWideScreen = true
                            )
                        }

                        Box(modifier = itemModifierT, contentAlignment = Alignment.Center) {
                            StatItem(
                                icon = Icons.Default.DateRange,
                                label = "Streak",
                                value = "${uiState.statistics.streakDays} days",
                                isWideScreen = true
                            )
                        }

                        Box(modifier = itemModifierT, contentAlignment = Alignment.Center) {
                            StatItem(
                                icon = Icons.Default.Star,
                                label = "Achievements",
                                value = uiState.statistics.achievements.toString(),
                                isWideScreen = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(modifier = itemModifierT, contentAlignment = Alignment.Center) {
                            StatItem(
                                icon = Icons.Default.Favorite,
                                label = "Fav advices",
                                value = uiState.statistics.moodsRecorded.toString(),
                                isWideScreen = true
                            )
                        }

                        Box(modifier = itemModifierT, contentAlignment = Alignment.Center) {
                            StatItem(
                                icon = Icons.Default.Create,
                                label = "Updated moods",
                                value = "${uiState.statistics.streakDays} days",
                                isWideScreen = true
                            )
                        }

                        Box(modifier = itemModifierT, contentAlignment = Alignment.Center) {
                            StatItem(
                                icon = Icons.Default.Warning,
                                label = "Missed Days",
                                value = uiState.statistics.achievements.toString(),
                                isWideScreen = true
                            )
                        }
                    }
                }

                // Right column - Settings
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 20.dp)
                ) {
                    Text(
                        text = "App Settings",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    SettingsItem(
                        icon = Icons.Default.Place,
                        title = "Region",
                        subtitle = uiState.profileData.region,
                        onClick = { },
                        isWideScreen = true
                    )

                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Language",
                        subtitle = uiState.profileData.language,
                        onClick = { },
                        isWideScreen = true
                    )

                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = if (uiState.profileData.notificationsEnabled) "On" else "Off",
                        onClick = { },
                        isWideScreen = true
                    )
                }
            }
        } else {
            // Narrow screen layout - stacked
            // Statistics Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Statistics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Common modifier for both rows
                val itemModifier = Modifier.weight(1f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = itemModifier, contentAlignment = Alignment.Center) {
                        StatItem(
                            icon = Icons.Default.CheckCircle,
                            label = "Moods Recorded",
                            value = uiState.statistics.moodsRecorded.toString(),
                            isWideScreen = false
                        )
                    }

                    Box(modifier = itemModifier, contentAlignment = Alignment.Center) {
                        StatItem(
                            icon = Icons.Default.DateRange,
                            label = "Streak",
                            value = "${uiState.statistics.streakDays} days",
                            isWideScreen = false
                        )
                    }

                    Box(modifier = itemModifier, contentAlignment = Alignment.Center) {
                        StatItem(
                            icon = Icons.Default.Star,
                            label = "Achievements",
                            value = uiState.statistics.achievements.toString(),
                            isWideScreen = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = itemModifier, contentAlignment = Alignment.Center) {
                        StatItem(
                            icon = Icons.Default.Favorite,
                            label = "Fav advices",
                            value = uiState.statistics.moodsRecorded.toString(),
                            isWideScreen = false
                        )
                    }

                    Box(modifier = itemModifier, contentAlignment = Alignment.Center) {
                        StatItem(
                            icon = Icons.Default.Create,
                            label = "Updated moods",
                            value = "${uiState.statistics.streakDays} days",
                            isWideScreen = false
                        )
                    }

                    Box(modifier = itemModifier, contentAlignment = Alignment.Center) {
                        StatItem(
                            icon = Icons.Default.Warning,
                            label = "Missed Days",
                            value = uiState.statistics.achievements.toString(),
                            isWideScreen = false
                        )
                    }
                }
            }

            // Settings Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "App Settings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SettingsItem(
                    icon = Icons.Default.Place,
                    title = "Region",
                    subtitle = uiState.profileData.region,
                    onClick = { },
                    isWideScreen = false
                )

                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Language",
                    subtitle = uiState.profileData.language,
                    onClick = { },
                    isWideScreen = false
                )

                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = if (uiState.profileData.notificationsEnabled) "On" else "Off",
                    onClick = { },
                    isWideScreen = false
                )
            }
        }

        Spacer(modifier = Modifier.height(if (isWideScreen) 28.dp else 15.dp))

        // Logout Button - adapts width based on screen size
        Button(
            onClick = {
                viewModel.logout()
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
                modifier = Modifier.padding(end = 10.dp).size(if (isWideScreen) 24.dp else 20.dp)
            )
            Text(
                text = "Log Out",
                fontSize = if (isWideScreen) 20.sp else 16.sp
            )
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    isWideScreen: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(if (isWideScreen) 36.dp else 28.dp)
        )

        Spacer(modifier = Modifier.height(if (isWideScreen) 6.dp else 4.dp))

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = if (isWideScreen) 20.sp else 16.sp
        )

        Text(
            text = label,
            fontSize = if (isWideScreen) 16.sp else 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isWideScreen: Boolean = false
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
                text = subtitle,
                fontSize = if (isWideScreen) 16.sp else 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(if (isWideScreen) 28.dp else 24.dp)
            )
        }
    }
}