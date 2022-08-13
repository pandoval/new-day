package com.example.newday

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun EditHabitScreen(habitString: String, navController: NavController, viewModel: HabitViewModel) {

    val habit = Json.decodeFromString<Habit>(habitString)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit habit") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(MainActivity.EDIT_SCREEN) }) {
                        Icon(
                            MainActivity.NewDayIcons.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {

                        var noDays = true;

                        for(day in habit.days) {
                            if(day) {
                                noDays = false;
                            }
                        }

                        if (!noDays) {
                            viewModel.insert(habit)
                            navController.navigate(MainActivity.EDIT_SCREEN)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please select at least one day")
                            }
                        }
                    }) {
                        Icon(
                            MainActivity.NewDayIcons.Check,
                            contentDescription = "Save habit button"
                        )
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                EditHabitContent(habit)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    )
}