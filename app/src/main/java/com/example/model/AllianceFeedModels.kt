package com.example.model

import java.util.UUID

enum class AllianceFeedEventType(val iconEmoji: String, val label: String) {
    DONATION("🪙", "Bağış"),
    TECH_UNLOCKED("🔬", "Teknoloji"),
    WAR_ALERT("⚔️", "Savaş Alarmı"),
    MEMBER_PROMOTION("🎖️", "Terfi")
}

data class AllianceFeedEvent(
    val id: String = UUID.randomUUID().toString(),
    val timestampFormatted: String,
    val authorName: String,
    val authorRole: String, // "İttifak Lideri", "General", "Kıdemli Üye", "Savaşçı"
    val eventType: AllianceFeedEventType,
    val message: String,
    val rewardText: String? = null
)

data class AllianceTechResearch(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val description: String,
    val currentPoints: Int,
    val targetPoints: Int,
    val currentLevel: Int,
    val maxLevel: Int = 10,
    val bonusDescription: String
) {
    val progressPercent: Float
        get() = (currentPoints.toFloat() / targetPoints.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)

    val isCompleted: Boolean
        get() = currentPoints >= targetPoints
}

data class AllianceDonationMilestone(
    val level: Int,
    val title: String,
    val description: String,
    val requiredTotalDonation: Int,
    val isUnlocked: Boolean,
    val rewardBuff: String
)

data class AllianceWarSchedule(
    val warId: String,
    val warTitle: String,
    val enemyAllianceName: String,
    val enemyAllianceTag: String,
    val targetSector: String,
    val scheduledTimeFormatted: String,
    val remainingMinutes: Int,
    val registeredMembers: Int,
    val maxMembers: Int = 50,
    val warStatus: String // "Kayıt Aşamasında", "Savaş Alanına Hazırlık", "Taarruz Başladı"
)

data class AllianceState(
    val allianceName: String = "TURKISH GLADIATORS",
    val allianceTag: String = "[TURK]",
    val allianceLevel: Int = 8,
    val totalMembers: Int = 47,
    val maxMembers: Int = 50,
    val totalDonationsGold: Int = 142500,
    val activeTech: AllianceTechResearch,
    val availableTechs: List<AllianceTechResearch>,
    val milestones: List<AllianceDonationMilestone>,
    val upcomingWar: AllianceWarSchedule,
    val feedEvents: List<AllianceFeedEvent>
)

object AllianceFeedCatalog {

