package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.model.LeaderboardEntryDto
import com.example.data.model.MatchHistoryRecordDto
import com.example.data.model.SeedLeaderboardData
import com.example.data.model.SkirmishChatMessageDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreRtsRepository(
    private val db: FirebaseFirestore,
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    companion object {
        private const val TAG = "FirestoreRtsRepo"
        const val COLLECTION_LEADERBOARD = "leaderboard"
        const val COLLECTION_MATCH_HISTORY = "match_history"
        const val COLLECTION_SKIRMISH_CHAT = "skirmish_chat"
    }

    // Convenience constructor: resolves the named database ID from string resources
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        FirebaseAuth.getInstance()
    )

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Observes real-time changes to the global leaderboard.
     * Returns top players ordered by totalVictoryPoints descending.
     */
    fun observeLeaderboard(): Flow<List<LeaderboardEntryDto>> = callbackFlow {
        val query = db.collection(COLLECTION_LEADERBOARD)
            .orderBy("totalVictoryPoints", Query.Direction.DESCENDING)
            .limit(30)

        var registration: ListenerRegistration? = null
        try {
            registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Leaderboard snapshot listener warning: ${error.message}. Providing local seed data.")
                    trySend(SeedLeaderboardData.sampleLeaderboard)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val entries = snapshot.toObjects(
                        LeaderboardEntryDto::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    )
                    trySend(entries)
                } else {
                    // Seed fallback so the UI always has rich player content
                    trySend(SeedLeaderboardData.sampleLeaderboard)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error attaching leaderboard listener", e)
            trySend(SeedLeaderboardData.sampleLeaderboard)
        }

        awaitClose {
            registration?.remove()
        }
    }

    /**
     * Observes match history for a specific user.
     */
    fun observeMatchHistory(userId: String): Flow<List<MatchHistoryRecordDto>> = callbackFlow {
        if (userId.isEmpty()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val query = db.collection(COLLECTION_MATCH_HISTORY)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(20)

        var registration: ListenerRegistration? = null
        try {
            registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Match history listener error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val records = snapshot?.toObjects(
                    MatchHistoryRecordDto::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ) ?: emptyList()
                trySend(records)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error attaching match history listener", e)
            trySend(emptyList())
        }

        awaitClose {
            registration?.remove()
        }
    }

    /**
     * Observes real-time skirmish chat messages & automated match events.
     */
    fun observeSkirmishChat(): Flow<List<SkirmishChatMessageDto>> = callbackFlow {
        val query = db.collection(COLLECTION_SKIRMISH_CHAT)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .limitToLast(50)

        var registration: ListenerRegistration? = null
        try {
            registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Skirmish chat listener warning: ${error.message}")
                    return@addSnapshotListener
                }

                val messages = snapshot?.toObjects(
                    SkirmishChatMessageDto::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ) ?: emptyList()
                trySend(messages)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error attaching chat listener", e)
        }

        awaitClose {
            registration?.remove()
        }
    }

    /**
     * Sends a skirmish chat message or system alert.
     */
    fun sendChatMessage(
        text: String,
        senderFaction: String,
        senderName: String,
        isSystem: Boolean = false,
        onComplete: (Boolean) -> Unit = {}
    ) {
        val user = auth.currentUser
        val uid = user?.uid ?: "commander_${senderFaction.lowercase()}"
        val name = if (user != null && !user.displayName.isNullOrBlank()) user.displayName!! else senderName

        val payload = mapOf(
            "senderId" to uid,
            "senderName" to name.take(40),
            "senderFaction" to senderFaction,
            "messageText" to text.take(300),
            "isSystemNotification" to isSystem,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection(COLLECTION_SKIRMISH_CHAT)
            .add(payload)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to send chat message", e)
                onComplete(false)
            }
    }

    /**
     * Records a completed match into Firestore match_history and updates the player's leaderboard entry.
     */
    fun recordMatchOutcome(
        record: MatchHistoryRecordDto,
        onComplete: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        val user = auth.currentUser
        val uid = user?.uid ?: record.userId.ifEmpty { "commander_local" }

        val matchPayload = mapOf(
            "userId" to uid,
            "playerName" to record.playerName.take(40),
            "playerFaction" to record.playerFaction,
            "skirmishMapTitle" to record.skirmishMapTitle,
            "isVictory" to record.isVictory,
            "totalVictoryPoints" to record.totalVictoryPoints,
            "mapControlPoints" to record.mapControlPoints,
            "resourceExtractionPoints" to record.resourceExtractionPoints,
            "unitDestructionPoints" to record.unitDestructionPoints,
            "matchDurationSeconds" to record.matchDurationSeconds,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection(COLLECTION_MATCH_HISTORY)
            .add(matchPayload)
            .addOnSuccessListener {
                // Update or create Leaderboard Entry for this user
                updateLeaderboardForUser(uid, record) { success, msg ->
                    onComplete(success, msg)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to record match history", e)
                onComplete(false, e.localizedMessage)
            }
    }

    private fun updateLeaderboardForUser(
        uid: String,
        record: MatchHistoryRecordDto,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val docRef = db.collection(COLLECTION_LEADERBOARD).document(uid)

        docRef.get().addOnSuccessListener { snapshot ->
            val existingPoints = snapshot.getLong("totalVictoryPoints") ?: 0L
            val existingWins = snapshot.getLong("matchesWon") ?: 0L
            val existingPlayed = snapshot.getLong("matchesPlayed") ?: 0L

            val newTotalPoints = existingPoints + record.totalVictoryPoints
            val newPlayed = existingPlayed + 1
            val newWins = if (record.isVictory) existingWins + 1 else existingWins
            val winRate = if (newPlayed > 0) ((newWins * 100) / newPlayed) else 0L

            val rankTitle = when {
                newTotalPoints >= 15000L -> "Yüce Başkomutan"
                newTotalPoints >= 10000L -> "Orgeneral"
                newTotalPoints >= 6000L -> "Tümgeneral"
                newTotalPoints >= 3000L -> "Tuğgeneral"
                newTotalPoints >= 1000L -> "Albay"
                else -> "Yüzbaşı"
            }

            val payload = mutableMapOf<String, Any>(
                "userId" to uid,
                "playerName" to record.playerName.take(40),
                "playerFaction" to record.playerFaction,
                "totalVictoryPoints" to newTotalPoints,
                "matchesWon" to newWins,
                "matchesPlayed" to newPlayed,
                "winRatePct" to winRate,
                "rankTitle" to rankTitle,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            if (!snapshot.exists()) {
                payload["createdAt"] = FieldValue.serverTimestamp()
            }

            docRef.set(payload)
                .addOnSuccessListener {
                    onComplete(true, "Skor başarıyla liderlik tablosuna kaydedildi!")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to update leaderboard entry", e)
                    onComplete(false, e.localizedMessage)
                }
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to fetch user leaderboard document", e)
            onComplete(false, e.localizedMessage)
        }
    }

    /**
     * Firebase Google Sign-In authentication.
     */
    fun signInWithGoogleToken(
        idToken: String,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                result.user?.let { onSuccess(it) } ?: onError("Kullanıcı verisi alınamadı.")
            }
            .addOnFailureListener { ex ->
                Log.e(TAG, "Google auth credential sign-in error", ex)
                onError(ex.localizedMessage ?: "Giriş işlemi başarısız.")
            }
    }

    fun signOut() {
        auth.signOut()
    }
}
