package com.example.data.local

import org.json.JSONArray
import org.json.JSONObject

object ReplayJsonSerializer {

    fun serializeFrames(frames: List<ReplayTimelineFrame>): String {
        val root = JSONArray()
        for (f in frames) {
            val fObj = JSONObject()
            fObj.put("time", f.timestampSeconds)
            if (f.eventAlert != null) {
                fObj.put("alert", f.eventAlert)
            }
            val unitsArr = JSONArray()
            for (u in f.units) {
                val uObj = JSONObject()
                uObj.put("id", u.id)
                uObj.put("name", u.name)
                uObj.put("enemy", u.isEnemy)
                uObj.put("x", u.posX.toDouble())
                uObj.put("y", u.posY.toDouble())
                uObj.put("hp", u.hpPct.toDouble())
                uObj.put("type", u.unitType)
                unitsArr.put(uObj)
            }
            fObj.put("units", unitsArr)
            root.put(fObj)
        }
        return root.toString()
    }

    fun deserializeFrames(jsonString: String): List<ReplayTimelineFrame> {
        val list = mutableListOf<ReplayTimelineFrame>()
        try {
            val root = JSONArray(jsonString)
            for (i in 0 until root.length()) {
                val fObj = root.getJSONObject(i)
                val time = fObj.optInt("time", 0)
                val alert = if (fObj.has("alert")) fObj.getString("alert") else null
                val unitsArr = fObj.optJSONArray("units")
                val units = mutableListOf<ReplayUnitSnapshot>()
                if (unitsArr != null) {
                    for (j in 0 until unitsArr.length()) {
                        val uObj = unitsArr.getJSONObject(j)
                        units.add(
                            ReplayUnitSnapshot(
                                id = uObj.optString("id", "u_$j"),
                                name = uObj.optString("name", "Birlik"),
                                isEnemy = uObj.optBoolean("enemy", false),
                                posX = uObj.optDouble("x", 200.0).toFloat(),
                                posY = uObj.optDouble("y", 200.0).toFloat(),
                                hpPct = uObj.optDouble("hp", 1.0).toFloat(),
                                unitType = uObj.optString("type", "TANK")
                            )
                        )
                    }
                }
                list.add(ReplayTimelineFrame(time, alert, units))
            }
        } catch (_: Exception) {}
        return list
    }

    fun createSampleReplayFrames(isVictory: Boolean): List<ReplayTimelineFrame> {
        val frames = mutableListOf<ReplayTimelineFrame>()
        val totalSeconds = 24 // 24-step simulation playback

        for (sec in 0..totalSeconds) {
            val t = sec.toFloat() / totalSeconds.toFloat()
            val alert = when (sec) {
                3 -> "⚔️ İlk Temas: Keşif kolu düşman tanklarını tespit etti"
                8 -> "💥 Ağır Kuşatma: Sektör B-2'de tank çatışması şiddetlendi"
                14 -> "✈️ Hava Desteği: 5. Nesil KAAN / F-22 hava sahasına girdi"
                20 -> if (isVictory) "🏆 Düşman karargahı vuruldu, zafer yakın!" else "⚠️ Üs savunması yarıldı!"
                else -> null
            }

            // Player units moving from west to east
            val playerTankX = 200f + t * 450f
            val playerTankY = 260f + kotlin.math.sin(t * 3.14f) * 40f
            val playerJetX = 150f + t * 700f
            val playerJetY = 180f + kotlin.math.cos(t * 3.14f) * 60f

            // Enemy units
            val enemyTankX = 850f - t * 300f
            val enemyTankY = 290f - kotlin.math.sin(t * 3.14f) * 30f

            val units = listOf(
                ReplayUnitSnapshot(
                    id = "p_tank_1",
                    name = "Altay T2",
                    isEnemy = false,
                    posX = playerTankX,
                    posY = playerTankY,
                    hpPct = (1.0f - t * 0.3f).coerceIn(0.2f, 1f),
                    unitType = "TANK"
                ),
                ReplayUnitSnapshot(
                    id = "p_jet_1",
                    name = "KAAN Hayalet Jet",
                    isEnemy = false,
                    posX = playerJetX,
                    posY = playerJetY,
                    hpPct = 0.95f,
                    unitType = "JET"
                ),
                ReplayUnitSnapshot(
                    id = "e_tank_1",
                    name = "Düşman T-90M",
                    isEnemy = true,
                    posX = enemyTankX,
                    posY = enemyTankY,
                    hpPct = if (isVictory) (1.0f - t * 0.95f).coerceAtLeast(0f) else (1.0f - t * 0.4f),
                    unitType = "TANK"
                )
            )

            frames.add(ReplayTimelineFrame(sec * 10, alert, units))
        }

        return frames
    }
}
