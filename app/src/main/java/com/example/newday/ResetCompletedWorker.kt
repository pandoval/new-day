package com.example.newday

import android.content.Context
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.newday.habit.HabitApplication
import com.example.newday.habit.HabitDatabase
import com.example.newday.habit.HabitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ResetCompletedWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        Log.d("OOGABOOGA", "Doing work")

        val dao = (applicationContext as HabitApplication).database.habitDao()

        (applicationContext as HabitApplication).applicationScope.launch(Dispatchers.IO) {

            for(habit in dao.getAlphabetizedHabits().first()) {
                Log.d("OOGABOOGA", "Go through habits")
                if(habit.completed) {
                    habit.completed = false
                    (applicationContext as HabitApplication).applicationScope.launch {
                        dao.insert(habit)
                    }
                }
            }
        }

        return Result.success()
    }
}