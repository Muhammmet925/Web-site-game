package com.example.model

object RtsCatalog {

    fun getUnitTemplates(): List<RtsUnitTemplate> {
        return listOf(
            // ==================== 5. YÜZYIL ====================
            RtsUnitTemplate(
                id = "inf_med_tr",
                name = "Hun Süvarisi (Atlı Bozkır Akıncısı)",
                nation = Nation.TURKEY,
                era = Era.MEDIEVAL_5TH,
                unitType = UnitType.INFANTRY,
                costGold = 90,
                productionTimeSeconds = 3,
                maxHp = 110f,
                attackPower = 18f,
                speed = 68f,
                description = "Bozkır taktiğiyle hareket halinde yaylım oku atabilen hızlı Hun süvari birliği.",
                modernEquivalentId = "inf_mod_tr"
            ),
            RtsUnitTemplate(
                id = "siege_med_tr",
                name = "Mancınık & Kuşatma Makinesi",
                nation = Nation.TURKEY,
                era = Era.MEDIEVAL_5TH,
                unitType = UnitType.SIEGE,
                costGold = 180,
                productionTimeSeconds = 6,
                maxHp = 140f,
                attackPower = 40f,
                speed = 30f,
                description = "Taş gülleler ve alevli zift atarak tahkimatları yıkan kuşatma makinesi.",
                modernEquivalentId = "tank_mod_tr"
            ),
            RtsUnitTemplate(
                id = "sub_med_tr",
                name = "Kadırga & Hücum Sandalı",
                nation = Nation.TURKEY,
                era = Era.MEDIEVAL_5TH,
                unitType = UnitType.SUBMARINE,
                costGold = 160,
                productionTimeSeconds = 5,
                maxHp = 160f,
                attackPower = 25f,
                speed = 45f,
                description = "Kıyı sularında kürek gücüyle hızla ilerleyen hücum kalyatası.",
                modernEquivalentId = "sub_mod_tr"
            ),

            // ==================== KLASİK & BARUT DÖNEMİ ====================
            RtsUnitTemplate(
                id = "inf_cla_tr",
                name = "Yeniçeri Tüfekçi Bölüğü",
                nation = Nation.TURKEY,
                era = Era.CLASSICAL_GUNPOWDER,
                unitType = UnitType.INFANTRY,
                costGold = 140,
                productionTimeSeconds = 4,
                maxHp = 130f,
                attackPower = 28f,
                speed = 50f,
                description = "Fitilli tüfek ve yatağan kılıçla donatılmış elit piyade birliği.",
                modernEquivalentId = "inf_mod_tr"
            ),
            RtsUnitTemplate(
                id = "siege_cla_tr",
                name = "Şahi Kuşatma Topu",
                nation = Nation.TURKEY,
                era = Era.CLASSICAL_GUNPOWDER,
                unitType = UnitType.SIEGE,
                costGold = 320,
                productionTimeSeconds = 8,
                maxHp = 220f,
                attackPower = 75f,
                speed = 25f,
                description = "Fatih devri devasa bronz döküm top. Devasa alan hasarı verir.",
                modernEquivalentId = "tank_mod_tr"
            ),
            RtsUnitTemplate(
                id = "sub_cla_tr",
                name = "Zırhlı Kalyon",
                nation = Nation.TURKEY,
                era = Era.CLASSICAL_GUNPOWDER,
                unitType = UnitType.SUBMARINE,
                costGold = 300,
                productionTimeSeconds = 7,
                maxHp = 280f,
                attackPower = 55f,
                speed = 40f,
                description = "Çok katlı borda toplarıyla düşman gemilerini batıran yelkenli amiral gemisi.",
                modernEquivalentId = "sub_mod_tr"
            ),

            // ==================== ENDÜSTRİ DEVRİMİ ====================
            RtsUnitTemplate(
                id = "inf_ind_tr",
                name = "Kuvayı Milliye Müfrezesi",
                nation = Nation.TURKEY,
                era = Era.INDUSTRIAL,
                unitType = UnitType.INFANTRY,
                costGold = 180,
                productionTimeSeconds = 4,
                maxHp = 160f,
                attackPower = 35f,
                speed = 50f,
                description = "Mavzer tüfekleri ve el bombalarıyla donatılmış fedakar cephe piyadeleri.",
                modernEquivalentId = "inf_mod_tr"
            ),
            RtsUnitTemplate(
                id = "tank_ind_tr",
                name = "T-26 / Zırhlı Muharebe Aracı",
                nation = Nation.TURKEY,
                era = Era.INDUSTRIAL,
                unitType = UnitType.TANK,
                costGold = 380,
                productionTimeSeconds = 7,
                maxHp = 320f,
                attackPower = 60f,
                speed = 42f,
                description = "45mm top taşıyan ilk mekanize tank birliği.",
                modernEquivalentId = "tank_mod_tr"
            ),
            RtsUnitTemplate(
                id = "jet_ind_tr",
                name = "Spitfire & Vecihi Tayyaresi",
                nation = Nation.TURKEY,
                era = Era.INDUSTRIAL,
                unitType = UnitType.JET,
                costGold = 450,
                productionTimeSeconds = 8,
                maxHp = 220f,
                attackPower = 70f,
                speed = 80f,
                description = "Pervaneli avcı uçağı. Karaya dalış yaparak roket ve makineli tüfek saldırısı yapar.",
                modernEquivalentId = "jet_mod_tr"
            ),
            RtsUnitTemplate(
                id = "sub_ind_tr",
                name = "Dumlupınar I Denizaltısı",
                nation = Nation.TURKEY,
                era = Era.INDUSTRIAL,
                unitType = UnitType.SUBMARINE,
                costGold = 420,
                productionTimeSeconds = 8,
                maxHp = 340f,
                attackPower = 80f,
                speed = 38f,
                description = "Torpidolarla donatılmış dizel-elektrik muharebe denizaltısı.",
                modernEquivalentId = "sub_mod_tr"
            ),

            // ==================== MODERN ÇAĞ (MODERN WARFARE) ====================
            // TÜRKİYE
            RtsUnitTemplate(
                id = "inf_mod_tr",
                name = "Bordo Bereli Özel Kuvvetler",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.INFANTRY,
                costGold = 220,
                productionTimeSeconds = 4,
                maxHp = 220f,
                attackPower = 55f,
                speed = 55f,
                description = "Lazer hedef işaretleme, anti-tank Cirit füzeleri ve susturuculu piyade tüfekleri.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "tank_mod_tr",
                name = "Altay T2 Ana Muharebe Tankı",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.TANK,
                costGold = 550,
                productionTimeSeconds = 9,
                maxHp = 520f,
                attackPower = 120f,
                speed = 45f,
                description = "120mm yivsiz top, AKKOR aktif koruma sistemi ve yerli kompozit reaktif zırh.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "jet_mod_tr",
                name = "Bayraktar SİHA (TB2 & KIZILELMA)",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.JET,
                costGold = 580,
                productionTimeSeconds = 8,
                maxHp = 380f,
                attackPower = 140f,
                speed = 92f,
                description = "MAM-L mühimmatı ve AESA radarla hassas cerrahi hava vuruşu yapan SİHA.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "siege_mod_tr",
                name = "BORA / TAYFUN Balistik Füzesi",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.SIEGE,
                costGold = 650,
                productionTimeSeconds = 11,
                maxHp = 350f,
                attackPower = 180f,
                speed = 35f,
                description = "Taktik karadan karaya uzun menzilli hassas balistik füze bataryası.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "sub_mod_tr",
                name = "TCG Anadolu Amfibi Hücum Gemisi (Tersane)",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.SUBMARINE,
                costGold = 750,
                productionTimeSeconds = 12,
                maxHp = 700f,
                attackPower = 150f,
                speed = 40f,
                description = "SİHA ve helikopter konuşlu amfibi sancak gemisi ve denizaltı savunma platformu.",
                modernEquivalentId = null
            ),

            // ABD
            RtsUnitTemplate(
                id = "inf_cla_usa",
                name = "18. YY Milis Askeri (Minuteman)",
                nation = Nation.USA,
                era = Era.CLASSICAL_GUNPOWDER,
                unitType = UnitType.INFANTRY,
                costGold = 130,
                productionTimeSeconds = 4,
                maxHp = 125f,
                attackPower = 26f,
                speed = 52f,
                description = "Bağımsızlık savaşı yivli tüfekli milis piyadesi.",
                modernEquivalentId = "tank_mod_usa"
            ),
            RtsUnitTemplate(
                id = "tank_ind_usa",
                name = "M4 Sherman Tankı",
                nation = Nation.USA,
                era = Era.INDUSTRIAL,
                unitType = UnitType.TANK,
                costGold = 390,
                productionTimeSeconds = 7,
                maxHp = 350f,
                attackPower = 65f,
                speed = 44f,
                description = "Seri üretim çelik canavar. 75mm top ve güvenilir süspansiyon.",
                modernEquivalentId = "tank_mod_usa"
            ),
            RtsUnitTemplate(
                id = "tank_mod_usa",
                name = "M1A2 Abrams SEPv3",
                nation = Nation.USA,
                era = Era.MODERN,
                unitType = UnitType.TANK,
                costGold = 580,
                productionTimeSeconds = 10,
                maxHp = 560f,
                attackPower = 125f,
                speed = 44f,
                description = "Seyreltilmiş uranyum zırh, Trophy APS ve 120mm M256 yivsiz top.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "jet_mod_usa",
                name = "F-22 Raptor Hayalet Jet",
                nation = Nation.USA,
                era = Era.MODERN,
                unitType = UnitType.JET,
                costGold = 700,
                productionTimeSeconds = 11,
                maxHp = 390f,
                attackPower = 155f,
                speed = 100f,
                description = "Hava üstünlüğü avcısı, radar görünmezliği ve AMRAAM füzeleri.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "sub_mod_usa",
                name = "Ohio Sınıfı Nükleer Denizaltı (Tersane)",
                nation = Nation.USA,
                era = Era.MODERN,
                unitType = UnitType.SUBMARINE,
                costGold = 720,
                productionTimeSeconds = 12,
                maxHp = 680f,
                attackPower = 160f,
                speed = 42f,
                description = "Trident II balistik füzeleri ve sessiz nükleer tahrikli devasa avcı.",
                modernEquivalentId = null
            ),

            // RUSYA
            RtsUnitTemplate(
                id = "inf_med_ru",
                name = "Slav Savaşçısı (Druzhina Muhafızı)",
                nation = Nation.RUSSIA,
                era = Era.MEDIEVAL_5TH,
                unitType = UnitType.INFANTRY,
                costGold = 95,
                productionTimeSeconds = 3,
                maxHp = 130f,
                attackPower = 20f,
                speed = 52f,
                description = "Ağır balta ve kalkan taşıyan fedakar 5. YY Slav öncü savaşçısı.",
                modernEquivalentId = "tank_mod_ru"
            ),
            RtsUnitTemplate(
                id = "tank_ind_ru",
                name = "T-34/85 Efsanevi Tankı",
                nation = Nation.RUSSIA,
                era = Era.INDUSTRIAL,
                unitType = UnitType.TANK,
                costGold = 370,
                productionTimeSeconds = 6,
                maxHp = 360f,
                attackPower = 68f,
                speed = 45f,
                description = "Eğimli zırh, 85mm ZiS-S-53 topu ve kitle üretimiyle cepheyi yaran tank.",
                modernEquivalentId = "tank_mod_ru"
            ),
            RtsUnitTemplate(
                id = "tank_mod_ru",
                name = "T-14 Armata & T-90M Proryv",
                nation = Nation.RUSSIA,
                era = Era.MODERN,
                unitType = UnitType.TANK,
                costGold = 540,
                productionTimeSeconds = 9,
                maxHp = 550f,
                attackPower = 122f,
                speed = 45f,
                description = "İnsansız taret, Relikt patlayıcı reaktif zırh ve 125mm 2A82-1M top.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "jet_mod_ru",
                name = "Su-57 Felon 5. Nesil Jet",
                nation = Nation.RUSSIA,
                era = Era.MODERN,
                unitType = UnitType.JET,
                costGold = 640,
                productionTimeSeconds = 10,
                maxHp = 400f,
                attackPower = 148f,
                speed = 96f,
                description = "3D itki vektörleme, süper-manevra kabiliyeti ve R-77 havadan havaya füzeler.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "sub_mod_ru",
                name = "Akula Sınıfı Nükleer Taarruz Denizaltısı",
                nation = Nation.RUSSIA,
                era = Era.MODERN,
                unitType = UnitType.SUBMARINE,
                costGold = 690,
                productionTimeSeconds = 11,
                maxHp = 660f,
                attackPower = 150f,
                speed = 44f,
                description = "Çift gövde titanyum yapı ve Kalibr seyir füzeleri taşıyan süper sessiz denizaltı.",
                modernEquivalentId = null
            ),

            // ÇİN
            RtsUnitTemplate(
                id = "inf_med_cn",
                name = "Han Hanedanı Okçusu",
                nation = Nation.CHINA,
                era = Era.MEDIEVAL_5TH,
                unitType = UnitType.INFANTRY,
                costGold = 85,
                productionTimeSeconds = 3,
                maxHp = 95f,
                attackPower = 17f,
                speed = 58f,
                description = "Bambu yaylar ve zehirli oklarla donatılmış kalabalık 5. YY Han piyadesi.",
                modernEquivalentId = "tank_mod_cn"
            ),
            RtsUnitTemplate(
                id = "tank_ind_cn",
                name = "Tip 59 Ana Muharebe Tankı",
                nation = Nation.CHINA,
                era = Era.INDUSTRIAL,
                unitType = UnitType.TANK,
                costGold = 340,
                productionTimeSeconds = 6,
                maxHp = 330f,
                attackPower = 62f,
                speed = 44f,
                description = "100mm yivli top taşıyan dayanıklı ve seri üretilebilen zırhlı muharebe tankı.",
                modernEquivalentId = "tank_mod_cn"
            ),
            RtsUnitTemplate(
                id = "tank_mod_cn",
                name = "Tip 99A2 Ana Muharebe Tankı",
                nation = Nation.CHINA,
                era = Era.MODERN,
                unitType = UnitType.TANK,
                costGold = 490,
                productionTimeSeconds = 8,
                maxHp = 510f,
                attackPower = 115f,
                speed = 46f,
                description = "Lazer savunma sistemi, 125mm top ve FY-4 reaktif zırhı.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "jet_mod_cn",
                name = "J-20 Mighty Dragon Jeti",
                nation = Nation.CHINA,
                era = Era.MODERN,
                unitType = UnitType.JET,
                costGold = 620,
                productionTimeSeconds = 10,
                maxHp = 385f,
                attackPower = 142f,
                speed = 94f,
                description = "Kanartlı hayalet jet, PL-15 uzun menzilli hava-hava füzeleri.",
                modernEquivalentId = null
            ),
            RtsUnitTemplate(
                id = "sub_mod_cn",
                name = "Tip 094 Jin Sınıfı Balistik Denizaltı",
                nation = Nation.CHINA,
                era = Era.MODERN,
                unitType = UnitType.SUBMARINE,
                costGold = 660,
                productionTimeSeconds = 11,
                maxHp = 630f,
                attackPower = 145f,
                speed = 41f,
                description = "JL-2 balistik füzeleri taşıyan nükleer caydırıcılık denizaltısı.",
                modernEquivalentId = null
            )
        )
    }

