package com.example.newday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.newday.habit.Habit

@Composable
fun MainScreen(habits: List<Habit>, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DayOfWeekHere") },
                actions = {
                    IconButton(onClick = { navController.navigate(MainActivity.EDIT_SCREEN) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_edit_24),
                            contentDescription = "Edit button"
                        )
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                HabitList(habits)
            }
        }
    )
}

@Composable
fun HabitList(habits: List<Habit>) {
    LazyColumn {
        items(habits) {
            Text(it.name)
        }
    }
}