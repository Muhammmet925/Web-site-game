package com.example.model

/**
 * Tactical Group Commands executable on a control group of military units
 */
enum class TacticalGroupCommand(
    val title: String,
    val iconEmoji: String,
    val description: String
) {
    ATTACK_MOVE(
        title = "Taarruz",
        iconEmoji = "⚔️",
        description = "Düşman üssüne doğru agresif taarruza geç"
    ),
    HOLD_POSITION(
        title = "Siper Al",
        iconEmoji = "🛡️",
        description = "Mevcut konumda dur ve alanı savun"
    ),
    RETREAT_TO_HQ(
        title = "Üsse Dön",
        iconEmoji = "🏠",
        description = "Komuta Merkezine geri çekil"
    ),
    SCATTER(
        title = "Toplu Dağıl",
        iconEmoji = "💥",
        description = "Hava/topçu saldırısından kaçmak için saçıl"
    )
}

/**
 * Data model for a player-defined tactical control group (Quick Selection & Squad Controls)
 */
data class TacticalControlGroup(
    val id: Int,
    val name: String,
    val unitIds: Set<String> = emptySet(),
    val iconEmoji: String = "🛡️",
    val accentColorHex: Long = 0xFF00F5D4
) {
    fun getAliveUnits(allUnits: List<RtsUnit>): List<RtsUnit> {
        return allUnits.filter { unitIds.contains(it.id) && it.hp > 0 && it.isPlayerSide }
    }

    fun getAverageHpPercent(allUnits: List<RtsUnit>): Float {
        val alive = getAliveUnits(allUnits)
        if (alive.isEmpty()) return 0f
        val totalHp = alive.sumOf { it.hp.toDouble() }
        val totalMaxHp = alive.sumOf { it.maxHp.toDouble() }
        return if (totalMaxHp > 0) (totalHp / totalMaxHp).toFloat().coerceIn(0f, 1f) else 0f
    }

    fun getCompositionSummary(allUnits: List<RtsUnit>): String {
        val alive = getAliveUnits(allUnits)
        if (alive.isEmpty()) return "Boş"
        val counts = alive.groupingBy { it.unitType }.eachCount()
        return counts.entries.joinToString(" • ") { (type, count) ->
            val icon = when (type) {
                UnitType.TANK -> "🚜"
                UnitType.JET -> "✈️"
                UnitType.SUBMARINE -> "🚢"
                UnitType.INFANTRY -> "🪖"
                UnitType.SIEGE -> "🏹"
            }
            "$icon$count"
        }
    }
}

object TacticalControlGroupCatalog {
    fun createDefaultGroups(): List<TacticalControlGroup> = listOf(
        TacticalControlGroup(id = 1, name = "Grup 1", iconEmoji = "🛡️", accentColorHex = 0xFF00F5D4),
        TacticalControlGroup(id = 2, name = "Grup 2", iconEmoji = "⚡", accentColorHex = 0xFFFFB703),
        TacticalControlGroup(id = 3, name = "Grup 3", iconEmoji = "✈️", accentColorHex = 0xFF38BDF8),
        TacticalControlGroup(id = 4, name = "Grup 4", iconEmoji = "🎯", accentColorHex = 0xFFFF453A),
        TacticalControlGroup(id = 5, name = "Grup 5", iconEmoji = "🚢", accentColorHex = 0xFFA78BFA)
    )
}
