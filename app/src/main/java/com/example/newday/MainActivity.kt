package com.example.newday

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.newday.enums.Day
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitApplication
import com.example.newday.habit.HabitViewModel
import com.example.newday.quote.Quote
import com.example.newday.quote.QuoteViewModel
import com.example.newday.ui.theme.NewDayTheme
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModel.HabitViewModelFactory((application as HabitApplication).habitRepository)
    }

    private val quoteViewModel: QuoteViewModel by viewModels {
        QuoteViewModel.QuoteViewModelFactory((application as HabitApplication).quoteRepository)
    }

    private var habits: MutableState<List<Habit>>? = null

    private lateinit var dailyQuote: MutableState<Quote>

    private lateinit var day: Day
    private lateinit var dayTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar and nav bar color to AlmostBlack
        this.window.statusBarColor = 0xff161511.toInt()
        this.window.navigationBarColor = 0xff161511.toInt()

        //reset completed
        resetCompleted()

        val now = LocalDate.now()
        day = when(now.dayOfWeek.name) {
            "SUNDAY" -> Day.SUN
            "MONDAY" -> Day.MON
            "TUESDAY" -> Day.TUES
            "WEDNESDAY" -> Day.WED
            "THURSDAY" -> Day.THURS
            "FRIDAY" -> Day.FRI
            "SATURDAY" -> Day.SAT
            else -> Day.MON
        }

        dayTitle = "${day.full}, ${now.month.name.lowercase()
            .replaceFirstChar { it.titlecase(Locale.getDefault()) }} ${now.dayOfMonth}"

        setContent {

            habits = remember { mutableStateOf(habitViewModel.allHabits.value?: listOf()) }
            dailyQuote = remember { mutableStateOf(quoteViewModel.dailyQuote.value ?: Quote()) }

            val navController = rememberNavController()

            NewDayTheme {
                NavHost(navController, MAIN_SCREEN) {
                    composable(MAIN_SCREEN) {
                        MainScreen(
                            habits?.value ?: listOf(),
                            navController,
                            habitViewModel,
                            day,
                            dayTitle,
                            dailyQuote
                        )
                    }
                    composable(EDIT_SCREEN) {
                        EditScreen(habits?.value ?: listOf(), navController, habitViewModel)
                    }
                    composable("$EDIT_HABIT_SCREEN/{habit}") {
                        EditHabitScreen(
                            it.arguments?.getString("habit") ?: "",
                            navController,
                            habitViewModel
                        )
                    }
                    composable(ADD_HABIT_SCREEN) {
                        AddHabitScreen(habitViewModel, navController)
                    }
                }
            }
        }

        habitViewModel.allHabits.observe(this) { habits ->
            this.habits?.value = habits
        }

        quoteViewModel.dailyQuote.observe(this) {
            this.dailyQuote.value = it ?: Quote()
        }
    }

    companion object {
        const val MAIN_SCREEN = "main_screen"
        const val EDIT_SCREEN = "edit_screen"
        const val EDIT_HABIT_SCREEN = "edit_habit_screen"
        const val ADD_HABIT_SCREEN = "add_habit_screen"
        val NewDayIcons = Icons.Filled
    }

    private fun resetCompleted() {
        Log.d("OOGABOOGA", "send worker")
        val calendar = Calendar.getInstance()
        val nowMillis = calendar.timeInMillis

        if (calendar[Calendar.HOUR_OF_DAY] > 0 ||
            calendar[Calendar.HOUR_OF_DAY] == 0 && calendar[Calendar.MINUTE] + 1 >= 0
        ) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0

        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val diff = calendar.timeInMillis - nowMillis


        val builder = PeriodicWorkRequest.Builder(
            ResetCompletedWorker::class.java,
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS + 1,
            TimeUnit.MILLISECONDS
        ).setInitialDelay(diff, TimeUnit.MILLISECONDS)
        val request = builder.build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("reset_completed", ExistingPeriodicWorkPolicy.REPLACE, request)
    }
}