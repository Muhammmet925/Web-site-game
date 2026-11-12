package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.EraTransitionEffect
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

/**
 * Fullscreen atmospheric overlay triggered during Era Transitions:
 * - Medieval 5th Century: Rolling dense mist/fog, torch embers, archaic seal.
 * - Classical & Gunpowder: Billowing black powder smoke, golden imperial crest, cannon sparks.
 * - Industrial Revolution: Steam clouds, rotating mechanical brass gears, forge fire glow.
 * - Modern Warfare: Cybernetic military HUD glitch, satellite radar sweep, holographic targeting grid.
 */
@Composable
fun EraTransitionOverlay(
    effect: EraTransitionEffect,
    nation: Nation,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val targetEra = effect.targetEra

    // Automatic fade out after 4.5 seconds or on tap
    val progress = remember { Animatable(0f) }
    LaunchedEffect(effect) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 4500, easing = LinearEasing)
        )
        onDismiss()
    }

    // Infinite animations for gears, fog, radar sweep, and glitch
    val infiniteTransition = rememberInfiniteTransition(label = "era_fx")
    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gear_rotation"
    )

    val waveAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_sweep"
    )

    val glitchJitter by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(80, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glitch_jitter"
    )

    // Screen-flash animation (intense white/cyan flash at transition start)
    val flashAnim = remember { Animatable(1.0f) }
    LaunchedEffect(effect) {
        flashAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack.copy(alpha = 0.90f))
            .clickable { onDismiss() }
            .testTag("era_transition_overlay")
    ) {
        // Atmospheric Visual FX Canvas Layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            when (targetEra) {
                Era.MEDIEVAL_5TH -> drawMedievalFogFx(waveAnim)
                Era.CLASSICAL_GUNPOWDER -> drawGunpowderSmokeFx(waveAnim)
                Era.INDUSTRIAL -> drawIndustrialSteamAndGearsFx(rotationAnim, waveAnim)
                Era.MODERN -> drawModernGlitchAndRadarFx(rotationAnim, waveAnim, glitchJitter)
            }

            // Radial Blur & Shockwave Ring on technological shift
            val shockwaveRadius = size.minDimension * 0.7f * (1f - flashAnim.value)
            if (flashAnim.value > 0.05f) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.85f * flashAnim.value),
                            CyanNeon.copy(alpha = 0.5f * flashAnim.value),
                            Color.Transparent
                        ),
                        center = center,
                        radius = size.maxDimension * 0.9f
                    ),
                    radius = size.maxDimension,
                    center = center
                )
            }
        }

        // Screen Flash Overlay (Fades out quickly to simulate energetic technological shift)
        if (flashAnim.value > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Color.White.copy(alpha = 0.80f * flashAnim.value), Color.Transparent)
                        )
                    )
            )
        }

        // Center Content Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Era Badge & Emblem
            val accentColor = when (targetEra) {
                Era.MEDIEVAL_5TH -> AmberGold
                Era.CLASSICAL_GUNPOWDER -> WarningOrange
                Era.INDUSTRIAL -> Color(0xFFFB923C)
                Era.MODERN -> CyanNeon
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(2.dp, accentColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (targetEra) {
                        Era.MEDIEVAL_5TH -> Icons.Default.Security
                        Era.CLASSICAL_GUNPOWDER -> Icons.Default.FlashOn
                        Era.INDUSTRIAL -> Icons.Default.PrecisionManufacturing
                        Era.MODERN -> Icons.Default.AirplanemodeActive
                    },
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle tag
            Surface(
                color = accentColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStrokeWrapper(accentColor)
            ) {
                Text(
                    text = "ÇAĞ GEÇİŞİ TAMAMLANDI • ${targetEra.yearTag}",
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Major Title
            Text(
                text = targetEra.title.uppercase(),
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = targetEra.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                modifier = Modifier.fillMaxWidth(0.85f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Unlocked Technological Capabilities Card
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.7f), SurfaceBorder))
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "YENİ AÇILAN ASKERİ DOKTRİNLER (${nation.flagEmoji} ${nation.title})",
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    when (targetEra) {
                        Era.MEDIEVAL_5TH -> {
                            UnlockedItemRow("🏰 Feodal Kale Komuta Merkezi", "Temel Altın madenciliği ve kılıçlı akıncılar devrede.")
                            UnlockedItemRow("⚔️ Atlı Akıncı & Mancınık", "Ağır ahşap kuşatma silahları kışlada açıldı.")
                            UnlockedItemRow("🔥 General Gücü: Mancınık Barajı", "Düşman üssüne yoğun taş/alev barajı.")
                        }
                        Era.CLASSICAL_GUNPOWDER -> {
                            UnlockedItemRow("💣 Şahi Kuşatma Topu & Kalyon", "Barut teknolojisiyle yüksek menzilli kara ve deniz topçusu.")
                            UnlockedItemRow("💂 Tüfekli Yeniçeri & Süvari", "Ateşli silahlarla donatılmış piyade bölükleri.")
                            UnlockedItemRow("💥 General Gücü: Barut Bombardımanı", "Hedef alana yıkıcı gülle fırtınası.")
                        }
                        Era.INDUSTRIAL -> {
                            UnlockedItemRow("🏭 Seri Üretim Tank Fabrikası", "Buhar ve çelik ile zırhlı paletli tanklar üretilebilir.")
                            UnlockedItemRow("✈️ Çift Kanatlı Avcı & Dizel Denizaltı", "İlk hava muharebesi ve torpido taarruzları.")
                            UnlockedItemRow("⚙️ General Gücü: Endüstriyel Seferberlik", "Üretim sürelerinde %40 hızlanma.")
                        }
                        Era.MODERN -> {
                            UnlockedItemRow("🛡️ ${nation.uniqueTankName}", "Kompozit zırhlı ve aktif korumalı modern ana muharebe tankı.")
                            UnlockedItemRow("✈️ ${nation.uniqueJetName}", "5. Nesil radara yakalanmayan hayalet jet & SİHA.")
                            UnlockedItemRow("🌊 ${nation.uniqueSubmarineName}", "Havadan bağımsız tahrikli (AIP) nükleer/taarruz denizaltısı.")
                            UnlockedItemRow("📡 General Gücü: Taktik Hava / SİHA Akını", "Hedef alanda yüksek hassasiyetli lazer güdümlü mühimmat.")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Auto-dismiss Timer Bar
            Column(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = accentColor,
                    trackColor = SurfaceElevated
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("dismiss_era_overlay_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "SAVAŞ ALANINA GEÇ", color = SpaceBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun UnlockedItemRow(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "•", color = MatrixGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = TextSecondary, fontSize = 10.sp, lineHeight = 13.sp)
        }
    }
}

@Composable
private fun BorderStrokeWrapper(color: Color): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(1.dp, color)
}

// ==================== ATMOSPHERIC CANVAS DRAW FUNCTIONS ====================

/**
 * Medieval Fog: Rolling mist waves with glowing ember particles
 */
private fun DrawScope.drawMedievalFogFx(waveAnim: Float) {
    val w = size.width
    val h = size.height

    // 1. Drifting fog layers
    val fogColor1 = Color(0xFF334155).copy(alpha = 0.28f)
    val fogColor2 = Color(0xFF1E293B).copy(alpha = 0.35f)

    for (layer in 0..2) {
        val path = Path()
        val yBase = h * (0.3f + layer * 0.25f)
        val shift = (waveAnim * w * (1f + layer * 0.5f)) % w

        path.moveTo(-w + shift, yBase)
        var x = -w + shift
        while (x < w * 2 + shift) {
            val waveHeight = 25f + layer * 15f
            path.relativeQuadraticBezierTo(80f, -waveHeight, 160f, 0f)
            path.relativeQuadraticBezierTo(80f, waveHeight, 160f, 0f)
            x += 320f
        }
        path.lineTo(w * 2, h)
        path.lineTo(0f, h)
        path.close()

        drawPath(path = path, color = if (layer % 2 == 0) fogColor1 else fogColor2)
    }

    // 2. Torch ember sparks floating upwards
    for (i in 0 until 18) {
        val seed = i * 73
        val emberX = (seed % w.toInt()).toFloat()
        val emberSpeed = 0.4f + (seed % 10) * 0.06f
        val emberY = (h - ((waveAnim * emberSpeed * h * 1.5f + seed * 23) % h))
        val emberSize = 2.5f + (seed % 4)

        drawCircle(
            color = AmberGold.copy(alpha = 0.6f),
            radius = emberSize,
            center = Offset(emberX + sin(emberY * 0.05f) * 12f, emberY)
        )
    }
}

/**
 * Classical & Gunpowder Smoke: Billowing smoke plumes and sparks
 */
private fun DrawScope.drawGunpowderSmokeFx(waveAnim: Float) {
    val w = size.width
    val h = size.height

    // Smoke rings expanding
    val smokeColor = Color(0xFF27272A).copy(alpha = 0.38f)
    for (i in 0 until 6) {
        val radius = ((waveAnim + i * 0.16f) % 1f) * (w * 0.6f)
        val alpha = (1f - ((waveAnim + i * 0.16f) % 1f)).coerceIn(0f, 0.4f)
        drawCircle(
            color = smokeColor.copy(alpha = alpha),
            radius = radius,
            center = Offset(w * 0.5f, h * 0.45f),
            style = Stroke(width = 40f)
        )
    }

    // Fiery gunpowder sparks
    for (i in 0 until 24) {
        val angle = (i * 15f) * (Math.PI / 180.0)
        val dist = ((waveAnim * 1.8f + i * 0.08f) % 1f) * (w * 0.55f)
        val sx = (w * 0.5f + cos(angle) * dist).toFloat()
        val sy = (h * 0.45f + sin(angle) * dist).toFloat()

        drawCircle(
            color = if (i % 2 == 0) WarningOrange else AmberGold,
            radius = 3f,
            center = Offset(sx, sy)
        )
    }
}

/**
 * Industrial Steam & Gears: Rotating mechanical brass gears and steam clouds
 */
private fun DrawScope.drawIndustrialSteamAndGearsFx(rotationAnim: Float, waveAnim: Float) {
    val w = size.width
    val h = size.height

    // Draw Left Background Gear
    rotate(rotationAnim, pivot = Offset(60f, h * 0.3f)) {
        drawGear(center = Offset(60f, h * 0.3f), radius = 90f, teeth = 12, color = Color(0xFFB45309).copy(alpha = 0.35f))
    }

    // Draw Right Interlocking Gear (reverse rotation)
    rotate(-rotationAnim * 1.5f, pivot = Offset(w - 70f, h * 0.7f)) {
        drawGear(center = Offset(w - 70f, h * 0.7f), radius = 75f, teeth = 10, color = Color(0xFF78350F).copy(alpha = 0.4f))
    }

    // Steam puffs
    val steamColor = Color(0xFFE2E8F0).copy(alpha = 0.12f)
    for (s in 0 until 5) {
        val steamY = (h - ((waveAnim * h + s * 140f) % h))
        val steamX = (w * 0.2f + s * (w * 0.15f))
        drawCircle(
            color = steamColor,
            radius = 45f + s * 12f,
            center = Offset(steamX, steamY)
        )
    }
}

private fun DrawScope.drawGear(center: Offset, radius: Float, teeth: Int, color: Color) {
    drawCircle(color = color, radius = radius, center = center)
    drawCircle(color = SpaceBlack, radius = radius * 0.45f, center = center)

    val toothWidth = (2f * Math.PI * radius / teeth * 0.4f).toFloat()
    val toothHeight = radius * 0.22f

    for (t in 0 until teeth) {
        val angle = (t * (360f / teeth)) * (Math.PI / 180.0)
        val tx = (center.x + cos(angle) * (radius + toothHeight * 0.5f)).toFloat()
        val ty = (center.y + sin(angle) * (radius + toothHeight * 0.5f)).toFloat()
        drawRect(
            color = color,
            topLeft = Offset(tx - toothWidth / 2f, ty - toothHeight / 2f),
            size = Size(toothWidth, toothHeight)
        )
    }
}

/**
 * Modern Warfare Digital Glitch & Radar: Holographic grid, scanlines, and satellite sweep
 */
private fun DrawScope.drawModernGlitchAndRadarFx(rotationAnim: Float, waveAnim: Float, jitter: Float) {
    val w = size.width
    val h = size.height

    // 1. Digital Grid
    val gridStep = 45f
    var x = 0f
    while (x < w) {
        drawLine(
            color = CyanNeon.copy(alpha = 0.15f),
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 1f
        )
        x += gridStep
    }
    var y = 0f
    while (y < h) {
        drawLine(
            color = CyanNeon.copy(alpha = 0.15f),
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1f
        )
        y += gridStep
    }

    // 2. Green Satellite Radar Sweep Line
    val radarCenter = Offset(w * 0.5f, h * 0.45f)
    val radarRadius = w * 0.42f
    drawCircle(
        color = MatrixGreen.copy(alpha = 0.25f),
        radius = radarRadius,
        center = radarCenter,
        style = Stroke(width = 1.5f)
    )
    drawCircle(
        color = MatrixGreen.copy(alpha = 0.15f),
        radius = radarRadius * 0.5f,
        center = radarCenter,
        style = Stroke(width = 1f)
    )

    val sweepAngle = (rotationAnim * 1.5f) * (Math.PI / 180.0)
    val sweepEnd = Offset(
        (radarCenter.x + cos(sweepAngle) * radarRadius).toFloat(),
        (radarCenter.y + sin(sweepAngle) * radarRadius).toFloat()
    )
    drawLine(
        color = MatrixGreen.copy(alpha = 0.7f),
        start = radarCenter,
        end = sweepEnd,
        strokeWidth = 2.5f
    )

    // 3. Scanline sweep
    val scanY = waveAnim * h
    drawLine(
        color = CyanGlow.copy(alpha = 0.6f),
        start = Offset(0f, scanY),
        end = Offset(w, scanY),
        strokeWidth = 2f
    )

    // 4. Glitch Chromatic Aberration Lines
    if (jitter > 1f) {
        drawRect(
            color = LaserCrimson.copy(alpha = 0.18f),
            topLeft = Offset(0f, h * 0.25f + jitter * 8f),
            size = Size(w, 18f)
        )
        drawRect(
            color = CyanNeon.copy(alpha = 0.18f),
            topLeft = Offset(0f, h * 0.65f - jitter * 6f),
            size = Size(w, 12f)
        )
    }
}
