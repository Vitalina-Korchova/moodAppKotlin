package com.example.moodapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.moodapp.R

@Composable
fun BottomNavigation(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier.height(35.dp),
            containerColor = Color.Transparent
        ) {
            val items = listOf(Screen.HistoryMood, Screen.Profile, Screen.MoodTips)

            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.label,
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    selected = navController.currentDestination?.route == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SideNavigation(navController: NavController) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        val items = listOf(Screen.HistoryMood, Screen.Profile, Screen.MoodTips)

        items.forEach { screen ->
            NavigationRailItem(
                icon = {
                    Row(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start

                    ) {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.label,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = screen.label,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                    }
                },
                selected = navController.currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class Screen(val route: String, val icon: Int, val label: String) {
    object HistoryMood : Screen("history_mood", R.drawable.icon_history, "History")
    object Profile : Screen("profile", R.drawable.icon_profile, "Profile")
    object MoodTips : Screen("tips_mood", R.drawable.icon_tips, "MoodTips")
}
