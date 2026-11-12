package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchReplayDao {

    @Query("SELECT * FROM match_replays ORDER BY recordedTimestamp DESC")
    fun getAllReplays(): Flow<List<MatchReplayEntity>>

    @Query("SELECT * FROM match_replays WHERE id = :id LIMIT 1")
    suspend fun getReplayById(id: String): MatchReplayEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplay(replay: MatchReplayEntity)

    @Query("DELETE FROM match_replays WHERE id = :id")
    suspend fun deleteReplayById(id: String)

    @Query("SELECT COUNT(*) FROM match_replays")
    suspend fun getReplayCount(): Int
}
