package fr.b2s.cnhstopwatch.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StopwatchEntity::class], version = 2, exportSchema = false)
abstract class CNHDatabase : RoomDatabase() {

    abstract fun stopwatchDao(): StopwatchDao

    companion object {
        @Volatile
        private var INSTANCE: CNHDatabase? = null

        fun getDatabase(context: Context): CNHDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CNHDatabase::class.java,
                    "cnh_stopwatch_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
