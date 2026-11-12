package com.example.model

data class PassiveSkill(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val description: String,
    val combatBonusText: String,
    val speedMultiplier: Float = 1.0f,
    val damageMultiplier: Float = 1.0f,
    val healthMultiplier: Float = 1.0f,
    val productionMultiplier: Float = 1.0f
)

data class HistoricalCommander(
    val id: String,
    val name: String,
    val title: String,
    val nationTitle: String,
    val flagEmoji: String,
    val eraTitle: String,
    val avatarEmoji: String,
    val biography: String,
    val strategicDoctrine: String,
    val isUnlocked: Boolean = true,
    val unlockRequirementText: String? = null,
    val starRating: Int = 5,
    val availableSkills: List<PassiveSkill>,
    val equippedSkillIds: List<String> = emptyList() // Max 3 equipped loadout slots
)

object CommanderCatalog {

    val allPassiveSkills = listOf(
        PassiveSkill(
            id = "skill_sakarya_defense",
            name = "Hattı Müdafaa Doktrini",
            iconEmoji = "🛡️",
            description = "Tüm savunma ve siper birliklerine sarsılmaz metanet kazandırır.",
            combatBonusText = "+%20 Birlik Canı (HP) & Siper Dayanıklılığı",
            healthMultiplier = 1.20f
        ),
        PassiveSkill(
            id = "skill_siha_air_supremacy",
            name = "Hava ve SİHA Üstünlüğü",
            iconEmoji = "🦅",
            description = "Bayraktar TB3, Kızılelma ve KAAN filolarının hassas güdüm algoritması.",
            combatBonusText = "+%25 SİHA & Jet Hava Vuruş Gücü",
            damageMultiplier = 1.25f
        ),
        PassiveSkill(
            id = "skill_logistics_rush",
            name = "Milli İkmal Seferberliği",
            iconEmoji = "🏭",
            description = "Tank fabrikası ve tersanelerde seri üretim bandı optimizasyonu.",
            combatBonusText = "+%18 Fabrika Üretim Hızı",
            productionMultiplier = 1.18f
        ),
        PassiveSkill(
            id = "skill_sahi_siege",
            name = "Şahi Kuşatma Mühendisliği",
            iconEmoji = "💣",
            description = "Devasa bronz döküm toplarla surları ve tahkimatları tek atışta çökertme.",
            combatBonusText = "+%30 Ağır Kuşatma ve Topçu Hasarı",
            damageMultiplier = 1.30f
        ),
        PassiveSkill(
            id = "skill_blitzkrieg_armor",
            name = "Yıldırım Zırhlı Taarruz",
            iconEmoji = "⚡",
            description = "Paletli zırhlı unsurların motor ve itki sistemlerini aşırı yükleme.",
            combatBonusText = "+%15 Tank Hızı & Ani Darbe Hasarı",
            speedMultiplier = 1.15f
        ),
        PassiveSkill(
            id = "skill_legion_discipline",
            name = "Roma Lejyon Disiplini",
            iconEmoji = "🏛️",
            description = "Kaplumbağa (Testudo) dizilimi ile düşman okçu ve topçu ateşini sönümleme.",
            combatBonusText = "+%15 Hasar Azaltma & %10 Daha Ucuz Asker Maliyeti",
            healthMultiplier = 1.15f
        ),
        PassiveSkill(
            id = "skill_art_of_war",
            name = "Savaş Sanatı & Aldatma",
            iconEmoji = "📜",
            description = "Düşman radar menzilini kısaltır ve pusu saldırılarını güçlendirir.",
            combatBonusText = "-%35 Düşman Tespit Menzili & +%20 Kritik Pusu Hasarı",
            damageMultiplier = 1.20f
        )
    )

