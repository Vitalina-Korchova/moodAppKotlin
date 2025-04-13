package com.example.moodapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Активність створена")
        setContent {
            MoodAppTheme {
                val navController = rememberNavController()
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

                val routesWithBottomNav = listOf("history_mood", "profile")
                val showBottomNav = currentDestination in routesWithBottomNav
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNav) {
                            BottomNavigation(navController = navController)
                        }
                    }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "signin_screen",
                        modifier = Modifier.padding(innerPadding)
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
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Активність стала видимою")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Активність доступна для взаємодії")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Активність більше не в центрі уваги")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Активність більше не видима")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Активність знищена")
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

