package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RtsBuilding
import com.example.model.RtsUnit
import com.example.model.SkirmishMap
import com.example.model.UnitType
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DangerRed
import com.example.ui.theme.LaserCrimson
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.hypot

/**
 * Transparent, Top-Right Mini-Map Component for Landscape RTS:
 * - Transparent glassmorphic aesthetic (semi-transparent dark background).
 * - Renders a simplified, thematic overview of the current skirmish map (desert rivers, straits, mountains, oceans).
 * - Highlights active player units with green blips & sonar rings.
 * - Highlights enemy locations with red blips.
 * - Simulates Fog-of-War (Savaş Sisi): areas outside player vision range are darkened with tactical fog.
 * - Interactive: Tapping anywhere on the mini-map instantly centers the main camera.
 */
@Composable
fun RtsMiniMap(
    buildings: List<RtsBuilding>,
    units: List<RtsUnit>,
    cameraOffsetX: Float,
    cameraOffsetY: Float,
    cameraZoom: Float,
    skirmishMap: SkirmishMap? = null,
    worldWidth: Float = 2400f,
    worldHeight: Float = 1600f,
    onMiniMapTap: (worldX: Float, worldY: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "minimap_anim")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radar"
    )

    val playerUnits = units.filter { it.isPlayerSide && it.hp > 0 }
    val enemyUnits = units.filter { !it.isPlayerSide && it.hp > 0 }

    Surface(
        modifier = modifier
            .size(175.dp, 115.dp)
            .testTag("rts_minimap"),
        color = SpaceBlack.copy(alpha = 0.62f), // Transparent glassmorphism
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(CyanNeon.copy(alpha = 0.75f), Color(skirmishMap?.accentColorHex ?: 0xFF00F5D4).copy(alpha = 0.5f))
            )
        ),
        shadowElevation = 10.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(worldWidth, worldHeight) {
                        detectTapGestures { tapOffset ->
                            val scaleX = worldWidth / size.width
                            val scaleY = worldHeight / size.height
                            val targetWorldX = tapOffset.x * scaleX
                            val targetWorldY = tapOffset.y * scaleY
                            onMiniMapTap(targetWorldX, targetWorldY)
                        }
                    }
            ) {
                val mapW = size.width
                val mapH = size.height
                val sx = mapW / worldWidth
                val sy = mapH / worldHeight

                // 1. SKIRMISH MAP THEMATIC TERRAIN OVERVIEW
                drawSkirmishTerrainOverview(skirmishMap, mapW, mapH, sx, sy)

                // 2. FOG-OF-WAR (SAVAŞ SİSİ) OVERLAY
                // Calculate vision sources from friendly buildings and units
                val visionCenters = mutableListOf<Pair<Offset, Float>>()

                // Friendly Buildings vision (~220px world radius)
                buildings.forEach { b ->
                    visionCenters.add(Pair(Offset(b.worldX * sx, b.worldY * sy), 26.dp.toPx()))
                }

                // Friendly Units vision (~180px - 280px world radius)
                playerUnits.forEach { u ->
                    val r = when (u.unitType) {
                        UnitType.JET -> 34.dp.toPx()
                        UnitType.SUBMARINE -> 28.dp.toPx()
                        else -> 22.dp.toPx()
                    }
                    visionCenters.add(Pair(Offset(u.posX * sx, u.posY * sy), r))
                }

                // Draw base Fog of War (Dark veil over unexplored/unseen regions)
                drawRect(color = SpaceBlack.copy(alpha = 0.55f), size = size)

                // Reveal vision pockets around friendly assets
                visionCenters.forEach { (center, radius) ->
                    drawCircle(
                        color = Color.Transparent,
                        radius = radius,
                        center = center,
                        blendMode = androidx.compose.ui.graphics.BlendMode.DstOut
                    )
                }

                // 3. FRIENDLY BUILDINGS
                buildings.forEach { b ->
                    val bx = b.worldX * sx
                    val by = b.worldY * sy
                    val color = when (b.type.name) {
                        "HQ" -> CyanNeon
                        "FACTORY" -> AmberGold
                        "AIRFIELD" -> Color(0xFF67E8F9)
                        "SHIPYARD" -> Color(0xFF38BDF8)
                        else -> MatrixGreen
                    }
                    drawRect(
                        color = color,
                        topLeft = Offset(bx - 2.5.dp.toPx(), by - 2.5.dp.toPx()),
                        size = Size(5.dp.toPx(), 5.dp.toPx())
                    )
                }

                // 4. ACTIVE PLAYER UNITS (GREEN BLIPS & SONAR RINGS)
                playerUnits.forEach { u ->
                    val ux = u.posX * sx
                    val uy = u.posY * sy
                    val isSelected = u.isSelected

                    // Sonar ring for moving/attacking units
                    if (u.isMoving || u.isAttacking || isSelected) {
                        drawCircle(
                            color = MatrixGreen.copy(alpha = 0.45f * (1f - pulseAnim)),
                            radius = (4.dp.toPx() + pulseAnim * 8.dp.toPx()),
                            center = Offset(ux, uy),
                            style = Stroke(width = 1f)
                        )
                    }

                    // Solid unit core
                    drawCircle(
                        color = if (isSelected) AmberGold else MatrixGreen,
                        radius = if (isSelected) 3.5.dp.toPx() else 2.2.dp.toPx(),
                        center = Offset(ux, uy)
                    )
                }

                // 5. ENEMY LOCATIONS (RED BLIPS)
                enemyUnits.forEach { u ->
                    val ux = u.posX * sx
                    val uy = u.posY * sy

                    // Check if enemy is within friendly vision (Fog of War check)
                    val isInFriendlyVision = visionCenters.any { (center, r) ->
                        hypot((ux - center.x).toDouble(), (uy - center.y).toDouble()) <= r
                    }

                    if (isInFriendlyVision) {
                        // High-visibility illuminated enemy with alert ring
                        drawCircle(
                            color = LaserCrimson.copy(alpha = 0.5f * (1f - pulseAnim)),
                            radius = (4.dp.toPx() + pulseAnim * 6.dp.toPx()),
                            center = Offset(ux, uy),
                            style = Stroke(width = 1.2f)
                        )
                        drawCircle(
                            color = LaserCrimson,
                            radius = 2.8.dp.toPx(),
                            center = Offset(ux, uy)
                        )
                    } else {
                        // Enemy veiled in fog: Faded ghost marker
                        drawCircle(
                            color = LaserCrimson.copy(alpha = 0.35f),
                            radius = 2f.dp.toPx(),
                            center = Offset(ux, uy)
                        )
                    }
                }

                // 6. CAMERA VIEWPORT BOUNDARY RECTANGLE
                val viewWorldW = (1920f / cameraZoom).coerceAtMost(worldWidth)
                val viewWorldH = (1080f / cameraZoom).coerceAtMost(worldHeight)

                val viewCenterWorldX = worldWidth / 2f - (cameraOffsetX / cameraZoom)
                val viewCenterWorldY = worldHeight / 2f - (cameraOffsetY / cameraZoom)

                val viewLeft = (viewCenterWorldX - viewWorldW / 2f) * sx
                val viewTop = (viewCenterWorldY - viewWorldH / 2f) * sy
                val viewW = viewWorldW * sx
                val viewH = viewWorldH * sy

                drawRect(
                    color = AmberGold,
                    topLeft = Offset(viewLeft, viewTop),
                    size = Size(viewW, viewH),
                    style = Stroke(width = 1.5f)
                )
            }

            // Top Header: Skirmish Map Title & Fog / Unit Count Status
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .background(SpaceBlack.copy(alpha = 0.65f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = skirmishMap?.ambientIcon?.let { "$it ${skirmishMap.title.take(15)}" } ?: "🗺️ Savaş Sahası",
                        color = Color(skirmishMap?.accentColorHex ?: 0xFF00F5D4),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(MatrixGreen))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "${playerUnits.size}", color = MatrixGreen, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(LaserCrimson))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "${enemyUnits.size}", color = LaserCrimson, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Bottom Subtitle Tag
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SpaceBlack.copy(alpha = 0.70f))
                    .padding(horizontal = 4.dp, vertical = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(8.dp))
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = "Savaş Sisi: Aktif", color = CyanNeon, fontSize = 6.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Renders thematic simplified terrain contours on the mini-map according to the chosen Skirmish battlefield
 */
