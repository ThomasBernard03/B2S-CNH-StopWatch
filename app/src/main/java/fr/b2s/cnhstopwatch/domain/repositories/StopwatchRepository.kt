package fr.b2s.cnhstopwatch.domain.repositories

import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import kotlinx.coroutines.flow.Flow

interface StopwatchRepository {
    suspend fun createStopwatch(name: String) : Stopwatch
    suspend fun startStopwatch(id: Long)
    suspend fun stopStopwatch(id: Long)
    suspend fun resetStopwatch(id: Long)
    suspend fun deleteStopwatch(id: Long)
    fun getAllStopwatches(): Flow<List<Stopwatch>>
    fun getStopwatchById(id: Long): Flow<Stopwatch?>
}
