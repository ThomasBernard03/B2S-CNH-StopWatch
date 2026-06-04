package fr.b2s.cnhstopwatch.domain.repositories

import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import kotlinx.coroutines.flow.Flow

interface StopwatchRepository {
    suspend fun createStopwatch(name: String)
    fun getAllStopwatches(): Flow<List<Stopwatch>>
}
