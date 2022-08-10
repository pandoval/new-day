package com.example.newday.habit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_table")
data class Habit(
    @PrimaryKey
    val name: String,
    val color: String,
    val completed: Boolean,
    val day: String
)