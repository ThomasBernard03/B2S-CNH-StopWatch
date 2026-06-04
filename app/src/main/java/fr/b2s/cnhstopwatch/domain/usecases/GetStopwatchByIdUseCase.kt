package fr.b2s.cnhstopwatch.domain.usecases

import fr.b2s.cnhstopwatch.domain.models.Stopwatch
import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository
import kotlinx.coroutines.flow.Flow

class GetStopwatchByIdUseCase(
    private val stopwatchRepository: StopwatchRepository
) {
    operator fun invoke(id: Long): Flow<Stopwatch?> {
        return stopwatchRepository.getStopwatchById(id)
    }
}
