package fr.b2s.cnhstopwatch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StopwatchDao {

    @Insert
    suspend fun insert(stopwatch: StopwatchEntity) : Long

    @Update
    suspend fun update(stopwatch: StopwatchEntity)

    @Query("SELECT * FROM stopwatches ORDER BY createdAt DESC")
    fun getAll(): Flow<List<StopwatchEntity>>

    @Query("SELECT * FROM stopwatches WHERE id = :id")
    suspend fun getById(id: Long): StopwatchEntity?

    @Query("SELECT * FROM stopwatches WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<StopwatchEntity?>
}
