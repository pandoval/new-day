package com.example.newday.habit

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Habit::class], version = 1, exportSchema = false)
@TypeConverters(HabitConverters::class)
abstract class HabitDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao

    private class HabitDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.habitDao())
                }
            }
        }

        suspend fun populateDatabase(habitDao: HabitDao) {
            habitDao.deleteAll()
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HabitDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_database"
                ).addCallback(HabitDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}
