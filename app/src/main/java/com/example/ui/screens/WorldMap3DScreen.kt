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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.Nation
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
import kotlin.math.sqrt

enum class WorldMapProjection {
    CYLINDRICAL_3D_PLANE,
    SPHERICAL_GLOBE
}

data class WorldRegion(
    val id: String,
    val name: String,
    val ownerNation: Nation?,
    val centerNormX: Float, // Normalized 0f..1f
    val centerNormY: Float, // Normalized 0f..1f
    val radiusNorm: Float,
    val geoCoords: String,
    val goldYield: Int,
    val oilYield: Int,
    val energyYield: Int,
    val defenseLevel: Int,
    val description: String
)

data class IntercontinentalStrike(
    val id: String,
    val fromRegionId: String,
    val toRegionId: String,
    val fromNorm: Offset,
    val toNorm: Offset,
    var progress: Float = 0f,
    val isMissile: Boolean = true // true: Ballistic Arc (BORA/TAYFUN), false: Jet Squadron
)

/**
 * Interactive 3D World Map & Territory Management Screen:
 * - Switchable Projection: 3D Cylindrical Flat Plane (Mobile optimized) vs 3D Spherical Globe.
 * - Interactive Territories with touch/raycast highlight borders.
 * - Nation headquarters: Anadolu (Türkiye), Washington (USA), Moscow (Russia), Beijing (China).
 * - Real-time Intercontinental Warfare Simulator with 3D Parabolic Trajectory Arc rendering.
 * - Geographic Coordinates Quick-Focus bar.
 */
