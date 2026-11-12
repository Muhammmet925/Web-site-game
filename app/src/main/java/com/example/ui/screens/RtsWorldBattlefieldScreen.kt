package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Era
import com.example.model.Nation
import com.example.model.RtsBuilding
import com.example.model.RtsBuildingType
import com.example.model.RtsCatalog
import com.example.model.RtsUnit
import com.example.model.RtsUnitTemplate
import com.example.model.UnitType
import com.example.simulation.RtsUiState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EnergyYellow
import com.example.ui.theme.LaserCrimson
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange
import kotlin.math.cos
import kotlin.math.sin

import com.example.ui.components.ProductionQueueCountdownTray
import com.example.ui.components.RtsMiniMap
import kotlin.math.cos
import kotlin.math.sin

enum class TouchInteractionMode {
    PAN_AND_SELECT,
    DRAG_TO_SELECT_BOX
}

/**
 * Full-Screen Landscape RTS Battlefield Map (C&C Generals / Clash of Clans style):
 * - Renders realistic green terrain map with grass, concrete pads, roads, and blue river.
 * - Interactive Camera2D with 1-finger Pan and Pinch-to-Zoom.
 * - Interactive Mini-Map: Touch to pan camera anywhere in the 1400x900 world!
 * - Enhanced Selection Box: Laser corner brackets with live selected unit counter.
 * - Unit Selection Marker: Rotating tactical chevrons, corner ticks, and health gauges.
 * - Visually placed buildings (HQ, War Factory, Airfield, Shipyard in water, Power Plant, Supply Depot).
 * - Animated units (Tanks on roads, Jets with flight altitude shadows, Submarines with water ripple wakes).
 */
