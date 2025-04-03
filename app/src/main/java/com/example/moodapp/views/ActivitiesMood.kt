package com.example.moodapp.views

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.ui.theme.customPurple
import com.example.moodapp.viewModel.ActivitiesMoodViewModel

@Composable
fun ActivitiesMood(
    navController: NavController,
    viewModel: ActivitiesMoodViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    if (state.isNavigateToHistory) {
        navController.navigate("history_mood")
        viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.NavigationHandled)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What have you been up to?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 55.dp)
                .align(Alignment.CenterHorizontally)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                items(state.allActivities) { activity ->
                    val isSelected = state.selectedActivities.contains(activity)
                    val backgroundColor = if (isSelected) customPurple else Color.White
                    val textColor = if (isSelected) Color.White else customPurple

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(2.dp, customPurple),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                            .clickable {

                                viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.ActivityToggled(activity))
                            }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activity,
                            fontSize = 14.sp,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                // Send event to ViewModel when save button is clicked
                viewModel.onEvent(ActivitiesMoodViewModel.ActivitiesEvent.SaveButtonClicked)
                Log.d("ActivitiesMood", "Selected activities: ${state.selectedActivities}")
            },
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = customPurple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(text = "Save", fontSize = 20.sp)
        }
    }
}