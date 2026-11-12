package com.example.model

enum class CombatLogType(val title: String, val badgeIcon: String) {
    DAMAGE_DEALT("Hasar Verildi", "💥"),
    DAMAGE_TAKEN("Hasar Alındı", "⚠️"),
    ENEMY_KILLED("Düşman İmha", "☠️"),
    FRIENDLY_LOST("Dost Zayiat", "💔"),
    CRITICAL_STRIKE("Kritik Vuruş", "⚡")
}

data class CombatEngagementLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: String,
    val attackerName: String,
    val targetName: String,
    val damage: Float,
    val isAttackerPlayer: Boolean,
    val isCasualty: Boolean,
    val type: CombatLogType,
    val weaponSystem: String = "Standart Mühimmat",
    val highlightColorHex: Long = 0xFF00F5D4
)

data class SessionCombatStats(
    val totalDamageDealt: Float = 0f,
    val totalDamageTaken: Float = 0f,
    val enemyCasualties: Int = 0,
    val friendlyCasualties: Int = 0,
    val totalEngagements: Int = 0
) {
    val killDeathRatio: Float
        get() = if (friendlyCasualties <= 0) enemyCasualties.toFloat()
        else (enemyCasualties.toFloat() / friendlyCasualties.toFloat())
}
