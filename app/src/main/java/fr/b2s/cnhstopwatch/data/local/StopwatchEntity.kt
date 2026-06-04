package fr.b2s.cnhstopwatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopwatches")
data class StopwatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isRunning: Boolean = false,
    val startedAt: Long = 0L,
    val accumulatedMillis: Long = 0L
)