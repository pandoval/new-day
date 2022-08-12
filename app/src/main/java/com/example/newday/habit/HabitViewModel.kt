package com.example.newday.habit

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitRepository): ViewModel() {

    val allHabits: LiveData<List<Habit>> = repository.allHabits.asLiveData()

    fun insert(habit: Habit) = viewModelScope.launch {
        repository.insert(habit)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun deleteById(id: Long) = viewModelScope.launch {
        repository.deleteById(id)
    }

    class HabitViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HabitViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


