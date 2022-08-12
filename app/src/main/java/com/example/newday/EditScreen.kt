package com.example.newday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.newday.habit.Habit

@Composable
fun EditScreen(habits: List<Habit>, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit habits") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(MainActivity.MAIN_SCREEN) }) {
                        Icon(
                            MainActivity.NewDayIcons.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(MainActivity.ADD_HABIT_SCREEN) }) {
                        Icon(
                            MainActivity.NewDayIcons.Add,
                            contentDescription = "Add habit button"
                        )
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                EditableHabitList(habits)
            }
        }
    )
}

@Composable
fun EditableHabitList(habits: List<Habit>) {
    LazyColumn {
        items(habits) { habit ->
            EditableHabit(habit)
        }
    }
}

@Composable
fun EditableHabit(habit: Habit) {
    Row {
        Text(habit.name)

    }
}