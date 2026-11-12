package com.example

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simulation.GameViewModel
import com.example.ui.components.EraTransitionOverlay
import com.example.ui.screens.BaseGameScreen
import com.example.ui.screens.EconomySandboxScreen
import com.example.ui.screens.GddViewerScreen
import com.example.ui.screens.GlobalWarDashboardScreen
import com.example.ui.screens.RtsWorldBattlefieldScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.BattleLogScreen
import com.example.ui.screens.MilitaryMarketScreen
import com.example.ui.screens.WorldMap3DScreen
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceDark
import com.example.ui.screens.CommanderGalleryScreen
import com.example.ui.screens.AllianceFeedPanel

enum class ActiveModal {
    NONE,
    GDD_STUDIO,
    BASE_MANAGEMENT,
    ECONOMY_SANDBOX,
    GLOBAL_WAR_DASHBOARD,
    WORLD_MAP,
    BATTLE_LOG,
    MILITARY_MARKET,
    DIPLOMACY_19_NATIONS,
    TECH_RESEARCH,
    COMBAT_LOG,
    SKIRMISH_MAP_SELECT,
    LEADERBOARD,
    GESTURE_SETTINGS,
    POST_MATCH_ANALYSIS,
    MATCH_REPLAYS,
    COMMANDER_GALLERY,
    ALLIANCE_FEED
}

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. KESİN YATAY MODA ZORLA (Landscape / SensorLandscape)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // 2. IMMERSIVE FULLSCREEN (Durum çubuğu ve navigasyon barını tamamen gizle)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                LandscapeRtsApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeRtsApp(viewModel: GameViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var activeModal by remember { mutableStateOf(ActiveModal.NONE) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Initialize Room Database for match replays
    LaunchedEffect(Unit) {
        viewModel.initializeLocalDatabase(context)
    }

    // Toast feedback listener
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
    ) {
        // ==================== 100% FULL-SCREEN LANDSCAPE RTS BATTLEFIELD ====================
        // No vertical menus, no bottom navigation bars wasting vertical space!
        RtsWorldBattlefieldScreen(
            state = uiState,
            onPanCamera = { dx, dy -> viewModel.panCamera(dx, dy) },
            onSetZoom = { z -> viewModel.setCameraZoom(z) },
            onResetCamera = { viewModel.resetCamera() },
            onCenterCameraOn = { wx, wy -> viewModel.centerCameraOn(wx, wy) },
            onSelectBuilding = { viewModel.selectBuilding(it) },
            onSelectInRect = { minX, minY, maxX, maxY -> viewModel.selectUnitsInRect(minX, minY, maxX, maxY) },
            onSelectSingleUnit = { viewModel.selectSingleUnit(it) },
            onMoveOrder = { tx, ty -> viewModel.orderMoveSelectedUnits(tx, ty) },
            onEnqueueUnit = { buildingId, template -> viewModel.enqueueUnit(buildingId, template) },
            onCancelOrder = { buildingId, orderIdx -> viewModel.cancelProductionOrder(buildingId, orderIdx) },
            onUpgradeEra = { viewModel.upgradeToNextEra() },
            onTriggerPower = { viewModel.triggerGeneralsPower(it) },
            onStressTest = { viewModel.trigger100UnitStressTest() },
            onOpenGddStudio = { activeModal = ActiveModal.GDD_STUDIO },
            onOpenBaseManagement = { activeModal = ActiveModal.BASE_MANAGEMENT },
            onOpenEconomySandbox = { activeModal = ActiveModal.ECONOMY_SANDBOX },
            onOpenFactionSelect = { viewModel.toggleFactionSelect(true) },
            onToggleSimulation = { viewModel.setSimulationRunning(it) },
            onCycleSpeed = {
                val next = when (uiState.simulationSpeed) {
                    1.0f -> 2.0f
                    2.0f -> 5.0f
                    else -> 1.0f
                }
                viewModel.setSimulationSpeed(next)
            },
            onToggle3DMode = { viewModel.toggle3DMode() },
            onToggleSkirmishAi = { viewModel.toggleSkirmishAi() },
            onSelectFactoryFilter = { viewModel.selectFactoryFilter(it) },
            onOpenWarDashboard = { activeModal = ActiveModal.GLOBAL_WAR_DASHBOARD },
            onOpenWorldMap = { activeModal = ActiveModal.WORLD_MAP },
            onOpenBattleLog = { activeModal = ActiveModal.BATTLE_LOG },
            onOpenMilitaryMarket = { activeModal = ActiveModal.MILITARY_MARKET },
            onOpenDiplomacy = { activeModal = ActiveModal.DIPLOMACY_19_NATIONS },
            onOpenTechResearch = { activeModal = ActiveModal.TECH_RESEARCH },
            onOpenCombatLog = { activeModal = ActiveModal.COMBAT_LOG },
            onOpenSkirmishMapSelect = { activeModal = ActiveModal.SKIRMISH_MAP_SELECT },
            onOpenLeaderboard = { activeModal = ActiveModal.LEADERBOARD },
            onOpenPostMatch = {
                viewModel.triggerPostMatchSummary(isVictory = true)
                activeModal = ActiveModal.POST_MATCH_ANALYSIS
            },
            onOpenGestureSettings = { activeModal = ActiveModal.GESTURE_SETTINGS },
            onOpenReplays = { activeModal = ActiveModal.MATCH_REPLAYS },
            onOpenCommanderGallery = { activeModal = ActiveModal.COMMANDER_GALLERY },
            onOpenAllianceFeed = { activeModal = ActiveModal.ALLIANCE_FEED },
            onSendChatMessage = { msg, isSys -> viewModel.sendSkirmishChatMessage(msg, isSys) },
            onDismissNotification = { viewModel.dismissInGameNotification(it) },
            onSelectControlGroup = { viewModel.selectControlGroup(it) },
            onAssignSelectedToGroup = { viewModel.assignSelectedUnitsToGroup(it) },
            onClearControlGroup = { viewModel.clearControlGroup(it) },
            onExecuteGroupCommand = { groupId, cmd -> viewModel.executeGroupCommand(groupId, cmd) },
            onExecuteUnitCommand = { unitId, cmd -> viewModel.executeUnitCommand(unitId, cmd) },
            onBuildBuilding = { viewModel.buildNewBuilding(it) },
            onToggleHudVisibility = { viewModel.toggleHudVisibility() },
            onToggleSoundMute = { viewModel.toggleSoundMute() }
        )

        // ==================== MODAL SHEETS FOR GDD & ARCHITECTURE ====================
        if (activeModal != ActiveModal.NONE) {
            ModalBottomSheet(
                onDismissRequest = { activeModal = ActiveModal.NONE },
                sheetState = sheetState,
                containerColor = SurfaceDark
            ) {
                when (activeModal) {
                    ActiveModal.GDD_STUDIO -> GddViewerScreen()
                    ActiveModal.GLOBAL_WAR_DASHBOARD -> GlobalWarDashboardScreen(
                        stats = uiState.globalWarStats,
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.WORLD_MAP -> WorldMap3DScreen(
                        currentNation = uiState.selectedNation,
                        currentEra = uiState.currentEra,
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.BATTLE_LOG -> BattleLogScreen(
                        logs = uiState.battleLogs,
                        onClearLogs = { viewModel.clearBattleLogs() },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.MILITARY_MARKET -> MilitaryMarketScreen(
                        nations = uiState.diplomaticNations,
                        activeDeals = uiState.activeTradeDeals,
                        currentGold = uiState.resources.gold,
                        currentOil = uiState.resources.oil,
                        onImproveDiplomacy = { viewModel.improveDiplomacy(it) },
                        onPurchaseHardware = { viewModel.purchaseForeignMilitaryHardware(it) },
                        onCancelDeal = { viewModel.cancelTradeDeal(it) },
                        onClose = { activeModal = ActiveModal.NONE },
                        playerNation = uiState.selectedNation
                    )
                    ActiveModal.DIPLOMACY_19_NATIONS -> com.example.ui.components.NationsTradeReadinessComponent(
                        allNations = uiState.diplomaticNations,
                        playerNation = uiState.selectedNation,
                        currentGold = uiState.resources.gold,
                        currentOil = uiState.resources.oil,
                        onImproveDiplomacy = { viewModel.improveDiplomacy(it) },
                        onPurchaseHardware = { viewModel.purchaseForeignMilitaryHardware(it) },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.TECH_RESEARCH -> com.example.ui.screens.TechResearchScreen(
                        techTree = uiState.techTree,
                        currentEra = uiState.currentEra,
                        currentTechPoints = uiState.resources.techPoints,
                        currentGold = uiState.resources.gold,
                        currentIron = uiState.resources.iron,
                        activeResearchId = uiState.activeResearchId,
                        onStartResearch = { viewModel.startResearch(it) },
                        onCancelResearch = { viewModel.cancelResearch(it) },
                        onUpgradeEra = { viewModel.upgradeToNextEra() },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.COMBAT_LOG -> com.example.ui.components.CombatLogComponent(
                        logs = uiState.combatLogs,
                        stats = uiState.sessionCombatStats,
                        onClearLogs = { viewModel.clearCombatLogs() },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.SKIRMISH_MAP_SELECT -> com.example.ui.screens.SkirmishMapSelectScreen(
                        maps = uiState.skirmishMaps,
                        selectedMapId = uiState.selectedSkirmishMap.id,
                        onSelectMap = { viewModel.selectSkirmishMap(it) },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.LEADERBOARD -> com.example.ui.screens.GlobalLeaderboardScreen(
                        leaderboardEntries = uiState.leaderboardEntries,
                        matchHistory = uiState.matchHistory,
                        currentUserName = uiState.commanderName,
                        currentUserFaction = uiState.selectedNation,
                        isAuthenticated = uiState.isGoogleAuthenticated,
                        onSubmitCurrentSessionScore = {
                            viewModel.triggerPostMatchSummary(isVictory = true)
                            uiState.postMatchStats?.let { stats ->
                                viewModel.submitPostMatchScoreToLeaderboard(stats)
                            }
                        },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.GESTURE_SETTINGS -> com.example.ui.screens.TacticalGestureSettingsScreen(
                        currentBindings = uiState.gestureBindings,
                        onSaveBindings = { viewModel.saveGestureBindings(it) },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.POST_MATCH_ANALYSIS -> {
                        val stats = uiState.postMatchStats ?: com.example.ui.components.PostMatchStats(
                            isVictory = true,
                            playerFaction = uiState.selectedNation,
                            mapTitle = uiState.selectedSkirmishMap.title,
                            matchDurationSeconds = 480L
                        )
                        com.example.ui.components.PostMatchAnalysisOverlay(
                            stats = stats,
                            resourceHistory = uiState.resourceHistoryPoints,
                            isSubmittingScore = uiState.isSubmittingScore,
                            submissionFeedback = uiState.submissionFeedback,
                            onSubmitScoreToLeaderboard = { viewModel.submitPostMatchScoreToLeaderboard(it) },
                            onClose = { activeModal = ActiveModal.NONE }
                        )
                    }
                    ActiveModal.BASE_MANAGEMENT -> BaseGameScreen(
                        state = uiState,
                        onSelectBuilding = { viewModel.selectBuilding(it) },
                        onEnqueueUnit = { buildingId, template -> viewModel.enqueueUnit(buildingId, template) },
                        onCancelOrder = { buildingId, orderIdx -> viewModel.cancelProductionOrder(buildingId, orderIdx) },
                        onUpgradeEra = { viewModel.upgradeToNextEra() },
                        onSelectNation = { viewModel.setNation(it) }
                    )
                    ActiveModal.MATCH_REPLAYS -> com.example.ui.screens.MatchReplayViewerModal(
                        savedReplays = uiState.localReplays,
                        onSaveCurrentSessionReplay = { viewModel.saveCurrentSessionAsReplay() },
                        onDeleteReplay = { viewModel.deleteReplay(it) },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.COMMANDER_GALLERY -> CommanderGalleryScreen(
                        commanders = uiState.commanders,
                        activeCommanderId = uiState.activeCommanderId,
                        onSelectActiveCommander = { viewModel.selectActiveCommander(it) },
                        onToggleEquipSkill = { cmdId, skillId -> viewModel.toggleEquipCommanderSkill(cmdId, skillId) },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.ALLIANCE_FEED -> AllianceFeedPanel(
                        initialState = uiState.allianceState,
                        onDonateGold = { viewModel.donateToAllianceTech(it) },
                        onClose = { activeModal = ActiveModal.NONE }
                    )
                    ActiveModal.ECONOMY_SANDBOX -> EconomySandboxScreen()
                    ActiveModal.NONE -> {}
                }
            }
        }

        // ==================== FACTION SELECTION MODAL SCREEN ====================
        AnimatedVisibility(
            visible = uiState.showFactionSelect,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            com.example.ui.screens.FactionSelectScreen(
                currentNation = uiState.selectedNation,
                onSelectFaction = { faction -> viewModel.selectFactionAndInitialize(faction) },
                onClose = { viewModel.toggleFactionSelect(false) }
            )
        }

        // ==================== ERA TRANSITION FULLSCREEN ATMOSPHERIC OVERLAY ====================
        AnimatedVisibility(
            visible = uiState.eraTransitionEffect != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            uiState.eraTransitionEffect?.let { effect ->
                EraTransitionOverlay(
                    effect = effect,
                    nation = uiState.selectedNation,
                    onDismiss = { viewModel.dismissEraTransition() }
                )
            }
        }
    }
}
