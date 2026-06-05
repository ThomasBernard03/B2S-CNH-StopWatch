package fr.b2s.cnhstopwatch.domain.usecases

import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository

class CreateStopwatchUseCase(
    private val stopwatchRepository: StopwatchRepository
) {
    suspend operator fun invoke(name: String) =
        stopwatchRepository.createStopwatch(name)
}
