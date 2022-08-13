package com.example.newday

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitViewModel
import com.example.newday.ui.theme.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
                EditableHabitList(habits, viewModel, navController)
                DeleteAllDialog(deleteDialog = deleteDialog, viewModel = viewModel)
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditableHabitList(habits: List<Habit>, viewModel: HabitViewModel, navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.padding(vertical = 0.dp)
    ) {
        items(habits, { habit: Habit -> habit.id}) { habit ->
            val dismissState = rememberDismissState()
            if (dismissState.isDismissed(DismissDirection.EndToStart)){
                viewModel.deleteById(habit.id)
            }
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = {
                    FractionalThreshold(.15f)
                },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> TextSecondary
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> SecondaryRed
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Swipe to delete icon",
                            modifier = Modifier.scale(scale)
                        )
                    }
                },
                dismissContent = {
                    EditableHabit(habit, navController)
                }
            )
        }
    }
}

@Composable
fun EditableHabit(habit: Habit, navController: NavController) {
    Card(shape = RectangleShape, modifier = Modifier
        .fillMaxWidth()
        .height(48.dp).clickable {
            val habitString = Json.encodeToString(habit)
            navController.navigate("${MainActivity.EDIT_HABIT_SCREEN}/$habitString")
        }) {

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