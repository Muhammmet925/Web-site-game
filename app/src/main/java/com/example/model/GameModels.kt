package com.example.model

enum class Era(
    val title: String,
    val yearTag: String,
    val upgradeCostGold: Int,
    val upgradeCostTech: Int,
    val description: String
) {
    MEDIEVAL_5TH(
        title = "5. Yüzyıl (Orta Çağ Başlangıcı)",
        yearTag = "MS 400 - 1000",
        upgradeCostGold = 0,
        upgradeCostTech = 0,
        description = "Kılıç, ok, mancınık ve atlı akıncıların çağı. Feodal kale yapıları ve ham cevher madenciliği."
    ),
    CLASSICAL_GUNPOWDER(
        title = "Klasik & Barut Dönemi",
        yearTag = "14. - 18. Yüzyıl",
        upgradeCostGold = 800,
        upgradeCostTech = 400,
        description = "Barutun keşfi, Şahi kuşatma topları, tüfekli yeniçeriler ve zırhlı kalyonlar."
    ),
    INDUSTRIAL(
        title = "Endüstri Devrimi",
        yearTag = "19. - 20. Yüzyıl (I. & II. Dünya Savaşı)",
        upgradeCostGold = 1800,
        upgradeCostTech = 1000,
        description = "Buhar, çelik, seri üretim tank fabrikaları, ilk askeri uçaklar ve dizel denizaltılar."
    ),
    MODERN(
        title = "Modern Çağ (Modern Warfare)",
        yearTag = "21. Yüzyıl & Gelecek",
        upgradeCostGold = 3500,
        upgradeCostTech = 2200,
        description = "5. Nesil radara yakalanmayan jetler (F-22, Su-57), ana muharebe tankları (M1 Abrams, Altay), SİHA'lar ve nükleer denizaltılar."
    )
}

enum class Nation(
    val title: String,
    val flagEmoji: String,
    val specialty: String,
    val uniqueTankName: String,
    val uniqueJetName: String,
    val uniqueSubmarineName: String
) {
    TURKEY(
        title = "Türkiye",
        flagEmoji = "🇹🇷",
        specialty = "SİHA Hava Harekâtı & Hızlı Zırhlı Manevra",
        uniqueTankName = "Altay T2",
        uniqueJetName = "KAAN / Bayraktar KIZILELMA",
        uniqueSubmarineName = "Reis Sınıfı (AIP TCG Piri Reis)"
    ),
    USA(
        title = "ABD",
        flagEmoji = "🇺🇸",
        specialty = "Hava Üstünlüğü & Yüksek Teknoloji Lojistiği",
        uniqueTankName = "M1A2 Abrams SEPv3",
        uniqueJetName = "F-22 Raptor / F-35 Lightning",
        uniqueSubmarineName = "Virginia Sınıfı Nükleer Denizaltı"
    ),
    RUSSIA(
        title = "Rusya",
        flagEmoji = "🇷🇺",
        specialty = "Ağır Zırh Kalkanı & Yoğun Roket/Topçu Barajı",
        uniqueTankName = "T-90M Proryv / T-14 Armata",
        uniqueJetName = "Su-57 Felon / Su-35",
        uniqueSubmarineName = "Yasen-M Sınıfı Taarruz Denizaltısı"
    ),
    CHINA(
        title = "Çin",
        flagEmoji = "🇨🇳",
        specialty = "Seri Üretim İnsan Gücü & EMP / Hipersonik Füzeler",
        uniqueTankName = "Type 99A2",
        uniqueJetName = "J-20 Mighty Dragon",
        uniqueSubmarineName = "Type 093B Shang Sınıfı"
    )
}

enum class UnitType(val title: String, val categoryIcon: String) {
    INFANTRY("Piyade / Özel Kuvvetler", "Person"),
    TANK("Zırhlı Birlik / Tank", "CarRepair"),
    JET("Hava Kuvvetleri / Jet", "AirplanemodeActive"),
    SUBMARINE("Deniz Kuvvetleri / Denizaltı", "DirectionsBoat"),
    SIEGE("Kuşatma / Ağır Topçu", "Hardware")
}

