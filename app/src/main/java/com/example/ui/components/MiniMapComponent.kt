package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
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
 * MiniMapComponent:
 * Visualizes the game world, allowing players to navigate the larger map more easily
 * and clearly see building placements, player/enemy units, and the current camera viewport.
 * Tapping any coordinate on the mini-map pans the main battlefield camera directly to that position.
 */
@Composable
fun MiniMapComponent(
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
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_sweep"
    )

    val playerUnits = units.filter { it.isPlayerSide && it.hp > 0 }
    val enemyUnits = units.filter { !it.isPlayerSide && it.hp > 0 }

    Surface(
        modifier = modifier
            .size(175.dp, 115.dp)
            .testTag("mini_map_component"),
        color = SpaceBlack.copy(alpha = 0.70f),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(CyanNeon.copy(alpha = 0.8f), SurfaceBorder)
            )
        ),
        shadowElevation = 8.dp
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

                // 1. Terrain Base Grid / Background
                drawRect(color = Color(0xFF132A13), size = size)

                // Thematic terrain features (Rivers/Desert/Road lines)
                val riverColor = Color(0xFF1E3A8A).copy(alpha = 0.45f)
                drawLine(
                    color = riverColor,
                    start = Offset(mapW * 0.7f, 0f),
                    end = Offset(mapW * 0.82f, mapH),
                    strokeWidth = 3.dp.toPx()
                )

                // 2. Fog-of-War Vision Calculation
                val visionCenters = mutableListOf<Pair<Offset, Float>>()
                buildings.forEach { b ->
                    visionCenters.add(Pair(Offset(b.worldX * sx, b.worldY * sy), 24.dp.toPx()))
                }
                playerUnits.forEach { u ->
                    val r = if (u.unitType == UnitType.JET) 32.dp.toPx() else 20.dp.toPx()
                    visionCenters.add(Pair(Offset(u.posX * sx, u.posY * sy), r))
                }

                // Dark fog overlay
                drawRect(color = SpaceBlack.copy(alpha = 0.48f), size = size)

                // Clear visible radius
                visionCenters.forEach { (center, radius) ->
                    drawCircle(
                        color = Color.Transparent,
                        radius = radius,
                        center = center,
                        blendMode = androidx.compose.ui.graphics.BlendMode.DstOut
                    )
                }

                // 3. Clear Building Placements
                buildings.forEach { building ->
                    val bx = building.worldX * sx
                    val by = building.worldY * sy

                    val buildingColor = when (building.type.name) {
                        "HQ" -> CyanNeon
                        "FACTORY" -> AmberGold
                        "AIRFIELD" -> Color(0xFF38BDF8)
                        "SHIPYARD" -> Color(0xFF0284C7)
                        "POWER_PLANT" -> Color(0xFFFACC15)
                        else -> MatrixGreen
                    }

                    // Building Box with glow outline
                    val boxSize = 6.dp.toPx()
                    drawRect(
                        color = buildingColor,
                        topLeft = Offset(bx - boxSize / 2, by - boxSize / 2),
                        size = Size(boxSize, boxSize)
                    )
                    drawRect(
                        color = Color.White.copy(alpha = 0.6f),
                        topLeft = Offset(bx - boxSize / 2, by - boxSize / 2),
                        size = Size(boxSize, boxSize),
                        style = Stroke(width = 1f)
                    )
                }

                // 4. Player Units
                playerUnits.forEach { u ->
                    val ux = u.posX * sx
                    val uy = u.posY * sy

                    if (u.isSelected) {
                        drawCircle(
                            color = AmberGold.copy(alpha = 0.5f),
                            radius = 4.dp.toPx(),
                            center = Offset(ux, uy),
                            style = Stroke(width = 1f)
                        )
                    }

                    drawCircle(
                        color = if (u.isSelected) AmberGold else MatrixGreen,
                        radius = 2.2.dp.toPx(),
                        center = Offset(ux, uy)
                    )
                }

                // 5. Enemy Units
                enemyUnits.forEach { u ->
                    val ux = u.posX * sx
                    val uy = u.posY * sy

                    val inVision = visionCenters.any { (c, r) ->
                        hypot((ux - c.x).toDouble(), (uy - c.y).toDouble()) <= r
                    }

                    drawCircle(
                        color = if (inVision) LaserCrimson else LaserCrimson.copy(alpha = 0.35f),
                        radius = 2.2.dp.toPx(),
                        center = Offset(ux, uy)
                    )
                }

                // 6. Camera Viewport Boundary
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
                    style = Stroke(width = 1.2f)
                )
            }

            // Top Header: Label + Quick GPS badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = CyanNeon,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = skirmishMap?.title?.take(14) ?: "Savaş Alanı",
                    color = TextPrimary,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${buildings.size} Bina",
                    color = AmberGold,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
