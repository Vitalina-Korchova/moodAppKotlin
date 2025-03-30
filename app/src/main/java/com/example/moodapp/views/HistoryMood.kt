package com.example.moodapp.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.model.MoodEntry
import com.example.moodapp.ui.theme.customPurple
import com.example.moodapp.viewModel.HistoryMoodViewModel

@Composable
fun HistoryMood(
    navController: NavController,
    viewModel: HistoryMoodViewModel = viewModel()
) {
    // Collect state from ViewModel
    val state by viewModel.state.collectAsState()

    // Animation for filter section expansion
    val rotationState by animateFloatAsState(
        targetValue = if (state.areFiltersExpanded) 180f else 0f,
        label = "Rotation Animation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = customPurple
            )
        } else {
            Column {
                Text(
                    text = "Your Mood History",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Filters card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Filter section header with toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Using ViewModel event to toggle filters
                                    viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.ToggleFiltersSection)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Search Filters",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = if (state.areFiltersExpanded) "Collapse" else "Expand",
                                modifier = Modifier
                                    .rotate(rotationState)
                                    .size(24.dp)
                            )
                        }

                        // Animated filter content
                        AnimatedVisibility(
                            visible = state.areFiltersExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                // Activity search field
                                OutlinedTextField(
                                    value = state.searchText,
                                    onValueChange = { text ->
                                        // Using ViewModel event for text changes
                                        viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.SearchTextChanged(text))
                                    },
                                    label = { Text("Search activities") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    trailingIcon = {
                                        if (state.searchText.isNotEmpty()) {
                                            IconButton(onClick = {
                                                // Using ViewModel event to clear text
                                                viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.SearchTextChanged(""))
                                            }) {
                                                Icon(
                                                    Icons.Default.Clear,
                                                    contentDescription = "Clear"
                                                )
                                            }
                                        }
                                    }
                                )

                                // Mood dropdown
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = state.selectedMood,
                                        onValueChange = { },
                                        label = { Text("Select mood") },
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true,
                                        trailingIcon = {
                                            IconButton(onClick = {
                                                // Using ViewModel event to toggle dropdown
                                                viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.ToggleDropdown)
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowDown,
                                                    contentDescription = if (state.isDropdownExpanded)
                                                        "Close menu" else "Open menu",
                                                    modifier = Modifier.rotate(
                                                        if (state.isDropdownExpanded) 180f else 0f
                                                    )
                                                )
                                            }
                                        }
                                    )

                                    // Dropdown menu for mood options
                                    DropdownMenu(
                                        expanded = state.isDropdownExpanded,
                                        onDismissRequest = {
                                            // Using ViewModel event to close dropdown
                                            viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.ToggleDropdown)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .background(Color.White)
                                    ) {
                                        // Using the mood options from ViewModel state
                                        state.moodOptions.forEach { mood ->
                                            DropdownMenuItem(
                                                text = { Text(mood) },
                                                onClick = {
                                                    // Using ViewModel event to select mood
                                                    viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.MoodFilterSelected(mood))
                                                }
                                            )
                                        }
                                    }
                                }

                                // Clear filters button
                                Button(
                                    onClick = {
                                        // Using ViewModel event to clear all filters
                                        viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.ClearFilters)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Gray
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Clear Filters")
                                }
                            }
                        }
                    }
                }

                // Display content based on filtered entries from ViewModel state
                if (state.filteredEntries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No entries found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Try adjusting your search filters",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Button(
                                onClick = {
                                    // Using ViewModel events to clear filters and expand filter section
                                    viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.ClearFilters)
                                    viewModel.onEvent(HistoryMoodViewModel.HistoryEvent.ToggleFiltersSection)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = customPurple
                                ),
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Clear Filters")
                            }
                        }
                    }
                } else {
                    // List of mood entries using the filteredEntries from ViewModel state
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.filteredEntries) { entry ->
                            MoodCard(entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoodCard(entry: MoodEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date
            Text(
                text = entry.date,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mood and image row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Mood image
                Image(
                    painter = painterResource(id = entry.moodImageResId),
                    contentDescription = "Mood: ${entry.mood}",
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = entry.mood,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = customPurple
                )
            }

            // Activities area
            Text(
                text = "Activities:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
            )

            if (entry.activities.isEmpty()) {
                Text(
                    text = "No activities recorded",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .height(((entry.activities.size + 1) / 2 * 40).dp.coerceAtMost(120.dp))
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(entry.activities.size) { index ->
                        val activity = entry.activities[index]
                        Box(
                            modifier = Modifier
                                .background(
                                    color = customPurple.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activity,
                                fontSize = 12.sp,
                                color = customPurple,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}