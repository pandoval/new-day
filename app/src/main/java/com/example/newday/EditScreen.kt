package com.example.newday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitViewModel
import com.example.newday.ui.theme.*

@Composable
fun EditScreen(habits: List<Habit>, navController: NavController, viewModel: HabitViewModel) {

    val deleteDialog = remember {
        mutableStateOf(false)
    }

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
                    IconButton(onClick = { deleteDialog.value = true }) {
                        Icon(
                            MainActivity.NewDayIcons.Delete,
                            contentDescription = "Delete all habits button"
                        )
                    }
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
                DeleteAllDialog(deleteDialog = deleteDialog, viewModel = viewModel)
            }
        }
    )
}

@Composable
fun EditableHabitList(habits: List<Habit>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        items(habits) { habit ->
            EditableHabit(habit)
        }
    }
}

@Composable
fun EditableHabit(habit: Habit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)) {

        Box(modifier = Modifier.wrapContentSize(Alignment.CenterStart)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    habit.name,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .width(92.dp),
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.End)
                ) {
                    for (i in 0 until habit.days.size) {
                            EditableHabitDayIcon(Day.getDay(i), habit.days[i])
                    }
                }
            }
        }
    }
}

@Composable
fun EditableHabitDayIcon(day: Day, selected: Boolean) {
    Text(day.abr, fontSize = 12.sp, color = if(selected) AlmostBlack else Text, modifier = Modifier
        .size(16.dp)
        .drawBehind {
            drawCircle(color = if (selected) SecondaryBlue else DarkestGray, radius = 40.dp.value)
        }, textAlign = TextAlign.Center)
}

@Composable
fun DeleteAllDialog(deleteDialog: MutableState<Boolean>, viewModel: HabitViewModel) {
    if (deleteDialog.value) {
        AlertDialog(
            onDismissRequest = { deleteDialog.value = false },
            title = { Text("Delete all habits") },
            text = { Text("This action cannot be undone") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteAll()
                    deleteDialog.value = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { deleteDialog.value = false }) {
                    Text("Cancel")
                }
            })
    }
}