data class RtsUnitTemplate(
    val id: String,
    val name: String,
    val nation: Nation,
    val era: Era,
    val unitType: UnitType,
    val costGold: Int,
    val productionTimeSeconds: Int,
    val maxHp: Float,
    val attackPower: Float,
    val speed: Float,
    val description: String,
    val modernEquivalentId: String? = null
)

data class ProductionOrder(
    val orderId: String,
    val unitTemplate: RtsUnitTemplate,
    var timeRemaining: Float,
    val totalTime: Float
)

enum class UnitRank(
    val title: String,
    val badgeSymbol: String,
    val bonusDamagePct: Int,
    val bonusHpPct: Int,
    val requiredXp: Int
) {
    RECRUIT("Acemi", "★", 0, 0, 0),
    VETERAN("Kıdemli", "★★", 15, 10, 100),
    ELITE("Seçkin", "★★★", 30, 20, 250),
    HEROIC("Kahraman", "★★★★", 50, 35, 500);

    val nextRank: UnitRank?
        get() = when (this) {
            RECRUIT -> VETERAN
            VETERAN -> ELITE
            ELITE -> HEROIC
            HEROIC -> null
        }
}

data class RtsUnit(
    val id: String,
    val templateId: String,
    val name: String,
    val nation: Nation,
    var era: Era,
    val unitType: UnitType,
    var hp: Float,
    var maxHp: Float,
    var attackPower: Float,
    var speed: Float,
    var posX: Float,
    var posY: Float,
    var targetX: Float,
    var targetY: Float,
    var isSelected: Boolean = false,
    val isPlayerSide: Boolean = true,
    var isMoving: Boolean = false,
    var isAttacking: Boolean = false,
    var xp: Int = 0,
    var rank: UnitRank = UnitRank.RECRUIT,
    var killsCount: Int = 0,
    var baseAttackPower: Float = attackPower,
    var baseMaxHp: Float = maxHp,
    var activeStance: UnitStance = UnitStance.AGGRESSIVE,
    var isHoldingFire: Boolean = false,
    var isRepairing: Boolean = false,
    var patrolOriginX: Float = posX,
    var patrolOriginY: Float = posY,
    var patrolTargetX: Float = targetX,
    var patrolTargetY: Float = targetY,
    var isPatrolHeadingForward: Boolean = true
) {
    val rankBadge: String get() = rank.badgeSymbol

    fun gainXp(amount: Int): Boolean {
        xp += amount
        val oldRank = rank
        val newRank = when {
            xp >= UnitRank.HEROIC.requiredXp -> UnitRank.HEROIC
            xp >= UnitRank.ELITE.requiredXp -> UnitRank.ELITE
            xp >= UnitRank.VETERAN.requiredXp -> UnitRank.VETERAN
            else -> UnitRank.RECRUIT
        }
        if (newRank != oldRank) {
            rank = newRank
            attackPower = baseAttackPower * (1f + newRank.bonusDamagePct / 100f)
            val oldMax = maxHp
            maxHp = baseMaxHp * (1f + newRank.bonusHpPct / 100f)
            hp = (hp + (maxHp - oldMax)).coerceAtMost(maxHp)
            return true // Leveled up!
        }
        return false
    }
}

enum class BattleLogCategory(val title: String, val icon: String) {
    ALL("Tümü", "📋"),
    COMBAT("Çatışma", "⚔️"),
    BUILDING("İnşaat", "🏗️"),
    TERRITORY("Bölge", "🌍"),
    TRADE("Ticaret", "🚢"),
    RANK("Terfi", "⭐")
}

data class BattleLogEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val timestamp: String,
    val category: BattleLogCategory,
    val message: String,
    val highlightColorHex: Long = 0xFF00E5FF
)

data class GlobalTradeDeal(
    val dealId: String,
    val sellerNationName: String,
    val sellerFlag: String,
    val unitName: String,
    val unitType: UnitType,
    val costGold: Int,
    val costOil: Int,
    var deliveryTimeRemaining: Float,
    val totalDeliveryTime: Float,
    val attackPower: Float,
    val hp: Float,
    val stealthMultiplier: Float = 1.0f
)

