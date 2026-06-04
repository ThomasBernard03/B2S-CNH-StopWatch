package fr.b2s.cnhstopwatch.domain.repositories

interface StopwatchRepository {
    suspend fun createStopwatch(name: String)
}
