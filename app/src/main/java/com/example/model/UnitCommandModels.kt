package com.example.model

/**
 * Combat & operational stance of an individual military unit
 */
enum class UnitStance(val title: String, val badge: String) {
    AGGRESSIVE("Taarruz", "⚔️"),
    PATROL("Devriye", "🔄"),
    GUARD("Muhafız", "🛡️"),
    REPAIRING("Onarım", "🔧"),
    HOLD_FIRE("Ateş Kes", "🤫")
}

/**
 * Specific context menu commands executable on an individual unit
 */
enum class UnitTacticalCommand(
    val title: String,
    val iconEmoji: String,
    val description: String,
    val allowedUnitTypes: Set<UnitType>
) {
    PATROL(
        title = "Devriye",
        iconEmoji = "🔄",
        description = "Mevcut koordinat ile ileri nokta arasında otomatik devriye turu atar.",
        allowedUnitTypes = setOf(UnitType.TANK, UnitType.JET, UnitType.SUBMARINE, UnitType.INFANTRY)
    ),
    GUARD(
        title = "Muhafız",
        iconEmoji = "🛡️",
        description = "Belirlenen alanı ve yakındaki stratejik tesisleri korur.",
        allowedUnitTypes = setOf(UnitType.TANK, UnitType.JET, UnitType.SUBMARINE, UnitType.INFANTRY, UnitType.SIEGE)
    ),
    REPAIR(
        title = "Tamir Et",
        iconEmoji = "🔧",
        description = "Saha bakım kitini aktive ederek zırh ve gövde canını yeniler.",
        allowedUnitTypes = setOf(UnitType.TANK, UnitType.SUBMARINE, UnitType.SIEGE)
    ),
    HOLD_FIRE(
        title = "Ateş Kes",
        iconEmoji = "🤫",
        description = "Ateş açmaz; gizlilik moduna girerek pusu ve keşif yapar.",
        allowedUnitTypes = setOf(UnitType.SUBMARINE, UnitType.JET, UnitType.INFANTRY, UnitType.TANK)
    );

    fun isAvailableFor(type: UnitType): Boolean = allowedUnitTypes.contains(type)
}