data class DiplomaticNation(
    val id: String,
    val name: String,
    val flagEmoji: String,
    val militaryRank: Int,
    var relationshipScore: Int, // -100 to +100
    val mapCoordsVector3: String,
    val eraSpecialty: String,
    val exportUnitName: String,
    val exportUnitType: UnitType,
    val exportWeaponSystem: String,
    val exportCostGold: Int,
    val exportCostOil: Int,
    val exportProductionTime: Int,
    val exportAttackPower: Float,
    val exportHp: Float,
    val stealthMultiplier: Float = 1.0f
) {
    val isAllianceReady: Boolean
        get() = relationshipScore >= 50

    val relationshipStatus: String
        get() = when {
            relationshipScore >= 75 -> "Stratejik Müttefik (+75)"
            relationshipScore >= 50 -> "Dost Ticari Ortak (+50)"
            relationshipScore >= 0 -> "Tarafsız (0)"
            relationshipScore >= -50 -> "Gergin İlişkiler (-25)"
            else -> "Düşman / Ambargo (-75)"
        }
}

enum class RtsBuildingType(
    val title: String,
    val baseCostGold: Int,
    val energyCost: Int, // positive consumes, negative produces
    val description: String,
    val allowedUnitTypes: List<UnitType>
) {
    HQ(
        title = "Komuta Merkezi",
        baseCostGold = 500,
        energyCost = 20,
        description = "Üssün operasyon merkezi. Dönem geçişi (Era Upgrade) burada tetiklenir.",
        allowedUnitTypes = emptyList()
    ),
    POWER_PLANT(
        title = "Soğuk Füzyon Santrali",
        baseCostGold = 250,
        energyCost = -150, // Generates 150 MW
        description = "Tüm üssün radar ve üretim binalarına enerji sağlar.",
        allowedUnitTypes = emptyList()
    ),
    SUPPLY_DEPOT(
        title = "Tedarik & Lojistik Deposu",
        baseCostGold = 200,
        energyCost = 15,
        description = "Tedarik kamyonları ve toplayıcılar buraya kaynak aktararak saniyede sabit Altın sağlar.",
        allowedUnitTypes = emptyList()
    ),
    BARRACKS(
        title = "Kışla (Barracks)",
        baseCostGold = 180,
        energyCost = 15,
        description = "Piyadeler, keskin nişancılar ve anti-tank roket timlerini eğitir.",
        allowedUnitTypes = listOf(UnitType.INFANTRY)
    ),
    FACTORY(
        title = "Tank Fabrikası (War Factory)",
        baseCostGold = 450,
        energyCost = 40,
        description = "SADECE ZIRHLI BİRLİK ÜRETİR: Leopard 2, M1 Abrams, Altay, T-90.",
        allowedUnitTypes = listOf(UnitType.TANK, UnitType.SIEGE)
    ),
    AIRFIELD(
        title = "Hava Üssü (Airfield)",
        baseCostGold = 600,
        energyCost = 60,
        description = "SADECE SAVAŞ UÇAĞI & JET ÜRETİR: F-22, F-35, Su-57, KAAN.",
        allowedUnitTypes = listOf(UnitType.JET)
    ),
    SHIPYARD(
        title = "Tersane (Shipyard)",
        baseCostGold = 550,
        energyCost = 50,
        description = "SADECE DENİZALTI VE SAVAŞ GEMİSİ ÜRETİR: Reis Sınıfı, Virginia, Yasen.",
        allowedUnitTypes = listOf(UnitType.SUBMARINE)
    )
}

data class RtsBuilding(
    val id: String,
    val type: RtsBuildingType,
    val level: Int = 1,
    val gridX: Int = 0,
    val gridY: Int = 0,
    val worldX: Float = 0f,
    val worldY: Float = 0f,
    val queue: MutableList<ProductionOrder> = mutableListOf(),
    val maxQueueCapacity: Int = 5,
    val isConstructing: Boolean = false,
    val constructionTimeSeconds: Int = 6,
    val constructionRemainingSeconds: Int = 0
)

