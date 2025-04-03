package com.example.moodapp.model

data class MoodEntry(
    val id:String,
    val date: String,
    val mood: String,
    val moodImageResId: Int,
    val activities: List<String>
)