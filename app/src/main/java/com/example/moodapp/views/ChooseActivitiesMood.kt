package com.example.moodapp.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.res.stringResource
import com.example.moodapp.R
import com.example.moodapp.utils.MoodDatabase
import com.example.moodapp.viewModel.ChooseActivitiesMoodViewModel
import com.example.moodapp.viewModel.ChooseActivitiesMoodViewModelFactory

@SuppressLint("ContextCastToActivity")
@Composable
fun ChooseActivitiesMood(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    database: MoodDatabase
) {
    // Create ViewModel using factory
    val viewModel: ChooseActivitiesMoodViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ChooseActivitiesMoodViewModelFactory(database)
    )

    val state by viewModel.state.collectAsState()

    // Adaptive parameters based on WindowSizeClass
    val isTablet = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ||
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium

    val titleFontSize = if (isTablet) 32.sp else 24.sp
    val dateFontSize = if (isTablet) 20.sp else 16.sp
    val moodIconSize = if (isTablet) 80.dp else 52.dp
    val moodTextSize = if (isTablet) 20.sp else 14.sp
    val subtitleFontSize = if (isTablet) 28.sp else 20.sp
    val activityTextSize = if (isTablet) 18.sp else 14.sp
    val buttonTextSize = if (isTablet) 24.sp else 20.sp
    val spacingBetweenSections = if (isTablet) 32.dp else 24.dp
    val spacingBetweenElements = if (isTablet) 24.dp else 16.dp
    val spacingBetweenMoods = if (isTablet) 16.dp else 6.dp
    val contentPadding = if (isTablet) 32.dp else 16.dp
    val gridSpacing = if (isTablet) 24.dp else 16.dp
    val activityPadding = if (isTablet) 16.dp else 8.dp
    val cornerRadius = if (isTablet) 12.dp else 8.dp
    val borderWidth = if (isTablet) 3.dp else 2.dp
    val buttonWidth = if (isTablet) 200.dp else 150.dp
    val buttonCornerRadius = if (isTablet) 20.dp else 16.dp
    val buttonElevation = if (isTablet) 12.dp else 8.dp
    val gridCellCount = if (isTablet) 3 else 2

    // Handle navigation
    if (state.isNavigateToHistory) {
        navController.navigate("history_mood")
        viewModel.onEvent(ChooseActivitiesMoodViewModel.CombinedEvent.NavigationHandled)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mood Section
            Text(
                text = stringResource(R.string.chs_mood_question),
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = if (isTablet) 24.dp else 16.dp)
            )

            Spacer(modifier = Modifier.height(spacingBetweenElements / 2))

            Text(
                text = state.currentDate,
                fontSize = dateFontSize
            )

            Spacer(modifier = Modifier.height(spacingBetweenElements))

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingBetweenMoods),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                state.moodOptions.forEach { (mood, iconResId) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.onEvent(ChooseActivitiesMoodViewModel.CombinedEvent.MoodSelected(mood))
                                Log.d("ChooseActivitiesMood", "User selected mood: $mood")
                            }
                            .padding(horizontal = if (isTablet) 8.dp else 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .border(
                                    BorderStroke(
                                        if (mood == state.selectedMood) borderWidth else 0.dp,
                                        if (mood == state.selectedMood) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(cornerRadius)
                                )
                                .padding(if (mood == state.selectedMood) 4.dp else 0.dp)
                        ) {
                            Image(
                                painter = painterResource(id = iconResId),
                                contentDescription = "$mood mood",
                                modifier = Modifier.size(moodIconSize),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.height(if (isTablet) 8.dp else 4.dp))

                        Text(
                            stringResource(id = viewModel.getMoodResource(mood)),
                            fontSize = moodTextSize,
                            fontWeight = if (mood == state.selectedMood) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacingBetweenSections))

            // Activities Section
            Text(
                text = stringResource(R.string.chs_mood_question_activities),
                fontSize = subtitleFontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(spacingBetweenElements))

            // Activities Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridCellCount),
                verticalArrangement = Arrangement.spacedBy(gridSpacing),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isTablet) 300.dp else 220.dp)
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
                                viewModel.onEvent(ChooseActivitiesMoodViewModel.CombinedEvent.ActivityToggled(activity))
                                Log.d("ChooseActivitiesMood", "Activity clicked: $activity")
                            }
                            .padding(horizontal = activityPadding, vertical = activityPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = viewModel.getActivityResource(activity)),
                            fontSize = activityTextSize,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacingBetweenElements))

            // Save Button
            Button(
                onClick = {
                    viewModel.onEvent(ChooseActivitiesMoodViewModel.CombinedEvent.SaveButtonClicked)
                    Log.d("ChooseActivitiesMood", "Save button clicked. Selected mood: ${state.selectedMood}, Activities: ${state.selectedActivities}")
                },
                modifier = Modifier
                    .width(buttonWidth)
                    .padding(vertical = if (isTablet) 16.dp else 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(buttonCornerRadius),
                elevation = ButtonDefaults.buttonElevation(buttonElevation),
                enabled = !state.isSaving // Disable button while saving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.chs_mood_save),
                        fontSize = buttonTextSize,
                        modifier = Modifier.padding(vertical = if (isTablet) 8.dp else 4.dp)
                    )
                }
            }
        }

        // Error message display
        state.error?.let { errorMessage ->
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(errorMessage)
            }
        }

        // Loading indicator overlay
        if (state.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(if (isTablet) 64.dp else 48.dp)
                )
            }
        }
    }
}