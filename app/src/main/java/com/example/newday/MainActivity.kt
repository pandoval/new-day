package com.example.newday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitApplication
import com.example.newday.habit.HabitViewModel
import com.example.newday.ui.theme.NewDayTheme

class MainActivity : ComponentActivity() {

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModel.HabitViewModelFactory((application as HabitApplication).repository)
    }

    private lateinit var habits: MutableState<List<Habit>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar color to AlmostBlack
        this.window.statusBarColor = 0xff161511.toInt()

        setContent {

            habits = remember { mutableStateOf(listOf()) }

            val navController = rememberNavController()

            NewDayTheme {
                NavHost(navController, MAIN_SCREEN) {
                    composable(MAIN_SCREEN) {
                        MainScreen(habits.value, navController)
                    }
                    composable(EDIT_SCREEN) {
                        EditScreen(habits.value, navController)
                    }
                    composable(EDIT_HABIT_SCREEN) {
                        EditHabitScreen(habits.value, navController)
                    }
                    composable(ADD_HABIT_SCREEN) {
                        AddHabitScreen(habitViewModel, navController)
                    }
                }
            }
        }

        habitViewModel.allHabits.observe(this) { habits ->
            this.habits.value = habits
        }
    }

    companion object {
        const val MAIN_SCREEN = "main_screen"
        const val EDIT_SCREEN = "edit_screen"
        const val EDIT_HABIT_SCREEN = "edit_habit_screen"
        const val ADD_HABIT_SCREEN = "add_habit_screen"
        val NewDayIcons = Icons.Filled
    }
}