data class EraTransitionEffect(
    val targetEra: Era,
    val triggerTimeMs: Long = System.currentTimeMillis()
)

data class GeneralsPower(
    val id: String,
    val nation: Nation,
    val era: Era,
    val name: String,
    val description: String,
    val cooldownSeconds: Int,
    var currentCooldownSeconds: Int = 0,
    val costGold: Int = 0,
    val strikeType: String // "AIRSTRIKE", "BARRAGE", "EMP", "REINFORCEMENTS"
) {
    val isReady: Boolean
        get() = currentCooldownSeconds <= 0
}

data class RtsResources(
    val food: Double = 1500.0,
    val maxFood: Double = 10000.0,
    val gold: Double = 2500.0,
    val maxGold: Double = 15000.0,
    val iron: Double = 1200.0,
    val maxIron: Double = 8000.0,
    val oil: Double = 800.0,
    val maxOil: Double = 5000.0,
    val techPoints: Double = 600.0,
    val maxTechPoints: Double = 8000.0,
    val energyProduced: Int = 200,
    val energyConsumed: Int = 110,
    val goldDrainRate: Double = 0.0,
    val ironDrainRate: Double = 0.0,
    val goldIncomeRate: Double = 35.0,
    val ironIncomeRate: Double = 20.0
) {
    val netGoldRate: Double get() = goldIncomeRate - goldDrainRate
    val netIronRate: Double get() = ironIncomeRate - ironDrainRate
    val isGoldDepleting: Boolean get() = netGoldRate < 0
    val isIronDepleting: Boolean get() = netIronRate < 0

    val energyEfficiency: Float
        get() = if (energyConsumed <= 0) 1.0f
        else if (energyProduced >= energyConsumed) 1.0f
        else (energyProduced.toFloat() / energyConsumed.toFloat()).coerceIn(0.25f, 1.0f)

    val isEnergyDeficit: Boolean
        get() = energyProduced < energyConsumed
}

data class FactionData(
    val nation: Nation,
    val startingEra: Era,
    val startingGold: Double,
    val startingTechPoints: Double,
    val startingEnergyProduced: Int,
    val startingEnergyConsumed: Int,
    val factionPassive: String,
    val passiveDescription: String,
    val speedBonusUnitType: UnitType? = null,
    val discountPercent: Float = 0f,
    val armorBonusPercent: Float = 0f
)

object FactionCatalog {
    val factions: List<FactionData> = listOf(
        FactionData(
            nation = Nation.TURKEY,
            startingEra = Era.MODERN,
            startingGold = 4500.0,
            startingTechPoints = 1400.0,
            startingEnergyProduced = 220,
            startingEnergyConsumed = 100,
            factionPassive = "SİHA Doktrini & Asimetrik Manevra",
            passiveDescription = "Bayraktar SİHA ve KAAN jetleri %25 daha hızlı üretilir. Taktik SİHA Akını General Gücü bekleme süresi 15s daha kısadır.",
            speedBonusUnitType = UnitType.JET
        ),
        FactionData(
            nation = Nation.USA,
            startingEra = Era.MODERN,
            startingGold = 6000.0,
            startingTechPoints = 2500.0,
            startingEnergyProduced = 280,
            startingEnergyConsumed = 120,
            factionPassive = "Hava Üstünlüğü & Yüksek Teknoloji Lojistiği",
            passiveDescription = "M1 Abrams ve F-22 jet alımlarında %15 maliyet indirimi. +60 MW nükleer reaktör gücü.",
            discountPercent = 0.15f
        ),
        FactionData(
            nation = Nation.RUSSIA,
            startingEra = Era.INDUSTRIAL,
            startingGold = 4800.0,
            startingTechPoints = 900.0,
            startingEnergyProduced = 190,
            startingEnergyConsumed = 95,
            factionPassive = "Ağır Zırh Çeliği & Ağır Topçu Barajı",
            passiveDescription = "T-90 tankları ve kara kuşatma birimleri +%20 daha yüksek zırh ve can puanına (HP) sahiptir.",
            armorBonusPercent = 0.20f
        ),
        FactionData(
            nation = Nation.CHINA,
            startingEra = Era.CLASSICAL_GUNPOWDER,
            startingGold = 5500.0,
            startingTechPoints = 700.0,
            startingEnergyProduced = 230,
            startingEnergyConsumed = 110,
            factionPassive = "Seri Üretim & Sayısal İnsan Gücü",
            passiveDescription = "Kışla ve fabrika üretim süreleri %30 daha hızlıdır. Tedarik lojistik depoları saniyede +15 fazladan Altın üretir.",
            speedBonusUnitType = UnitType.INFANTRY
        )
    )

