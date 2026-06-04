package fr.b2s.cnhstopwatch.data.repositories

import fr.b2s.cnhstopwatch.data.local.StopwatchDao
import fr.b2s.cnhstopwatch.data.local.StopwatchEntity
import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StopwatchRepositoryImpl(
    private val stopwatchDao: StopwatchDao
) : StopwatchRepository {

    override suspend fun createStopwatch(name: String) {
        val entity = StopwatchEntity(name = name)
        stopwatchDao.insert(entity)
    }

    override fun getAllStopwatches(): Flow<List<Stopwatch>> {
        return stopwatchDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun StopwatchEntity.toDomain() = Stopwatch(
        id = id,
        name = name,
        createdAt = createdAt
    )
}
