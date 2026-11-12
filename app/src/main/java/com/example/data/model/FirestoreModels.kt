package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

/**
 * Firestore DTO for Global Leaderboard.
 * Collection: /leaderboard/{userId}
 */
@IgnoreExtraProperties
data class LeaderboardEntryDto(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val playerName: String = "",
    val playerFaction: String = "",
    val totalVictoryPoints: Long = 0L,
    val matchesWon: Long = 0L,
    val matchesPlayed: Long = 0L,
    val winRatePct: Long = 0L,
    val rankTitle: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)

/**
 * Firestore DTO for Match History records.
 * Collection: /match_history/{matchId}
 */
@IgnoreExtraProperties
data class MatchHistoryRecordDto(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val playerName: String = "",
    val playerFaction: String = "",
    val skirmishMapTitle: String = "",
    val isVictory: Boolean = true,
    val totalVictoryPoints: Long = 0L,
    val mapControlPoints: Long = 0L,
    val resourceExtractionPoints: Long = 0L,
    val unitDestructionPoints: Long = 0L,
    val matchDurationSeconds: Long = 0L,
    @ServerTimestamp
    val createdAt: Timestamp? = null
)

/**
 * Firestore DTO for Skirmish Multiplayer Chat & Events.
 * Collection: /skirmish_chat/{messageId}
 */
@IgnoreExtraProperties
data class SkirmishChatMessageDto(
    @DocumentId
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderFaction: String = "",
    val messageText: String = "",
    val isSystemNotification: Boolean = false,
    @ServerTimestamp
    val createdAt: Timestamp? = null
)

object SeedLeaderboardData {
    val sampleLeaderboard = listOf(
        LeaderboardEntryDto(
            id = "seed_1",
            userId = "user_gazi",
            playerName = "Gazi Mareşal",
            playerFaction = "Türkiye",
            totalVictoryPoints = 18450L,
            matchesWon = 48L,
            matchesPlayed = 52L,
            winRatePct = 92L,
            rankTitle = "Yüce Başkomutan"
        ),
        LeaderboardEntryDto(
            id = "seed_2",
            userId = "user_bradley",
            playerName = "General Bradley",
            playerFaction = "ABD",
            totalVictoryPoints = 16200L,
            matchesWon = 42L,
            matchesPlayed = 49L,
            winRatePct = 85L,
            rankTitle = "Orgeneral"
        ),
        LeaderboardEntryDto(
            id = "seed_3",
            userId = "user_kuznetsov",
            playerName = "Amiral Kuznetsov",
            playerFaction = "Rusya",
            totalVictoryPoints = 14900L,
            matchesWon = 39L,
            matchesPlayed = 47L,
            winRatePct = 82L,
            rankTitle = "Donanma Amirali"
        ),
        LeaderboardEntryDto(
            id = "seed_4",
            userId = "user_long",
            playerName = "General Long",
            playerFaction = "Çin",
            totalVictoryPoints = 13800L,
            matchesWon = 36L,
            matchesPlayed = 45L,
            winRatePct = 80L,
            rankTitle = "Korgeneral"
        ),
        LeaderboardEntryDto(
            id = "seed_5",
            userId = "user_rommel",
            playerName = "Panzerkommandant",
            playerFaction = "Almanya",
            totalVictoryPoints = 12400L,
            matchesWon = 31L,
            matchesPlayed = 41L,
            winRatePct = 75L,
            rankTitle = "Tümgeneral"
        ),
        LeaderboardEntryDto(
            id = "seed_6",
            userId = "user_aybars",
            playerName = "Aybars Taktikçi",
            playerFaction = "Türkiye",
            totalVictoryPoints = 11150L,
            matchesWon = 28L,
            matchesPlayed = 38L,
            winRatePct = 73L,
            rankTitle = "Tuğgeneral"
        )
    )
}
