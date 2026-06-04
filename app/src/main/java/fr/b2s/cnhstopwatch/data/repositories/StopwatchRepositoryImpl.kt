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

    override suspend fun startStopwatch(id: Long) {
        val entity = stopwatchDao.getById(id) ?: return
        val updated = entity.copy(
            isRunning = true,
            startedAt = System.currentTimeMillis()
        )
        stopwatchDao.update(updated)
    }

    override suspend fun stopStopwatch(id: Long) {
        val entity = stopwatchDao.getById(id) ?: return
        val updated = entity.copy(
            isRunning = false,
            accumulatedMillis = entity.accumulatedMillis + (System.currentTimeMillis() - entity.startedAt),
            startedAt = 0L
        )
        stopwatchDao.update(updated)
    }

    override suspend fun resetStopwatch(id: Long) {
        val entity = stopwatchDao.getById(id) ?: return
        val updated = entity.copy(
            isRunning = false,
            startedAt = 0L,
            accumulatedMillis = 0L
        )
        stopwatchDao.update(updated)
    }

    override fun getAllStopwatches(): Flow<List<Stopwatch>> {
        return stopwatchDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStopwatchById(id: Long): Flow<Stopwatch?> {
        return stopwatchDao.getByIdFlow(id).map { it?.toDomain() }
    }

    private fun StopwatchEntity.toDomain() = Stopwatch(
        id = id,
        name = name,
        createdAt = createdAt,
        isRunning = isRunning,
        startedAt = startedAt,
        accumulatedMillis = accumulatedMillis
    )
}
