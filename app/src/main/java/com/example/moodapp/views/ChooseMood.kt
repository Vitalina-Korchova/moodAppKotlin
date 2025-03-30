package com.example.moodapp.views

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.ui.theme.customPurple
import com.example.moodapp.viewModel.ChooseMoodViewModel

@Composable
fun ChooseMood(
    navController: NavController,
    viewModel: ChooseMoodViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isNavigateToActivities) {
        navController.navigate("activities_mood")
        viewModel.onEvent(ChooseMoodViewModel.MoodEvent.NavigationHandled)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "How are you today?", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = state.currentDate)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.moodOptions.forEach { (mood, iconResId) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {

                            viewModel.onEvent(ChooseMoodViewModel.MoodEvent.MoodSelected(mood))
                            Log.d("Mood", "User selected mood: $mood")
                        }
                    ) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = "$mood mood",
                            modifier = Modifier.size(52.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = mood,
                            fontSize = 16.sp,

                            fontWeight = if (mood == state.selectedMood) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                    viewModel.onEvent(ChooseMoodViewModel.MoodEvent.SaveButtonClicked)
                },
                modifier = Modifier.padding(5.dp),
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
}