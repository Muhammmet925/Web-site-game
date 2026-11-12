package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.Era
import com.example.model.Nation
import com.example.model.RtsBuilding
import com.example.model.RtsBuildingType
import com.example.model.RtsCatalog
import com.example.model.RtsResources
import com.example.model.RtsUnit
import com.example.model.UnitType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Nova Base", appName)
    }

    @Test
    fun `verify production queue building type constraints`() {
        val warFactory = RtsBuilding(id = "factory_1", type = RtsBuildingType.FACTORY, gridX = 0, gridY = 0)
        val airfield = RtsBuilding(id = "airfield_1", type = RtsBuildingType.AIRFIELD, gridX = 1, gridY = 0)
        val shipyard = RtsBuilding(id = "shipyard_1", type = RtsBuildingType.SHIPYARD, gridX = 2, gridY = 0)

        // War Factory only allows Tank & Siege
        assertTrue(warFactory.type.allowedUnitTypes.contains(UnitType.TANK))
        assertFalse(warFactory.type.allowedUnitTypes.contains(UnitType.JET))
        assertFalse(warFactory.type.allowedUnitTypes.contains(UnitType.SUBMARINE))

        // Airfield only allows Jet
        assertTrue(airfield.type.allowedUnitTypes.contains(UnitType.JET))
        assertFalse(airfield.type.allowedUnitTypes.contains(UnitType.TANK))

        // Shipyard only allows Submarine
        assertTrue(shipyard.type.allowedUnitTypes.contains(UnitType.SUBMARINE))
        assertFalse(shipyard.type.allowedUnitTypes.contains(UnitType.INFANTRY))
    }

    @Test
    fun `verify era transition progression and unit evolution`() {
        val templates = RtsCatalog.getUnitTemplates()
        val medievalCatapult = templates.find { it.id == "siege_med_tr" }
        assertEquals("tank_mod_tr", medievalCatapult?.modernEquivalentId)

        val altayTank = templates.find { it.id == "tank_mod_tr" }
        assertEquals("Altay T2 Ana Muharebe Tankı", altayTank?.name)
        assertEquals(UnitType.TANK, altayTank?.unitType)
        assertEquals(Era.MODERN, altayTank?.era)
    }

    @Test
    fun `verify generals powers evolve across eras for Turkey`() {
        val powersMedieval = RtsCatalog.getGeneralsPowers(Nation.TURKEY, Era.MEDIEVAL_5TH)
        assertTrue(powersMedieval.any { it.name == "Atlı Akını" || it.name == "Mancınık Barajı" })

        val powersModern = RtsCatalog.getGeneralsPowers(Nation.TURKEY, Era.MODERN)
        assertTrue(powersModern.any { it.name.contains("Bayraktar SİHA") })
        assertTrue(powersModern.any { it.name.contains("TAYFUN") })
    }

    @Test
    fun `verify era transition effect holds target era`() {
        val effect = com.example.model.EraTransitionEffect(targetEra = Era.MODERN)
        assertEquals(Era.MODERN, effect.targetEra)
        assertTrue(effect.triggerTimeMs > 0L)
    }

    @Test
    fun `verify buildings have valid world battlefield coordinates`() {
        val factory = RtsBuilding(
            id = "b_factory",
            type = RtsBuildingType.FACTORY,
            gridX = 1,
            gridY = 2,
            worldX = 260f,
            worldY = 360f
        )
        assertTrue(factory.worldX > 0f)
        assertTrue(factory.worldY > 0f)
        assertEquals(260f, factory.worldX)
    }

    @Test
    fun `verify unit xp and rank promotion calculations`() {
        val testUnit = RtsUnit(
            id = "test_tank_1",
            templateId = "tank_mod_tr",
            name = "Altay T2",
            nation = Nation.TURKEY,
            era = Era.MODERN,
            unitType = UnitType.TANK,
            hp = 420f,
            maxHp = 420f,
            attackPower = 45f,
            speed = 3.2f,
            posX = 100f,
            posY = 100f,
            targetX = 100f,
            targetY = 100f,
            xp = 0,
            rank = com.example.model.UnitRank.RECRUIT
        )

        // Give 120 XP -> Should reach VETERAN (+15% dmg, +10% max hp)
        val leveledUp = testUnit.gainXp(120)
        assertTrue(leveledUp)
        assertEquals(com.example.model.UnitRank.VETERAN, testUnit.rank)
        assertEquals("★★", testUnit.rankBadge)
        assertTrue(testUnit.attackPower > testUnit.baseAttackPower)
        assertTrue(testUnit.maxHp > testUnit.baseMaxHp)

        // Give 500 more XP -> Should reach HEROIC
        testUnit.gainXp(500)
        assertEquals(com.example.model.UnitRank.HEROIC, testUnit.rank)
        assertEquals("★★★★", testUnit.rankBadge)
        assertEquals(50, com.example.model.UnitRank.HEROIC.bonusDamagePct)
    }

    @Test
    fun `verify 20 world superpowers and modern export catalog`() {
        val nations = com.example.model.WorldSuperpowersCatalog.initial20Nations
        assertEquals(20, nations.size)

        val turkey = nations.find { it.name == "Türkiye" }
        assertTrue(turkey != null)
        assertEquals("🇹🇷", turkey!!.flagEmoji)
        assertEquals("Bayraktar KIZILELMA", turkey.exportUnitName)
        assertEquals(UnitType.JET, turkey.exportUnitType)

        val usa = nations.find { it.name == "ABD" }
        assertTrue(usa != null)
        assertEquals("F-35 Lightning II", usa!!.exportUnitName)

        val russia = nations.find { it.name == "Rusya" }
        assertTrue(russia != null)
        assertEquals("Su-57 Felon", russia!!.exportUnitName)
    }

    @Test
    fun `verify battle log entry and category`() {
        val entry = com.example.model.BattleLogEntry(
            id = "log_1",
            timestamp = "14:32:10",
            category = com.example.model.BattleLogCategory.COMBAT,
            message = "Altay T2 birimi düşman tankını saf dışı bıraktı.",
            highlightColorHex = 0xFFFF453A
        )
        assertEquals(com.example.model.BattleLogCategory.COMBAT, entry.category)
        assertTrue(entry.timestamp.isNotEmpty())
        assertEquals("Altay T2 birimi düşman tankını saf dışı bıraktı.", entry.message)
    }

    @Test
    fun `verify resource reserves and real-time depletion calculations`() {
        val res = com.example.model.RtsResources(
            gold = 2500.0,
            iron = 1200.0,
            goldIncomeRate = 35.0,
            ironIncomeRate = 20.0,
            goldDrainRate = 44.0, // 2 active queues
            ironDrainRate = 28.0
        )

        assertTrue(res.isGoldDepleting)
        assertTrue(res.isIronDepleting)
        assertEquals(-9.0, res.netGoldRate, 0.001)
        assertEquals(-8.0, res.netIronRate, 0.001)
    }

    @Test
    fun `verify technology research tree across historical eras`() {
        val techTree = com.example.model.TechCatalog.createDefaultTechTree()
        assertTrue(techTree.isNotEmpty())

        val medievalTechs = techTree.filter { it.era == Era.MEDIEVAL_5TH }
        val classicalTechs = techTree.filter { it.era == Era.CLASSICAL_GUNPOWDER }
        val industrialTechs = techTree.filter { it.era == Era.INDUSTRIAL }
        val modernTechs = techTree.filter { it.era == Era.MODERN }

        assertTrue(medievalTechs.isNotEmpty())
        assertTrue(classicalTechs.isNotEmpty())
        assertTrue(industrialTechs.isNotEmpty())
        assertTrue(modernTechs.isNotEmpty())

        val stealthJetTech = modernTechs.find { it.id == "tech_mod_stealth_jets" }
        assertTrue(stealthJetTech != null)
        assertTrue(stealthJetTech!!.unlocksUnitName?.contains("KAAN") == true)
        assertEquals(UnitType.JET, stealthJetTech.unlocksUnitType)
    }

    @Test
    fun `verify combat engagement log and session statistics`() {
        val stats = com.example.model.SessionCombatStats(
            totalDamageDealt = 1450f,
            totalDamageTaken = 620f,
            enemyCasualties = 6,
            friendlyCasualties = 2,
            totalEngagements = 14
        )
        assertEquals(3.0f, stats.killDeathRatio, 0.01f)

        val log = com.example.model.CombatEngagementLog(
            timestamp = "14:52:18",
            attackerName = "Altay T2",
            targetName = "Düşman T-90",
            damage = 185f,
            isAttackerPlayer = true,
            isCasualty = true,
            type = com.example.model.CombatLogType.ENEMY_KILLED,
            weaponSystem = "120mm Yivsiz Top"
        )
        assertEquals(com.example.model.CombatLogType.ENEMY_KILLED, log.type)
        assertTrue(log.isCasualty)
        assertTrue(log.isAttackerPlayer)
    }

    @Test
    fun `verify skirmish historical geographic maps catalog`() {
        val maps = com.example.model.SkirmishMapCatalog.maps
        assertEquals(6, maps.size)

        val gallipoli = maps.find { it.id == "map_gallipoli" }
        assertTrue(gallipoli != null)
        assertEquals("Çanakkale & Gelibolu Yarımadası (1915)", gallipoli?.title)
        assertEquals(Era.INDUSTRIAL, gallipoli?.historicalEra)

        val hormuz = maps.find { it.id == "map_hormuz" }
        assertTrue(hormuz != null)
        assertEquals(Era.MODERN, hormuz?.historicalEra)
        assertTrue(hormuz!!.terrainModifiers.contains("Petrol Platformu Gelirleri"))
    }

    @Test
    fun `verify tactical control groups assignment and composition`() {
        val tank1 = RtsUnit(
            id = "tank_1",
            templateId = "tank_mod_tr",
            name = "Altay T2",
            nation = Nation.TURKEY,
            era = Era.MODERN,
            unitType = UnitType.TANK,
            hp = 500f,
            maxHp = 500f,
            attackPower = 110f,
            speed = 45f,
            posX = 300f,
            posY = 300f,
            targetX = 300f,
            targetY = 300f,
            isPlayerSide = true
        )
        val jet1 = RtsUnit(
            id = "jet_1",
            templateId = "jet_mod_tr",
            name = "Bayraktar KIZILELMA",
            nation = Nation.TURKEY,
            era = Era.MODERN,
            unitType = UnitType.JET,
            hp = 250f,
            maxHp = 250f,
            attackPower = 140f,
            speed = 95f,
            posX = 320f,
            posY = 320f,
            targetX = 320f,
            targetY = 320f,
            isPlayerSide = true
        )

        val group = com.example.model.TacticalControlGroup(
            id = 1,
            name = "Grup 1",
            unitIds = setOf("tank_1", "jet_1")
        )

        val alive = group.getAliveUnits(listOf(tank1, jet1))
        assertEquals(2, alive.size)
        assertEquals(1.0f, group.getAverageHpPercent(listOf(tank1, jet1)), 0.01f)
        val summary = group.getCompositionSummary(listOf(tank1, jet1))
        assertTrue(summary.contains("🚜") || summary.contains("✈️"))
    }

    @Test
    fun `verify era timeline progress calculator updates dynamically`() {
        val techTree = com.example.model.TechCatalog.createDefaultTechTree()
        val resources = RtsResources(
            gold = 900.0,
            techPoints = 500.0
        )

        val medievalState = com.example.model.EraProgressCalculator.calculate(
            currentEra = Era.MEDIEVAL_5TH,
            techTree = techTree,
            resources = resources
        )

        assertEquals(Era.MEDIEVAL_5TH, medievalState.currentEra)
        assertEquals(Era.CLASSICAL_GUNPOWDER, medievalState.nextEra)
        assertTrue(medievalState.progressPercent > 0f)
        assertTrue(medievalState.hasRequiredGold)
        assertTrue(medievalState.hasRequiredTech)
        assertTrue(medievalState.isReadyToUpgrade)

        // Pinnacle Era (MODERN)
        val modernState = com.example.model.EraProgressCalculator.calculate(
            currentEra = Era.MODERN,
            techTree = techTree,
            resources = resources
        )
        assertEquals(Era.MODERN, modernState.currentEra)
        assertEquals(null, modernState.nextEra)
        assertEquals(1.0f, modernState.progressPercent, 0.01f)
    }

    @Test
    fun `verify unit tactical context commands and stances`() {
        // Tank has Patrol, Guard, Repair, Hold Fire
        assertTrue(com.example.model.UnitTacticalCommand.PATROL.isAvailableFor(UnitType.TANK))
        assertTrue(com.example.model.UnitTacticalCommand.GUARD.isAvailableFor(UnitType.TANK))
        assertTrue(com.example.model.UnitTacticalCommand.REPAIR.isAvailableFor(UnitType.TANK))
        assertTrue(com.example.model.UnitTacticalCommand.HOLD_FIRE.isAvailableFor(UnitType.TANK))

        // Jet has Patrol, Guard, Hold Fire (Repair not on Jet)
        assertTrue(com.example.model.UnitTacticalCommand.PATROL.isAvailableFor(UnitType.JET))
        assertTrue(com.example.model.UnitTacticalCommand.GUARD.isAvailableFor(UnitType.JET))
        assertTrue(com.example.model.UnitTacticalCommand.HOLD_FIRE.isAvailableFor(UnitType.JET))
        assertFalse(com.example.model.UnitTacticalCommand.REPAIR.isAvailableFor(UnitType.JET))

        // Submarine has Hold Fire, Patrol, Guard, Repair
        assertTrue(com.example.model.UnitTacticalCommand.HOLD_FIRE.isAvailableFor(UnitType.SUBMARINE))
        assertTrue(com.example.model.UnitTacticalCommand.PATROL.isAvailableFor(UnitType.SUBMARINE))

        val unit = RtsUnit(
            id = "test_tank",
            templateId = "tank_mod_tr",
            name = "Altay T2",
            nation = Nation.TURKEY,
            era = Era.MODERN,
            unitType = UnitType.TANK,
            hp = 450f,
            maxHp = 500f,
            attackPower = 110f,
            speed = 45f,
            posX = 200f,
            posY = 200f,
            targetX = 200f,
            targetY = 200f
        )
        assertEquals(com.example.model.UnitStance.AGGRESSIVE, unit.activeStance)
        assertFalse(unit.isHoldingFire)
        assertFalse(unit.isRepairing)
    }

    @Test
    fun `verify post match victory points formula`() {
        val stats = com.example.ui.components.PostMatchStats(
            isVictory = true,
            playerFaction = Nation.TURKEY,
            mapTitle = "Hürmüz Boğazı",
            matchDurationSeconds = 600L,
            mapControlPercentage = 80,
            totalGoldHarvested = 15000.0,
            totalIronHarvested = 6000.0,
            totalOilExtracted = 3000.0,
            enemyUnitsDestroyed = 20,
            friendlyUnitsLost = 4
        )

        assertEquals(800L, stats.mapControlPoints) // 80 * 10
        assertEquals(800L, stats.resourceExtractionPoints) // (15000+6000+3000)/30 = 800
        assertEquals(740L, stats.unitDestructionPoints) // 20*40 - 4*15 = 800 - 60 = 740
        assertEquals(2340L, stats.totalVictoryPoints) // 800 + 800 + 740 = 2340
    }

    @Test
    fun `verify tactical gesture bindings catalog`() {
        val defaults = com.example.model.TacticalGestureCatalog.createDefaultBindings()
        assertTrue(defaults.isNotEmpty())

        val attackMove = defaults.find { it.command == com.example.model.TacticalCommand.ATTACK_MOVE }
        assertEquals(com.example.model.PhysicalGesture.DOUBLE_TAP, attackMove?.gesture)

        val retreat = defaults.find { it.command == com.example.model.TacticalCommand.RETREAT }
        assertEquals(com.example.model.PhysicalGesture.TWO_FINGER_SWIPE_DOWN, retreat?.gesture)
    }

    @Test
    fun `verify seed leaderboard entries integrity`() {
        val list = com.example.data.model.SeedLeaderboardData.sampleLeaderboard
        assertTrue(list.size >= 5)

        val gazi = list.first()
        assertEquals("Gazi Mareşal", gazi.playerName)
        assertEquals("Türkiye", gazi.playerFaction)
        assertTrue(gazi.totalVictoryPoints > 15000L)
        assertEquals("Yüce Başkomutan", gazi.rankTitle)
    }

    @Test
    fun `verify replay frame json serialization and deserialization`() {
        val sampleFrames = com.example.data.local.ReplayJsonSerializer.createSampleReplayFrames(isVictory = true)
        assertTrue(sampleFrames.isNotEmpty())

        val json = com.example.data.local.ReplayJsonSerializer.serializeFrames(sampleFrames)
        assertTrue(json.startsWith("["))
        assertTrue(json.contains("Altay T2"))

        val restored = com.example.data.local.ReplayJsonSerializer.deserializeFrames(json)
        assertEquals(sampleFrames.size, restored.size)
        assertEquals(sampleFrames.first().units.size, restored.first().units.size)
        assertEquals("Altay T2", restored.first().units.first().name)
    }

    @Test
    fun `verify in game notification types and severities`() {
        val researchNotif = com.example.model.InGameNotificationType.RESEARCH_COMPLETE
        assertEquals(com.example.model.NotificationSeverity.TRIUMPH, researchNotif.severity)
        assertEquals("🔬", researchNotif.iconEmoji)

        val unitUnderAttack = com.example.model.InGameNotificationType.UNIT_UNDER_ATTACK
        assertEquals(com.example.model.NotificationSeverity.WARNING, unitUnderAttack.severity)

        val baseSiege = com.example.model.InGameNotificationType.BASE_UNDER_SIEGE
        assertEquals(com.example.model.NotificationSeverity.CRITICAL, baseSiege.severity)
    }

    @Test
    fun `verify room database replay insertion and query`() = kotlinx.coroutines.runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inMemoryDb = androidx.room.Room.inMemoryDatabaseBuilder(
            context,
            com.example.data.local.AppDatabase::class.java
        ).allowMainThreadQueries().build()

        val dao = inMemoryDb.matchReplayDao()
        val testReplay = com.example.data.local.MatchReplayEntity(
            id = "test_replay_001",
            title = "Test Skirmish",
            mapName = "Hürmüz Boğazı",
            factionTitle = "Türkiye",
            enemyFactionTitle = "Koalisyon",
            durationSeconds = 120L,
            totalUnitsInvolved = 10,
            isVictory = true,
            framesJson = "[]"
        )

        dao.insertReplay(testReplay)
        val loaded = dao.getReplayById("test_replay_001")
        assertEquals("Test Skirmish", loaded?.title)
        assertEquals(true, loaded?.isVictory)

        dao.deleteReplayById("test_replay_001")
        val afterDelete = dao.getReplayById("test_replay_001")
        assertEquals(null, afterDelete)

        inMemoryDb.close()
    }

    // ==================== NEW RTS ERA & PRODUCTION ENGINE TESTS ====================
    @Test
    fun `verify engine eras and civilization multipliers`() {
        val bronze = com.example.engine.EngineEra.BRONZE_AGE
        val gunpowder = com.example.engine.EngineEra.GUNPOWDER_AGE
        val industrial = com.example.engine.EngineEra.INDUSTRIAL_AGE
        val modern = com.example.engine.EngineEra.MODERN_AGE

        assertEquals(1, bronze.order)
        assertEquals(2, gunpowder.order)
        assertEquals(3, industrial.order)
        assertEquals(4, modern.order)
        assertEquals("factory_bronze", bronze.visualAssetId)
        assertEquals("factory_modern", modern.visualAssetId)

        val osmanli = com.example.engine.Civilization.OSMANLI
        val roma = com.example.engine.Civilization.ROMA
        val almanya = com.example.engine.Civilization.ALMANYA

        assertEquals(1.15f, osmanli.productionSpeedMultiplier, 0.001f)
        assertEquals(0.90f, roma.costDiscountMultiplier, 0.001f)
        assertEquals(1.12f, almanya.healthMultiplier, 0.001f)
    }

    @Test
    fun `verify polymorphic unit evolution based on era and civilization`() {
        // Gunpowder + Osmanlı -> Yeniçeri with +25% damage bonus
        val yeniceri = com.example.engine.Infantry(
            era = com.example.engine.EngineEra.GUNPOWDER_AGE,
            civilization = com.example.engine.Civilization.OSMANLI
        )
        assertEquals("Yeniçeri Taburu", yeniceri.name)
        // Base damage = 25 * 2 = 50. With Osmanlı Gunpowder bonus (+25%): 50 * 1.25 = 62.5
        assertEquals(62.5f, yeniceri.damage, 0.01f)

        // Modern + Almanya -> Leopard 2A7 with extra health bonus
        val leopard = com.example.engine.Vehicle(
            era = com.example.engine.EngineEra.MODERN_AGE,
            civilization = com.example.engine.Civilization.ALMANYA
        )
        assertEquals("Leopard 2A7 Ağır Muharebe Tankı", leopard.name)
        // Base HP = 280 * 4 = 1120. Almanya civ multiplier = 1.12. Special Modern German Armor (+15%):
        // 1120 * 1.12 * 1.15 = ~1442.56
        assertTrue(leopard.health > 1400f)

        // Bronze + Roma -> Lejyoner with 10% cost discount
        val lejyoner = com.example.engine.Infantry(
            era = com.example.engine.EngineEra.BRONZE_AGE,
            civilization = com.example.engine.Civilization.ROMA
        )
        assertEquals("Roma Lejyoneri", lejyoner.name)
        // Base cost = 80 * 1 = 80. Roma discount 0.90 -> 72
        assertEquals(72, lejyoner.cost)
    }

    @Test
    fun `verify military factory queue and era visual state transition`() {
        val factory = com.example.engine.MilitaryFactory(
            id = "test_factory",
            initialEra = com.example.engine.EngineEra.BRONZE_AGE,
            initialCivilization = com.example.engine.Civilization.OSMANLI
        )

        val initialState = factory.factoryState.value
        assertEquals("factory_bronze", initialState.visualAssetId)
        assertEquals(com.example.engine.EngineEra.BRONZE_AGE, initialState.currentEra)

        // Update era to Modern
        factory.updateEra(com.example.engine.EngineEra.MODERN_AGE)
        val modernState = factory.factoryState.value
        assertEquals("factory_modern", modernState.visualAssetId)
        assertEquals(com.example.engine.EngineEra.MODERN_AGE, modernState.currentEra)

        // Enqueue unit
        val unit = com.example.engine.Infantry(
            era = com.example.engine.EngineEra.MODERN_AGE,
            civilization = com.example.engine.Civilization.OSMANLI
        )
        val enqueued = factory.enqueueUnit(unit)
        assertTrue(enqueued)
        assertTrue(factory.factoryState.value.isProducing)
    }

    @Test
    fun `verify historical commander catalog and passive loadout capacity`() {
        val commanders = com.example.model.CommanderCatalog.createInitialCommanders()
        assertTrue(commanders.size >= 5)

        val ataturk = commanders.find { it.id == "cmd_ataturk" }
        assertTrue(ataturk != null)
        assertEquals("Gazi Mareşal Mustafa Kemal", ataturk!!.name)
        assertEquals(5, ataturk.starRating)
        assertTrue(ataturk.isUnlocked)
        assertTrue(ataturk.equippedSkillIds.size <= 3)

        val fatih = commanders.find { it.id == "cmd_fatih" }
        assertTrue(fatih != null)
        assertEquals("Fatih Sultan Mehmed", fatih!!.name)
        assertTrue(fatih.availableSkills.any { it.name.contains("Şahi") })
    }

    @Test
    fun `verify alliance feed tech research and milestone progression`() {
        val allianceState = com.example.model.AllianceFeedCatalog.createInitialAllianceState()
        assertEquals("TURKISH GLADIATORS", allianceState.allianceName)
        assertEquals("[TURK]", allianceState.allianceTag)
        assertTrue(allianceState.activeTech.targetPoints > 0)
        assertTrue(allianceState.activeTech.progressPercent > 0f)

        // Milestones
        val unlockedMilestones = allianceState.milestones.filter { it.isUnlocked }
        assertTrue(unlockedMilestones.isNotEmpty())

        // Upcoming 50v50 War
        val war = allianceState.upcomingWar
        assertEquals("RED STAR COALITION", war.enemyAllianceName)
        assertEquals(44, war.registeredMembers)
        assertEquals(50, war.maxMembers)
    }

    @Test
    fun `verify building construction mechanics and base expansion`() {
        val vm = com.example.simulation.GameViewModel()
        val initialBuildingCount = vm.uiState.value.buildings.size
        val initialGold = vm.uiState.value.resources.gold

        // Build a new Tank Factory (450 Gold)
        val success = vm.buildNewBuilding(RtsBuildingType.FACTORY)
        assertTrue(success)
        assertEquals(initialBuildingCount + 1, vm.uiState.value.buildings.size)
        assertEquals(initialGold - 450, vm.uiState.value.resources.gold, 0.001)

        val latestBuilding = vm.uiState.value.buildings.last()
        assertEquals(RtsBuildingType.FACTORY, latestBuilding.type)
        assertTrue(latestBuilding.worldX > 0f)
        assertTrue(latestBuilding.worldY > 0f)

        // Build Cold Fusion Power Plant (+150 MW generated)
        val initialProduced = vm.uiState.value.resources.energyProduced
        val powerSuccess = vm.buildNewBuilding(RtsBuildingType.POWER_PLANT)
        assertTrue(powerSuccess)
        assertEquals(initialProduced + 150, vm.uiState.value.resources.energyProduced)
    }

    @Test
    fun `verify 3 slot loadout equipment system on commanders`() {
        val vm = com.example.simulation.GameViewModel()
        val commanders = vm.uiState.value.commanders
        val ataturk = commanders.first { it.id == "cmd_ataturk" }

        // Initial Atatürk loadout
        assertTrue(ataturk.equippedSkillIds.size <= 3)

        // Select Atatürk as active supreme commander
        vm.selectActiveCommander("cmd_ataturk")
        assertEquals("cmd_ataturk", vm.uiState.value.activeCommanderId)
        assertTrue(vm.uiState.value.commanderName.contains("Mustafa Kemal"))
    }

    @Test
    fun `verify resource header food counts and minimap building placements`() {
        val resources = com.example.model.RtsResources(food = 1850.0, gold = 3200.0, oil = 950.0)
        assertEquals(1850, resources.food.toInt())
        assertEquals(3200, resources.gold.toInt())
        assertEquals(950, resources.oil.toInt())

        val vm = com.example.simulation.GameViewModel()
        val buildings = vm.uiState.value.buildings
        assertTrue(buildings.isNotEmpty())
        assertTrue(buildings.any { it.type == RtsBuildingType.HQ })
    }

    @Test
    fun `verify enlarged map camera boundaries and unit tactical command selection`() {
        val vm = com.example.simulation.GameViewModel()

        // Verify camera pan expands up to 1600f / 1100f
        vm.panCamera(2000f, 1500f)
        assertEquals(1600f, vm.uiState.value.cameraOffsetX, 0.01f)
        assertEquals(1100f, vm.uiState.value.cameraOffsetY, 0.01f)

        // Select a unit and execute command
        val firstUnit = vm.uiState.value.units.first { it.isPlayerSide }
        vm.executeUnitCommand(firstUnit.id, com.example.model.UnitTacticalCommand.GUARD)
        val updatedUnit = vm.uiState.value.units.first { it.id == firstUnit.id }
        assertEquals(com.example.model.UnitStance.GUARD, updatedUnit.activeStance)
    }
}



