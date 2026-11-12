package com.example.model

enum class NotificationSeverity {
    INFO,
    WARNING,
    CRITICAL,
    TRIUMPH
}

enum class InGameNotificationType(
    val title: String,
    val iconEmoji: String,
    val severity: NotificationSeverity,
    val soundAction: String
) {
    RESEARCH_COMPLETE(
        title = "ARAŞTIRMA TAMAMLANDI",
        iconEmoji = "🔬",
        severity = NotificationSeverity.TRIUMPH,
        soundAction = "tech_complete"
    ),
    UNIT_UNDER_ATTACK(
        title = "BİRLİK ATEŞ ALTINDA",
        iconEmoji = "⚔️",
        severity = NotificationSeverity.WARNING,
        soundAction = "combat_alert"
    ),
    BASE_UNDER_SIEGE(
        title = "ÜS KUŞATMA ALTINDA!",
        iconEmoji = "🚨",
        severity = NotificationSeverity.CRITICAL,
        soundAction = "siege_alarm"
    ),
    ERA_ADVANCED(
        title = "YENİ ÇAĞ BAŞLADI",
        iconEmoji = "👑",
        severity = NotificationSeverity.TRIUMPH,
        soundAction = "era_fanfare"
    ),
    TRADE_DELIVERED(
        title = "ASKERİ LOJİSTİK TESLİMATI",
        iconEmoji = "🚢",
        severity = NotificationSeverity.INFO,
        soundAction = "ship_horn"
    ),
    TOWER_DEFENSE_TRIGGER(
        title = "SAVUNMA KULELERİ AKTİF",
        iconEmoji = "🏰",
        severity = NotificationSeverity.WARNING,
        soundAction = "turret_fire"
    ),
    UNIT_PRODUCED(
        title = "FABRİKA ÜRETİMİ TAMAMLANDI",
        iconEmoji = "🏭",
        severity = NotificationSeverity.TRIUMPH,
        soundAction = "unit_ready"
    ),
    COMMANDER_PROMOTED(
        title = "BAŞKOMUTAN ATANDI",
        iconEmoji = "🎖️",
        severity = NotificationSeverity.TRIUMPH,
        soundAction = "officer_promoted"
    )
}

data class InGameNotification(
    val id: String,
    val type: InGameNotificationType,
    val message: String,
    val targetWorldX: Float? = null,
    val targetWorldY: Float? = null,
    val timestampMs: Long = System.currentTimeMillis(),
    val durationMs: Long = 4500L
)
