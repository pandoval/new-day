package com.example.newday.habit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habit_table ORDER BY name ASC")
    fun getAlphabetizedHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Query("DELETE FROM habit_table")
    suspend fun deleteAll()

    @Query("DELETE FROM habit_table WHERE name = :name")
    suspend fun deleteByName(name: String)

}