private fun DrawScope.drawSkirmishTerrainOverview(
    map: SkirmishMap?,
    mapW: Float,
    mapH: Float,
    sx: Float,
    sy: Float
) {
    when (map?.id) {
        "map_mesopotamia" -> {
            // Sandy Arid Ground
            drawRect(color = Color(0xFF6B583E), size = size)
            // Euphrates and Tigris Dual Blue Rivers
            val river1 = Path().apply {
                moveTo(mapW * 0.25f, 0f)
                cubicTo(mapW * 0.35f, mapH * 0.4f, mapW * 0.20f, mapH * 0.7f, mapW * 0.45f, mapH)
            }
            drawPath(river1, color = Color(0xFF0284C7), style = Stroke(width = 4.dp.toPx()))

            val river2 = Path().apply {
                moveTo(mapW * 0.70f, 0f)
                cubicTo(mapW * 0.60f, mapH * 0.35f, mapW * 0.75f, mapH * 0.65f, mapW * 0.65f, mapH)
            }
            drawPath(river2, color = Color(0xFF0284C7), style = Stroke(width = 4.dp.toPx()))

            // Green Oasis Patches
            drawCircle(color = Color(0xFF2E7D32), radius = 6.dp.toPx(), center = Offset(mapW * 0.32f, mapH * 0.45f))
            drawCircle(color = Color(0xFF2E7D32), radius = 8.dp.toPx(), center = Offset(mapW * 0.68f, mapH * 0.25f))
        }
        "map_constantinople" -> {
            // Marmara Sea & Golden Horn Water
            drawRect(color = Color(0xFF0F172A), size = size)
            // Land Mass (European & Asian sides)
            drawRect(color = Color(0xFF334155), topLeft = Offset(0f, 0f), size = Size(mapW * 0.55f, mapH * 0.75f))
            drawRect(color = Color(0xFF334155), topLeft = Offset(mapW * 0.72f, mapH * 0.25f), size = Size(mapW * 0.28f, mapH * 0.75f))
            // Historical Double City Walls (Grey line)
            drawLine(
                color = AmberGold,
                start = Offset(mapW * 0.30f, 0f),
                end = Offset(mapW * 0.30f, mapH * 0.75f),
                strokeWidth = 2.dp.toPx()
            )
        }
        "map_gallipoli" -> {
            // Aegean Sea & Dardanelles Strait (Curved Blue)
            drawRect(color = Color(0xFF0284C7), size = size)
            // Gallipoli Peninsula Ridge (Arid green/brown)
            val peninsula = Path().apply {
                moveTo(0f, 0f)
                lineTo(mapW * 0.60f, 0f)
                lineTo(mapW * 0.45f, mapH)
                lineTo(0f, mapH)
                close()
            }
            drawPath(peninsula, color = Color(0xFF4D5B39))
            // Seddülbahir & Anafartalar Ridge Lines
            drawLine(
                color = Color(0xFF78350F),
                start = Offset(mapW * 0.25f, mapH * 0.15f),
                end = Offset(mapW * 0.35f, mapH * 0.85f),
                strokeWidth = 2.dp.toPx()
            )
        }
        "map_normandy" -> {
            // English Channel Waters in Northern Third
            drawRect(color = Color(0xFF1E3A8A), size = size)
            // Normandy Coast & Bocage Countryside
            drawRect(color = Color(0xFF166534), topLeft = Offset(0f, mapH * 0.35f), size = Size(mapW, mapH * 0.65f))
            // Sandy Landing Beach Strip (Omaha/Utah)
            drawRect(color = Color(0xFFD97706), topLeft = Offset(0f, mapH * 0.32f), size = Size(mapW, mapH * 0.05f))
        }
        "map_caucasus" -> {
            // Sarp Mountain Ranges (Dark Slate & Rocky White Peaks)
            drawRect(color = Color(0xFF1E293B), size = size)
            // Mountain Ridge Lines
            drawLine(color = Color(0xFFE2E8F0), start = Offset(0f, mapH * 0.3f), end = Offset(mapW, mapH * 0.25f), strokeWidth = 3.dp.toPx())
            drawLine(color = Color(0xFFE2E8F0), start = Offset(0f, mapH * 0.75f), end = Offset(mapW, mapH * 0.7f), strokeWidth = 3.dp.toPx())
            // Aras River Valley Corridor in Center
            drawLine(color = Color(0xFF0284C7), start = Offset(0f, mapH * 0.52f), end = Offset(mapW, mapH * 0.48f), strokeWidth = 3.dp.toPx())
        }
        "map_hormuz" -> {
            // Persian Gulf & Gulf of Oman Deep Blue Waters
            drawRect(color = Color(0xFF0369A1), size = size)
            // Northern Coastline (Iran side)
            drawRect(color = Color(0xFF78350F), topLeft = Offset(0f, 0f), size = Size(mapW, mapH * 0.28f))
            // Southern Coastline (Musandam / UAE side)
            drawRect(color = Color(0xFF78350F), topLeft = Offset(0f, mapH * 0.78f), size = Size(mapW * 0.55f, mapH * 0.22f))
            // Strategic Strait Islands & Oil Platform Pins
            drawCircle(color = Color(0xFFCA8A04), radius = 4.dp.toPx(), center = Offset(mapW * 0.52f, mapH * 0.42f))
            drawCircle(color = Color(0xFFCA8A04), radius = 3.dp.toPx(), center = Offset(mapW * 0.65f, mapH * 0.58f))
        }
        else -> {
            // Default Terrain Grass & River Channel
            drawRect(color = Color(0xFF1E3A1E), size = size)
            drawRect(color = Color(0xFF0284C7), topLeft = Offset(mapW * 0.72f, 0f), size = Size(mapW * 0.28f, mapH))
            drawRect(color = Color(0xFF334155), topLeft = Offset(140f * sx, 100f * sy), size = Size(580f * sx, 420f * sy))
        }
    }
}
