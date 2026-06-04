package fr.b2s.cnhstopwatch.domain.usecases

import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository

class StartStopwatchUseCase(
    private val stopwatchRepository: StopwatchRepository
) {
    suspend operator fun invoke(id: Long) {
        stopwatchRepository.startStopwatch(id)
    }
}
