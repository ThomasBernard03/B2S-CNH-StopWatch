package fr.b2s.cnhstopwatch.domain.usecases

import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository
import kotlinx.coroutines.flow.Flow

class GetAllStopwatchesUseCase(
    private val stopwatchRepository: StopwatchRepository
) {
    operator fun invoke(): Flow<List<Stopwatch>> {
        return stopwatchRepository.getAllStopwatches()
    }
}
