package com.example.moodapp

import com.example.moodapp.model.MoodEntry
import retrofit2.http.GET

interface ApiService {

    @GET("v3/05749646-c2f2-43dd-bb9b-10dae011221e")
    suspend fun getData(): List<MoodEntry>
}