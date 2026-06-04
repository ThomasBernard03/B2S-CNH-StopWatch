package fr.b2s.cnhstopwatch.domain.usecases

import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository

class StopStopwatchUseCase(
    private val stopwatchRepository: StopwatchRepository
) {
    suspend operator fun invoke(id: Long) {
        stopwatchRepository.stopStopwatch(id)
    }
}
