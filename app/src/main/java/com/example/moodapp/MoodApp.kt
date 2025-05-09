package com.example.moodapp


import android.app.Application
import com.example.moodapp.utils.MoodDatabase


class MoodApp : Application() {
    // Ініціалізація БД
    val database by lazy { MoodDatabase.getDatabase(this) }
}