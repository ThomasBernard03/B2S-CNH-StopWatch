package fr.b2s.cnhstopwatch.domain.models

data class Stopwatch(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val isRunning: Boolean = false,
    val startedAt: Long = 0L,
    val accumulatedMillis: Long = 0L
)
