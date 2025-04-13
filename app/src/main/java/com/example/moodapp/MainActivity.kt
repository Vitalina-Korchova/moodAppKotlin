package com.example.moodapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.MoodAppTheme
import com.example.moodapp.views.ActivitiesMood
import com.example.moodapp.views.Authorization
import com.example.moodapp.views.BottomNavigation
import com.example.moodapp.views.ChooseMood
import com.example.moodapp.views.HistoryMood
import com.example.moodapp.views.Profile
import com.example.moodapp.views.Registration
import com.example.moodapp.views.SideNavigation

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Активність створена")
        setContent {
            MoodAppTheme {
                val navController = rememberNavController()
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                val windowSizeClass = calculateWindowSizeClass(this)
                val routesWithNav = listOf("history_mood", "profile")
                val showNav = currentDestination in routesWithNav

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showNav && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                            BottomNavigation(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Row(modifier = Modifier.padding(innerPadding)) {
                        if (showNav && windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                            SideNavigation(navController = navController)
                        }

                        NavHost(
                            navController = navController,
                            startDestination = "signin_screen",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                        composable("choose_mood") {
                            ChooseMood(navController)
                        }
                        composable("activities_mood") {
                            ActivitiesMood(navController)
                        }
                        composable("history_mood"){
                            HistoryMood(navController)
                        }
                        composable("signin_screen"){
                            Authorization(navController)
                        }
                        composable("signup_screen"){
                            Registration(navController)
                        }
                        composable("profile") {
                            Profile(navController)
                        }
                    }
                }
            }
        }
      }
    }
}

@Composable
fun ContentLayout(navController: NavController) {


}

@Preview(showBackground = true)
@Composable
fun ContentLayoutPreview() {
    val navController = rememberNavController()
    MoodAppTheme {
        ContentLayout(navController)
    }
}

