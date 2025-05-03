package com.example.moodapp.model

data class MoodEntry(
    val id:String,
    val date: String,
    val mood: String,
    val moodImageResId: String,
    val activities: List<String>
)