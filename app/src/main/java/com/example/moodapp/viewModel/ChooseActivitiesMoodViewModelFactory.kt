package com.example.moodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodapp.R
import com.example.moodapp.repository.MoodRepository
import com.example.moodapp.utils.MoodDatabase

class ChooseActivitiesMoodViewModelFactory(
    private val database: MoodDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChooseActivitiesMoodViewModel::class.java)) {
            val repository = MoodRepository(database)

            val moodStringResources = mapOf(
                "Happy" to R.string.chs_mood_icon_happy,
                "Good" to R.string.chs_mood_icon_good,
                "Neutral" to R.string.chs_mood_icon_neutral,
                "Sad" to R.string.chs_mood_icon_sad,
                "Bad" to R.string.chs_mood_icon_bad
            )

            val activityStringResources = mapOf(
                "Work" to R.string.chs_mood_activities_work,
                "Study" to R.string.chs_mood_activities_study,
                "Exercise" to R.string.chs_mood_activities_exercise,
                "Reading" to R.string.chs_mood_activities_reading,
                "Family" to R.string.chs_mood_activities_family,
                "Cooking" to R.string.chs_mood_activities_cooking,
                "Gaming" to R.string.chs_mood_activities_gaming,
                "Meditation" to R.string.chs_mood_activities_meditation,
                "Shopping" to R.string.chs_mood_activities_shopping,
                "Cleaning" to R.string.chs_mood_activities_cleaning,
                "Sleeping" to R.string.chs_mood_activities_sleeping,
                "Watching TV" to R.string.chs_mood_activities_watching_tv,
                "Traveling" to R.string.chs_mood_activities_traveling,
                "Music" to R.string.chs_mood_activities_music,
                "Art" to R.string.chs_mood_activities_art,
                "Sports" to R.string.chs_mood_activities_sport
            )

            return ChooseActivitiesMoodViewModel(
                repository = repository,
                moodStringResources = moodStringResources,
                activityStringResources = activityStringResources
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}