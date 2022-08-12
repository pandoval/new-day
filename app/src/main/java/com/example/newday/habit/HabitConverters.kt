package com.example.newday.habit

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HabitConverters {
    @TypeConverter
    fun fromBoolArray(value : BooleanArray) = Json.encodeToString(value)

    @TypeConverter
    fun toBoolArray(value: String) = Json.decodeFromString<BooleanArray>(value)
}