@Composable
fun WorldMap3DScreen(
    currentNation: Nation,
    currentEra: Era,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var projection by remember { mutableStateOf(WorldMapProjection.CYLINDRICAL_3D_PLANE) }
    var selectedRegionId by remember { mutableStateOf<String?>("anadolu") }
    var globeRotationX by remember { mutableFloatStateOf(0f) }
    var globeRotationY by remember { mutableFloatStateOf(0f) }
    val activeStrikes = remember { mutableStateListOf<IntercontinentalStrike>() }

    // Predefined Strategic World Territories
    val regions = remember {
        listOf(
            WorldRegion(
                id = "anadolu",
                name = "Anadolu & Doğu Akdeniz",
                ownerNation = Nation.TURKEY,
                centerNormX = 0.54f,
                centerNormY = 0.38f,
                radiusNorm = 0.055f,
                geoCoords = "39.0° N, 35.0° E",
                goldYield = 65,
                oilYield = 85,
                energyYield = 120,
                defenseLevel = 4,
                description = "Kıtaların kesişim noktası. Boğazlar hakimiyeti, İHA üsleri ve stratejik doğalgaz boru hatları."
            ),
            WorldRegion(
                id = "north_america",
                name = "Kuzey Amerika & Pentagon",
                ownerNation = Nation.USA,
                centerNormX = 0.22f,
                centerNormY = 0.35f,
                radiusNorm = 0.09f,
                geoCoords = "38.9° N, 77.0° W",
                goldYield = 90,
                oilYield = 70,
                energyYield = 180,
                defenseLevel = 5,
                description = "Küresel hava ve deniz lojistik merkezi. NORAD erken uyarı radarları ve uçak gemisi filoları."
            ),
            WorldRegion(
                id = "eurasia",
                name = "Avrasya & Sibirya",
                ownerNation = Nation.RUSSIA,
                centerNormX = 0.68f,
                centerNormY = 0.26f,
                radiusNorm = 0.11f,
                geoCoords = "55.7° N, 37.6° E",
                goldYield = 75,
                oilYield = 110,
                energyYield = 140,
                defenseLevel = 5,
                description = "Geniş kara sahası, nükleer denizaltı üsleri ve derin petrol/mineral yatakları."
            ),
            WorldRegion(
                id = "east_asia",
                name = "Doğu Asya & Pasifik",
                ownerNation = Nation.CHINA,
                centerNormX = 0.80f,
                centerNormY = 0.40f,
                radiusNorm = 0.085f,
                geoCoords = "39.9° N, 116.4° E",
                goldYield = 110,
                oilYield = 50,
                energyYield = 200,
                defenseLevel = 5,
                description = "Dünya sanayi kalbi. Seri üretim mega fabrikalar, hipersonik füze rampaları ve yapay adalar."
            ),
            WorldRegion(
                id = "europe",
                name = "Batı & Orta Avrupa",
                ownerNation = null,
                centerNormX = 0.48f,
                centerNormY = 0.30f,
                radiusNorm = 0.05f,
                geoCoords = "48.8° N, 2.3° E",
                goldYield = 80,
                oilYield = 20,
                energyYield = 90,
                defenseLevel = 3,
                description = "Yüksek teknoloji araştırma merkezleri ve kritik endüstriyel altyapı."
            ),
            WorldRegion(
                id = "middle_east_oil",
                name = "Basra & Körfez Havzası",
                ownerNation = null,
                centerNormX = 0.59f,
                centerNormY = 0.45f,
                radiusNorm = 0.045f,
                geoCoords = "25.0° N, 50.0° E",
                goldYield = 120,
                oilYield = 220,
                energyYield = 40,
                defenseLevel = 3,
                description = "Küresel petrolün kalbi. Stratejik ham petrol rafinerileri ve tanker hatları."
            ),
            WorldRegion(
                id = "africa",
                name = "Afrika Kıtası",
                ownerNation = null,
                centerNormX = 0.50f,
                centerNormY = 0.62f,
                radiusNorm = 0.09f,
                geoCoords = "9.1° N, 18.2° E",
                goldYield = 95,
                oilYield = 90,
                energyYield = 50,
                defenseLevel = 2,
                description = "Nadir toprak elementleri, lityum yatakları ve güneş enerjisi potansiyeli."
            ),
            WorldRegion(
                id = "south_america",
                name = "Güney Amerika",
                ownerNation = null,
                centerNormX = 0.30f,
                centerNormY = 0.68f,
                radiusNorm = 0.075f,
                geoCoords = "14.2° S, 51.9° W",
                goldYield = 60,
                oilYield = 70,
                energyYield = 60,
                defenseLevel = 2,
                description = "Biyo-çeşitlilik, lityum üçgeni ve Atlantik deniz koridoru."
            )
        )
    }

    // Animation ticker for ballistic parabolic arc trajectories
    val infiniteTransition = rememberInfiniteTransition(label = "worldMapAnim")
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animTime"
    )

    // Update active strike positions
    activeStrikes.forEach { strike ->
        strike.progress = (strike.progress + 0.015f).coerceIn(0f, 1f)
    }
    activeStrikes.removeAll { it.progress >= 1f }

    val selectedRegion = regions.find { it.id == selectedRegionId }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
    ) {
        // ==================== 1. 3D INTERACTIVE TACTICAL CANVAS ====================
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(projection) {
                    detectDragGestures { _, dragAmount ->
                        if (projection == WorldMapProjection.SPHERICAL_GLOBE) {
                            globeRotationX = (globeRotationX + dragAmount.x * 0.4f) % 360f
                            globeRotationY = (globeRotationY - dragAmount.y * 0.4f).coerceIn(-60f, 60f)
                        }
                    }
                }
                .pointerInput(regions) {
                    detectTapGestures { tapOffset ->
                        val w = size.width
                        val h = size.height
                        val normX = tapOffset.x / w
                        val normY = tapOffset.y / h

                        // Raycast hit check on territories
                        val hit = regions.minByOrNull { region ->
                            val dx = region.centerNormX - normX
                            val dy = region.centerNormY - normY
                            sqrt((dx * dx + dy * dy).toDouble())
                        }

                        if (hit != null) {
                            val dist = sqrt(
                                (hit.centerNormX - normX) * (hit.centerNormX - normX) +
                                (hit.centerNormY - normY) * (hit.centerNormY - normY)
                            )
                            if (dist < hit.radiusNorm * 1.5f) {
                                selectedRegionId = hit.id
                            }
                        }
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Background Tactical Deep Ocean Grid
            drawWorldOceanBackground(canvasWidth, canvasHeight, projection, animTime)

            // Render Continents and Borders
            drawContinentsAndBorders(
                regions = regions,
                selectedRegionId = selectedRegionId,
                projection = projection,
                rotX = globeRotationX,
                rotY = globeRotationY,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                animTime = animTime
            )

            // Render Intercontinental 3D Parabolic Missile Arcs
            drawIntercontinentalStrikes(
                strikes = activeStrikes,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                animTime = animTime
            )
        }

        // ==================== 2. TOP HUD: TITLE & PROJECTION SWITCHER ====================
        Surface(
            color = SurfaceDark.copy(alpha = 0.92f),
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.5f), AmberGold.copy(alpha = 0.5f)))
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "3D KÜRESEL SAVAŞ VE BÖLGE HARİTASI", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Projection Switcher Pill
                Surface(
                    color = if (projection == WorldMapProjection.CYLINDRICAL_3D_PLANE) CyanNeon else SurfaceElevated,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .clickable { projection = WorldMapProjection.CYLINDRICAL_3D_PLANE }
                        .testTag("switch_proj_plane")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = null,
                            tint = if (projection == WorldMapProjection.CYLINDRICAL_3D_PLANE) SpaceBlack else TextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "3D Düzlem (Mobil Hızlı)",
                            color = if (projection == WorldMapProjection.CYLINDRICAL_3D_PLANE) SpaceBlack else TextPrimary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = if (projection == WorldMapProjection.SPHERICAL_GLOBE) CyanNeon else SurfaceElevated,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .clickable { projection = WorldMapProjection.SPHERICAL_GLOBE }
                        .testTag("switch_proj_globe")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = if (projection == WorldMapProjection.SPHERICAL_GLOBE) SpaceBlack else TextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "3D Küre (Döndürülebilir)",
                            color = if (projection == WorldMapProjection.SPHERICAL_GLOBE) SpaceBlack else TextPrimary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Close Button
                Surface(
                    color = DangerRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable { onClose() }.testTag("close_world_map_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = DangerRed,
                        modifier = Modifier.padding(6.dp).size(14.dp)
                    )
                }
            }
        }

        // ==================== 3. BOTTOM-LEFT: REGION INSPECTOR HUD ====================
        if (selectedRegion != null) {
            Surface(
                color = SurfaceDark.copy(alpha = 0.94f),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (selectedRegion.ownerNation != null) CyanNeon else AmberGold,
                            SurfaceBorder
                        )
                    )
                ),
                shadowElevation = 10.dp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
                    .width(320.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Region Title & Owner Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = selectedRegion.ownerNation?.flagEmoji ?: "🌐",
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = selectedRegion.name,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Koordinat: ${selectedRegion.geoCoords}",
                                color = TextMuted,
                                fontSize = 8.sp
                            )
                        }

                        Surface(
                            color = if (selectedRegion.ownerNation != null) MatrixGreen.copy(alpha = 0.2f) else SurfaceElevated,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = selectedRegion.ownerNation?.title ?: "Tarafsız Bölge",
                                color = if (selectedRegion.ownerNation != null) MatrixGreen else TextSecondary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = selectedRegion.description,
                        color = TextSecondary,
                        fontSize = 8.sp,
                        lineHeight = 11.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Strategic Resources Yields Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceElevated)
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "💰 Altın", color = AmberGold, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text(text = "+${selectedRegion.goldYield}/s", color = TextPrimary, fontSize = 9.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🛢️ Petrol", color = Color(0xFFF59E0B), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text(text = "+${selectedRegion.oilYield}/s", color = TextPrimary, fontSize = 9.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⚡ Enerji", color = EnergyYellow, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text(text = "+${selectedRegion.energyYield} MW", color = TextPrimary, fontSize = 9.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🛡️ Savunma", color = CyanNeon, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Sv. ${selectedRegion.defenseLevel}", color = TextPrimary, fontSize = 9.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tactical Actions (Intercontinental Strike Simulation)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                // Launch Ballistic Missile from current nation to selected region
                                val fromRegion = regions.find { it.ownerNation == currentNation } ?: regions.first()
                                activeStrikes.add(
                                    IntercontinentalStrike(
                                        id = "strike_${System.currentTimeMillis()}",
                                        fromRegionId = fromRegion.id,
                                        toRegionId = selectedRegion.id,
                                        fromNorm = Offset(fromRegion.centerNormX, fromRegion.centerNormY),
                                        toNorm = Offset(selectedRegion.centerNormX, selectedRegion.centerNormY),
                                        isMissile = true
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LaserCrimson),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp).weight(1f).testTag("launch_missile_strike_btn")
                        ) {
                            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "🚀 BORA/TAYFUN Ateşle", color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val fromRegion = regions.find { it.ownerNation == currentNation } ?: regions.first()
                                activeStrikes.add(
                                    IntercontinentalStrike(
                                        id = "jet_${System.currentTimeMillis()}",
                                        fromRegionId = fromRegion.id,
                                        toRegionId = selectedRegion.id,
                                        fromNorm = Offset(fromRegion.centerNormX, fromRegion.centerNormY),
                                        toNorm = Offset(selectedRegion.centerNormX, selectedRegion.centerNormY),
                                        isMissile = false
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp).weight(1f).testTag("deploy_jet_strike_btn")
                        ) {
                            Icon(imageVector = Icons.Default.FlightTakeoff, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "✈️ Jet Filosu Sevk", color = SpaceBlack, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ==================== 4. BOTTOM-RIGHT: GEOGRAPHIC COORDINATES QUICK FOCUS ====================
        Surface(
            color = SurfaceDark.copy(alpha = 0.92f),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated))),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "COĞRAFİ KOORDİNAT ODAKLANMA", color = TextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    regions.filter { it.ownerNation != null }.forEach { r ->
                        val isSelected = (r.id == selectedRegionId)
                        Surface(
                            color = if (isSelected) CyanNeon else SurfaceElevated,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .clickable { selectedRegionId = r.id }
                                .testTag("focus_coord_${r.id}")
                        ) {
                            Text(
                                text = "${r.ownerNation?.flagEmoji} ${r.name.split(" ").first()}",
                                color = if (isSelected) SpaceBlack else TextPrimary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Tactical Deep Ocean Grid Background
 */
private fun DrawScope.drawWorldOceanBackground(
    w: Float,
    h: Float,
    projection: WorldMapProjection,
    animTime: Float
) {
    // Gradient ocean: Midnight navy to abyssal teal
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF030D1B), Color(0xFF05172C), Color(0xFF020914))
        ),
        size = Size(w, h)
    )

    // Latitude and Longitude Tactical Grid lines
    val gridPaint = Color(0xFF0E2A47)
    val stepX = w / 12f
    val stepY = h / 8f

    for (i in 0..12) {
        val x = i * stepX
        drawLine(
            color = gridPaint,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
        )
    }

    for (j in 0..8) {
        val y = j * stepY
        drawLine(
            color = gridPaint,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
        )
    }

    // Equator Line (Accent cyan)
    val equatorY = h * 0.5f
    drawLine(
        color = CyanGlow.copy(alpha = 0.4f),
        start = Offset(0f, equatorY),
        end = Offset(w, equatorY),
        strokeWidth = 1.5f
    )
}

/**
 * Continents, Territories and Raycast Highlight Borders
 */
private fun DrawScope.drawContinentsAndBorders(
    regions: List<WorldRegion>,
    selectedRegionId: String?,
    projection: WorldMapProjection,
    rotX: Float,
    rotY: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    animTime: Float
) {
    regions.forEach { region ->
        val isSelected = (region.id == selectedRegionId)
        val center = Offset(region.centerNormX * canvasWidth, region.centerNormY * canvasHeight)
        val radius = region.radiusNorm * canvasWidth

        val territoryColor = when (region.ownerNation) {
            Nation.TURKEY -> LaserCrimson.copy(alpha = if (isSelected) 0.65f else 0.45f)
            Nation.USA -> Color(0xFF2563EB).copy(alpha = if (isSelected) 0.65f else 0.45f)
            Nation.RUSSIA -> Color(0xFFDC2626).copy(alpha = if (isSelected) 0.65f else 0.45f)
            Nation.CHINA -> Color(0xFFEA580C).copy(alpha = if (isSelected) 0.65f else 0.45f)
            null -> Color(0xFF1E3A5F).copy(alpha = if (isSelected) 0.55f else 0.35f)
        }

        val borderColor = when (region.ownerNation) {
            Nation.TURKEY -> AmberGold
            Nation.USA -> CyanNeon
            Nation.RUSSIA -> Color(0xFFF87171)
            Nation.CHINA -> Color(0xFFFBBF24)
            null -> Color(0xFF64748B)
        }

        // Fill Territory
        drawCircle(
            color = territoryColor,
            radius = radius,
            center = center
        )

        // Border Outline
        drawCircle(
            color = if (isSelected) CyanNeon else borderColor,
            radius = radius,
            center = center,
            style = Stroke(width = if (isSelected) 3f else 1.5f)
        )

        // Pulsing Neon Highlight Rings on Selected Region
        if (isSelected) {
            drawCircle(
                color = CyanNeon.copy(alpha = 0.4f * (1f - animTime)),
                radius = radius + animTime * 14.dp.toPx(),
                center = center,
                style = Stroke(width = 2f)
            )
        }

        // Region Label & Owner Flag
        drawCircle(
            color = borderColor,
            radius = 3.dp.toPx(),
            center = center
        )
    }
}

/**
 * 3D Parabolic Missile Arc Trajectory Simulation (BORA / TAYFUN / ICBM):
 * Uses 3D height offset parabola: height(t) = 4 * maxHeight * t * (1 - t)
 */
private fun DrawScope.drawIntercontinentalStrikes(
    strikes: List<IntercontinentalStrike>,
    canvasWidth: Float,
    canvasHeight: Float,
    animTime: Float
) {
    strikes.forEach { strike ->
        val start = Offset(strike.fromNorm.x * canvasWidth, strike.fromNorm.y * canvasHeight)
        val end = Offset(strike.toNorm.x * canvasWidth, strike.toNorm.y * canvasHeight)

        val totalDist = sqrt(((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y)).toDouble()).toFloat()
        val arcApexHeight = (totalDist * 0.35f).coerceIn(60f, 220f)

        // Draw parabolic curve path
        val path = Path()
        val steps = 30
        for (i in 0..steps) {
            val t = i.toFloat() / steps.toFloat()
            val x = start.x + (end.x - start.x) * t
            val linearY = start.y + (end.y - start.y) * t
            val parabolicOffset = 4f * arcApexHeight * t * (1f - t)
            val y = linearY - parabolicOffset

            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        // Trail glow
        drawPath(
            path = path,
            color = if (strike.isMissile) LaserCrimson.copy(alpha = 0.5f) else CyanNeon.copy(alpha = 0.5f),
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), phase = animTime * 20f)
            )
        )

        // Current projectile position along the 3D parabolic arc
        val curT = strike.progress
        val curX = start.x + (end.x - start.x) * curT
        val curLinearY = start.y + (end.y - start.y) * curT
        val curParabolicOffset = 4f * arcApexHeight * curT * (1f - curT)
        val curY = curLinearY - curParabolicOffset
        val curPos = Offset(curX, curY)

        // Draw missile warhead / jet icon
        drawCircle(
            color = if (strike.isMissile) AmberGold else CyanNeon,
            radius = 5.dp.toPx(),
            center = curPos
        )
        drawCircle(
            color = Color.White,
            radius = 2.5.dp.toPx(),
            center = curPos
        )

        // Trailing jet fire / rocket plume
        drawCircle(
            color = if (strike.isMissile) WarningOrange.copy(alpha = 0.6f) else MatrixGreen.copy(alpha = 0.6f),
            radius = 8.dp.toPx(),
            center = curPos,
            style = Stroke(width = 1.5f)
        )
    }
}
