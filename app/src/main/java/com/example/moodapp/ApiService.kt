package com.example.moodapp

import com.example.moodapp.model.MoodEntry
import retrofit2.http.GET

interface ApiService {

    @GET("v3/2b2ced53-00ae-420d-b727-0f1fab19a7a6")
    suspend fun getData(): List<MoodEntry>
}