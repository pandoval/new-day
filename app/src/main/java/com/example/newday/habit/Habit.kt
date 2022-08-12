package com.example.newday.habit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_table")
data class Habit(
    var name: String,
    var color: String,
    var completed: Boolean,
    var days: BooleanArray,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Habit

        if (name != other.name) return false
        if (color != other.color) return false
        if (completed != other.completed) return false
        if (!days.contentEquals(other.days)) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + completed.hashCode()
        result = 31 * result + days.contentHashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}