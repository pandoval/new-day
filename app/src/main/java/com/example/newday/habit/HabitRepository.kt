package com.example.newday.habit

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allHabits: Flow<List<Habit>> = habitDao.getAlphabetizedHabits()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(habit: Habit) {
        habitDao.insert(habit)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        habitDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id: Long) {
        habitDao.deleteById(id)
    }
}