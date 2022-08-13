package com.example.newday.habit

import android.app.Application
import com.example.newday.quote.QuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HabitApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { HabitDatabase.getDatabase(this,applicationScope) }
    val habitRepository by lazy { HabitRepository(database.habitDao()) }
    val quoteRepository by lazy { QuoteRepository(applicationScope) }
}