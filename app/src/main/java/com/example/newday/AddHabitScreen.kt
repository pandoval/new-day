package com.example.newday

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitViewModel
import com.example.newday.ui.theme.AlmostBlack
import com.example.newday.ui.theme.DarkGray
import com.example.newday.ui.theme.DarkerGray
import com.example.newday.ui.theme.SecondaryBlue
import kotlinx.coroutines.launch

@Composable
fun AddHabitScreen(viewModel: HabitViewModel, navController: NavController) {

    val habit by remember { mutableStateOf(Habit("", "", false, BooleanArray(7)))}

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add habit") },
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

@Composable
fun EditHabitContent(habit: Habit) {

    var name by remember { mutableStateOf(habit.name)}

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            singleLine = true,
            label = { Text("Name") },
            onValueChange = {
                name = it
                habit.name = name
            })

        Text("Days of the week:", fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            EditHabitDaySelectable(habit, Day.SUN)
            EditHabitDaySelectable(habit, Day.MON)
            EditHabitDaySelectable(habit, Day.TUES)
            EditHabitDaySelectable(habit, Day.WED)
            EditHabitDaySelectable(habit, Day.THURS)
            EditHabitDaySelectable(habit, Day.FRI)
            EditHabitDaySelectable(habit, Day.SAT)
        }
    }
}

@Composable
fun EditHabitDaySelectable(habit: Habit, day: Day) {

    var selected by remember {
        mutableStateOf(habit.days[day.num])
    }

    Button(
        onClick = {
            if(!selected) {
                habit.days[day.num] = true
                selected = true;
            } else {
                habit.days[day.num] = false
                selected = false;
            }
        },
        modifier = Modifier.size(45.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) SecondaryBlue else DarkGray
        )
    ) {
        Text(day.abr, color = if(selected) AlmostBlack else com.example.newday.ui.theme.Text)
    }
}