package com.example.moodapp.views

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AdaptiveNavigationBar(
    navController: NavController,
    windowSizeClass: WindowWidthSizeClass
) {
    when (windowSizeClass) {
        WindowWidthSizeClass.Compact -> BottomNavigation(navController)
        WindowWidthSizeClass.Medium,
        WindowWidthSizeClass.Expanded -> SideNavigation(navController)
    }
}
