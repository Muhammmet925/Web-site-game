package com.example.data.local

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MatchReplayRepository(private val dao: MatchReplayDao) {

    val allReplays: Flow<List<MatchReplayEntity>> = dao.getAllReplays()

    suspend fun getReplay(id: String): MatchReplayEntity? = dao.getReplayById(id)

    suspend fun saveReplay(replay: MatchReplayEntity) = dao.insertReplay(replay)

    suspend fun deleteReplay(id: String) = dao.deleteReplayById(id)

    suspend fun seedInitialReplaysIfEmpty() {
        val count = dao.getReplayCount()
        if (count == 0) {
            val sampleReplay1 = MatchReplayEntity(
                id = "replay_hurmuz_victory",
                title = "Hürmüz Boğazı Taktik Zaferi",
                mapName = "Hürmüz Boğazı & Basra Körfezi",
                factionTitle = "Türkiye",
                enemyFactionTitle = "Suriye & Rus Paktı",
                durationSeconds = 240L,
                totalUnitsInvolved = 18,
                isVictory = true,
                recordedTimestamp = System.currentTimeMillis() - 3600000L,
                framesJson = ReplayJsonSerializer.serializeFrames(
                    ReplayJsonSerializer.createSampleReplayFrames(isVictory = true)
                )
            )

            val sampleReplay2 = MatchReplayEntity(
                id = "replay_danube_clash",
                title = "Tuna Nehri Savunma Hattı",
                mapName = "Tuna Nehri & Karpat Dağları",
                factionTitle = "Türkiye",
                enemyFactionTitle = "Koalisyon Güçleri",
                durationSeconds = 180L,
                totalUnitsInvolved = 14,
                isVictory = false,
                recordedTimestamp = System.currentTimeMillis() - 7200000L,
                framesJson = ReplayJsonSerializer.serializeFrames(
                    ReplayJsonSerializer.createSampleReplayFrames(isVictory = false)
                )
            )

            dao.insertReplay(sampleReplay1)
            dao.insertReplay(sampleReplay2)
        }
    }
}
