package com.example.simulation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Era
import com.example.model.GeneralsPower
import com.example.model.Nation
import com.example.model.ProductionOrder
import com.example.model.RtsBuilding
import com.example.model.RtsBuildingType
import com.example.model.RtsCatalog
import com.example.model.RtsResources
import com.example.model.RtsUnit
import com.example.model.RtsUnitTemplate
import com.example.model.UnitType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import com.example.audio.RtsAudioManager
import com.example.audio.UnitAction
import com.example.data.model.LeaderboardEntryDto
import com.example.data.model.MatchHistoryRecordDto
import com.example.data.model.SeedLeaderboardData
import com.example.data.model.SkirmishChatMessageDto
import com.example.data.repository.FirestoreRtsRepository
import com.example.model.GestureBinding
import com.example.model.TacticalGestureCatalog
import com.example.ui.components.PostMatchStats
import com.example.ui.components.ResourceDataPoint
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.MatchReplayEntity
import com.example.data.local.MatchReplayRepository
import com.example.data.local.ReplayJsonSerializer
import com.example.data.local.ReplayUnitSnapshot
import com.example.model.InGameNotification
import com.example.model.InGameNotificationType
import com.example.engine.EngineEra
import com.example.engine.Civilization
import com.example.engine.MilitaryFactory
import com.example.model.HistoricalCommander
import com.example.model.CommanderCatalog
import com.example.model.AllianceState
import com.example.model.AllianceFeedCatalog
import com.example.model.AllianceFeedEvent
import com.example.model.AllianceFeedEventType

data class TacticalLog(
    val id: String,
    val time: String,
    val text: String,
    val isAlert: Boolean = false
)

data class ConflictDensityPoint(
    val timestamp: String,
    val intensity: Int,
    val enemyKills: Int
)

data class GlobalWarStats(
    val totalVictories: Int = 142,
    val totalDefeats: Int = 18,
    val totalBattles: Int = 160,
    val totalEnemiesDestroyed: Int = 380,
    val totalGoldLooted: Int = 24500,
    val activeBattlefieldUnits: Int = 14,
    val skirmishWave: Int = 1,
    val winRatePercent: Int = 88,
    val conflictDensityPoints: List<ConflictDensityPoint> = listOf(
        ConflictDensityPoint("00:00", 12, 4),
        ConflictDensityPoint("02:00", 28, 14),
        ConflictDensityPoint("04:00", 65, 35),
        ConflictDensityPoint("06:00", 92, 52),
        ConflictDensityPoint("08:00", 145, 88),
        ConflictDensityPoint("10:00", 210, 130),
        ConflictDensityPoint("12:00", 180, 115)
    )
)

data class RtsUiState(
    val currentEra: Era = Era.MODERN,
    val selectedNation: Nation = Nation.TURKEY,
    val resources: RtsResources = RtsResources(),
    val buildings: List<RtsBuilding> = emptyList(),
    val selectedBuilding: RtsBuilding? = null,
    val units: List<RtsUnit> = emptyList(),
    val selectedUnitIds: Set<String> = emptySet(),
    val generalsPowers: List<GeneralsPower> = emptyList(),
    val tacticalLogs: List<TacticalLog> = emptyList(),
    val isSimulationRunning: Boolean = true,
    val simulationSpeed: Float = 1.0f,
    val toastMessage: String? = null,
    val isStressTesting: Boolean = false,
    val lastStrikeEffect: StrikeEffect? = null,
    val cameraOffsetX: Float = 0f,
    val cameraOffsetY: Float = 0f,
    val cameraZoom: Float = 1.0f,
    val eraTransitionEffect: com.example.model.EraTransitionEffect? = null,
    val showFactionSelect: Boolean = false,
    val activeFaction: com.example.model.FactionData = com.example.model.FactionCatalog.getFaction(Nation.TURKEY),
    val isPerspective3D: Boolean = false,
    val isSkirmishAiActive: Boolean = true,
    val skirmishAiWave: Int = 1,
    val skirmishBotState: String = "Pusu & Gözetleme",
    val selectedFactoryFilter: RtsBuildingType? = null,
    val isAudioMuted: Boolean = false,
    val isHudVisible: Boolean = true,
    val globalWarStats: GlobalWarStats = GlobalWarStats(),
    val battleLogs: List<com.example.model.BattleLogEntry> = emptyList(),
    val diplomaticNations: List<com.example.model.DiplomaticNation> = com.example.model.WorldSuperpowersCatalog.initial20Nations,
    val activeTradeDeals: List<com.example.model.GlobalTradeDeal> = emptyList(),
    val techTree: List<com.example.model.TechNode> = com.example.model.TechCatalog.createDefaultTechTree(),
    val activeResearchId: String? = null,
    val combatLogs: List<com.example.model.CombatEngagementLog> = emptyList(),
    val sessionCombatStats: com.example.model.SessionCombatStats = com.example.model.SessionCombatStats(),
    val skirmishMaps: List<com.example.model.SkirmishMap> = com.example.model.SkirmishMapCatalog.maps,
    val selectedSkirmishMap: com.example.model.SkirmishMap = com.example.model.SkirmishMapCatalog.maps.first(),
    val controlGroups: List<com.example.model.TacticalControlGroup> = com.example.model.TacticalControlGroupCatalog.createDefaultGroups(),
    val leaderboardEntries: List<LeaderboardEntryDto> = SeedLeaderboardData.sampleLeaderboard,
    val matchHistory: List<MatchHistoryRecordDto> = emptyList(),
    val skirmishChatMessages: List<SkirmishChatMessageDto> = emptyList(),
    val gestureBindings: List<GestureBinding> = TacticalGestureCatalog.createDefaultBindings(),
    val resourceHistoryPoints: List<ResourceDataPoint> = emptyList(),
    val postMatchStats: PostMatchStats? = null,
    val isSubmittingScore: Boolean = false,
    val submissionFeedback: String? = null,
    val isGoogleAuthenticated: Boolean = false,
    val commanderName: String = "Gazi Mareşal",
    val inGameNotifications: List<InGameNotification> = emptyList(),
    val localReplays: List<MatchReplayEntity> = emptyList(),
    val commanders: List<HistoricalCommander> = CommanderCatalog.createInitialCommanders(),
    val activeCommanderId: String = "cmd_ataturk",
    val allianceState: AllianceState = AllianceFeedCatalog.createInitialAllianceState()
)

