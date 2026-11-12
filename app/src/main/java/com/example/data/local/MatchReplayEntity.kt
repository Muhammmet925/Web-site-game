package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_replays")
data class MatchReplayEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val mapName: String,
    val factionTitle: String,
    val enemyFactionTitle: String,
    val durationSeconds: Long,
    val totalUnitsInvolved: Int,
    val isVictory: Boolean,
    val recordedTimestamp: Long = System.currentTimeMillis(),
    val framesJson: String // Serialized trajectory snapshots of all units & combat events
)

data class ReplayUnitSnapshot(
    val id: String,
    val name: String,
    val isEnemy: Boolean,
    val posX: Float,
    val posY: Float,
    val hpPct: Float,
    val unitType: String
)

data class ReplayTimelineFrame(
    val timestampSeconds: Int,
    val eventAlert: String? = null,
    val units: List<ReplayUnitSnapshot>
)