@Composable
fun RtsWorldBattlefieldScreen(
    state: RtsUiState,
    onPanCamera: (Float, Float) -> Unit,
    onSetZoom: (Float) -> Unit,
    onResetCamera: () -> Unit,
    onCenterCameraOn: (Float, Float) -> Unit,
    onSelectBuilding: (RtsBuilding?) -> Unit,
    onSelectInRect: (Float, Float, Float, Float) -> Unit,
    onSelectSingleUnit: (String?) -> Unit,
    onMoveOrder: (Float, Float) -> Unit,
    onEnqueueUnit: (String, RtsUnitTemplate) -> Unit,
    onCancelOrder: (String, Int) -> Unit,
    onUpgradeEra: () -> Unit,
    onTriggerPower: (String) -> Unit,
    onStressTest: () -> Unit,
    onOpenGddStudio: () -> Unit,
    onOpenBaseManagement: () -> Unit,
    onOpenEconomySandbox: () -> Unit,
    onOpenFactionSelect: () -> Unit,
    onToggleSimulation: (Boolean) -> Unit,
    onCycleSpeed: () -> Unit,
    onToggle3DMode: () -> Unit = {},
    onToggleSkirmishAi: () -> Unit = {},
    onSelectFactoryFilter: (RtsBuildingType?) -> Unit = {},
    onOpenWarDashboard: () -> Unit = {},
    onOpenWorldMap: () -> Unit = {},
    onOpenBattleLog: () -> Unit = {},
    onOpenMilitaryMarket: () -> Unit = {},
    onOpenDiplomacy: () -> Unit = {},
    onOpenTechResearch: () -> Unit = {},
    onOpenCombatLog: () -> Unit = {},
    onOpenSkirmishMapSelect: () -> Unit = {},
    onOpenLeaderboard: () -> Unit = {},
    onOpenPostMatch: () -> Unit = {},
    onOpenGestureSettings: () -> Unit = {},
    onOpenReplays: () -> Unit = {},
    onOpenCommanderGallery: () -> Unit = {},
    onOpenAllianceFeed: () -> Unit = {},
    onSendChatMessage: (String, Boolean) -> Unit = { _, _ -> },
    onDismissNotification: (String) -> Unit = {},
    onSelectControlGroup: (Int) -> Unit = {},
    onAssignSelectedToGroup: (Int) -> Unit = {},
    onClearControlGroup: (Int) -> Unit = {},
    onExecuteGroupCommand: (Int, com.example.model.TacticalGroupCommand) -> Unit = { _, _ -> },
    onExecuteUnitCommand: (String, com.example.model.UnitTacticalCommand) -> Unit = { _, _ -> },
    onBuildBuilding: (RtsBuildingType) -> Unit = {},
    onToggleHudVisibility: () -> Unit = {},
    onToggleSoundMute: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Interaction mode: Pan vs Drag-Select-Box
    var touchMode by remember { mutableStateOf(TouchInteractionMode.PAN_AND_SELECT) }
    var dragBoxStart by remember { mutableStateOf<Offset?>(null) }
    var dragBoxCurrent by remember { mutableStateOf<Offset?>(null) }
    var lastWaypoint by remember { mutableStateOf<Offset?>(null) }
    var isCommandConsoleExpanded by remember { mutableStateOf(false) }
    var commandConsoleTab by remember { androidx.compose.runtime.mutableIntStateOf(0) } // 0 = Production, 1 = Build Facilities

    // Pulsing radar, marker rotation and jet animation
    val infiniteTransition = rememberInfiniteTransition(label = "battlefield_anim")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radar"
    )

    val markerRotationAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "marker_rotation"
    )

    val jetOffsetAnim by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jet_hover"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .testTag("rts_world_battlefield_screen")
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Virtual World Map Dimensions (1200 x 800 dp)
        val worldWidthPx = 1350f
        val worldHeightPx = 850f

        // Pinch-to-zoom gesture state
        val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
            onSetZoom(state.cameraZoom * zoomChange)
            onPanCamera(offsetChange.x, offsetChange.y)
        }

        // ==================== LAYER 1: 2D RTS BATTLEFIELD WORLD (CAMERA TRANSFORMED) ====================
        Box(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformState)
                .graphicsLayer {
                    if (state.isPerspective3D) {
                        rotationX = 14f
                        scaleX = state.cameraZoom
                        scaleY = state.cameraZoom
                        cameraDistance = 8000f
                    } else {
                        rotationX = 0f
                        scaleX = state.cameraZoom
                        scaleY = state.cameraZoom
                    }
                    translationX = state.cameraOffsetX
                    translationY = state.cameraOffsetY
                }
                .pointerInput(touchMode, state.selectedUnitIds, state.cameraZoom) {
                    if (touchMode == TouchInteractionMode.PAN_AND_SELECT) {
                        detectTapGestures(
                            onTap = { screenTap ->
                                // Convert screen tap into world coordinates
                                val worldX = (screenTap.x - size.width / 2f - state.cameraOffsetX) / state.cameraZoom + size.width / 2f
                                val worldY = (screenTap.y - size.height / 2f - state.cameraOffsetY) / state.cameraZoom + size.height / 2f

                                // Check if a building was tapped (within 55px radius)
                                val tappedBuilding = state.buildings.minByOrNull { b ->
                                    Math.hypot((worldX - b.worldX).toDouble(), (worldY - b.worldY).toDouble())
                                }
                                val buildingDist = tappedBuilding?.let {
                                    Math.hypot((worldX - it.worldX).toDouble(), (worldY - it.worldY).toDouble()).toFloat()
                                } ?: Float.MAX_VALUE

                                if (buildingDist < 65f) {
                                    onSelectBuilding(tappedBuilding)
                                    onSelectSingleUnit(null)
                                } else if (state.selectedUnitIds.isNotEmpty()) {
                                    // Move command (Right click equivalent)
                                    lastWaypoint = Offset(worldX, worldY)
                                    onMoveOrder(worldX, worldY)
                                } else {
                                    // Check if a unit was tapped (within 35px radius)
                                    val tappedUnit = state.units.minByOrNull { u ->
                                        Math.hypot((worldX - u.posX).toDouble(), (worldY - u.posY).toDouble())
                                    }
                                    val unitDist = tappedUnit?.let {
                                        Math.hypot((worldX - it.posX).toDouble(), (worldY - it.posY).toDouble()).toFloat()
                                    } ?: Float.MAX_VALUE

                                    if (unitDist < 40f) {
                                        onSelectSingleUnit(tappedUnit?.id)
                                        onSelectBuilding(null)
                                    } else {
                                        // Clear selection
                                        onSelectSingleUnit(null)
                                        onSelectBuilding(null)
                                    }
                                }
                            }
                        )
                    }
                }
                .pointerInput(touchMode) {
                    if (touchMode == TouchInteractionMode.PAN_AND_SELECT) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            onPanCamera(dragAmount.x, dragAmount.y)
                        }
                    } else {
                        detectDragGestures(
                            onDragStart = { offset ->
                                dragBoxStart = offset
                                dragBoxCurrent = offset
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                dragBoxCurrent = change.position
                            },
                            onDragEnd = {
                                if (dragBoxStart != null && dragBoxCurrent != null) {
                                    // Convert screen box to world box
                                    val sMinX = Math.min(dragBoxStart!!.x, dragBoxCurrent!!.x)
                                    val sMaxX = Math.max(dragBoxStart!!.x, dragBoxCurrent!!.x)
                                    val sMinY = Math.min(dragBoxStart!!.y, dragBoxCurrent!!.y)
                                    val sMaxY = Math.max(dragBoxStart!!.y, dragBoxCurrent!!.y)

                                    val wMinX = (sMinX - size.width / 2f - state.cameraOffsetX) / state.cameraZoom + size.width / 2f
                                    val wMaxX = (sMaxX - size.width / 2f - state.cameraOffsetX) / state.cameraZoom + size.width / 2f
                                    val wMinY = (sMinY - size.height / 2f - state.cameraOffsetY) / state.cameraZoom + size.height / 2f
                                    val wMaxY = (sMaxY - size.height / 2f - state.cameraOffsetY) / state.cameraZoom + size.height / 2f

                                    if (wMaxX - wMinX > 10f || wMaxY - wMinY > 10f) {
                                        onSelectInRect(wMinX, wMinY, wMaxX, wMaxY)
                                    }
                                }
                                dragBoxStart = null
                                dragBoxCurrent = null
                            },
                            onDragCancel = {
                                dragBoxStart = null
                                dragBoxCurrent = null
                            }
                        )
                    }
                }
        ) {
            // 1. Terrain Map Asset as the Living World Background (Vast Skirmish Battlefield - Enriched & Enlarged)
            Image(
                painter = painterResource(id = R.drawable.rts_terrain_map),
                contentDescription = "RTS Battlefield Terrain",
                modifier = Modifier.size(3000.dp, 2000.dp),
                contentScale = ContentScale.FillBounds
            )

            // 2. Procedural World Canvas: Shadows, Waypoints, FX, Units & Buildings
            Canvas(modifier = Modifier.size(3000.dp, 2000.dp)) {
                // Waypoint ping indicator
                lastWaypoint?.let { wp ->
                    drawCircle(
                        color = MatrixGreen.copy(alpha = 0.5f),
                        radius = 18.dp.toPx() * (1f + pulseAnim * 0.4f),
                        center = wp,
                        style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
                    )
                    drawCircle(color = MatrixGreen, radius = 4.dp.toPx(), center = wp)
                }

                // Generals Power Strike AoE
                state.lastStrikeEffect?.let { effect ->
                    val strikeColor = Color(effect.colorHex)
                    drawCircle(
                        color = strikeColor.copy(alpha = 0.3f),
                        radius = 65.dp.toPx() * (1f + pulseAnim * 0.5f),
                        center = Offset(effect.x, effect.y)
                    )
                    drawCircle(
                        color = strikeColor,
                        radius = 65.dp.toPx(),
                        center = Offset(effect.x, effect.y),
                        style = Stroke(width = 3f)
                    )
                }

                // Draw Unit Shadows first (Ground plane)
                state.units.forEach { unit ->
                    val shadowOffset = if (unit.unitType == UnitType.JET) 24.dp.toPx() else 4.dp.toPx()
                    drawOval(
                        color = Color.Black.copy(alpha = if (unit.unitType == UnitType.JET) 0.35f else 0.45f),
                        topLeft = Offset(unit.posX - 12.dp.toPx() + shadowOffset * 0.6f, unit.posY - 6.dp.toPx() + shadowOffset),
                        size = Size(24.dp.toPx(), 12.dp.toPx())
                    )
                }

                // FOG OF WAR VISION CALCULATION:
                // Friendly units and buildings illuminate a radius of the map. Enemy units outside this radius remain hidden in fog.
                val friendlyVisionPockets = mutableListOf<Pair<Offset, Float>>()

                // Buildings provide 280px vision radius
                state.buildings.forEach { b ->
                    friendlyVisionPockets.add(Pair(Offset(b.worldX, b.worldY), 280f))
                }
                // Friendly units provide vision based on type (Jet/Scout = 380px, Tank = 240px, Infantry = 200px)
                state.units.filter { it.isPlayerSide }.forEach { u ->
                    val visionRadius = when (u.unitType) {
                        UnitType.JET -> 400f
                        UnitType.TANK -> 260f
                        UnitType.SUBMARINE -> 240f
                        UnitType.INFANTRY -> 220f
                        UnitType.SIEGE -> 230f
                    }
                    friendlyVisionPockets.add(Pair(Offset(u.posX, u.posY), visionRadius))
                }

                // Draw Units (Friendly units always visible; enemy units only visible if scouted/within friendly vision)
                state.units.forEach { unit ->
                    val isPlayer = unit.isPlayerSide
                    val pos = Offset(unit.posX, unit.posY)

                    // Check if enemy unit is scouted / within friendly vision
                    val isVisibleInFog = isPlayer || friendlyVisionPockets.any { (center, radius) ->
                        Math.hypot((pos.x - center.x).toDouble(), (pos.y - center.y).toDouble()) <= radius
                    }

                    if (isVisibleInFog) {
                        val isSelected = state.selectedUnitIds.contains(unit.id)

                        // Draw Unit Selection Marker (Tactical Reticle & Chevrons)
                        if (isSelected) {
                            val baseRadius = if (unit.unitType == UnitType.JET) 16.dp.toPx() else 14.dp.toPx()
                            drawTacticalSelectionMarker(pos, baseRadius, markerRotationAnim, pulseAnim)

                            // If moving, draw tactical target line to waypoint
                            if (unit.isMoving) {
                                drawLine(
                                    color = MatrixGreen.copy(alpha = 0.6f),
                                    start = pos,
                                    end = Offset(unit.targetX, unit.targetY),
                                    strokeWidth = 1.5f,
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                                )
                            }
                        }

                        when (unit.unitType) {
                            UnitType.TANK -> drawRtsTank(unit, isSelected, pos)
                            UnitType.JET -> drawRtsJet(unit, isSelected, pos, jetOffsetAnim)
                            UnitType.SUBMARINE -> drawRtsSubmarine(unit, isSelected, pos, pulseAnim)
                            UnitType.INFANTRY -> drawRtsInfantry(unit, isSelected, pos)
                            UnitType.SIEGE -> drawRtsSiege(unit, isSelected, pos)
                        }

                        // Mini Health Bar & Rank Indicators
                        val hpRatio = (unit.hp / unit.maxHp).coerceIn(0f, 1f)
                        val barW = 26.dp.toPx()
                        val barH = 3.dp.toPx()
                        val barTop = pos.y - 20.dp.toPx()

                        drawRect(color = SpaceBlack, topLeft = Offset(pos.x - barW / 2f, barTop), size = Size(barW, barH))
                        drawRect(
                            color = if (hpRatio > 0.4f) MatrixGreen else DangerRed,
                            topLeft = Offset(pos.x - barW / 2f, barTop),
                            size = Size(barW * hpRatio, barH)
                        )

                        // Draw Rank Badges (Stars above health bar for Veteran, Elite, Heroic units)
                        if (unit.rank != com.example.model.UnitRank.RECRUIT) {
                            val starCount = when (unit.rank) {
                                com.example.model.UnitRank.VETERAN -> 2
                                com.example.model.UnitRank.ELITE -> 3
                                com.example.model.UnitRank.HEROIC -> 4
                                else -> 1
                            }
                            val starRadius = 2.dp.toPx()
                            val starSpacing = 5.dp.toPx()
                            val startX = pos.x - ((starCount - 1) * starSpacing) / 2f
                            for (s in 0 until starCount) {
                                drawCircle(
                                    color = AmberGold,
                                    radius = starRadius,
                                    center = Offset(startX + s * starSpacing, barTop - 4.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }

            // 3. Buildings on World Map with Interactive Overlays & 3D Styling
            state.buildings.forEach { building ->
                val isSelected = state.selectedBuilding?.id == building.id
                val activeOrder = building.queue.firstOrNull()

                Box(
                    modifier = Modifier
                        .offset(x = building.worldX.dp - 40.dp, y = building.worldY.dp - 35.dp)
                        .size(80.dp, 70.dp)
                        .clickable {
                            onSelectBuilding(building)
                            onSelectSingleUnit(null)
                        }
                        .testTag("world_building_${building.id}")
                ) {
                    // Building Tactical Interactive Highlight Area
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = if (isSelected) CyanNeon.copy(alpha = 0.15f) else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, CyanNeon)
                                 else if (activeOrder != null) androidx.compose.foundation.BorderStroke(1.dp, AmberGold.copy(alpha = 0.7f))
                                 else null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Top Tag: Building Icon & Queue count only if producing
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelected || activeOrder != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(getBuildingThemeColor(building.type).copy(alpha = 0.6f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getBuildingVectorIcon(building.type),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(1.dp))
                                }

                                if (building.queue.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(AmberGold)
                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                    ) {
                                        Text(text = "${building.queue.size}Q", color = SpaceBlack, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Center: Only show label if selected
                            if (isSelected) {
                                Surface(
                                    color = SurfaceDark.copy(alpha = 0.85f),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(1.dp)
                                ) {
                                    Text(
                                        text = building.type.title.split("(").first().take(10),
                                        color = CyanNeon,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            // Bottom: Active Progress Bar if producing
                            if (activeOrder != null) {
                                LinearProgressIndicator(
                                    progress = { 1f - (activeOrder.timeRemaining / activeOrder.totalTime) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.dp)),
                                    color = AmberGold,
                                    trackColor = SpaceBlack
                                )
                            } else {
                                Spacer(modifier = Modifier.size(1.dp))
                            }
                        }
                    }

                    // Rotating Production Ring Floating Above Building if producing
                    if (activeOrder != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-14).dp)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(AmberGold)
                                .border(1.dp, SpaceBlack, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "%.0fs".format(activeOrder.timeRemaining),
                                color = SpaceBlack,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ==================== LAYER 2: DRAG-TO-SELECT BOX (SCREEN SPACE) ====================
        if (dragBoxStart != null && dragBoxCurrent != null) {
            val minX = Math.min(dragBoxStart!!.x, dragBoxCurrent!!.x)
            val maxX = Math.max(dragBoxStart!!.x, dragBoxCurrent!!.x)
            val minY = Math.min(dragBoxStart!!.y, dragBoxCurrent!!.y)
            val maxY = Math.max(dragBoxStart!!.y, dragBoxCurrent!!.y)

            // Convert to world space to calculate how many units are inside
            val wMinX = (minX - screenWidth.value / 2f - state.cameraOffsetX) / state.cameraZoom + screenWidth.value / 2f
            val wMaxX = (maxX - screenWidth.value / 2f - state.cameraOffsetX) / state.cameraZoom + screenWidth.value / 2f
            val wMinY = (minY - screenHeight.value / 2f - state.cameraOffsetY) / state.cameraZoom + screenHeight.value / 2f
            val wMaxY = (maxY - screenHeight.value / 2f - state.cameraOffsetY) / state.cameraZoom + screenHeight.value / 2f
            val selectedCount = state.units.count { u -> u.isPlayerSide && u.posX in wMinX..wMaxX && u.posY in wMinY..wMaxY }

            com.example.ui.components.SelectionBoxOverlay(
                dragStart = dragBoxStart,
                dragCurrent = dragBoxCurrent,
                selectedUnitsCount = selectedCount
            )
        }

        // ==================== LAYER 3: RTS FLOATING HUD (CLASH OF CLANS & C&C GENERALS) ====================

        // REAL-TIME NOTIFICATION CUSTOM TOAST STACK (Research Complete, Unit Under Attack, Base Siege)
        com.example.ui.components.RealtimeNotificationOverlay(
            notifications = state.inGameNotifications,
            onFocusLocation = { fx, fy -> onCenterCameraOn(fx, fy) },
            onDismissNotification = onDismissNotification,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // TOP HUD: Top-Bar Resource Tracker Component with Gold, Iron, Energy and Real-Time Depletion Indicators
        AnimatedVisibility(
            visible = state.isHudVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .fillMaxWidth(0.72f) // Leaves room for Mini-Map in Top-Right
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                com.example.ui.components.TopBarResourceTracker(
                    resources = state.resources,
                    nation = state.selectedNation,
                    currentEra = state.currentEra,
                    buildings = state.buildings,
                    simulationSpeed = state.simulationSpeed,
                    is3DMode = state.isPerspective3D,
                    isSkirmishAiActive = state.isSkirmishAiActive,
                    skirmishWave = state.skirmishAiWave,
                    isAudioMuted = state.isAudioMuted,
                    isHudVisible = state.isHudVisible,
                    onCycleSpeed = onCycleSpeed,
                    onToggle3D = onToggle3DMode,
                    onToggleSkirmishAi = onToggleSkirmishAi,
                    onToggleAudio = onToggleSoundMute,
                    onToggleHudVisibility = onToggleHudVisibility,
                    onOpenTechResearch = onOpenTechResearch,
                    onOpenDiplomacy = onOpenDiplomacy,
                    onOpenBattleLog = onOpenBattleLog,
                    onOpenMilitaryMarket = onOpenMilitaryMarket,
                    onOpenWorldMap = onOpenWorldMap,
                    onOpenWarDashboard = onOpenWarDashboard,
                    onOpenFactionSelect = onOpenFactionSelect,
                    onOpenBaseManagement = onOpenBaseManagement,
                    onOpenCombatLog = onOpenCombatLog,
                    onOpenSkirmishMapSelect = onOpenSkirmishMapSelect,
                    onOpenLeaderboard = onOpenLeaderboard,
                    onOpenPostMatch = onOpenPostMatch,
                    onOpenGestureSettings = onOpenGestureSettings,
                    onOpenReplays = onOpenReplays,
                    onOpenCommanderGallery = onOpenCommanderGallery,
                    onOpenAllianceFeed = onOpenAllianceFeed
                )
            }
        }

        // Floating Restore HUD Button when HUD is hidden
        if (!state.isHudVisible) {
            Surface(
                color = SurfaceDark.copy(alpha = 0.88f),
                shape = RoundedCornerShape(8.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyanNeon, SurfaceBorder))),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clickable { onToggleHudVisibility() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Fullscreen, contentDescription = "HUD Göster", tint = CyanNeon, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "HUD Göster", color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // TOP-RIGHT: Interactive Mini-Map & Camera Tools & Quick Powers Bar
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Interactive Mini-Map (Touch anywhere to Pan Main Camera)
            RtsMiniMap(
                buildings = state.buildings,
                units = state.units,
                cameraOffsetX = state.cameraOffsetX,
                cameraOffsetY = state.cameraOffsetY,
                cameraZoom = state.cameraZoom,
                skirmishMap = state.selectedSkirmishMap,
                onMiniMapTap = { wx, wy -> onCenterCameraOn(wx, wy) }
            )

            // Touch Mode & Camera Tools (Pan vs Box Select, Reset Zoom)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                // Pan / Box Select Toggle
                Surface(
                    color = if (touchMode == TouchInteractionMode.DRAG_TO_SELECT_BOX) MatrixGreen else SurfaceDark.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(6.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated))),
                    modifier = Modifier.clickable {
                        touchMode = if (touchMode == TouchInteractionMode.PAN_AND_SELECT)
                            TouchInteractionMode.DRAG_TO_SELECT_BOX
                        else
                            TouchInteractionMode.PAN_AND_SELECT
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (touchMode == TouchInteractionMode.DRAG_TO_SELECT_BOX) Icons.Default.CropFree else Icons.Default.PanTool,
                            contentDescription = null,
                            tint = if (touchMode == TouchInteractionMode.DRAG_TO_SELECT_BOX) SpaceBlack else CyanNeon,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (touchMode == TouchInteractionMode.DRAG_TO_SELECT_BOX) "Kutu" else "Kaydır",
                            color = if (touchMode == TouchInteractionMode.DRAG_TO_SELECT_BOX) SpaceBlack else TextPrimary,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Reset Camera
                IconButton(
                    onClick = onResetCamera,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = SurfaceDark.copy(alpha = 0.85f)),
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.CenterFocusStrong, contentDescription = "Merkeze Odakla", tint = CyanNeon, modifier = Modifier.size(12.dp))
                }

                // Zoom Out / In
                IconButton(
                    onClick = { onSetZoom(state.cameraZoom - 0.2f) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = SurfaceDark.copy(alpha = 0.85f)),
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Uzaklaş", tint = TextSecondary, modifier = Modifier.size(11.dp))
                }
                IconButton(
                    onClick = { onSetZoom(state.cameraZoom + 0.2f) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = SurfaceDark.copy(alpha = 0.85f)),
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Yakınlaş", tint = CyanNeon, modifier = Modifier.size(11.dp))
                }
            }

            // Generals Powers Sleek Icon Badges
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                state.generalsPowers.forEach { power ->
                    val isReady = power.isReady
                    val canAfford = state.resources.gold >= power.costGold

                    Surface(
                        color = if (isReady && canAfford) SurfaceDark.copy(alpha = 0.92f) else SurfaceDark.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isReady) LaserCrimson else SurfaceBorder
                        ),
                        modifier = Modifier.clickable(enabled = isReady && canAfford) { onTriggerPower(power.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = if (isReady) LaserCrimson else TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = power.name.split(" ").first().take(10),
                                color = if (isReady) TextPrimary else TextMuted,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (!isReady) "${power.currentCooldownSeconds}s" else "${power.costGold}G",
                                color = if (isReady) AmberGold else DangerRed,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // FLOATING UNIT CONTEXT MENU (Patrol, Guard, Repair, Hold Fire)
        val singleSelectedUnit = if (state.selectedUnitIds.size == 1) {
            state.units.find { state.selectedUnitIds.contains(it.id) && it.isPlayerSide }
        } else null

        if (singleSelectedUnit != null && state.isHudVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 126.dp)
            ) {
                com.example.ui.components.UnitContextMenuOverlay(
                    unit = singleSelectedUnit,
                    onExecuteCommand = { cmd -> onExecuteUnitCommand(singleSelectedUnit.id, cmd) },
                    onDismiss = { onSelectSingleUnit(null) }
                )
            }
        }

        // BOTTOM-LEFT: Tactical Unit/Building Inspector (Appears ONLY when unit or building is selected)
        val selectedUnit = state.units.find { state.selectedUnitIds.contains(it.id) }
        val selectedBuilding = state.selectedBuilding

        if (state.isHudVisible && (selectedUnit != null || selectedBuilding != null)) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Surface(
                    color = SurfaceDark.copy(alpha = 0.94f),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.5f), SurfaceBorder))),
                    shadowElevation = 8.dp,
                    modifier = Modifier.width(230.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        if (selectedUnit != null) {
                            // Unit Inspector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = selectedUnit.name, color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${selectedUnit.activeStance.badge} ${selectedUnit.activeStance.title}",
                                        color = AmberGold,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (state.selectedUnitIds.size > 1) "${state.selectedUnitIds.size} B" else "1 B", color = TextSecondary, fontSize = 8.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (selectedUnit.hp / selectedUnit.maxHp).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                color = MatrixGreen,
                                trackColor = SpaceBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "HP: ${selectedUnit.hp.toInt()}/${selectedUnit.maxHp.toInt()}", color = TextSecondary, fontSize = 9.sp)
                                Text(text = "Saldırı: ${selectedUnit.attackPower.toInt()}", color = DangerRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Hız: ${selectedUnit.speed.toInt()}", color = AmberLight, fontSize = 9.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            // Unit Rank & Experience Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = selectedUnit.rankBadge, color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = selectedUnit.rank.title, color = AmberGold, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (selectedUnit.rank.bonusDamagePct > 0) {
                                        Text(text = " (+${selectedUnit.rank.bonusDamagePct}%)", color = MatrixGreen, fontSize = 8.sp)
                                    }
                                }
                                Text(text = "${selectedUnit.killsCount} Leş • ${selectedUnit.xp} XP", color = CyanNeon, fontSize = 8.sp)
                            }
                            val nextRankXp = selectedUnit.rank.nextRank?.requiredXp ?: selectedUnit.rank.requiredXp
                            val xpProgress = if (selectedUnit.rank == com.example.model.UnitRank.HEROIC) 1f
                                             else (selectedUnit.xp.toFloat() / nextRankXp.toFloat()).coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { xpProgress },
                                modifier = Modifier.fillMaxWidth().height(2.dp).clip(RoundedCornerShape(1.dp)),
                                color = AmberGold,
                                trackColor = SpaceBlack
                            )
                        } else if (selectedBuilding != null) {
                            // Building Inspector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = selectedBuilding.type.title, color = AmberGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                Text(text = "${selectedBuilding.queue.size}/5 Kuyruk", color = CyanNeon, fontSize = 9.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = selectedBuilding.type.description, color = TextSecondary, fontSize = 8.sp, maxLines = 2, lineHeight = 11.sp)
                        }
                    }
                }
            }
        }

        // Bottom chat bubble removed per user request for clean, uncluttered battlefield view

        // BOTTOM-ANCHORED CONTROL BAR: Dynamic commands (Move, Attack, Guard, Ability) based on selected units
        val selectedUnitsList = state.units.filter { state.selectedUnitIds.contains(it.id) && it.isPlayerSide }
        if (state.isHudVisible && selectedUnitsList.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                com.example.ui.components.BottomUnitControlBar(
                    selectedUnits = selectedUnitsList,
                    onExecuteCommand = { cmd ->
                        selectedUnitsList.forEach { u -> onExecuteUnitCommand(u.id, cmd) }
                    },
                    onMoveOrder = {
                        // Enter Move mode / activate Waypoint ping
                        lastWaypoint = Offset(selectedUnitsList.first().posX + 60f, selectedUnitsList.first().posY + 40f)
                    },
                    onAttackOrder = {
                        // Set aggressive attack stance for selected squad
                        selectedUnitsList.forEach { u ->
                            onExecuteUnitCommand(u.id, com.example.model.UnitTacticalCommand.GUARD)
                        }
                    },
                    onDeselectAll = { onSelectSingleUnit(null) }
                )
            }
        } else if (state.isHudVisible) {
            // BOTTOM-CENTER: Tactical Unit Control Groups Overlay (Quick Selection & Squad Commands)
            com.example.ui.components.TacticalControlGroupsOverlay(
                groups = state.controlGroups,
                units = state.units,
                selectedUnitIds = state.selectedUnitIds,
                onSelectGroup = onSelectControlGroup,
                onAssignSelectedToGroup = onAssignSelectedToGroup,
                onClearGroup = onClearControlGroup,
                onExecuteGroupCommand = onExecuteGroupCommand,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
            )
        }

        // BOTTOM-RIGHT: C&C Generals Command Console & Production Drawer (Collapsible)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            val activeBuilding = state.selectedBuilding ?: state.buildings.find { it.type == RtsBuildingType.FACTORY } ?: state.buildings.firstOrNull()

            if (!isCommandConsoleExpanded) {
                // Sleek Floating Pills for Production & Building Construction
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        color = SurfaceDark.copy(alpha = 0.90f),
                        shape = RoundedCornerShape(6.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AmberGold, SurfaceBorder))),
                        modifier = Modifier
                            .clickable {
                                commandConsoleTab = 0
                                isCommandConsoleExpanded = true
                            }
                            .testTag("expand_command_console_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = activeBuilding?.let { getBuildingVectorIcon(it.type) } ?: Icons.Default.PrecisionManufacturing,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${activeBuilding?.type?.title?.split(" ")?.first() ?: "Fabrika"} (${activeBuilding?.queue?.size ?: 0}/5) ▲",
                                color = TextPrimary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = MatrixGreen.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(6.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MatrixGreen, SurfaceBorder))),
                        modifier = Modifier
                            .clickable {
                                commandConsoleTab = 1
                                isCommandConsoleExpanded = true
                            }
                            .testTag("expand_build_facility_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Construction,
                                contentDescription = null,
                                tint = SpaceBlack,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "🏗️ İnşa (${state.buildings.size})",
                                color = SpaceBlack,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            } else {
                // Expanded Full Production & Construction Console
                Surface(
                    color = SurfaceDark.copy(alpha = 0.94f),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(if (commandConsoleTab == 0) AmberGold else MatrixGreen, SurfaceBorder))
                    ),
                    shadowElevation = 10.dp,
                    modifier = Modifier.width(330.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (commandConsoleTab == 0) (activeBuilding?.let { getBuildingVectorIcon(it.type) } ?: Icons.Default.PrecisionManufacturing) else Icons.Default.Construction,
                                    contentDescription = null,
                                    tint = if (commandConsoleTab == 0) AmberGold else MatrixGreen,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (commandConsoleTab == 0) (activeBuilding?.type?.title ?: "ÜRETİM KONSOLU") else "FABRİKA & TESİS İNŞAATI",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Era Upgrade Button in Header
                                val nextEra = when (state.currentEra) {
                                    Era.MEDIEVAL_5TH -> Era.CLASSICAL_GUNPOWDER
                                    Era.CLASSICAL_GUNPOWDER -> Era.INDUSTRIAL
                                    Era.INDUSTRIAL -> Era.MODERN
                                    Era.MODERN -> null
                                }

                                if (nextEra != null) {
                                    val canAfford = state.resources.gold >= nextEra.upgradeCostGold && state.resources.techPoints >= nextEra.upgradeCostTech
                                    Button(
                                        onClick = onUpgradeEra,
                                        enabled = canAfford,
                                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.height(22.dp).testTag("upgrade_era_hud_button")
                                    ) {
                                        Text(text = "Çağ Atla", color = SpaceBlack, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Minimize Button
                                Surface(
                                    color = SurfaceElevated,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.clickable { isCommandConsoleExpanded = false }
                                ) {
                                    Text(text = "▼", color = TextSecondary, fontSize = 8.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Switcher Tabs: 0 = Unit Production, 1 = Build Facilities
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = { commandConsoleTab = 0 },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (commandConsoleTab == 0) AmberGold else SurfaceElevated
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 3.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(24.dp)
                                    .testTag("tab_unit_production")
                            ) {
                                Text(
                                    text = "🏭 Birim Üret",
                                    color = if (commandConsoleTab == 0) SpaceBlack else TextSecondary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { commandConsoleTab = 1 },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (commandConsoleTab == 1) MatrixGreen else SurfaceElevated
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 3.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(24.dp)
                                    .testTag("tab_build_facilities")
                            ) {
                                Text(
                                    text = "🏗️ Fabrika İnşa Et",
                                    color = if (commandConsoleTab == 1) SpaceBlack else TextSecondary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (commandConsoleTab == 0) {
                            // TAB 0: Real-Time 5-Slot Countdown Tray & Fast Enqueue
                            if (activeBuilding != null && activeBuilding.type.allowedUnitTypes.isNotEmpty()) {
                                ProductionQueueCountdownTray(
                                    building = activeBuilding,
                                    availableTemplates = RtsCatalog.getUnitTemplates().filter { template ->
                                        template.era == state.currentEra &&
                                        (template.nation == state.selectedNation || state.currentEra != Era.MODERN)
                                    },
                                    currentGold = state.resources.gold,
                                    currentOil = state.resources.oil,
                                    onCancelOrder = onCancelOrder,
                                    onEnqueueUnit = onEnqueueUnit,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Text(
                                    text = "Bu tesis doğrudan birim üretmez. Komuta veya enerji desteği sağlar. Yeni fabrika kurmak için 'Fabrika İnşa Et' sekmesini kullanın.",
                                    color = TextSecondary,
                                    fontSize = 9.sp
                                )
                            }
                        } else {
                            // TAB 1: Base Expansion & Building Construction
                            val constructibleBuildings = listOf(
                                RtsBuildingType.FACTORY,
                                RtsBuildingType.AIRFIELD,
                                RtsBuildingType.SHIPYARD,
                                RtsBuildingType.POWER_PLANT,
                                RtsBuildingType.BARRACKS,
                                RtsBuildingType.SUPPLY_DEPOT
                            )
                            LazyColumn(
                                modifier = Modifier.height(175.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                items(constructibleBuildings) { bType ->
                                    val canAfford = state.resources.gold >= bType.baseCostGold
                                    Surface(
                                        color = if (canAfford) SurfaceElevated else SpaceBlack.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (canAfford) MatrixGreen.copy(alpha = 0.45f) else SurfaceBorder
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier.weight(1f),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = getBuildingVectorIcon(bType),
                                                    contentDescription = null,
                                                    tint = if (canAfford) MatrixGreen else TextMuted,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = bType.title,
                                                            color = TextPrimary,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "${bType.baseCostGold} 🪙",
                                                            color = AmberGold,
                                                            fontSize = 8.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                    Text(
                                                        text = bType.description,
                                                        color = TextMuted,
                                                        fontSize = 7.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }

                                            Button(
                                                onClick = { onBuildBuilding(bType) },
                                                enabled = canAfford,
                                                colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                                                shape = RoundedCornerShape(4.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier
                                                    .height(24.dp)
                                                    .testTag("build_btn_${bType.name.lowercase()}")
                                            ) {
                                                Text(
                                                    text = "İnşa Et",
                                                    color = SpaceBlack,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== PROCEDURAL UNIT DRAWING ====================

private fun DrawScope.drawRtsTank(unit: RtsUnit, isSelected: Boolean, pos: Offset) {
    val tankColor = if (!unit.isPlayerSide) LaserCrimson else AmberGold
    val radius = 13.dp.toPx()

    // Selection Halo
    if (isSelected) {
        drawCircle(color = MatrixGreen, radius = radius + 6.dp.toPx(), center = pos, style = Stroke(width = 2f))
    }

    // Caterpillar treads
    val treadW = 20.dp.toPx()
    val treadH = 5.dp.toPx()
    drawRect(color = Color(0xFF1E293B), topLeft = Offset(pos.x - treadW / 2f, pos.y - radius), size = Size(treadW, treadH))
    drawRect(color = Color(0xFF1E293B), topLeft = Offset(pos.x - treadW / 2f, pos.y + radius - treadH), size = Size(treadW, treadH))

    // Tank Hull
    drawRect(color = tankColor, topLeft = Offset(pos.x - 9.dp.toPx(), pos.y - 8.dp.toPx()), size = Size(18.dp.toPx(), 16.dp.toPx()))

    // Rotating Turret & Barrel
    val angle = Math.atan2((unit.targetY - unit.posY).toDouble(), (unit.targetX - unit.posX).toDouble())
    val barrelLen = 14.dp.toPx()
    val barrelEnd = Offset((pos.x + cos(angle) * barrelLen).toFloat(), (pos.y + sin(angle) * barrelLen).toFloat())
    drawLine(color = Color(0xFF0F172A), start = pos, end = barrelEnd, strokeWidth = 3.5f)
    drawCircle(color = Color(0xFF0F172A), radius = 5.dp.toPx(), center = pos)
}

private fun DrawScope.drawRtsJet(unit: RtsUnit, isSelected: Boolean, pos: Offset, hoverAnim: Float) {
    val jetPos = Offset(pos.x, pos.y + hoverAnim)
    val jetColor = if (!unit.isPlayerSide) LaserCrimson else CyanNeon
    val radius = 14.dp.toPx()

    // Selection Halo
    if (isSelected) {
        drawCircle(color = MatrixGreen, radius = radius + 6.dp.toPx(), center = jetPos, style = Stroke(width = 2f))
    }

    // Vapor Contrails behind Jet
    drawLine(color = CyanGlow.copy(alpha = 0.5f), start = Offset(jetPos.x - 22.dp.toPx(), jetPos.y), end = Offset(jetPos.x - 8.dp.toPx(), jetPos.y), strokeWidth = 2f)

    // Swept-Wing Jet Silhouette
    val path = Path()
    path.moveTo(jetPos.x + 14.dp.toPx(), jetPos.y) // Nose
    path.lineTo(jetPos.x - 8.dp.toPx(), jetPos.y - 12.dp.toPx()) // Left Wingtip
    path.lineTo(jetPos.x - 4.dp.toPx(), jetPos.y - 3.dp.toPx())
    path.lineTo(jetPos.x - 12.dp.toPx(), jetPos.y - 6.dp.toPx()) // Tail Left
    path.lineTo(jetPos.x - 10.dp.toPx(), jetPos.y) // Exhaust
    path.lineTo(jetPos.x - 12.dp.toPx(), jetPos.y + 6.dp.toPx()) // Tail Right
    path.lineTo(jetPos.x - 4.dp.toPx(), jetPos.y + 3.dp.toPx())
    path.lineTo(jetPos.x - 8.dp.toPx(), jetPos.y + 12.dp.toPx()) // Right Wingtip
    path.close()

    drawPath(path = path, color = jetColor, style = Fill)
    drawPath(path = path, color = SpaceBlack, style = Stroke(width = 1.5f))
}

private fun DrawScope.drawRtsSubmarine(unit: RtsUnit, isSelected: Boolean, pos: Offset, pulseAnim: Float) {
    val subColor = Color(0xFF0284C7)
    val radius = 12.dp.toPx()

    // Water ripple rings
    drawCircle(color = Color(0xFF38BDF8).copy(alpha = 0.35f * (1f - pulseAnim)), radius = 18.dp.toPx() * (1f + pulseAnim * 0.4f), center = pos)

    if (isSelected) {
        drawCircle(color = MatrixGreen, radius = radius + 6.dp.toPx(), center = pos, style = Stroke(width = 2f))
    }

    // Submarine Cigar Hull
    drawOval(color = subColor, topLeft = Offset(pos.x - 14.dp.toPx(), pos.y - 6.dp.toPx()), size = Size(28.dp.toPx(), 12.dp.toPx()))
    // Conning Tower & Periscope
    drawRect(color = Color(0xFF0369A1), topLeft = Offset(pos.x - 2.dp.toPx(), pos.y - 9.dp.toPx()), size = Size(6.dp.toPx(), 4.dp.toPx()))
    drawLine(color = Color(0xFFE2E8F0), start = Offset(pos.x + 1.dp.toPx(), pos.y - 9.dp.toPx()), end = Offset(pos.x + 1.dp.toPx(), pos.y - 14.dp.toPx()), strokeWidth = 1.5f)
}

private fun DrawScope.drawRtsInfantry(unit: RtsUnit, isSelected: Boolean, pos: Offset) {
    val infColor = if (!unit.isPlayerSide) LaserCrimson else MatrixGreen
    if (isSelected) {
        drawCircle(color = MatrixGreen, radius = 14.dp.toPx(), center = pos, style = Stroke(width = 2f))
    }
    // Squad of 3 soldiers
    drawCircle(color = infColor, radius = 4.dp.toPx(), center = Offset(pos.x - 4.dp.toPx(), pos.y - 3.dp.toPx()))
    drawCircle(color = infColor, radius = 4.dp.toPx(), center = Offset(pos.x + 4.dp.toPx(), pos.y - 3.dp.toPx()))
    drawCircle(color = infColor, radius = 4.dp.toPx(), center = Offset(pos.x, pos.y + 4.dp.toPx()))
}

private fun DrawScope.drawRtsSiege(unit: RtsUnit, isSelected: Boolean, pos: Offset) {
    val siegeColor = WarningOrange
    if (isSelected) {
        drawCircle(color = MatrixGreen, radius = 16.dp.toPx(), center = pos, style = Stroke(width = 2f))
    }
    drawRect(color = siegeColor, topLeft = Offset(pos.x - 10.dp.toPx(), pos.y - 7.dp.toPx()), size = Size(20.dp.toPx(), 14.dp.toPx()))
    drawLine(color = SpaceBlack, start = pos, end = Offset(pos.x + 15.dp.toPx(), pos.y), strokeWidth = 4f)
}

private fun getBuildingThemeColor(type: RtsBuildingType): Color {
    return when (type) {
        RtsBuildingType.HQ -> CyanNeon
        RtsBuildingType.POWER_PLANT -> EnergyYellow
        RtsBuildingType.SUPPLY_DEPOT -> AmberGold
        RtsBuildingType.BARRACKS -> MatrixGreen
        RtsBuildingType.FACTORY -> AmberLight
        RtsBuildingType.AIRFIELD -> CyanGlow
        RtsBuildingType.SHIPYARD -> Color(0xFF38BDF8)
    }
}

private fun getBuildingVectorIcon(type: RtsBuildingType): ImageVector {
    return when (type) {
        RtsBuildingType.HQ -> Icons.Default.Security
        RtsBuildingType.POWER_PLANT -> Icons.Default.Bolt
        RtsBuildingType.SUPPLY_DEPOT -> Icons.Default.Inventory2
        RtsBuildingType.BARRACKS -> Icons.Default.Person
        RtsBuildingType.FACTORY -> Icons.Default.CarRepair
        RtsBuildingType.AIRFIELD -> Icons.Default.AirplanemodeActive
        RtsBuildingType.SHIPYARD -> Icons.Default.DirectionsBoat
    }
}

private fun getUnitTypeVector(type: UnitType): ImageVector {
    return when (type) {
        UnitType.INFANTRY -> Icons.Default.Person
        UnitType.TANK -> Icons.Default.CarRepair
        UnitType.JET -> Icons.Default.AirplanemodeActive
        UnitType.SUBMARINE -> Icons.Default.DirectionsBoat
        UnitType.SIEGE -> Icons.Default.PrecisionManufacturing
    }
}

/**
 * Tactical Unit Selection Marker:
 * - Animated pulsing circular reticle.
 * - 4 Corner tick marks (+ crosshairs).
 * - 4 Rotating chevrons.
 */
private fun DrawScope.drawTacticalSelectionMarker(
    pos: Offset,
    radius: Float,
    rotationAnim: Float,
    pulseAnim: Float
) {
    val markerColor = MatrixGreen
    val haloRadius = radius + 8.dp.toPx()

    // 1. Animated Outer Pulsing Circle
    drawCircle(
        color = markerColor.copy(alpha = 0.35f * (1f - pulseAnim * 0.4f)),
        radius = haloRadius + pulseAnim * 4.dp.toPx(),
        center = pos,
        style = Stroke(width = 1.5f)
    )

    // 2. Solid Inner Circle
    drawCircle(
        color = markerColor,
        radius = haloRadius,
        center = pos,
        style = Stroke(width = 2f)
    )

    // 3. Four Tactical Corner Tick Marks (+)
    val tickLen = 6.dp.toPx()
    drawLine(markerColor, Offset(pos.x, pos.y - haloRadius - tickLen), Offset(pos.x, pos.y - haloRadius), 2f)
    drawLine(markerColor, Offset(pos.x, pos.y + haloRadius), Offset(pos.x, pos.y + haloRadius + tickLen), 2f)
    drawLine(markerColor, Offset(pos.x - haloRadius - tickLen, pos.y), Offset(pos.x - haloRadius, pos.y), 2f)
    drawLine(markerColor, Offset(pos.x + haloRadius, pos.y), Offset(pos.x + haloRadius + tickLen, pos.y), 2f)

    // 4. Rotating Chevrons around unit
    withTransform({
        rotate(rotationAnim, pivot = pos)
    }) {
        for (i in 0 until 4) {
            val angle = (i * 90f) * (Math.PI / 180f)
            val cx = (pos.x + cos(angle) * (haloRadius - 2f)).toFloat()
            val cy = (pos.y + sin(angle) * (haloRadius - 2f)).toFloat()
            drawCircle(color = CyanNeon, radius = 2.dp.toPx(), center = Offset(cx, cy))
        }
    }
}

