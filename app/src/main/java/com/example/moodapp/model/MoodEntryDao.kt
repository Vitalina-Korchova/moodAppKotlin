package com.example.moodapp.model

import androidx.room.*
import com.example.moodapp.model.MoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodEntry: MoodEntry)

    @Delete
    suspend fun delete(moodEntry: MoodEntry)

    @Update
    suspend fun update(moodEntry: MoodEntry)

    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getById(id: String): MoodEntry?

    @Query("SELECT * FROM mood_entries ORDER BY date DESC")
    fun getAll(): Flow<List<MoodEntry>>
}