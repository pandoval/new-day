package com.example.newday.habit

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HabitApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { HabitDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { HabitRepository(database.habitDao()) }
}