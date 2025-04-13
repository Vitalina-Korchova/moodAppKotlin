package com.example.moodapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.viewModel.ChooseMoodViewModel
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import android.annotation.SuppressLint
import androidx.compose.material3.windowsizeclass.WindowSizeClass

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun ChooseMood(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: ChooseMoodViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val isTablet = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    val titleFontSize = if (isTablet) 32.sp else 24.sp
    val dateFontSize = if (isTablet) 20.sp else 16.sp
    val moodIconSize = if (isTablet) 80.dp else 52.dp
    val moodTextSize = if (isTablet) 20.sp else 16.sp
    val buttonTextSize = if (isTablet) 24.sp else 20.sp
    val spacingBetweenElements = if (isTablet) 24.dp else 16.dp
    val spacingBetweenMoods = if (isTablet) 16.dp else 8.dp
    val buttonWidth = if (isTablet) 200.dp else 150.dp
    val buttonPadding = if (isTablet) 12.dp else 5.dp
    val contentPadding = if (isTablet) 32.dp else 16.dp

    if (state.isNavigateToActivities) {
        navController.navigate("activities_mood")
        viewModel.onEvent(ChooseMoodViewModel.MoodEvent.NavigationHandled)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.onPrimary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How are you today?",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(spacingBetweenElements / 2))

            Text(
                text = state.currentDate,
                fontSize = dateFontSize
            )

            Spacer(modifier = Modifier.height(spacingBetweenElements))

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingBetweenMoods),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.moodOptions.forEach { (mood, iconResId) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                viewModel.onEvent(ChooseMoodViewModel.MoodEvent.MoodSelected(mood))
                                Log.d("Mood", "User selected mood: $mood")
                            }
                            .padding(horizontal = if (isTablet) 8.dp else 4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = "$mood mood",
                            modifier = Modifier.size(moodIconSize),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(if (isTablet) 8.dp else 4.dp))

                        Text(
                            text = mood,
                            fontSize = moodTextSize,
                            fontWeight = if (mood == state.selectedMood) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacingBetweenElements))

            Button(
                onClick = {
                    viewModel.onEvent(ChooseMoodViewModel.MoodEvent.SaveButtonClicked)
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(buttonPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(if (isTablet) 20.dp else 16.dp),
                elevation = ButtonDefaults.buttonElevation(if (isTablet) 12.dp else 8.dp)
            ) {
                Text(
                    text = "Save",
                    fontSize = buttonTextSize,
                    modifier = Modifier.padding(vertical = if (isTablet) 8.dp else 4.dp)
                )
            }
        }
    }
}
