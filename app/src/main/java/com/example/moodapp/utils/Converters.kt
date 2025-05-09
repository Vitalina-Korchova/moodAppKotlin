package com.example.moodapp.utils

import androidx.room.TypeConverter
import com.example.moodapp.model.MoodEntry

class Converters {
    @TypeConverter
    fun fromActivitiesList(activities: List<String>): String {
        return activities.joinToString(",")
    }

    @TypeConverter
    fun toActivitiesList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}