    fun getFaction(nation: Nation): FactionData {
        return factions.firstOrNull { it.nation == nation } ?: factions.first()
    }
}

object WorldSuperpowersCatalog {
    val initial20Nations: List<DiplomaticNation> = listOf(
        DiplomaticNation("usa", "ABD", "🇺🇸", 1, 60, "(-770, 0, 380)", "Hava Üstünlüğü & Global Lojistik", "F-35 Lightning II", UnitType.JET, "5. Nesil Çok Amaçlı Gizli Savaş Uçağı", 1800, 450, 8, 220f, 650f, 0.25f),
        DiplomaticNation("russia", "Rusya", "🇷🇺", 2, 45, "(370, 0, 550)", "Ağır Zırh Kalkanı & Füze Barajı", "Su-57 Felon", UnitType.JET, "5. Nesil Süper-Manevralı Hayalet Jet", 1750, 500, 8, 210f, 680f, 0.30f),
        DiplomaticNation("china", "Çin", "🇨🇳", 3, 50, "(1160, 0, 390)", "Seri Üretim & Hipersonik Silahlar", "Type 99A2", UnitType.TANK, "Aktif Lazer Korumalı Ağır Tank", 1400, 300, 6, 195f, 920f, 1.0f),
        DiplomaticNation("india", "Hindistan", "🇮🇳", 4, 30, "(770, 0, 280)", "Kombine Silah Gücü & Nükleer Üçlü", "Tejas Mk2 / BrahMos", UnitType.JET, "Süpersonik Seyir Füzeli Avcı Uçağı", 1300, 280, 6, 175f, 580f, 0.6f),
        DiplomaticNation("south_korea", "Güney Kore", "🇰🇷", 5, 75, "(1270, 0, 370)", "Yüksek Teknoloji Sensör & Hassas Vuruş", "K2 Black Panther", UnitType.TANK, "Otomatik Dolduruculu Gelişmiş Zırhlı", 1550, 350, 7, 190f, 890f, 1.0f),
        DiplomaticNation("uk", "İngiltere", "🇬🇧", 6, 65, "(-10, 0, 510)", "Kraliyet Donanması & Stratejik İstihbarat", "Challenger 3", UnitType.TANK, "Chobham Zırhlı Ağır Muharebe Tankı", 1600, 380, 7, 185f, 950f, 1.0f),
        DiplomaticNation("japan", "Japonya", "🇯🇵", 7, 70, "(1390, 0, 350)", "Aegis Donanma Kalkanı & Denizaltı Avcısı", "Taigei Sınıfı Denizaltı", UnitType.SUBMARINE, "Lityum-İyon Bataryalı Hayalet Denizaltı", 1700, 420, 8, 200f, 820f, 0.2f),
        DiplomaticNation("turkey", "Türkiye", "🇹🇷", 8, 100, "(390, 0, 350)", "SİHA Sürü Doktrini & Asimetrik Manevra", "Bayraktar KIZILELMA", UnitType.JET, "İnsansız Hayalet Taarruz Uçağı", 1500, 320, 6, 205f, 620f, 0.2f),
        DiplomaticNation("pakistan", "Pakistan", "🇵🇰", 9, 85, "(730, 0, 330)", "Dağlık Savunma & Taktik Füze Gücü", "JF-17 Thunder Block 3", UnitType.JET, "AESA Radarlı Ekonomik Çok Rollü Jet", 1100, 220, 5, 160f, 540f, 0.5f),
        DiplomaticNation("italy", "İtalya", "🇮🇹", 10, 60, "(120, 0, 410)", "Akdeniz Donanması & Çevik Fırkateynler", "Todaro (Tip 212A)", UnitType.SUBMARINE, "Havadan Bağımsız AIP Akdeniz Denizaltısı", 1450, 310, 7, 180f, 750f, 0.3f),
        DiplomaticNation("france", "Fransa", "🇫🇷", 11, 40, "(20, 0, 460)", "Bağımsız Nükleer Caydırıcılık & Rafale", "Dassault Rafale F4", UnitType.JET, "Omnirole Nükleer Kapasiteli Avcı", 1850, 420, 8, 215f, 660f, 0.4f),
        DiplomaticNation("brazil", "Brezilya", "🇧🇷", 12, 55, "(-470, 0, -150)", "Amazon Hava Sahası & Nehir Donanması", "JAS 39 Gripen E", UnitType.JET, "Ağ Merkezli Elektronik Harp Jeti", 1350, 270, 6, 170f, 570f, 0.5f),
        DiplomaticNation("indonesia", "Endonezya", "🇮🇩", 13, 65, "(1060, 0, -60)", "Takımadalar Savunması & Hızlı Hücumbotlar", "Harimau Orta Tank", UnitType.TANK, "Güneydoğu Asya Amfibi Kaplan Tankı", 1200, 250, 5, 165f, 740f, 1.0f),
        DiplomaticNation("iran", "İran", "🇮🇷", 14, -20, "(510, 0, 350)", "Hürmüz Asimetrik Füze & İHA Ordusu", "Şahid-136 Sürüsü", UnitType.JET, "Düşük Maliyetli Kamikaze Dron Dalgası", 750, 150, 3, 140f, 320f, 0.4f),
        DiplomaticNation("egypt", "Mısır", "🇪🇬", 15, 35, "(310, 0, 300)", "Süveyş Kanalı Muhafızı & Ağır Zırh", "M1A1 Abrams Nil Sürümü", UnitType.TANK, "Çöl Şartlarına Uyarlanmış Ana Muharebe Tankı", 1300, 260, 6, 175f, 850f, 1.0f),
        DiplomaticNation("australia", "Avustralya", "🇦🇺", 16, 50, "(1330, 0, -250)", "Okyanusya Devriye & Erken Uyarı Radarları", "F/A-18F Super Hornet", UnitType.JET, "Çift Motorlu Taarruz Avcı Uçağı", 1650, 360, 7, 195f, 640f, 0.5f),
        DiplomaticNation("israel", "İsrail", "🇮🇱", 17, 25, "(350, 0, 310)", "Demir Kubbe Kalkanı & Trophy Aktif Koruma", "Merkava Mk 4M Windbreaker", UnitType.TANK, "Trophy Aktif Koruma Sistemli Zırhlı Kalkan", 1700, 340, 7, 210f, 960f, 0.9f),
        DiplomaticNation("ukraine", "Ukrayna", "🇺🇦", 18, 70, "(300, 0, 500)", "İHA İnovasyonu & Asimetrik Zırhlı Direniş", "T-84 Oplot-M", UnitType.TANK, "Duplet Patlayıcı Reaktif Zırhlı Tank", 1350, 280, 6, 185f, 870f, 1.0f),
        DiplomaticNation("germany", "Almanya", "🇩🇪", 19, 55, "(100, 0, 520)", "Hassas Mekanik Mühendislik & Rheinmetall", "Leopard 2A7+", UnitType.TANK, "L55 Yivsiz Toplu Avrupa Zırh Standardı", 1750, 380, 7, 215f, 980f, 1.0f),
        DiplomaticNation("spain", "İspanya", "🇪🇸", 20, 60, "(-30, 0, 400)", "Cebelitarık Boğaz Muhafızı & Amfibi Gemiler", "S-80 Plus Denizaltı", UnitType.SUBMARINE, "Biyo-Etanol AIP Okyanus Denizaltısı", 1500, 330, 7, 185f, 790f, 0.3f)
    )
}