    fun createInitialAllianceState(): AllianceState {
        val techList = listOf(
            AllianceTechResearch(
                id = "tech_composite_armor",
                title = "İttifak Kompozit Çelik & Zırh VI",
                iconEmoji = "🛡️",
                description = "Tüm ittifak üyelerinin tank ve zırhlı araçlarına kalıcı gövde takviyesi.",
                currentPoints = 38500,
                targetPoints = 50000,
                currentLevel = 6,
                maxLevel = 10,
                bonusDescription = "+%18 İttifak Zırhlı Birim Canı (HP) & Mayın Direnci"
            ),
            AllianceTechResearch(
                id = "tech_rapid_mobilization",
                title = "Hızlı Seferberlik & İntikal Doktrini",
                iconEmoji = "⚡",
                description = "Harita genelinde intikal ve stratejik hava indirme hızını artırır.",
                currentPoints = 12000,
                targetPoints = 30000,
                currentLevel = 4,
                maxLevel = 10,
                bonusDescription = "+%15 Birlik Hareket Hızı & %10 Fabrika Seri Üretim İvmesi"
            ),
            AllianceTechResearch(
                id = "tech_artillery_range",
                title = "Uydu Güdümlü Ağır Topçu Menzili",
                iconEmoji = "🎯",
                description = "Şahi ve obüs bataryalarının radar menzilini ve isabet hassasiyetini artırır.",
                currentPoints = 25000,
                targetPoints = 40000,
                currentLevel = 5,
                maxLevel = 10,
                bonusDescription = "+%25 Topçu Atış Menzili & %15 Tahkimat Hasarı"
            )
        )

        val milestones = listOf(
            AllianceDonationMilestone(
                level = 1,
                title = "Bronz İttifak Sandığı",
                description = "50,000 Altın bağış eşiği aşıldı.",
                requiredTotalDonation = 50000,
                isUnlocked = true,
                rewardBuff = "+%5 Günlük Altın ve Petrol Üretim Bonusu"
            ),
            AllianceDonationMilestone(
                level = 2,
                title = "Gümüş Savaş Cephaneliği",
                description = "100,000 Altın bağış eşiği aşıldı.",
                requiredTotalDonation = 100000,
                isUnlocked = true,
                rewardBuff = "+%8 İttifak Ordusu Taarruz Gücü"
            ),
            AllianceDonationMilestone(
                level = 3,
                title = "Altın Savunma Tahkimatı",
                description = "150,000 Altın bağış eşiği (Çok Yakında!).",
                requiredTotalDonation = 150000,
                isUnlocked = false,
                rewardBuff = "+%15 Üs Kule Savunma Sağlığı ve Hava Radar Kalkanı"
            ),
            AllianceDonationMilestone(
                level = 4,
                title = "Platin Dünya Savaşı Taarruzu",
                description = "250,000 Altın bağış eşiği.",
                requiredTotalDonation = 250000,
                isUnlocked = false,
                rewardBuff = "Özel Nükleer & Süpersonik Ağır Bombardıman Desteği"
            )
        )

        val war = AllianceWarSchedule(
            warId = "war_50v50_bosphorus",
            warTitle = "50v50 Küresel İttifak Meydan Muharebesi",
            enemyAllianceName = "RED STAR COALITION",
            enemyAllianceTag = "[RSC]",
            targetSector = "Sektör Delta-9: Hazar Petrol Havzası",
            scheduledTimeFormatted = "Bugün saat 21:00 (TSİ)",
            remainingMinutes = 42,
            registeredMembers = 44,
            maxMembers = 50,
            warStatus = "Kayıt Aşamasında (44/50 Asker Hazır)"
        )

        val events = listOf(
            AllianceFeedEvent(
                timestampFormatted = "12 dk önce",
                authorName = "Gazi_Mareşal_TR",
                authorRole = "İttifak Lideri",
                eventType = AllianceFeedEventType.WAR_ALERT,
                message = "50v50 Dünya Savaşı için Sektör Delta-9'a taarruz emri verildi. Tüm üyeler tank bataryalarını hazır etsin!",
                rewardText = "⚔️ Savaş Hazırlığı Aktif"
            ),
            AllianceFeedEvent(
                timestampFormatted = "24 dk önce",
                authorName = "AnadoluKartalı_99",
                authorRole = "Kıdemli General",
                eventType = AllianceFeedEventType.DONATION,
                message = "'İttifak Kompozit Çelik VI' araştırmasına 5,000 Altın bağışladı.",
                rewardText = "+1,250 İttifak Onur Puanı Kazandı"
            ),
            AllianceFeedEvent(
                timestampFormatted = "45 dk önce",
                authorName = "SultanMehmed_1453",
                authorRole = "General",
                eventType = AllianceFeedEventType.TECH_UNLOCKED,
                message = "'Barut Çağı Ağır Kuşatma Doktrini' Seviye 5 araştırması başarıyla tamamlandı!",
                rewardText = "Tüm İttifak: +%12 Topçu Hasarı"
            ),
            AllianceFeedEvent(
                timestampFormatted = "1 saat önce",
                authorName = "Yıldırım_Beyazit",
                authorRole = "Savaşçı",
                eventType = AllianceFeedEventType.DONATION,
                message = "İttifak hazinesine 2,000 Altın ve 500 Petrol bağışladı.",
                rewardText = "+500 İttifak Onur Puanı"
            )
        )

        return AllianceState(
            allianceName = "TURKISH GLADIATORS",
            allianceTag = "[TURK]",
            allianceLevel = 8,
            totalMembers = 47,
            maxMembers = 50,
            totalDonationsGold = 142500,
            activeTech = techList[0],
            availableTechs = techList,
            milestones = milestones,
            upcomingWar = war,
            feedEvents = events
        )
    }
}
