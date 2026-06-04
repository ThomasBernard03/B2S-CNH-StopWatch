package fr.b2s.cnhstopwatch.data.repositories

import fr.b2s.cnhstopwatch.data.local.StopwatchDao
import fr.b2s.cnhstopwatch.data.local.StopwatchEntity
import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository

internal class StopwatchRepositoryImpl(
    private val stopwatchDao: StopwatchDao
) : StopwatchRepository {

    override suspend fun createStopwatch(name: String) {
        val entity = StopwatchEntity(name = name)
        stopwatchDao.insert(entity)
    }
}
