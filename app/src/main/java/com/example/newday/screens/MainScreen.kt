package com.example.newday

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newday.enums.Day
import com.example.newday.habit.Habit
import com.example.newday.habit.HabitViewModel

@Composable
fun MainScreen(
    habits: List<Habit>,
    navController: NavController,
    viewModel: HabitViewModel,
    day: Day,
    dayTitle: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(dayTitle) },
                actions = {
                    IconButton(onClick = { navController.navigate(MainActivity.EDIT_SCREEN) }) {
                        Icon(
                            MainActivity.NewDayIcons.Edit,
                            contentDescription = "Edit button"
                        )
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                HabitList(habits, viewModel, day)
            }
        }
    )
}

@Composable
fun HabitList(habits: List<Habit>, viewModel: HabitViewModel, day: Day) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
    ) {
        items(habits) { habit ->
            if(habit.days[day.num]) {
                HabitListItem(habit, viewModel)
            }
        }
    }
}

@Composable
fun HabitListItem(habit: Habit, viewModel: HabitViewModel) {

    var completed by remember { mutableStateOf(habit.completed)}

    Card(shape = RoundedCornerShape(8.dp), modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
        .height(52.dp)
        .alpha(if (completed) .4f else 1f)
        .clickable {
            completed = !completed
            habit.completed = !habit.completed
            viewModel.insert(habit)
        }
        .border(1.dp, com.example.newday.ui.theme.Text, RoundedCornerShape(8.dp))) {

        Box(modifier = Modifier.wrapContentSize(Alignment.CenterStart)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    habit.name,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .width(240.dp),
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    val painter = painterResource(
                        if (completed) R.drawable.ic_baseline_check_box_24
                        else R.drawable.ic_baseline_check_box_outline_blank_24
                    )

                    Icon(painter, "${habit.name} checkbox")
                }
            }
        }
    }
}