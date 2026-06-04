package fr.b2s.cnhstopwatch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StopwatchDao {

    @Insert
    suspend fun insert(stopwatch: StopwatchEntity)

    @Query("SELECT * FROM stopwatches ORDER BY createdAt DESC")
    fun getAll(): Flow<List<StopwatchEntity>>
}
