package com.example.moodapp.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.viewModel.ActivitiesMoodViewModel

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun ActivitiesMood(
    navController: NavController,
    viewModel: ActivitiesMoodViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Get window size class for adaptive UI
    val activity = LocalContext.current as? ComponentActivity
    val windowSizeClass = activity?.let { calculateWindowSizeClass(it) }
    val isTablet = windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Medium

    // Adaptive sizing based on device type
    val titleFontSize = if (isTablet) 32.sp else 24.sp
    val activityTextSize = if (isTablet) 18.sp else 14.sp
    val buttonTextSize = if (isTablet) 24.sp else 20.sp
    val contentPadding = if (isTablet) 32.dp else 16.dp
    val gridSpacing = if (isTablet) 24.dp else 16.dp
    val activityPadding = if (isTablet) 16.dp else 8.dp
    val cornerRadius = if (isTablet) 12.dp else 8.dp
    val borderWidth = if (isTablet) 3.dp else 2.dp
    val buttonWidth = if (isTablet) 200.dp else 150.dp
    val buttonCornerRadius = if (isTablet) 20.dp else 16.dp
    val buttonElevation = if (isTablet) 12.dp else 8.dp
    val gridCellCount = if (isTablet) 3 else 3 // More columns on tablet

    if (state.isNavigateToHistory) {
        navController.navigate("history_mood")
        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.NavigationHandled)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(contentPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What have you been up to?",
            fontSize = titleFontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = if (isTablet) 80.dp else 55.dp)
                .align(Alignment.CenterHorizontally)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridCellCount),
                verticalArrangement = Arrangement.spacedBy(gridSpacing),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                items(state.allActivities) { activity ->
                    val isSelected = state.selectedActivities.contains(activity)
                    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(borderWidth, MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(cornerRadius)
                            )
                            .background(backgroundColor, shape = RoundedCornerShape(cornerRadius))
                            .clickable {
                                viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(activity))
                            }
                            .padding(horizontal = activityPadding, vertical = activityPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activity,
                            fontSize = activityTextSize,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.SaveButtonClicked)
                Log.d("ActivitiesMood", "Selected activities: ${state.selectedActivities}")
            },
            modifier = Modifier
                .width(buttonWidth)
                .padding(top = if (isTablet) 24.dp else 16.dp, bottom = if (isTablet) 16.dp else 8.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(buttonCornerRadius),
            elevation = ButtonDefaults.buttonElevation(buttonElevation)
        ) {
            Text(
                text = "Save",
                fontSize = buttonTextSize,
                modifier = Modifier.padding(vertical = if (isTablet) 8.dp else 4.dp)
            )
        }
    }
}