data class StrikeEffect(
    val x: Float,
    val y: Float,
    val powerName: String,
    val colorHex: Long
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RtsUiState())
    val uiState: StateFlow<RtsUiState> = _uiState.asStateFlow()

    private val repository: FirestoreRtsRepository? = runCatching {
        FirestoreRtsRepository(
            FirebaseFirestore.getInstance("ai-studio-android-novabase-9201d29c-71ac-4e5e-947d-643510dce57c")
        )
    }.getOrNull()

    private var economyTickCounter: Int = 0

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    val coreMilitaryFactory: MilitaryFactory = MilitaryFactory(
        id = "factory_main_base",
        initialEra = EngineEra.MODERN_AGE,
        initialCivilization = Civilization.OSMANLI,
        scope = viewModelScope
    )

    init {
        setupInitialColony()
        refreshGeneralsPowers()
        initFirestoreListeners()
        initMilitaryFactoryEvents()
        startMainLoop()
    }

    private fun initMilitaryFactoryEvents() {
        viewModelScope.launch {
            coreMilitaryFactory.unitProducedEvent.collect { unit ->
                triggerInGameNotification(
                    InGameNotificationType.UNIT_PRODUCED,
                    "🏭 Fabrika Üretimi: ${unit.name} (${unit.unitRole}) tamamlandı!"
                )
                addLog("FABRİKA: ${unit.name} üretildi ve haritaya sevk edildi.", isAlert = true)
            }
        }
    }

    private fun initFirestoreListeners() {
        val repo = repository ?: return
        viewModelScope.launch {
            repo.observeLeaderboard().collect { list ->
                if (list.isNotEmpty()) {
                    _uiState.update { it.copy(leaderboardEntries = list) }
                }
            }
        }
        viewModelScope.launch {
            repo.observeSkirmishChat().collect { msgs ->
                _uiState.update { it.copy(skirmishChatMessages = msgs) }
            }
        }
        val user = repo.currentUser
        if (user != null) {
            _uiState.update {
                it.copy(
                    isGoogleAuthenticated = true,
                    commanderName = if (!user.displayName.isNullOrBlank()) user.displayName!! else it.commanderName
                )
            }
            viewModelScope.launch {
                repo.observeMatchHistory(user.uid).collect { history ->
                    _uiState.update { it.copy(matchHistory = history) }
                }
            }
        }
    }

    private var replayRepository: MatchReplayRepository? = null

    fun initializeLocalDatabase(context: Context) {
        if (replayRepository != null) return
        try {
            val db = AppDatabase.getInstance(context)
            replayRepository = MatchReplayRepository(db.matchReplayDao())
            viewModelScope.launch {
                replayRepository?.seedInitialReplaysIfEmpty()
                replayRepository?.allReplays?.collect { replays ->
                    _uiState.update { it.copy(localReplays = replays) }
                }
            }
        } catch (_: Exception) {}
    }

    fun triggerInGameNotification(
        type: InGameNotificationType,
        message: String,
        targetWorldX: Float? = null,
        targetWorldY: Float? = null
    ) {
        val notif = InGameNotification(
            id = UUID.randomUUID().toString(),
            type = type,
            message = message,
            targetWorldX = targetWorldX,
            targetWorldY = targetWorldY
        )
        _uiState.update { current ->
            // Keep at most 3 active notifications in the stack
            val updated = (listOf(notif) + current.inGameNotifications).take(3)
            current.copy(inGameNotifications = updated)
        }
    }

    fun dismissInGameNotification(id: String) {
        _uiState.update { current ->
            current.copy(inGameNotifications = current.inGameNotifications.filter { it.id != id })
        }
    }

    fun saveCurrentSessionAsReplay() {
        val state = _uiState.value
        val replay = MatchReplayEntity(
            id = UUID.randomUUID().toString(),
            title = "${state.selectedSkirmishMap.title} - ${state.selectedNation.title} Savunması",
            mapName = state.selectedSkirmishMap.title,
            factionTitle = state.selectedNation.title,
            enemyFactionTitle = "Koalisyon Güçleri",
            durationSeconds = economyTickCounter.toLong().coerceAtLeast(45L),
            totalUnitsInvolved = state.units.size,
            isVictory = true,
            recordedTimestamp = System.currentTimeMillis(),
            framesJson = ReplayJsonSerializer.serializeFrames(
                ReplayJsonSerializer.createSampleReplayFrames(isVictory = true)
            )
        )
        viewModelScope.launch {
            replayRepository?.saveReplay(replay)
            _uiState.update { current ->
                current.copy(
                    toastMessage = "Maç tekrarı yerel Room veritabanına kaydedildi!",
                    localReplays = listOf(replay) + current.localReplays
                )
            }
        }
    }

    fun deleteReplay(id: String) {
        viewModelScope.launch {
            replayRepository?.deleteReplay(id)
            _uiState.update { current ->
                current.copy(
                    localReplays = current.localReplays.filter { it.id != id },
                    toastMessage = "Maç tekrarı silindi."
                )
            }
        }
    }

    // ==================== HISTORICAL COMMANDER GALLERY METHODS ====================
    fun selectActiveCommander(commanderId: String) {
        val cmd = _uiState.value.commanders.find { it.id == commanderId } ?: return
        _uiState.update { current ->
            current.copy(
                activeCommanderId = commanderId,
                commanderName = cmd.name,
                toastMessage = "Başkomutan atandı: ${cmd.flagEmoji} ${cmd.name} (${cmd.title})"
            )
        }
        triggerInGameNotification(
            InGameNotificationType.COMMANDER_PROMOTED,
            "🎖️ ${cmd.name} Başkomutanlık makamına geçti! Pasif doktrinler ordunuza uygulandı."
        )
        addLog("KOMUTAN ATAMASI: ${cmd.name} (${cmd.title}) ordunun başına geçti.", isAlert = true)
    }

    fun toggleEquipCommanderSkill(commanderId: String, skillId: String) {
        _uiState.update { current ->
            val updatedCommanders = current.commanders.map { cmd ->
                if (cmd.id == commanderId) {
                    val currentEquipped = cmd.equippedSkillIds.toMutableList()
                    if (currentEquipped.contains(skillId)) {
                        currentEquipped.remove(skillId)
                    } else if (currentEquipped.size < 3) {
                        currentEquipped.add(skillId)
                    }
                    cmd.copy(equippedSkillIds = currentEquipped)
                } else cmd
            }
            current.copy(commanders = updatedCommanders)
        }
    }

    // ==================== ALLIANCE FEED & TECH DONATION METHODS ====================
    fun donateToAllianceTech(amount: Int) {
        val state = _uiState.value
        if (state.resources.gold < amount) {
            _uiState.update { it.copy(toastMessage = "Yetersiz Altın! Bağış için $amount Altın gerekli.") }
            return
        }

        val updatedGold = state.resources.gold - amount
        val currentPoints = state.allianceState.activeTech.currentPoints + amount
        val updatedTech = state.allianceState.activeTech.copy(
            currentPoints = currentPoints.coerceAtMost(state.allianceState.activeTech.targetPoints)
        )
        val newEvent = AllianceFeedEvent(
            timestampFormatted = "Şimdi",
            authorName = state.commanderName,
            authorRole = "General",
            eventType = AllianceFeedEventType.DONATION,
            message = "'${updatedTech.title}' araştırmasına $amount Altın bağışladı.",
            rewardText = "+${amount / 4} İttifak Onur Puanı"
        )
        val updatedState = state.allianceState.copy(
            totalDonationsGold = state.allianceState.totalDonationsGold + amount,
            activeTech = updatedTech,
            feedEvents = listOf(newEvent) + state.allianceState.feedEvents
        )

        _uiState.update { current ->
            current.copy(
                resources = current.resources.copy(gold = updatedGold),
                allianceState = updatedState,
                toastMessage = "İttifak araştırmasına $amount Altın bağışlandı! (+${amount / 4} Onur Puanı)"
            )
        }

        triggerInGameNotification(
            InGameNotificationType.RESEARCH_COMPLETE,
            "🛡️ İttifak araştırmasına $amount Altın aktarıldı! (${updatedTech.title})"
        )
        addLog("İTTİFAK BAĞIŞI: $amount Altın teknoloji fonuna aktarıldı.")
    }

    private fun setupInitialColony() {
        val initialBuildings = listOf(
            RtsBuilding(
                id = "b_hq",
                type = RtsBuildingType.HQ,
                level = 1,
                gridX = 1,
                gridY = 1,
                worldX = 460f,
                worldY = 280f
            ),
            RtsBuilding(
                id = "b_power",
                type = RtsBuildingType.POWER_PLANT,
                level = 1,
                gridX = 0,
                gridY = 1,
                worldX = 220f,
                worldY = 180f
            ),
            RtsBuilding(
                id = "b_supply",
                type = RtsBuildingType.SUPPLY_DEPOT,
                level = 1,
                gridX = 2,
                gridY = 1,
                worldX = 360f,
                worldY = 460f
            ),
            RtsBuilding(
                id = "b_factory",
                type = RtsBuildingType.FACTORY,
                level = 1,
                gridX = 1,
                gridY = 2,
                worldX = 260f,
                worldY = 360f
            ),
            RtsBuilding(
                id = "b_airfield",
                type = RtsBuildingType.AIRFIELD,
                level = 1,
                gridX = 2,
                gridY = 2,
                worldX = 720f,
                worldY = 180f
            ),
            RtsBuilding(
                id = "b_shipyard",
                type = RtsBuildingType.SHIPYARD,
                level = 1,
                gridX = 0,
                gridY = 2,
                worldX = 900f,
                worldY = 420f
            ),
            RtsBuilding(
                id = "b_barracks",
                type = RtsBuildingType.BARRACKS,
                level = 1,
                gridX = 1,
                gridY = 0,
                worldX = 460f,
                worldY = 140f
            ),
            // Düşman Üssü Binaları (Doğu Sektörü / Skirmish Base)
            RtsBuilding(
                id = "b_enemy_hq",
                type = RtsBuildingType.HQ,
                level = 1,
                gridX = 8,
                gridY = 1,
                worldX = 1180f,
                worldY = 260f
            ),
            RtsBuilding(
                id = "b_enemy_factory",
                type = RtsBuildingType.FACTORY,
                level = 1,
                gridX = 8,
                gridY = 2,
                worldX = 1120f,
                worldY = 420f
            ),
            RtsBuilding(
                id = "b_enemy_power",
                type = RtsBuildingType.POWER_PLANT,
                level = 1,
                gridX = 9,
                gridY = 0,
                worldX = 1260f,
                worldY = 160f
            )
        )

        // Initial Units (Modern Era Turkey)
        val initialUnits = listOf(
            RtsUnit(
                id = "u_tank_1",
                templateId = "tank_mod_tr",
                name = "Altay T2 Zırhlı Birlik",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.TANK,
                hp = 520f,
                maxHp = 520f,
                attackPower = 120f,
                speed = 45f,
                posX = 260f,
                posY = 320f,
                targetX = 260f,
                targetY = 320f,
                isPlayerSide = true
            ),
            RtsUnit(
                id = "u_jet_1",
                templateId = "jet_mod_tr",
                name = "Bayraktar SİHA (TB2 & KIZILELMA)",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.JET,
                hp = 380f,
                maxHp = 380f,
                attackPower = 140f,
                speed = 88f,
                posX = 420f,
                posY = 180f,
                targetX = 420f,
                targetY = 180f,
                isPlayerSide = true
            ),
            RtsUnit(
                id = "u_sub_1",
                templateId = "sub_mod_tr",
                name = "TCG Anadolu Amfibi Sancak Gemisi",
                nation = Nation.TURKEY,
                era = Era.MODERN,
                unitType = UnitType.SUBMARINE,
                hp = 700f,
                maxHp = 700f,
                attackPower = 150f,
                speed = 40f,
                posX = 160f,
                posY = 420f,
                targetX = 160f,
                targetY = 420f,
                isPlayerSide = true
            ),
            // Enemy Raider Tank
            RtsUnit(
                id = "u_enemy_1",
                templateId = "tank_mod_ru",
                name = "Düşman T-90M Öncü Tankı",
                nation = Nation.RUSSIA,
                era = Era.MODERN,
                unitType = UnitType.TANK,
                hp = 480f,
                maxHp = 480f,
                attackPower = 95f,
                speed = 40f,
                posX = 880f,
                posY = 360f,
                targetX = 700f,
                targetY = 360f,
                isPlayerSide = false
            )
        )

        _uiState.update {
            it.copy(
                buildings = initialBuildings,
                units = initialUnits,
                resources = RtsResources(gold = 4500.0, techPoints = 1200.0, energyProduced = 200, energyConsumed = 110)
            )
        }
        addLog("C&C Generals: Epoch RTS Motoru başlatıldı. Komuta üssü aktif.")
        addLog("SKIRMISH AI BOT: Doğu üssünde bilgisayar rakip konuşlandırıldı.", isAlert = true)
    }

    private fun startMainLoop() {
        // High frequency game update loop (20 fps = 50ms)
        viewModelScope.launch {
            while (isActive) {
                delay(50L)
                if (_uiState.value.isSimulationRunning) {
                    val dt = 0.05f * _uiState.value.simulationSpeed
                    updateProductionQueues(dt)
                    updateUnitsMovementAndCombat(dt)
                    updateGeneralsPowersCooldown(dt)
                    updateTradeDeals(dt)
                    updateTechResearch(dt)
                }
            }
        }

        // Low frequency economy tick (1 fps) with real-time queue depletion
        viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                if (_uiState.value.isSimulationRunning) {
                    tickEconomy()
                }
            }
        }

        // Skirmish AI Bot Karar & Üretim Döngüsü (Her 7 saniyede bir)
        viewModelScope.launch {
            while (isActive) {
                delay(7000L)
                if (_uiState.value.isSimulationRunning && _uiState.value.isSkirmishAiActive) {
                    tickSkirmishAiBot()
                }
            }
        }
    }

    private fun tickEconomy() {
        val state = _uiState.value
        val efficiency = state.resources.energyEfficiency
        val speed = state.simulationSpeed

        // Active production queue depletion tracking
        val activeQueues = state.buildings.sumOf { it.queue.size }
        val goldDrain = if (activeQueues > 0) activeQueues * 22.0 else 0.0
        val ironDrain = if (activeQueues > 0) activeQueues * 14.0 else 0.0

        val goldIncome = 35.0 * efficiency * speed
        val ironIncome = 20.0 * efficiency * speed
        val techIncome = 8.0 * efficiency * speed

        val netGold = goldIncome - (goldDrain * speed)
        val netIron = ironIncome - (ironDrain * speed)

        val newGold = (state.resources.gold + netGold).coerceIn(0.0, state.resources.maxGold)
        val newIron = (state.resources.iron + netIron).coerceIn(0.0, state.resources.maxIron)
        val newTech = (state.resources.techPoints + techIncome).coerceIn(0.0, state.resources.maxTechPoints)

        economyTickCounter++
        val shouldRecordHistory = economyTickCounter % 4 == 0 || state.resourceHistoryPoints.isEmpty()
        val updatedHistory = if (shouldRecordHistory) {
            val minutesElapsed = economyTickCounter / 60f
            val timeLabel = "%02d:%02d".format((economyTickCounter / 60), (economyTickCounter % 60))
            val pt = ResourceDataPoint(
                timeLabel = timeLabel,
                minute = minutesElapsed,
                gold = newGold,
                iron = newIron,
                oil = state.resources.oil
            )
            (state.resourceHistoryPoints + pt).takeLast(40)
        } else {
            state.resourceHistoryPoints
        }

        _uiState.update { current ->
            current.copy(
                resources = current.resources.copy(
                    gold = newGold,
                    iron = newIron,
                    techPoints = newTech,
                    goldDrainRate = goldDrain,
                    ironDrainRate = ironDrain
                ),
                resourceHistoryPoints = updatedHistory
            )
        }
    }

    // ==================== C&C GENERALS PRODUCTION QUEUE LOGIC ====================
    fun enqueueUnit(buildingId: String, template: RtsUnitTemplate) {
        val state = _uiState.value
        val building = state.buildings.find { it.id == buildingId } ?: return

        // 1. Constraint: Check building allowed unit types
        if (!building.type.allowedUnitTypes.contains(template.unitType)) {
            _uiState.update {
                it.copy(toastMessage = "HATA: ${building.type.title} sadece ${building.type.allowedUnitTypes.joinToString { t -> t.title }} üretebilir!")
            }
            return
        }

        // 2. Constraint: Check max queue capacity (5 items)
        if (building.queue.size >= building.maxQueueCapacity) {
            _uiState.update {
                it.copy(toastMessage = "Üretim kuyruğu dolu! (Maksimum 5 sipariş)")
            }
            return
        }

        // 3. Constraint: Check funds
        if (state.resources.gold < template.costGold) {
            _uiState.update {
                it.copy(toastMessage = "Yetersiz fon! (${template.costGold} Altın gerekli)")
            }
            return
        }

        // Deduct gold & enqueue order
        val newGold = state.resources.gold - template.costGold
        val newOrder = ProductionOrder(
            orderId = UUID.randomUUID().toString(),
            unitTemplate = template,
            timeRemaining = template.productionTimeSeconds.toFloat(),
            totalTime = template.productionTimeSeconds.toFloat()
        )

        val updatedBuildings = state.buildings.map { b ->
            if (b.id == buildingId) {
                val newQueue = b.queue.toMutableList().apply { add(newOrder) }
                b.copy(queue = newQueue)
            } else b
        }

        _uiState.update {
            it.copy(
                resources = it.resources.copy(gold = newGold),
                buildings = updatedBuildings,
                toastMessage = "${template.name} kuyruğa eklendi (${building.type.title})"
            )
        }
        addLog("${building.type.title}: ${template.name} siparişi alındı. (${template.costGold} Altın)")
    }

    fun cancelProductionOrder(buildingId: String, orderIndex: Int) {
        val state = _uiState.value
        val building = state.buildings.find { it.id == buildingId } ?: return
        if (orderIndex < 0 || orderIndex >= building.queue.size) return

        val orderToCancel = building.queue[orderIndex]
        val refundGold = orderToCancel.unitTemplate.costGold.toDouble()

        val updatedBuildings = state.buildings.map { b ->
            if (b.id == buildingId) {
                val newQueue = b.queue.toMutableList().apply { removeAt(orderIndex) }
                b.copy(queue = newQueue)
            } else b
        }

        _uiState.update {
            it.copy(
                resources = it.resources.copy(gold = it.resources.gold + refundGold),
                buildings = updatedBuildings,
                toastMessage = "Sipariş iptal edildi. +${refundGold.toInt()} Altın iade edildi."
            )
        }
        addLog("İptal: ${orderToCancel.unitTemplate.name} üretimi durduruldu, ${refundGold.toInt()} Altın iade.")
    }

    private fun updateProductionQueues(dt: Float) {
        val state = _uiState.value
        val efficiency = state.resources.energyEfficiency
        val spawnedUnits = mutableListOf<RtsUnit>()
        var queueUpdated = false

        val updatedBuildings = state.buildings.map { b ->
            if (b.queue.isNotEmpty()) {
                queueUpdated = true
                val newQueue = b.queue.toMutableList()
                val currentOrder = newQueue[0]
                currentOrder.timeRemaining -= dt * efficiency

                if (currentOrder.timeRemaining <= 0f) {
                    // Production Complete! Dequeue and Spawn Unit on Map
                    newQueue.removeAt(0)
                    val template = currentOrder.unitTemplate

                    // Spawn position offset from building
                    val spawnX = (150f + b.gridX * 160f) + (Math.random() * 40 - 20).toFloat()
                    val spawnY = (180f + b.gridY * 120f) + 60f

                    val newUnit = RtsUnit(
                        id = "u_${System.currentTimeMillis()}_${(Math.random()*100).toInt()}",
                        templateId = template.id,
                        name = template.name,
                        nation = template.nation,
                        era = template.era,
                        unitType = template.unitType,
                        hp = template.maxHp,
                        maxHp = template.maxHp,
                        attackPower = template.attackPower,
                        speed = template.speed,
                        posX = spawnX,
                        posY = spawnY,
                        targetX = spawnX + 80f,
                        targetY = spawnY,
                        isPlayerSide = true,
                        isMoving = true
                    )
                    spawnedUnits.add(newUnit)
                    addLog("ÜRETİM TAMAMLANDI: ${template.name} haritaya sevk edildi!", isAlert = true)
                }
                b.copy(queue = newQueue)
            } else b
        }

        if (queueUpdated || spawnedUnits.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    buildings = updatedBuildings,
                    units = it.units + spawnedUnits
                )
            }
        }
    }

    // ==================== ERA TRANSITION (DÖNEM GEÇİŞİ) SYSTEM ====================
    fun upgradeToNextEra() {
        val state = _uiState.value
        val nextEra = when (state.currentEra) {
            Era.MEDIEVAL_5TH -> Era.CLASSICAL_GUNPOWDER
            Era.CLASSICAL_GUNPOWDER -> Era.INDUSTRIAL
            Era.INDUSTRIAL -> Era.MODERN
            Era.MODERN -> {
                _uiState.update { it.copy(toastMessage = "Zaten en gelişmiş çağdasınız: Modern Warfare!") }
                return
            }
        }

        if (state.resources.gold < nextEra.upgradeCostGold || state.resources.techPoints < nextEra.upgradeCostTech) {
            _uiState.update {
                it.copy(toastMessage = "Yetersiz kaynak! ${nextEra.upgradeCostGold} Altın ve ${nextEra.upgradeCostTech} Teknoloji Puanı gerekli.")
            }
            return
        }

        // Deduct Upgrade Cost
        val newGold = state.resources.gold - nextEra.upgradeCostGold
        val newTech = state.resources.techPoints - nextEra.upgradeCostTech

        // AUTOMATIC UNIT UPGRADE / CONVERSION (Tüm eski birimleri yeni çağın muadillerine dönüştür)
        val allTemplates = RtsCatalog.getUnitTemplates()
        val upgradedUnits = state.units.map { unit ->
            if (unit.isPlayerSide) {
                // Find modern equivalent template
                val currentTemplate = allTemplates.find { it.id == unit.templateId }
                val targetTemplate = if (currentTemplate?.modernEquivalentId != null) {
                    allTemplates.find { it.id == currentTemplate.modernEquivalentId }
                } else {
                    allTemplates.find { it.unitType == unit.unitType && it.era == nextEra && it.nation == state.selectedNation }
                } ?: currentTemplate

                if (targetTemplate != null) {
                    unit.copy(
                        name = targetTemplate.name,
                        templateId = targetTemplate.id,
                        era = nextEra,
                        maxHp = targetTemplate.maxHp,
                        hp = targetTemplate.maxHp,
                        attackPower = targetTemplate.attackPower,
                        speed = targetTemplate.speed
                    )
                } else unit
            } else unit
        }

        _uiState.update {
            it.copy(
                currentEra = nextEra,
                resources = it.resources.copy(gold = newGold, techPoints = newTech),
                units = upgradedUnits,
                eraTransitionEffect = com.example.model.EraTransitionEffect(targetEra = nextEra),
                toastMessage = "ÇAĞ ATLANDI: ${nextEra.title}! Eski birimler ve binalar modernize edildi!"
            )
        }

        triggerInGameNotification(
            InGameNotificationType.ERA_ADVANCED,
            "👑 ${nextEra.title} dönemi başladı! Tüm üs yapıları ve askeri envanter modernize edildi."
        )

        val engineEra = when (nextEra) {
            Era.MEDIEVAL_5TH -> EngineEra.BRONZE_AGE
            Era.CLASSICAL_GUNPOWDER -> EngineEra.GUNPOWDER_AGE
            Era.INDUSTRIAL -> EngineEra.INDUSTRIAL_AGE
            Era.MODERN -> EngineEra.MODERN_AGE
        }
        coreMilitaryFactory.updateEra(engineEra)

        refreshGeneralsPowers()
        addLog("DÖNEM GEÇİŞİ: Koloni başarıyla ${nextEra.title} çağına geçti! Tüm birimler modernize edildi.", isAlert = true)
    }

    fun setEraDirect(era: Era) {
        _uiState.update {
            it.copy(
                currentEra = era,
                eraTransitionEffect = com.example.model.EraTransitionEffect(targetEra = era)
            )
        }
        refreshGeneralsPowers()
        addLog("Dönem değiştirildi: ${era.title}")
    }

    fun dismissEraTransition() {
        _uiState.update { it.copy(eraTransitionEffect = null) }
    }

    fun panCamera(dx: Float, dy: Float) {
        _uiState.update {
            it.copy(
                cameraOffsetX = (it.cameraOffsetX + dx).coerceIn(-1600f, 1600f),
                cameraOffsetY = (it.cameraOffsetY + dy).coerceIn(-1100f, 1100f)
            )
        }
    }

    fun setCameraZoom(zoom: Float) {
        _uiState.update {
            it.copy(cameraZoom = zoom.coerceIn(0.45f, 2.4f))
        }
    }

    fun resetCamera() {
        _uiState.update {
            it.copy(cameraOffsetX = 0f, cameraOffsetY = 0f, cameraZoom = 1.0f)
        }
    }

    fun centerCameraOn(worldX: Float, worldY: Float) {
        val zoom = _uiState.value.cameraZoom
        val targetOffsetX = ((1500f - worldX) * zoom).coerceIn(-1600f, 1600f)
        val targetOffsetY = ((1000f - worldY) * zoom).coerceIn(-1100f, 1100f)
        _uiState.update {
            it.copy(
                cameraOffsetX = targetOffsetX,
                cameraOffsetY = targetOffsetY
            )
        }
    }

    // ==================== REAL-TIME BASE EXPANSION & BUILDING CONSTRUCTION ====================
    fun buildNewBuilding(type: RtsBuildingType, targetX: Float? = null, targetY: Float? = null): Boolean {
        val state = _uiState.value
        if (state.resources.gold < type.baseCostGold) {
            _uiState.update {
                it.copy(toastMessage = "Yetersiz Altın! ${type.title} için ${type.baseCostGold} Altın gerekli.")
            }
            return false
        }

        // Deduct cost and apply energy changes
        val updatedGold = state.resources.gold - type.baseCostGold
        var updatedProduced = state.resources.energyProduced
        var updatedConsumed = state.resources.energyConsumed
        if (type.energyCost < 0) {
            updatedProduced += kotlin.math.abs(type.energyCost)
        } else {
            updatedConsumed += type.energyCost
        }

        // Placement coordinates: If target provided, use it; otherwise compute open position in the player sector
        val existingBuildingsCount = state.buildings.size
        val posX = targetX ?: (240f + (existingBuildingsCount % 5) * 170f)
        val posY = targetY ?: (240f + (existingBuildingsCount / 5) * 160f)

        val newBuilding = RtsBuilding(
            id = "b_${type.name.lowercase()}_${System.currentTimeMillis() % 10000}",
            type = type,
            gridX = existingBuildingsCount % 6,
            gridY = existingBuildingsCount / 6,
            worldX = posX,
            worldY = posY,
            isConstructing = false
        )

        _uiState.update { current ->
            current.copy(
                resources = current.resources.copy(
                    gold = updatedGold,
                    energyProduced = updatedProduced,
                    energyConsumed = updatedConsumed
                ),
                buildings = current.buildings + newBuilding,
                selectedBuilding = newBuilding,
                toastMessage = "İNŞAAT TAMAMLANDI: ${type.title} üsse konuşlandırıldı!"
            )
        }

        triggerInGameNotification(
            InGameNotificationType.ERA_ADVANCED,
            "🏗️ ${type.title} başarıyla inşa edildi! Yeni üretim ve savunma kapasitesi aktif."
        )
        addLog("İNŞAAT: ${type.title} ($posX, $posY) koordinatında kuruldu.", isAlert = true)
        return true
    }

    fun setNation(nation: Nation) {
        val faction = com.example.model.FactionCatalog.getFaction(nation)
        selectFactionAndInitialize(faction)
    }

    fun toggleFactionSelect(show: Boolean) {
        _uiState.update { it.copy(showFactionSelect = show) }
    }

    fun selectFactionAndInitialize(faction: com.example.model.FactionData) {
        val allTemplates = RtsCatalog.getUnitTemplates()
        val templatesForEra = allTemplates.filter { it.era == faction.startingEra }

        // Spawn appropriate starting units for this faction & era
        val mainUnitTemplate = templatesForEra.find { it.unitType == UnitType.TANK && it.nation == faction.nation }
            ?: templatesForEra.find { it.unitType == UnitType.TANK }
            ?: templatesForEra.find { it.unitType == UnitType.SIEGE }
            ?: templatesForEra.first()

        val airOrNavalTemplate = templatesForEra.find { (it.unitType == UnitType.JET || it.unitType == UnitType.SUBMARINE) && it.nation == faction.nation }
            ?: templatesForEra.find { it.unitType == UnitType.JET || it.unitType == UnitType.SUBMARINE }
            ?: templatesForEra.first()

        val newUnits = listOf(
            RtsUnit(
                id = "u_starter_1",
                templateId = mainUnitTemplate.id,
                name = mainUnitTemplate.name,
                nation = faction.nation,
                era = faction.startingEra,
                unitType = mainUnitTemplate.unitType,
                hp = mainUnitTemplate.maxHp,
                maxHp = mainUnitTemplate.maxHp,
                attackPower = mainUnitTemplate.attackPower,
                speed = mainUnitTemplate.speed,
                posX = 260f,
                posY = 340f,
                targetX = 260f,
                targetY = 340f,
                isPlayerSide = true
            ),
            RtsUnit(
                id = "u_starter_2",
                templateId = airOrNavalTemplate.id,
                name = airOrNavalTemplate.name,
                nation = faction.nation,
                era = faction.startingEra,
                unitType = airOrNavalTemplate.unitType,
                hp = airOrNavalTemplate.maxHp,
                maxHp = airOrNavalTemplate.maxHp,
                attackPower = airOrNavalTemplate.attackPower,
                speed = airOrNavalTemplate.speed,
                posX = if (airOrNavalTemplate.unitType == UnitType.SUBMARINE) 900f else 680f,
                posY = if (airOrNavalTemplate.unitType == UnitType.SUBMARINE) 420f else 180f,
                targetX = if (airOrNavalTemplate.unitType == UnitType.SUBMARINE) 900f else 680f,
                targetY = if (airOrNavalTemplate.unitType == UnitType.SUBMARINE) 420f else 180f,
                isPlayerSide = true
            ),
            // Enemy unit for tactical combat
            RtsUnit(
                id = "u_enemy_1",
                templateId = "tank_enemy",
                name = "Düşman Keşif Kolu",
                nation = if (faction.nation == Nation.USA) Nation.RUSSIA else Nation.USA,
                era = faction.startingEra,
                unitType = UnitType.TANK,
                hp = 400f,
                maxHp = 400f,
                attackPower = 80f,
                speed = 40f,
                posX = 700f,
                posY = 300f,
                targetX = 700f,
                targetY = 300f,
                isPlayerSide = false
            )
        )

        val newResources = RtsResources(
            gold = faction.startingGold,
            maxGold = 20000.0,
            techPoints = faction.startingTechPoints,
            maxTechPoints = 10000.0,
            energyProduced = faction.startingEnergyProduced,
            energyConsumed = faction.startingEnergyConsumed
        )

        val newPowers = RtsCatalog.getGeneralsPowers(faction.nation, faction.startingEra)

        _uiState.update {
            it.copy(
                selectedNation = faction.nation,
                currentEra = faction.startingEra,
                activeFaction = faction,
                resources = newResources,
                units = newUnits,
                generalsPowers = newPowers,
                showFactionSelect = false,
                selectedUnitIds = emptySet(),
                selectedBuilding = null,
                eraTransitionEffect = com.example.model.EraTransitionEffect(targetEra = faction.startingEra),
                toastMessage = "${faction.nation.flagEmoji} ${faction.nation.title} SEÇİLDİ! (${faction.startingEra.title})"
            )
        }

        val civ = when (faction.nation) {
            Nation.TURKEY -> Civilization.OSMANLI
            Nation.CHINA -> Civilization.CIN
            Nation.USA -> Civilization.BRITANYA
            Nation.RUSSIA -> Civilization.ALMANYA
        }
        coreMilitaryFactory.updateCivilization(civ)

        addLog("ASİMETRİK SEFER BAŞLADI: ${faction.nation.flagEmoji} ${faction.nation.title} (${faction.startingEra.title}). Pasif: ${faction.factionPassive}", isAlert = true)
    }

    private fun refreshGeneralsPowers() {
        val powers = RtsCatalog.getGeneralsPowers(_uiState.value.selectedNation, _uiState.value.currentEra)
        _uiState.update { it.copy(generalsPowers = powers) }
    }

    // ==================== GENERALS POWERS TACTICAL STRIKES ====================
    fun triggerGeneralsPower(powerId: String) {
        val state = _uiState.value
        val power = state.generalsPowers.find { it.id == powerId } ?: return

        if (!power.isReady) {
            _uiState.update { it.copy(toastMessage = "${power.name} bekleme süresinde! (${power.currentCooldownSeconds}s)") }
            return
        }

        if (state.resources.gold < power.costGold) {
            _uiState.update { it.copy(toastMessage = "Yetersiz Altın! (${power.costGold} Altın gerekli)") }
            return
        }

        // Deduct cost and set cooldown
        val newGold = state.resources.gold - power.costGold
        val updatedPowers = state.generalsPowers.map { p ->
            if (p.id == powerId) p.copy(currentCooldownSeconds = p.cooldownSeconds) else p
        }

        // Target center of enemy or center of canvas
        val targetX = 640f + (Math.random() * 80 - 40).toFloat()
        val targetY = 260f + (Math.random() * 80 - 40).toFloat()

        // Damage enemy units in radius (Area of Effect)
        val blastRadius = 140f
        val damage = 220f
        val updatedUnits = state.units.map { u ->
            if (!u.isPlayerSide) {
                val dist = Math.hypot((u.posX - targetX).toDouble(), (u.posY - targetY).toDouble()).toFloat()
                if (dist <= blastRadius) {
                    u.copy(hp = (u.hp - damage).coerceAtLeast(0f))
                } else u
            } else u
        }.filter { it.hp > 0f }

        val strikeEffect = StrikeEffect(
            x = targetX,
            y = targetY,
            powerName = power.name,
            colorHex = when (power.strikeType) {
                "AIRSTRIKE" -> 0xFFFF3366
                "BARRAGE" -> 0xFFFFB703
                "EMP" -> 0xFF00E5FF
                else -> 0xFF00F5D4
            }
        )

        _uiState.update {
            it.copy(
                resources = it.resources.copy(gold = newGold),
                generalsPowers = updatedPowers,
                units = updatedUnits,
                lastStrikeEffect = strikeEffect,
                toastMessage = "GENERAL GÜCÜ TETİKLENDİ: ${power.name}!"
            )
        }
        addLog("GENERAL GÜCÜ: ${power.name} hedef bölgeye icra edildi! Düşman unsurları vuruldu.", isAlert = true)
    }

    private fun updateGeneralsPowersCooldown(dt: Float) {
        val updated = _uiState.value.generalsPowers.map { p ->
            if (p.currentCooldownSeconds > 0) {
                p.copy(currentCooldownSeconds = (p.currentCooldownSeconds - 1).coerceAtLeast(0))
            } else p
        }
        _uiState.update { it.copy(generalsPowers = updated) }
    }

    // ==================== MULTI-UNIT SELECTION & MOVEMENT ====================
    fun selectUnitsInRect(minX: Float, minY: Float, maxX: Float, maxY: Float) {
        val selected = _uiState.value.units.filter { u ->
            u.isPlayerSide && u.posX >= minX && u.posX <= maxX && u.posY >= minY && u.posY <= maxY
        }.map { it.id }.toSet()

        _uiState.update { it.copy(selectedUnitIds = selected) }
    }

    fun selectSingleUnit(unitId: String?) {
        _uiState.update {
            it.copy(selectedUnitIds = if (unitId != null) setOf(unitId) else emptySet())
        }
    }

    fun orderMoveSelectedUnits(targetX: Float, targetY: Float) {
        val state = _uiState.value
        if (state.selectedUnitIds.isEmpty()) return

        var offsetIdx = 0
        val updated = state.units.map { u ->
            if (state.selectedUnitIds.contains(u.id)) {
                // Formation spread so 100 units don't stack on exact same pixel
                val angle = (offsetIdx * 0.7f)
                val dist = (offsetIdx * 14f).coerceAtMost(60f)
                val spreadX = targetX + Math.cos(angle.toDouble()).toFloat() * dist
                val spreadY = targetY + Math.sin(angle.toDouble()).toFloat() * dist
                offsetIdx++
                u.copy(targetX = spreadX, targetY = spreadY, isMoving = true)
            } else u
        }

        val firstSelected = state.units.firstOrNull { state.selectedUnitIds.contains(it.id) }
        firstSelected?.let { RtsAudioManager.playUnitSound(it.unitType, UnitAction.MOVE) }

        _uiState.update {
            it.copy(
                units = updated,
                toastMessage = "${state.selectedUnitIds.size} birim intikal emri aldı."
            )
        }
    }

    private fun updateUnitsMovementAndCombat(dt: Float) {
        val state = _uiState.value
        val playerUnits = state.units.filter { it.isPlayerSide }.toMutableList()
        val enemyUnits = state.units.filter { !it.isPlayerSide }.toMutableList()

        // 1. Pathfinding & Movement
        val movedUnits = state.units.map { u ->
            var px = u.posX
            var py = u.posY
            var tx = u.targetX
            var ty = u.targetY

            // Skirmish AI Bot Pathfinding for Enemy Units
            if (!u.isPlayerSide && state.isSkirmishAiActive) {
                // Check if any player unit or building is nearby to acquire target
                val nearestPlayerUnit = playerUnits.minByOrNull { pu ->
                    Math.hypot((pu.posX - px).toDouble(), (pu.posY - py).toDouble())
                }
                val distToPlayer = nearestPlayerUnit?.let {
                    Math.hypot((it.posX - px).toDouble(), (it.posY - py).toDouble()).toFloat()
                } ?: Float.MAX_VALUE

                if (distToPlayer < 240f && nearestPlayerUnit != null) {
                    // Lock onto player unit
                    tx = nearestPlayerUnit.posX
                    ty = nearestPlayerUnit.posY
                } else if (px > 740f) {
                    // Waypoint 1: Nehir Geçiş Köprüsü
                    tx = 720f
                    ty = 360f
                } else {
                    // Waypoint 2: Oyuncu Komuta Merkezi (HQ)
                    tx = 460f
                    ty = 280f
                }
            }

            val dx = tx - px
            val dy = ty - py
            val dist = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

            var moving = false
            var headingForward = u.isPatrolHeadingForward
            var nextTx = tx
            var nextTy = ty

            // Patrol ping-pong turnaround logic
            if (u.activeStance == com.example.model.UnitStance.PATROL) {
                if (dist <= 10f) {
                    headingForward = !headingForward
                    nextTx = if (headingForward) u.patrolTargetX else u.patrolOriginX
                    nextTy = if (headingForward) u.patrolTargetY else u.patrolOriginY
                }
            }

            if (dist > 6f && !u.isRepairing) {
                val step = u.speed * dt
                px += (dx / dist) * step
                py += (dy / dist) * step
                moving = true
            }
            u.copy(
                posX = px,
                posY = py,
                targetX = nextTx,
                targetY = nextTy,
                isMoving = moving,
                isPatrolHeadingForward = headingForward
            )
        }

        // 2. Real-Time Combat Engagements
        val combatRange = 130f
        var earnedGold = 0.0
        var earnedTech = 0.0
        var destroyedEnemyCount = 0
        val promotedUnitAlerts = mutableListOf<String>()

        var totalDamageDealtThisTick = 0f
        var totalDamageTakenThisTick = 0f
        var enemyCasualtiesThisTick = 0
        var friendlyCasualtiesThisTick = 0
        val newCombatLogs = mutableListOf<com.example.model.CombatEngagementLog>()
        val timeNow = timeFormat.format(java.util.Date())

        val survivedUnits = movedUnits.map { u ->
            var currentHp = u.hp
            var attacking = false
            var currentXp = u.xp
            var kills = u.killsCount
            var stance = u.activeStance
            var repairing = u.isRepairing

            // Unit self/field repair tick (+18 HP per second)
            if (repairing) {
                currentHp = (currentHp + 18f * dt).coerceAtMost(u.maxHp)
                if (currentHp >= u.maxHp) {
                    repairing = false
                    stance = com.example.model.UnitStance.AGGRESSIVE
                }
            }

            if (u.isPlayerSide) {
                // Player unit attacks nearest enemy in range (unless Holding Fire)
                if (!u.isHoldingFire && !repairing) {
                    val nearbyEnemy = movedUnits.firstOrNull { !it.isPlayerSide && Math.hypot((it.posX - u.posX).toDouble(), (it.posY - u.posY).toDouble()) <= combatRange }
                    if (nearbyEnemy != null) {
                        attacking = true
                        currentXp += 1 // Gains XP during continuous combat
                    }
                }
                // Take damage from enemy attackers in range
                val enemyAttackers = movedUnits.filter { !it.isPlayerSide && Math.hypot((it.posX - u.posX).toDouble(), (it.posY - u.posY).toDouble()) <= combatRange }
                for (att in enemyAttackers) {
                    val dmg = att.attackPower * dt * 0.35f
                    currentHp -= dmg
                    totalDamageTakenThisTick += dmg
                    if (Math.random() < 0.08) {
                        newCombatLogs.add(
                            com.example.model.CombatEngagementLog(
                                timestamp = timeNow,
                                attackerName = att.name,
                                targetName = u.name,
                                damage = dmg * 6f, // representative engagement burst
                                isAttackerPlayer = false,
                                isCasualty = false,
                                type = com.example.model.CombatLogType.DAMAGE_TAKEN,
                                weaponSystem = "Düşman Ateşi",
                                highlightColorHex = 0xFFFF453A
                            )
                        )
                        if (Math.random() < 0.40) {
                            triggerInGameNotification(
                                InGameNotificationType.UNIT_UNDER_ATTACK,
                                "⚔️ ${u.name} düşman ateşi altında!",
                                u.posX,
                                u.posY
                            )
                        }
                    }
                }
            } else {
                // Enemy unit attacks player units in range
                val nearbyPlayer = movedUnits.firstOrNull { it.isPlayerSide && Math.hypot((it.posX - u.posX).toDouble(), (it.posY - u.posY).toDouble()) <= combatRange }
                if (nearbyPlayer != null) {
                    attacking = true
                }
                // Take damage from player attackers in range (only if player is not holding fire)
                val playerAttackers = movedUnits.filter { it.isPlayerSide && !it.isHoldingFire && !it.isRepairing && Math.hypot((it.posX - u.posX).toDouble(), (it.posY - u.posY).toDouble()) <= combatRange }
                for (att in playerAttackers) {
                    val dmg = att.attackPower * dt * 0.4f
                    currentHp -= dmg
                    totalDamageDealtThisTick += dmg
                    if (Math.random() < 0.08) {
                        newCombatLogs.add(
                            com.example.model.CombatEngagementLog(
                                timestamp = timeNow,
                                attackerName = att.name,
                                targetName = u.name,
                                damage = dmg * 6f,
                                isAttackerPlayer = true,
                                isCasualty = false,
                                type = com.example.model.CombatLogType.DAMAGE_DEALT,
                                weaponSystem = "${att.name} Ana Bataryası",
                                highlightColorHex = 0xFF00F5D4
                            )
                        )
                    }
                }
            }

            val updatedUnit = u.copy(
                hp = currentHp,
                isAttacking = attacking,
                xp = currentXp,
                killsCount = kills,
                activeStance = stance,
                isRepairing = repairing
            )
            if (updatedUnit.isPlayerSide && currentXp > u.xp) {
                val leveledUp = updatedUnit.gainXp(0)
                if (leveledUp) {
                    promotedUnitAlerts.add("${updatedUnit.name} rütbe atladı: ${updatedUnit.rank.title} ${updatedUnit.rankBadge}!")
                    addBattleLog(
                        com.example.model.BattleLogCategory.RANK,
                        "⭐ RÜTBE TERFİSİ: ${updatedUnit.name} yeni rütbeye ulaştı ➔ ${updatedUnit.rank.title} ${updatedUnit.rankBadge}! (+${updatedUnit.rank.bonusDamagePct}% Hasar, +${updatedUnit.rank.bonusHpPct}% Zırh/HP)",
                        0xFFFFD700
                    )
                }
            }
            updatedUnit
        }.filter { u ->
            if (u.hp <= 0f) {
                // Trigger Death Sound for destroyed unit
                RtsAudioManager.playUnitSound(u.unitType, UnitAction.DEATH)

                if (!u.isPlayerSide) {
                    earnedGold += 150.0
                    earnedTech += 40.0
                    destroyedEnemyCount++
                    enemyCasualtiesThisTick++
                    newCombatLogs.add(
                        com.example.model.CombatEngagementLog(
                            timestamp = timeNow,
                            attackerName = "Dost Kuvvetler",
                            targetName = u.name,
                            damage = u.maxHp,
                            isAttackerPlayer = true,
                            isCasualty = true,
                            type = com.example.model.CombatLogType.ENEMY_KILLED,
                            weaponSystem = "İmha Edici Taarruz",
                            highlightColorHex = 0xFF00F5D4
                        )
                    )
                    addBattleLog(
                        com.example.model.BattleLogCategory.COMBAT,
                        "💥 ZAFER: Düşman ${u.name} imha edildi! (+150 Altın, +40 TP)",
                        0xFF00F5D4
                    )
                } else {
                    friendlyCasualtiesThisTick++
                    newCombatLogs.add(
                        com.example.model.CombatEngagementLog(
                            timestamp = timeNow,
                            attackerName = "Düşman Birlikleri",
                            targetName = u.name,
                            damage = u.maxHp,
                            isAttackerPlayer = false,
                            isCasualty = true,
                            type = com.example.model.CombatLogType.FRIENDLY_LOST,
                            weaponSystem = "Düşman Ağır Saldırısı",
                            highlightColorHex = 0xFFFF3366
                        )
                    )
                    addBattleLog(
                        com.example.model.BattleLogCategory.COMBAT,
                        "⚠️ AĞIR KAYIP: Dost ${u.name} muharebede imha edildi!",
                        0xFFFF3366
                    )
                }
                false // Destroyed
            } else true
        }

        // Award +50 XP and +1 Kill count to nearby player units if enemy destroyed
        val finalUnits = if (destroyedEnemyCount > 0) {
            survivedUnits.map { u ->
                if (u.isPlayerSide && u.isAttacking) {
                    u.killsCount += destroyedEnemyCount
                    val leveledUp = u.gainXp(50 * destroyedEnemyCount)
                    if (leveledUp) {
                        promotedUnitAlerts.add("${u.name} rütbe atladı: ${u.rank.title} ${u.rankBadge}!")
                        addBattleLog(
                            com.example.model.BattleLogCategory.RANK,
                            "⭐ RÜTBE TERFİSİ: ${u.name} düşman yok ederek ${u.rank.title} ${u.rankBadge} rütbesine terfi etti! (+${u.rank.bonusDamagePct}% Hasar)",
                            0xFFFFD700
                        )
                    }
                }
                u
            }
        } else survivedUnits

        // Trigger occasional attack sounds during firefight
        val anyAttacker = finalUnits.firstOrNull { it.isAttacking }
        if (anyAttacker != null && Math.random() < 0.12) {
            RtsAudioManager.playUnitSound(anyAttacker.unitType, UnitAction.ATTACK)
        }

        // Base Siege Alarm: Check if enemy unit penetrates into HQ perimeter (x=460f, y=280f)
        val enemyNearHq = finalUnits.firstOrNull { !it.isPlayerSide && Math.hypot((it.posX - 460f).toDouble(), (it.posY - 280f).toDouble()) <= 200.0 }
        if (enemyNearHq != null && Math.random() < 0.04) {
            triggerInGameNotification(
                InGameNotificationType.BASE_UNDER_SIEGE,
                "🚨 ÜS KUŞATMA ALTINDA: ${enemyNearHq.name} Komuta Merkezine yaklaştı!",
                460f,
                280f
            )
        }

        val updatedCombatStats = state.sessionCombatStats.copy(
            totalDamageDealt = state.sessionCombatStats.totalDamageDealt + totalDamageDealtThisTick,
            totalDamageTaken = state.sessionCombatStats.totalDamageTaken + totalDamageTakenThisTick,
            enemyCasualties = state.sessionCombatStats.enemyCasualties + enemyCasualtiesThisTick,
            friendlyCasualties = state.sessionCombatStats.friendlyCasualties + friendlyCasualtiesThisTick,
            totalEngagements = state.sessionCombatStats.totalEngagements + (if (totalDamageDealtThisTick > 0 || totalDamageTakenThisTick > 0) 1 else 0)
        )

        val mergedCombatLogs = if (newCombatLogs.isNotEmpty()) {
            (newCombatLogs + state.combatLogs).take(60)
        } else {
            state.combatLogs
        }

        if (destroyedEnemyCount > 0) {
            val newGold = (state.resources.gold + earnedGold).coerceAtMost(state.resources.maxGold)
            val newTech = (state.resources.techPoints + earnedTech).coerceAtMost(state.resources.maxTechPoints)
            val updatedStats = state.globalWarStats.copy(
                totalVictories = state.globalWarStats.totalVictories + destroyedEnemyCount,
                totalEnemiesDestroyed = state.globalWarStats.totalEnemiesDestroyed + destroyedEnemyCount,
                totalGoldLooted = state.globalWarStats.totalGoldLooted + earnedGold.toInt(),
                totalBattles = state.globalWarStats.totalBattles + destroyedEnemyCount,
                activeBattlefieldUnits = finalUnits.size
            )

            val promotionToast = promotedUnitAlerts.firstOrNull()
            _uiState.update {
                it.copy(
                    units = finalUnits,
                    resources = it.resources.copy(gold = newGold, techPoints = newTech),
                    globalWarStats = updatedStats,
                    sessionCombatStats = updatedCombatStats,
                    combatLogs = mergedCombatLogs,
                    toastMessage = promotionToast ?: "HEDEF İMHA EDİLDİ! (+$earnedGold Altın, +$earnedTech TP)"
                )
            }
            addLog("ÇATIŞMA RAPORU: $destroyedEnemyCount düşman unsuru etkisiz hale getirildi. (+${earnedGold.toInt()} Altın)", isAlert = true)
        } else {
            val promotionToast = promotedUnitAlerts.firstOrNull()
            _uiState.update {
                it.copy(
                    units = finalUnits,
                    sessionCombatStats = updatedCombatStats,
                    combatLogs = mergedCombatLogs,
                    toastMessage = promotionToast ?: it.toastMessage
                )
            }
        }
    }

    fun toggleSoundMute() {
        val newMuted = !_uiState.value.isAudioMuted
        RtsAudioManager.setMuted(newMuted)
        _uiState.update {
            it.copy(
                isAudioMuted = newMuted,
                toastMessage = if (newMuted) "🔇 SESLER KAPATILDI" else "🔊 SESLER AÇILDI"
            )
        }
    }

    // ==================== SKIRMISH AI DECISION ENGINE ====================
    private fun tickSkirmishAiBot() {
        val state = _uiState.value
        val nextWave = state.skirmishAiWave + 1

        // 1. Düşman Binalarını Kontrol Et ve Eksikse Kur
        val enemyBuildings = state.buildings.filter { !it.id.startsWith("b_") || it.id.startsWith("b_enemy_") }
        val hasEnemyFactory = state.buildings.any { it.id == "b_enemy_factory" }
        val hasEnemyAirfield = state.buildings.any { it.id == "b_enemy_airfield" }

        val newBuildings = state.buildings.toMutableList()
        if (!hasEnemyFactory) {
            newBuildings.add(
                RtsBuilding(
                    id = "b_enemy_factory",
                    type = RtsBuildingType.FACTORY,
                    level = 1,
                    worldX = 1120f,
                    worldY = 420f
                )
            )
            addLog("SKIRMISH BOT: Düşman yeni bir Tank Fabrikası inşa etti! (Doğu Sektörü)", isAlert = true)
        } else if (!hasEnemyAirfield && nextWave >= 3) {
            newBuildings.add(
                RtsBuilding(
                    id = "b_enemy_airfield",
                    type = RtsBuildingType.AIRFIELD,
                    level = 1,
                    worldX = 1240f,
                    worldY = 380f
                )
            )
            addLog("SKIRMISH BOT: Düşman ileri Hava Üssü konuşlandırdı! Jet saldırısı bekleniyor.", isAlert = true)
        }

        // 2. Yeni Düşman Taarruz Dalgası Üret
        val spawnX = 1140f + (Math.random() * 40 - 20).toFloat()
        val spawnY = 400f + (Math.random() * 60 - 30).toFloat()

        val enemyUnitType = if (nextWave % 2 == 0) UnitType.JET else UnitType.TANK
        val enemyName = when (state.currentEra) {
            Era.MEDIEVAL_5TH -> "Düşman Slav Süvarisi"
            Era.CLASSICAL_GUNPOWDER -> "Düşman Kuşatma Topçusu"
            Era.INDUSTRIAL -> "Düşman T-34 Taarruz Kolu"
            Era.MODERN -> if (enemyUnitType == UnitType.JET) "Düşman Su-57 Felon Jeti" else "Düşman T-90M Proryv Tankı"
        }

        val newEnemy = RtsUnit(
            id = "enemy_wave_${nextWave}_${UUID.randomUUID().toString().take(4)}",
            templateId = "enemy_template",
            name = enemyName,
            nation = Nation.RUSSIA,
            era = state.currentEra,
            unitType = enemyUnitType,
            hp = 420f + (nextWave * 20f),
            maxHp = 420f + (nextWave * 20f),
            attackPower = 80f + (nextWave * 10f),
            speed = if (enemyUnitType == UnitType.JET) 75f else 42f,
            posX = spawnX,
            posY = spawnY,
            targetX = 720f, // Köprü geçişine doğru
            targetY = 360f,
            isPlayerSide = false,
            isMoving = true
        )

        _uiState.update {
            it.copy(
                buildings = newBuildings,
                units = it.units + newEnemy,
                skirmishAiWave = nextWave,
                skirmishBotState = "Dalga $nextWave: Taarruz Kolu İlerliyor",
                toastMessage = "SKIRMISH AI: Düşman Dalga $nextWave taarruza geçti!"
            )
        }
        addLog("SKIRMISH AI TAARRUZU: $enemyName nehir köprüsüne doğru intikal ediyor! (Dalga $nextWave)", isAlert = true)
    }

    fun toggle3DMode() {
        val new3D = !_uiState.value.isPerspective3D
        _uiState.update {
            it.copy(
                isPerspective3D = new3D,
                toastMessage = if (new3D) "🎮 3D PERSPEKTİF MODUNA GEÇİLDİ (C&C Generals)" else "🗺️ 2D TAKTİK UYDU MODUNA GEÇİLDİ"
            )
        }
        addLog(if (new3D) "GRAFİK MOTORU: 3D Generals Perspektif Render Pipeline aktif edildi." else "GRAFİK MOTORU: 2D Taktik Uydu Haritası aktif edildi.")
    }

    fun toggleSkirmishAi() {
        val newAi = !_uiState.value.isSkirmishAiActive
        _uiState.update {
            it.copy(
                isSkirmishAiActive = newAi,
                toastMessage = if (newAi) "🤖 SKIRMISH AI AKTİF EDİLDİ" else "⏸️ SKIRMISH AI DURDURULDU"
            )
        }
    }

    fun selectFactoryFilter(type: RtsBuildingType?) {
        _uiState.update { it.copy(selectedFactoryFilter = type) }
    }

    // ==================== 100+ UNIT STRESS TEST (SPATIAL GRID BENCHMARK) ====================
    fun trigger100UnitStressTest() {
        val stressUnits = mutableListOf<RtsUnit>()
        val nation = _uiState.value.selectedNation
        val era = _uiState.value.currentEra

        for (i in 0 until 60) {
            stressUnits.add(
                RtsUnit(
                    id = "stress_tank_$i",
                    templateId = "tank_mod_tr",
                    name = "${nation.uniqueTankName} #$i",
                    nation = nation,
                    era = era,
                    unitType = UnitType.TANK,
                    hp = 500f,
                    maxHp = 500f,
                    attackPower = 100f,
                    speed = (35 + Math.random() * 20).toFloat(),
                    posX = (100f + Math.random() * 300f).toFloat(),
                    posY = (150f + Math.random() * 250f).toFloat(),
                    targetX = (400f + Math.random() * 300f).toFloat(),
                    targetY = (150f + Math.random() * 250f).toFloat(),
                    isPlayerSide = true,
                    isMoving = true
                )
            )
        }

        for (j in 0 until 40) {
            stressUnits.add(
                RtsUnit(
                    id = "stress_jet_$j",
                    templateId = "jet_mod_tr",
                    name = "${nation.uniqueJetName} #$j",
                    nation = nation,
                    era = era,
                    unitType = UnitType.JET,
                    hp = 350f,
                    maxHp = 350f,
                    attackPower = 130f,
                    speed = (80 + Math.random() * 30).toFloat(),
                    posX = (150f + Math.random() * 300f).toFloat(),
                    posY = (100f + Math.random() * 200f).toFloat(),
                    targetX = (500f + Math.random() * 250f).toFloat(),
                    targetY = (100f + Math.random() * 200f).toFloat(),
                    isPlayerSide = true,
                    isMoving = true
                )
            )
        }

        _uiState.update {
            it.copy(
                units = it.units + stressUnits,
                isStressTesting = true,
                toastMessage = "100+ BİRİM STRES TESTİ AKTİF EDİLDİ! (Spatial Partitioning devrede)"
            )
        }
        addLog("PERFORMANS TESTİ: Ekrana 100 tank ve jet konuşlandırıldı. Spatial Grid 60 FPS optimize edildi.", isAlert = true)
    }

    fun selectBuilding(building: RtsBuilding?) {
        _uiState.update { it.copy(selectedBuilding = building) }
    }

    fun setSimulationRunning(running: Boolean) {
        _uiState.update { it.copy(isSimulationRunning = running) }
    }

    fun setSimulationSpeed(speed: Float) {
        _uiState.update { it.copy(simulationSpeed = speed) }
    }

    private fun addLog(text: String, isAlert: Boolean = false) {
        val log = TacticalLog(
            id = UUID.randomUUID().toString(),
            time = timeFormat.format(Date()),
            text = text,
            isAlert = isAlert
        )
        _uiState.update { it.copy(tacticalLogs = (listOf(log) + it.tacticalLogs).take(30)) }
    }

    fun clearToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    // ==================== BATTLE LOG SYSTEM ====================
    fun addBattleLog(category: com.example.model.BattleLogCategory, message: String, colorHex: Long = 0xFF00E5FF) {
        val timestamp = timeFormat.format(Date())
        val entry = com.example.model.BattleLogEntry(
            timestamp = timestamp,
            category = category,
            message = message,
            highlightColorHex = colorHex
        )
        _uiState.update { current ->
            current.copy(
                battleLogs = (listOf(entry) + current.battleLogs).take(80)
            )
        }
    }

    fun clearBattleLogs() {
        _uiState.update { it.copy(battleLogs = emptyList()) }
    }

    // ==================== GLOBAL MILITARY MARKET & DIPLOMACY ====================
    fun improveDiplomacy(nationId: String, costGold: Int = 350) {
        val state = _uiState.value
        if (state.resources.gold < costGold) {
            _uiState.update { it.copy(toastMessage = "Diplomatik heyet için $costGold Altın gerekli!") }
            return
        }
        val updatedNations = state.diplomaticNations.map { nat ->
            if (nat.id == nationId) {
                val newScore = (nat.relationshipScore + 25).coerceAtMost(100)
                nat.copy(relationshipScore = newScore)
            } else nat
        }
        val targetNation = updatedNations.find { it.id == nationId }
        _uiState.update {
            it.copy(
                resources = it.resources.copy(gold = it.resources.gold - costGold),
                diplomaticNations = updatedNations,
                toastMessage = "${targetNation?.flagEmoji} ${targetNation?.name} ile ilişkiler +25 gelişti! (${targetNation?.relationshipStatus})"
            )
        }
        addBattleLog(
            com.example.model.BattleLogCategory.TRADE,
            "🤝 DİPLOMASİ: ${targetNation?.flagEmoji} ${targetNation?.name} ülkesine diplomatik ticaret heyeti gönderildi. İlişki puanı: ${targetNation?.relationshipScore}/100",
            0xFFFFB703
        )
    }

    fun purchaseForeignMilitaryHardware(nation: com.example.model.DiplomaticNation) {
        val state = _uiState.value
        if (!nation.isAllianceReady) {
            _uiState.update { it.copy(toastMessage = "TİCARET AMBARGOSU! ${nation.name} ile en az +50 Dostluk İlişkisi Gereklidir!") }
            return
        }
        if (state.resources.gold < nation.exportCostGold) {
            _uiState.update { it.copy(toastMessage = "Yetersiz Altın! (${nation.exportCostGold} Altın gerekli)") }
            return
        }
        if (state.resources.oil < nation.exportCostOil) {
            _uiState.update { it.copy(toastMessage = "Yetersiz Petrol! (${nation.exportCostOil} Petrol gerekli)") }
            return
        }

        // Lojistik teslimat süresi: normal üretim süresinden %50 daha uzun
        val deliverySeconds = (nation.exportProductionTime * 1.5f)

        val deal = com.example.model.GlobalTradeDeal(
            dealId = "deal_${System.currentTimeMillis()}",
            sellerNationName = nation.name,
            sellerFlag = nation.flagEmoji,
            unitName = nation.exportUnitName,
            unitType = nation.exportUnitType,
            costGold = nation.exportCostGold,
            costOil = nation.exportCostOil,
            deliveryTimeRemaining = deliverySeconds,
            totalDeliveryTime = deliverySeconds,
            attackPower = nation.exportAttackPower,
            hp = nation.exportHp,
            stealthMultiplier = nation.stealthMultiplier
        )

        _uiState.update {
            it.copy(
                resources = it.resources.copy(
                    gold = it.resources.gold - nation.exportCostGold,
                    oil = it.resources.oil - nation.exportCostOil
                ),
                activeTradeDeals = it.activeTradeDeals + deal,
                toastMessage = "${nation.flagEmoji} ${nation.exportUnitName} satın alındı! Kargo yola çıktı (%.0fs)".format(deliverySeconds)
            )
        }

        addBattleLog(
            com.example.model.BattleLogCategory.TRADE,
            "🚢 SİLAH TİCARETİ: ${nation.flagEmoji} ${nation.name} menşeli '${nation.exportUnitName}' satın alındı! Lojistik intikal başladı.",
            0xFF00F5D4
        )
    }

    fun cancelTradeDeal(dealId: String) {
        val state = _uiState.value
        val deal = state.activeTradeDeals.find { it.dealId == dealId } ?: return
        val refundGold = deal.costGold.toDouble()
        val refundOil = deal.costOil.toDouble()

        _uiState.update {
            it.copy(
                resources = it.resources.copy(
                    gold = it.resources.gold + refundGold,
                    oil = it.resources.oil + refundOil
                ),
                activeTradeDeals = it.activeTradeDeals.filter { d -> d.dealId != dealId },
                toastMessage = "${deal.unitName} sevkiyatı iptal edildi. Kaynaklar iade edildi."
            )
        }

        addBattleLog(
            com.example.model.BattleLogCategory.TRADE,
            "❌ İPTAL: ${deal.sellerFlag} ${deal.unitName} askeri siparişi iptal edildi. (${deal.costGold} Altın, ${deal.costOil} Petrol iade)",
            0xFFFF3366
        )
    }

    private fun updateTradeDeals(dt: Float) {
        val state = _uiState.value
        if (state.activeTradeDeals.isEmpty()) return

        val speed = state.simulationSpeed
        val completedDeals = mutableListOf<com.example.model.GlobalTradeDeal>()
        val ongoingDeals = mutableListOf<com.example.model.GlobalTradeDeal>()

        for (deal in state.activeTradeDeals) {
            val remaining = deal.deliveryTimeRemaining - (dt * speed)
            if (remaining <= 0f) {
                completedDeals.add(deal)
            } else {
                ongoingDeals.add(deal.copy(deliveryTimeRemaining = remaining))
            }
        }

        if (completedDeals.isNotEmpty()) {
            val newlySpawned = completedDeals.map { deal ->
                val spawnX = 460f + (Math.random() * 60 - 30).toFloat()
                val spawnY = 280f + (Math.random() * 60 - 30).toFloat()
                RtsUnit(
                    id = "imported_${System.currentTimeMillis()}_${(Math.random() * 100).toInt()}",
                    templateId = "imported_${deal.unitName.replace(" ", "_")}",
                    name = "${deal.sellerFlag} ${deal.unitName}",
                    nation = state.selectedNation,
                    era = state.currentEra,
                    unitType = deal.unitType,
                    hp = deal.hp,
                    maxHp = deal.hp,
                    attackPower = deal.attackPower,
                    speed = 45f,
                    posX = spawnX,
                    posY = spawnY,
                    targetX = spawnX + 70f,
                    targetY = spawnY,
                    isPlayerSide = true,
                    isMoving = true,
                    xp = 25,
                    rank = com.example.model.UnitRank.RECRUIT
                )
            }

            _uiState.update {
                it.copy(
                    activeTradeDeals = ongoingDeals,
                    units = it.units + newlySpawned,
                    toastMessage = "LOJİSTİK TESLİMAT: ${completedDeals.size} yabancı askeri birim üssünüze ulaştı!"
                )
            }

            completedDeals.forEach { deal ->
                addBattleLog(
                    com.example.model.BattleLogCategory.TRADE,
                    "📦 TESLİMAT TAMAMLANDI: ${deal.sellerFlag} ${deal.sellerNationName} menşeli '${deal.unitName}' üssünüze indi ve hizmete girdi!",
                    0xFF00F5D4
                )
            }
        } else {
            _uiState.update { it.copy(activeTradeDeals = ongoingDeals) }
        }
    }

    // ==================== TECHNOLOGY RESEARCH (AR-GE) SYSTEM ====================
    private fun updateTechResearch(dt: Float) {
        val state = _uiState.value
        val activeId = state.activeResearchId ?: return
        val activeTech = state.techTree.find { it.id == activeId && it.isResearching } ?: return

        val newRemaining = (activeTech.timeRemaining - dt).coerceAtLeast(0f)
        val newProgress = (1f - (newRemaining / activeTech.durationSeconds)).coerceIn(0f, 1f)

        if (newRemaining <= 0f) {
            // Research finished!
            val updatedTree = state.techTree.map { node ->
                if (node.id == activeId) {
                    node.copy(
                        isResearched = true,
                        isResearching = false,
                        progressPercent = 1f,
                        timeRemaining = 0f
                    )
                } else node
            }
            _uiState.update {
                it.copy(
                    techTree = updatedTree,
                    activeResearchId = null,
                    toastMessage = "🔬 TEKNOLOJİ TAMAMLANDI: ${activeTech.name}!"
                )
            }
            triggerInGameNotification(
                InGameNotificationType.RESEARCH_COMPLETE,
                "🔬 ${activeTech.name} teknolojisi tamamlandı ve askeri üretime açıldı!"
            )
            addBattleLog(
                category = com.example.model.BattleLogCategory.BUILDING,
                message = "🔬 Teknoloji Araştırması Tamamlandı: ${activeTech.name} açıldı!",
                colorHex = 0xFF00E5FF
            )
        } else {
            val updatedTree = state.techTree.map { node ->
                if (node.id == activeId) {
                    node.copy(
                        progressPercent = newProgress,
                        timeRemaining = newRemaining
                    )
                } else node
            }
            _uiState.update { it.copy(techTree = updatedTree) }
        }
    }

    fun startResearch(techId: String) {
        val state = _uiState.value
        if (state.activeResearchId != null) {
            _uiState.update { it.copy(toastMessage = "Zaten devam eden bir teknoloji araştırması var!") }
            return
        }
        val tech = state.techTree.find { it.id == techId } ?: return
        if (tech.isResearched) return

        if (state.resources.techPoints < tech.costTechPoints ||
            state.resources.gold < tech.costGold ||
            state.resources.iron < tech.costIron) {
            _uiState.update { it.copy(toastMessage = "Yetersiz kaynak! TP: ${tech.costTechPoints}, Altın: ${tech.costGold}, Demir: ${tech.costIron} gerekli.") }
            return
        }

        val newTechPoints = state.resources.techPoints - tech.costTechPoints
        val newGold = state.resources.gold - tech.costGold
        val newIron = state.resources.iron - tech.costIron

        val updatedTree = state.techTree.map { node ->
            if (node.id == techId) {
                node.copy(
                    isResearching = true,
                    progressPercent = 0f,
                    timeRemaining = node.durationSeconds.toFloat()
                )
            } else node
        }

        _uiState.update {
            it.copy(
                techTree = updatedTree,
                activeResearchId = techId,
                resources = it.resources.copy(
                    techPoints = newTechPoints,
                    gold = newGold,
                    iron = newIron
                ),
                toastMessage = "🔬 ${tech.name} araştırması başlatıldı (${tech.durationSeconds}s)!"
            )
        }
        addBattleLog(
            category = com.example.model.BattleLogCategory.BUILDING,
            message = "🔬 ${tech.name} araştırması başladı.",
            colorHex = 0xFF00E5FF
        )
    }

    fun cancelResearch(techId: String) {
        val state = _uiState.value
        val tech = state.techTree.find { it.id == techId && it.isResearching } ?: return
        val refundTech = tech.costTechPoints * 0.75
        val refundGold = tech.costGold * 0.75
        val refundIron = tech.costIron * 0.75

        val updatedTree = state.techTree.map { node ->
            if (node.id == techId) {
                node.copy(
                    isResearching = false,
                    progressPercent = 0f,
                    timeRemaining = 0f
                )
            } else node
        }

        _uiState.update {
            it.copy(
                techTree = updatedTree,
                activeResearchId = null,
                resources = it.resources.copy(
                    techPoints = (it.resources.techPoints + refundTech).coerceAtMost(it.resources.maxTechPoints),
                    gold = (it.resources.gold + refundGold).coerceAtMost(it.resources.maxGold),
                    iron = (it.resources.iron + refundIron).coerceAtMost(it.resources.maxIron)
                ),
                toastMessage = "Araştırma iptal edildi (%75 kaynak iadesi yapıldı)."
            )
        }
    }

    fun toggleHudVisibility() {
        _uiState.update { it.copy(isHudVisible = !it.isHudVisible) }
    }

    fun clearCombatLogs() {
        _uiState.update { it.copy(combatLogs = emptyList()) }
    }

    fun selectSkirmishMap(map: com.example.model.SkirmishMap) {
        _uiState.update {
            it.copy(
                selectedSkirmishMap = map,
                toastMessage = "SAVAŞ SAHASI: ${map.title} seçildi!"
            )
        }
        addBattleLog(
            category = com.example.model.BattleLogCategory.TERRITORY,
            message = "🗺️ Savaş Sahası Değiştirildi: '${map.title}' (${map.geographicRegion}) seçildi. ${map.tacticalTrait}.",
            colorHex = map.accentColorHex
        )
    }

    // ==================== TACTICAL UNIT CONTROL GROUPS (QUICK SELECTION & COMMANDS) ====================
    fun assignSelectedUnitsToGroup(groupId: Int) {
        val state = _uiState.value
        val selectedPlayerUnitIds = state.units
            .filter { state.selectedUnitIds.contains(it.id) && it.isPlayerSide && it.hp > 0 }
            .map { it.id }
            .toSet()

        if (selectedPlayerUnitIds.isEmpty()) {
            _uiState.update { it.copy(toastMessage = "Önce haritada dost birimleri seçin!") }
            return
        }

        val updatedGroups = state.controlGroups.map { group ->
            if (group.id == groupId) {
                group.copy(unitIds = selectedPlayerUnitIds)
            } else group
        }

        _uiState.update {
            it.copy(
                controlGroups = updatedGroups,
                toastMessage = "Grup $groupId oluşturuldu: ${selectedPlayerUnitIds.size} birlik atandı."
            )
        }
        addBattleLog(
            category = com.example.model.BattleLogCategory.COMBAT,
            message = "🎖️ Taktik Grup $groupId Atandı: ${selectedPlayerUnitIds.size} birlik kaydedildi.",
            colorHex = 0xFF00F5D4
        )
    }

    fun selectControlGroup(groupId: Int) {
        val state = _uiState.value
        val group = state.controlGroups.find { it.id == groupId } ?: return
        val aliveUnitIds = group.getAliveUnits(state.units).map { it.id }.toSet()

        if (aliveUnitIds.isEmpty()) {
            _uiState.update { it.copy(toastMessage = "Grup $groupId boş veya tüm birimleri imha edildi!") }
            return
        }

        val groupUnits = state.units.filter { aliveUnitIds.contains(it.id) }
        val avgX = groupUnits.map { it.posX }.average().toFloat()
        val avgY = groupUnits.map { it.posY }.average().toFloat()

        _uiState.update {
            it.copy(
                selectedUnitIds = aliveUnitIds,
                selectedBuilding = null,
                cameraOffsetX = 400f - avgX,
                cameraOffsetY = 250f - avgY,
                toastMessage = "Grup $groupId seçildi (${aliveUnitIds.size} Birlik)"
            )
        }
    }

    fun clearControlGroup(groupId: Int) {
        _uiState.update { state ->
            val updated = state.controlGroups.map { g ->
                if (g.id == groupId) g.copy(unitIds = emptySet()) else g
            }
            state.copy(controlGroups = updated, toastMessage = "Grup $groupId temizlendi.")
        }
    }

    fun executeGroupCommand(groupId: Int, command: com.example.model.TacticalGroupCommand) {
        val state = _uiState.value
        val group = state.controlGroups.find { it.id == groupId } ?: return
        val aliveUnits = group.getAliveUnits(state.units)

        if (aliveUnits.isEmpty()) {
            _uiState.update { it.copy(toastMessage = "Grup $groupId boş!") }
            return
        }

        val aliveIds = aliveUnits.map { it.id }.toSet()

        when (command) {
            com.example.model.TacticalGroupCommand.ATTACK_MOVE -> {
                val targetX = state.selectedSkirmishMap.enemyBaseWorldX
                val targetY = state.selectedSkirmishMap.enemyBaseWorldY
                val updatedUnits = state.units.map { u ->
                    if (aliveIds.contains(u.id)) {
                        u.copy(targetX = targetX, targetY = targetY)
                    } else u
                }
                _uiState.update {
                    it.copy(
                        units = updatedUnits,
                        selectedUnitIds = aliveIds,
                        toastMessage = "⚔️ Grup $groupId Taarruza Geçti! Hedef: Düşman Üssü"
                    )
                }
                addBattleLog(
                    category = com.example.model.BattleLogCategory.COMBAT,
                    message = "⚔️ Grup $groupId taarruz emri aldı (${aliveUnits.size} birim).",
                    colorHex = 0xFFFF453A
                )
            }
            com.example.model.TacticalGroupCommand.HOLD_POSITION -> {
                val updatedUnits = state.units.map { u ->
                    if (aliveIds.contains(u.id)) {
                        u.copy(targetX = u.posX, targetY = u.posY)
                    } else u
                }
                _uiState.update {
                    it.copy(
                        units = updatedUnits,
                        selectedUnitIds = aliveIds,
                        toastMessage = "🛡️ Grup $groupId Siper Aldı (Mevzi Savunması)"
                    )
                }
                addBattleLog(
                    category = com.example.model.BattleLogCategory.COMBAT,
                    message = "🛡️ Grup $groupId siper pozisyonuna geçti.",
                    colorHex = 0xFFFFB703
                )
            }
            com.example.model.TacticalGroupCommand.RETREAT_TO_HQ -> {
                val hqX = state.selectedSkirmishMap.baseWorldX
                val hqY = state.selectedSkirmishMap.baseWorldY
                val updatedUnits = state.units.mapIndexed { idx, u ->
                    if (aliveIds.contains(u.id)) {
                        val offsetAngle = (idx * 0.8f)
                        u.copy(
                            targetX = hqX + (kotlin.math.cos(offsetAngle) * 60f),
                            targetY = hqY + (kotlin.math.sin(offsetAngle) * 60f)
                        )
                    } else u
                }
                _uiState.update {
                    it.copy(
                        units = updatedUnits,
                        selectedUnitIds = aliveIds,
                        toastMessage = "🏠 Grup $groupId Üsse Geri Çekiliyor!"
                    )
                }
                addBattleLog(
                    category = com.example.model.BattleLogCategory.COMBAT,
                    message = "🏠 Grup $groupId üsse geri çekilme intikali başlattı.",
                    colorHex = 0xFF38BDF8
                )
            }
            com.example.model.TacticalGroupCommand.SCATTER -> {
                val count = aliveUnits.size.coerceAtLeast(1)
                val updatedUnits = state.units.mapIndexed { idx, u ->
                    if (aliveIds.contains(u.id)) {
                        val angle = (idx.toFloat() / count.toFloat()) * (2f * Math.PI.toFloat())
                        val scatterDist = 110f
                        u.copy(
                            targetX = (u.posX + kotlin.math.cos(angle) * scatterDist).coerceIn(100f, 1300f),
                            targetY = (u.posY + kotlin.math.sin(angle) * scatterDist).coerceIn(80f, 820f)
                        )
                    } else u
                }
                _uiState.update {
                    it.copy(
                        units = updatedUnits,
                        selectedUnitIds = aliveIds,
                        toastMessage = "💥 Grup $groupId Saçılma Düzenine Geçti!"
                    )
                }
                addBattleLog(
                    category = com.example.model.BattleLogCategory.COMBAT,
                    message = "💥 Grup $groupId hava ve topçu savunması için saçıldı.",
                    colorHex = 0xFFA78BFA
                )
            }
        }
    }

    // ==================== SPECIFIC UNIT TACTICAL COMMANDS (CONTEXT MENU) ====================
    fun executeUnitCommand(unitId: String, command: com.example.model.UnitTacticalCommand) {
        val state = _uiState.value
        val unit = state.units.find { it.id == unitId } ?: return

        val updatedUnits = state.units.map { u ->
            if (u.id == unitId) {
                when (command) {
                    com.example.model.UnitTacticalCommand.PATROL -> {
                        val patrolDistance = if (u.unitType == com.example.model.UnitType.JET) 220f else 130f
                        val patrolX = (u.posX + patrolDistance).coerceIn(120f, 1280f)
                        val patrolY = (u.posY + 30f).coerceIn(100f, 800f)
                        u.copy(
                            activeStance = com.example.model.UnitStance.PATROL,
                            patrolOriginX = u.posX,
                            patrolOriginY = u.posY,
                            patrolTargetX = patrolX,
                            patrolTargetY = patrolY,
                            targetX = patrolX,
                            targetY = patrolY,
                            isPatrolHeadingForward = true,
                            isMoving = true,
                            isHoldingFire = false,
                            isRepairing = false
                        )
                    }
                    com.example.model.UnitTacticalCommand.GUARD -> {
                        u.copy(
                            activeStance = com.example.model.UnitStance.GUARD,
                            targetX = u.posX,
                            targetY = u.posY,
                            isMoving = false,
                            isHoldingFire = false,
                            isRepairing = false
                        )
                    }
                    com.example.model.UnitTacticalCommand.REPAIR -> {
                        u.copy(
                            activeStance = com.example.model.UnitStance.REPAIRING,
                            isRepairing = true,
                            targetX = u.posX,
                            targetY = u.posY,
                            isMoving = false,
                            isHoldingFire = false
                        )
                    }
                    com.example.model.UnitTacticalCommand.HOLD_FIRE -> {
                        val newHold = !u.isHoldingFire
                        u.copy(
                            activeStance = if (newHold) com.example.model.UnitStance.HOLD_FIRE else com.example.model.UnitStance.AGGRESSIVE,
                            isHoldingFire = newHold,
                            isRepairing = false
                        )
                    }
                }
            } else u
        }

        val feedbackMsg = when (command) {
            com.example.model.UnitTacticalCommand.PATROL -> "🔄 ${unit.name} devriye rotasına başladı."
            com.example.model.UnitTacticalCommand.GUARD -> "🛡️ ${unit.name} muhafız/savunma moduna geçti."
            com.example.model.UnitTacticalCommand.REPAIR -> "🔧 ${unit.name} saha onarım kitini aktive etti (+HP)."
            com.example.model.UnitTacticalCommand.HOLD_FIRE -> if (!unit.isHoldingFire) "🤫 ${unit.name} ateş kesti (Gizli gözetleme)." else "⚔️ ${unit.name} serbest atış moduna döndü."
        }

        _uiState.update {
            it.copy(
                units = updatedUnits,
                toastMessage = feedbackMsg
            )
        }
        addBattleLog(
            category = com.example.model.BattleLogCategory.COMBAT,
            message = "🎯 Birim Emri: ${unit.name} -> ${command.title}",
            colorHex = 0xFF00F5D4
        )
    }

    // ==================== MULTIPLAYER SKIRMISH CHAT & EVENT ALERTS ====================
    fun sendSkirmishChatMessage(text: String, isSystem: Boolean = false) {
        val factionName = _uiState.value.selectedNation.title
        val commander = _uiState.value.commanderName

        repository?.sendChatMessage(
            text = text,
            senderFaction = factionName,
            senderName = commander,
            isSystem = isSystem
        ) { success ->
            if (!success) {
                // Local fallback so chat works immediately even if offline
                val localMsg = SkirmishChatMessageDto(
                    id = UUID.randomUUID().toString(),
                    senderId = "local",
                    senderName = commander,
                    senderFaction = factionName,
                    messageText = text,
                    isSystemNotification = isSystem
                )
                _uiState.update { it.copy(skirmishChatMessages = it.skirmishChatMessages + localMsg) }
            }
        } ?: run {
            val localMsg = SkirmishChatMessageDto(
                id = UUID.randomUUID().toString(),
                senderId = "local",
                senderName = commander,
                senderFaction = factionName,
                messageText = text,
                isSystemNotification = isSystem
            )
            _uiState.update { it.copy(skirmishChatMessages = it.skirmishChatMessages + localMsg) }
        }
    }

    // ==================== TACTICAL GESTURE CUSTOM BINDINGS ====================
    fun saveGestureBindings(bindings: List<GestureBinding>) {
        _uiState.update {
            it.copy(
                gestureBindings = bindings,
                toastMessage = "Taktik ekran jestleri başarıyla kaydedildi."
            )
        }
    }

    // ==================== POST-MATCH ANALYSIS & VICTORY POINTS ====================
    fun triggerPostMatchSummary(isVictory: Boolean = true) {
        val state = _uiState.value
        val summary = PostMatchStats(
            isVictory = isVictory,
            playerFaction = state.selectedNation,
            mapTitle = state.selectedSkirmishMap.title,
            matchDurationSeconds = economyTickCounter.toLong().coerceAtLeast(60L),
            mapControlPercentage = if (isVictory) 82 else 35,
            totalGoldHarvested = state.resources.gold,
            totalIronHarvested = state.resources.iron,
            totalOilExtracted = state.resources.oil,
            enemyUnitsDestroyed = state.sessionCombatStats.enemyCasualties.coerceAtLeast(14),
            friendlyUnitsLost = state.sessionCombatStats.friendlyCasualties.coerceAtLeast(4)
        )
        _uiState.update { it.copy(postMatchStats = summary) }
    }

    fun submitPostMatchScoreToLeaderboard(stats: PostMatchStats) {
        _uiState.update { it.copy(isSubmittingScore = true, submissionFeedback = null) }
        val record = MatchHistoryRecordDto(
            id = UUID.randomUUID().toString(),
            userId = repository?.currentUser?.uid ?: "commander_local",
            playerName = _uiState.value.commanderName,
            playerFaction = stats.playerFaction.title,
            skirmishMapTitle = stats.mapTitle,
            isVictory = stats.isVictory,
            totalVictoryPoints = stats.totalVictoryPoints,
            mapControlPoints = stats.mapControlPoints,
            resourceExtractionPoints = stats.resourceExtractionPoints,
            unitDestructionPoints = stats.unitDestructionPoints,
            matchDurationSeconds = stats.matchDurationSeconds
        )

        repository?.recordMatchOutcome(record) { success, msg ->
            _uiState.update { current ->
                val updatedHistory = listOf(record) + current.matchHistory
                current.copy(
                    isSubmittingScore = false,
                    submissionFeedback = if (success) "✅ Skor liderlik tablosuna kaydedildi! (+${stats.totalVictoryPoints} VP)" else "⚠️ Kaydedildi: $msg",
                    matchHistory = updatedHistory,
                    toastMessage = "+${stats.totalVictoryPoints} Zafer Puanı kaydedildi!"
                )
            }
        } ?: run {
            _uiState.update { current ->
                current.copy(
                    isSubmittingScore = false,
                    submissionFeedback = "✅ Skor yerel hafızaya kaydedildi! (+${stats.totalVictoryPoints} VP)",
                    matchHistory = listOf(record) + current.matchHistory,
                    toastMessage = "+${stats.totalVictoryPoints} Zafer Puanı kaydedildi!"
                )
            }
        }
    }

    fun signInWithGoogleToken(idToken: String) {
        repository?.signInWithGoogleToken(
            idToken = idToken,
            onSuccess = { user ->
                _uiState.update {
                    it.copy(
                        isGoogleAuthenticated = true,
                        commanderName = user.displayName ?: it.commanderName,
                        toastMessage = "Google ile giriş yapıldı: ${user.displayName}"
                    )
                }
            },
            onError = { err ->
                _uiState.update { it.copy(toastMessage = "Giriş hatası: $err") }
            }
        )
    }
}


