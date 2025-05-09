package com.example.moodapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.moodapp.utils.Converters
import java.util.UUID

@Entity(tableName = "mood_entries")
@TypeConverters(Converters::class)
data class MoodEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: String,
    val mood: String,
    val moodImageResId: String,
    val activities: List<String>
) {
    // для збереження списку активностей у БД як String
    fun activitiesToString(): String {
        return activities.joinToString(",")
    }

    companion object {
        // для відновлення списку активностей з String
        fun stringToActivities(str: String): List<String> {
            return if (str.isEmpty()) emptyList() else str.split(",")
        }
    }
}