package com.example.newday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.newday.habit.Habit

@Composable
fun EditHabitScreen(habits: List<Habit>, navController: NavController, adding: Boolean = false) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (adding) "Add Habit" else "Edit habit") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(MainActivity.MAIN_SCREEN) }) {
                        Icon(
                            MainActivity.NewDayIcons.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(MainActivity.EDIT_SCREEN) }) {
                        Icon(
                            MainActivity.NewDayIcons.Check,
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