    fun createInitialCommanders(): List<HistoricalCommander> = listOf(
        HistoricalCommander(
            id = "cmd_ataturk",
            name = "Gazi Mareşal Mustafa Kemal",
            title = "Ebedi Başkomutan",
            nationTitle = "Türkiye / Osmanlı",
            flagEmoji = "🇹🇷",
            eraTitle = "Modern Çağ & Milli Mücadele",
            avatarEmoji = "⭐",
            biography = "Çanakkale, Sakarya ve Büyük Taarruz'un muzaffer mareşali. Modern harp doktrinini millet seferberliği, ileri teknoloji ve zırhlı manevra ile birleştirmiş askeri deha.",
            strategicDoctrine = "Hattı müdafaa yoktur, sathı müdafaa vardır. O satıh bütün vatandır.",
            isUnlocked = true,
            starRating = 5,
            availableSkills = listOf(
                allPassiveSkills[0], // Hattı Müdafaa
                allPassiveSkills[1], // SİHA Üstünlüğü
                allPassiveSkills[2]  // İkmal Seferberliği
            ),
            equippedSkillIds = listOf("skill_sakarya_defense", "skill_siha_air_supremacy", "skill_logistics_rush")
        ),
        HistoricalCommander(
            id = "cmd_fatih",
            name = "Fatih Sultan Mehmed",
            title = "Çağ Açan Hükümdar",
            nationTitle = "Osmanlı İmparatorluğu",
            flagEmoji = "🇹🇷",
            eraTitle = "Barut Çağı (15. Yüzyıl)",
            avatarEmoji = "👑",
            biography = "İstanbul'u fetheden, gemileri karadan Haliç'e indiren ve devasa Şahi topları ile sur taktiklerini kökten değiştiren vizyoner mareşal.",
            strategicDoctrine = "İmkânın sınırını görmek için imkânsızı denemek lazımdır.",
            isUnlocked = true,
            starRating = 5,
            availableSkills = listOf(
                allPassiveSkills[3], // Şahi Kuşatma
                allPassiveSkills[2], // İkmal
                allPassiveSkills[0]  // Savunma
            ),
            equippedSkillIds = listOf("skill_sahi_siege", "skill_logistics_rush")
        ),
        HistoricalCommander(
            id = "cmd_patton",
            name = "General George S. Patton",
            title = "Old Blood and Guts",
            nationTitle = "Amerika Birleşik Devletleri",
            flagEmoji = "🇺🇸",
            eraTitle = "Sanayi & 2. Dünya Savaşı",
            avatarEmoji = "🎖️",
            biography = "3. Ordu'nun agresif tank komutanı. Düşman hatlarını durmaksızın yaran zırhlı taarruz ve lojistik yakıt baskısı uzmanı.",
            strategicDoctrine = "Hücum edin, daima hücum edin ve asla durmayın!",
            isUnlocked = true,
            starRating = 4,
            availableSkills = listOf(
                allPassiveSkills[4], // Yıldırım Zırhlı Taarruz
                allPassiveSkills[2],
                allPassiveSkills[1]
            ),
            equippedSkillIds = listOf("skill_blitzkrieg_armor")
        ),
        HistoricalCommander(
            id = "cmd_caesar",
            name = "Julius Caesar",
            title = "Roma Diktatörü ve Fatih",
            nationTitle = "Roma İmparatorluğu",
            flagEmoji = "🏛️",
            eraTitle = "Bronz & Klasik Çağ",
            avatarEmoji = "🦅",
            biography = "Galya fatihi ve Roma lejyonlarının efsanevi başbuğu. Hızlı tahkimat kurma ve mühendislik taburları ile tanınır.",
            strategicDoctrine = "Veni, vidi, vici (Geldim, gördüm, yendim).",
            isUnlocked = true,
            starRating = 4,
            availableSkills = listOf(
                allPassiveSkills[5], // Lejyon Disiplini
                allPassiveSkills[0],
                allPassiveSkills[6]
            ),
            equippedSkillIds = listOf("skill_legion_discipline")
        ),
        HistoricalCommander(
            id = "cmd_suntzu",
            name = "Sun Tzu",
            title = "Stratejinin Efendisi",
            nationTitle = "Kadim Çin",
            flagEmoji = "🇨🇳",
            eraTitle = "Klasik Çağ",
            avatarEmoji = "☯️",
            biography = "'Savaş Sanatı' eserinin müellifi. Düşmanın niyetini önceden okuyarak savaşmadan kazanma sanatının kurucusu.",
            strategicDoctrine = "En büyük zafer, savaşmadan kazanılan zaferdir.",
            isUnlocked = false,
            unlockRequirementText = "Seviye 3 İttifak Teknolojisi veya 10 Skirmish Zaferi Gerekli",
            starRating = 5,
            availableSkills = listOf(
                allPassiveSkills[6], // Savaş Sanatı
                allPassiveSkills[0],
                allPassiveSkills[2]
            ),
            equippedSkillIds = emptyList()
        )
    )
}