    // ==================== DİNAMİK ASİMETRİK FABRİKA FİLTRELEME FONKSİYONU ====================
    fun getAvailableProduction(
        buildingType: RtsBuildingType,
        nation: Nation,
        currentEra: Era
    ): List<RtsUnitTemplate> {
        return getUnitTemplates().filter { template ->
            template.nation == nation &&
            template.era.ordinal <= currentEra.ordinal &&
            buildingType.allowedUnitTypes.contains(template.unitType)
        }
    }

    fun getGeneralsPowers(nation: Nation, era: Era): List<GeneralsPower> {
        return when (nation) {
            Nation.TURKEY -> when (era) {
                Era.MEDIEVAL_5TH -> listOf(
                    GeneralsPower("gp_tr_1", Nation.TURKEY, Era.MEDIEVAL_5TH, "Atlı Akını", "Bozkır süvarileri düşman hattına anlık baskın düzenler.", 30, 0, 100, "REINFORCEMENTS"),
                    GeneralsPower("gp_tr_2", Nation.TURKEY, Era.MEDIEVAL_5TH, "Mancınık Barajı", "Seçili alana alevli taş ve zift yağmuru başlatır.", 45, 0, 180, "BARRAGE")
                )
                Era.CLASSICAL_GUNPOWDER -> listOf(
                    GeneralsPower("gp_tr_3", Nation.TURKEY, Era.CLASSICAL_GUNPOWDER, "Hilal Taktiği", "Tüm birliklerin hareket ve saldırı hızını 15 saniyeliğine %40 artırır.", 35, 0, 200, "BUFF"),
                    GeneralsPower("gp_tr_4", Nation.TURKEY, Era.CLASSICAL_GUNPOWDER, "Şahi Topu Kuşatma Barajı", "Ağır döküm toplar hedef bölgedeki tüm binaları sarsar.", 50, 0, 300, "BARRAGE")
                )
                Era.INDUSTRIAL -> listOf(
                    GeneralsPower("gp_tr_5", Nation.TURKEY, Era.INDUSTRIAL, "Kuvayı Milliye Seferberliği", "Haritaya anında 3 bölük tecrübeli piyade intikal ettirir.", 40, 0, 350, "REINFORCEMENTS"),
                    GeneralsPower("gp_tr_6", Nation.TURKEY, Era.INDUSTRIAL, "Topçu Hattı Yaylımı", "Ağır obüs bataryaları düşman zırhlılarını delik deşik eder.", 55, 0, 450, "BARRAGE")
                )
                Era.MODERN -> listOf(
                    GeneralsPower("gp_tr_7", Nation.TURKEY, Era.MODERN, "Bayraktar SİHA Hassas Bombardımanı", "Lazer güdümlü MAM-L mühimmatlarıyla seçilen hedefi tek vuruşta imha eder.", 35, 0, 400, "AIRSTRIKE"),
                    GeneralsPower("gp_tr_8", Nation.TURKEY, Era.MODERN, "BORA / TAYFUN Balistik Füzesi", "Hedef alandaki tüm düşman üssüne yıkıcı stratejik füze darbesi indirir.", 60, 0, 650, "BARRAGE")
                )
            }
            Nation.USA -> when (era) {
                Era.MODERN -> listOf(
                    GeneralsPower("gp_usa_1", Nation.USA, Era.MODERN, "A-10 Thunderbolt II (BRRRRT!)", "30mm Gatling topu ve Maverick füzeleriyle zırhlı konvoyu ezer.", 35, 0, 420, "AIRSTRIKE"),
                    GeneralsPower("gp_usa_2", Nation.USA, Era.MODERN, "B-2 Spirit Halı Bombardımanı (MOAB)", "Radara yakalanmayan hayalet bombardıman devasa patlama yaratır.", 65, 0, 700, "BARRAGE")
                )
                else -> listOf(
                    GeneralsPower("gp_usa_old_1", Nation.USA, era, "101. Hava İndirme Taburu", "Paraşütçü takviye birliği intikal eder.", 40, 0, 300, "REINFORCEMENTS"),
                    GeneralsPower("gp_usa_old_2", Nation.USA, era, "Donanma Topçu Desteği", "Kıyıdan yoğun top mermisi yağdırılır.", 50, 0, 400, "BARRAGE")
                )
            }
            Nation.RUSSIA -> when (era) {
                Era.MODERN -> listOf(
                    GeneralsPower("gp_ru_1", Nation.RUSSIA, Era.MODERN, "İskender Hipersonik Füzesi", "Hava savunma sistemlerini delen süper hızlı füze saldırısı.", 40, 0, 450, "AIRSTRIKE"),
                    GeneralsPower("gp_ru_2", Nation.RUSSIA, Era.MODERN, "Su-57 Termobarik Vakum Bombası", "Oksijeni emerek hedef bölgedeki tüm canlı ve zırhlı unsurları yok eder.", 65, 0, 720, "BARRAGE")
                )
                else -> listOf(
                    GeneralsPower("gp_ru_old_1", Nation.RUSSIA, era, "Katyuşa Roket Yaylımı", "Stalin'in orgu roket yaylım ateşi açar.", 45, 0, 380, "BARRAGE"),
                    GeneralsPower("gp_ru_old_2", Nation.RUSSIA, era, "Zırhlı Hücum Takviyesi", "Ekstra tank desteği sahaya sürülür.", 50, 0, 420, "REINFORCEMENTS")
                )
            }
            Nation.CHINA -> when (era) {
                Era.MODERN -> listOf(
                    GeneralsPower("gp_cn_1", Nation.CHINA, Era.MODERN, "EMP Elektromanyetik Şok Dalgası", "Düşman araç ve binalarının enerjisini 15 saniyeliğine tamamen keser.", 35, 0, 380, "EMP"),
                    GeneralsPower("gp_cn_2", Nation.CHINA, Era.MODERN, "DF-21D Hipersonik Taarruzu", "Uçak gemisi ve ağır hedeflere yönelik hipersonik balistik darbe.", 60, 0, 680, "BARRAGE")
                )
                else -> listOf(
                    GeneralsPower("gp_cn_old_1", Nation.CHINA, era, "Ateş Oku / Roket Barajı", "Eski Çin barutlu roket yaylımı.", 40, 0, 320, "BARRAGE"),
                    GeneralsPower("gp_cn_old_2", Nation.CHINA, era, "Kızıl Muhafız Akını", "Kalabalık insan seli taarruzu.", 45, 0, 350, "REINFORCEMENTS")
                )
            }
        }
    }
}
