package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GeneralsPower
import com.example.model.RtsUnit
import com.example.model.UnitType
import com.example.simulation.RtsUiState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CrystalPurple
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

@Composable
fun TacticalBattlefieldScreen(
    state: RtsUiState,
    onSelectInRect: (Float, Float, Float, Float) -> Unit,
    onMoveOrder: (Float, Float) -> Unit,
    onTriggerPower: (String) -> Unit,
    onStressTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Drag selection box coordinates
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragCurrent by remember { mutableStateOf<Offset?>(null) }
    var lastWaypoint by remember { mutableStateOf<Offset?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Generals Powers Bar (Interactive Tactical Strikes)
        GeneralsPowersBar(
            powers = state.generalsPowers,
            gold = state.resources.gold,
            onTrigger = onTriggerPower
        )

        // Interactive 2D RTS Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(14.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.radialGradient(listOf(SurfaceElevated, SurfaceBorder))
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(state.selectedUnitIds) {
                            detectTapGestures { tapOffset ->
                                if (state.selectedUnitIds.isNotEmpty()) {
                                    // Move command (Right-click equivalent)
                                    lastWaypoint = tapOffset
                                    onMoveOrder(tapOffset.x, tapOffset.y)
                                } else {
                                    // Tap single unit
                                    val clickedUnit = state.units.minByOrNull { u ->
                                        Math.hypot((tapOffset.x - u.posX).toDouble(), (tapOffset.y - u.posY).toDouble())
                                    }
                                    if (clickedUnit != null) {
                                        val dist = Math.hypot((tapOffset.x - clickedUnit.posX).toDouble(), (tapOffset.y - clickedUnit.posY).toDouble()).toFloat()
                                        if (dist < 40f) {
                                            onSelectInRect(clickedUnit.posX - 10f, clickedUnit.posY - 10f, clickedUnit.posX + 10f, clickedUnit.posY + 10f)
                                        }
                                    }
                                }
                            }
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    dragStart = offset
                                    dragCurrent = offset
                                },
                                onDrag = { change, _ ->
                                    dragCurrent = change.position
                                },
                                onDragEnd = {
                                    if (dragStart != null && dragCurrent != null) {
                                        val minX = Math.min(dragStart!!.x, dragCurrent!!.x)
                                        val maxX = Math.max(dragStart!!.x, dragCurrent!!.x)
                                        val minY = Math.min(dragStart!!.y, dragCurrent!!.y)
                                        val maxY = Math.max(dragStart!!.y, dragCurrent!!.y)

                                        if (maxX - minX > 15f || maxY - minY > 15f) {
                                            onSelectInRect(minX, minY, maxX, maxY)
                                        }
                                    }
                                    dragStart = null
                                    dragCurrent = null
                                },
                                onDragCancel = {
                                    dragStart = null
                                    dragCurrent = null
                                }
                            )
                        }
                        .testTag("rts_tactical_canvas")
                ) {
                    val canvasW = size.width
                    val canvasH = size.height

                    // 1. Draw Tactical Grid Background
                    val step = 40.dp.toPx()
                    var gx = 0f
                    while (gx < canvasW) {
                        drawLine(
                            color = SurfaceBorder.copy(alpha = 0.25f),
                            start = Offset(gx, 0f),
                            end = Offset(gx, canvasH),
                            strokeWidth = 1f
                        )
                        gx += step
                    }
                    var gy = 0f
                    while (gy < canvasH) {
                        drawLine(
                            color = SurfaceBorder.copy(alpha = 0.25f),
                            start = Offset(0f, gy),
                            end = Offset(canvasW, gy),
                            strokeWidth = 1f
                        )
                        gy += step
                    }

                    // 2. Draw Move Waypoint Ping
                    lastWaypoint?.let { wp ->
                        drawCircle(
                            color = MatrixGreen.copy(alpha = 0.4f),
                            radius = 16.dp.toPx(),
                            center = wp,
                            style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
                        )
                        drawCircle(
                            color = MatrixGreen,
                            radius = 4.dp.toPx(),
                            center = wp
                        )
                    }

                    // 3. Draw Generals Power Strike Effect
                    state.lastStrikeEffect?.let { effect ->
                        drawCircle(
                            color = Color(effect.colorHex).copy(alpha = 0.35f),
                            radius = 45.dp.toPx(),
                            center = Offset(effect.x, effect.y)
                        )
                        drawCircle(
                            color = Color(effect.colorHex),
                            radius = 45.dp.toPx(),
                            center = Offset(effect.x, effect.y),
                            style = Stroke(width = 3f)
                        )
                    }

                    // 4. Draw Units (Tanks, Jets, Submarines)
                    state.units.forEach { unit ->
                        val unitCenter = Offset(unit.posX, unit.posY)
                        val isSelected = state.selectedUnitIds.contains(unit.id)
                        val radius = if (unit.unitType == UnitType.JET) 14.dp.toPx() else 11.dp.toPx()

                        // Selection Halo
                        if (isSelected) {
                            drawCircle(
                                color = MatrixGreen,
                                radius = radius + 6.dp.toPx(),
                                center = unitCenter,
                                style = Stroke(width = 2f)
                            )
                        }

                        // Unit Body Color
                        val unitColor = if (!unit.isPlayerSide) LaserCrimson
                        else when (unit.unitType) {
                            UnitType.TANK -> AmberGold
                            UnitType.JET -> CyanNeon
                            UnitType.SUBMARINE -> Color(0xFF38BDF8)
                            UnitType.INFANTRY -> MatrixGreen
                            UnitType.SIEGE -> WarningOrange
                        }

                        // Draw Unit Silhouette
                        drawCircle(
                            color = unitColor,
                            radius = radius,
                            center = unitCenter
                        )
                        drawCircle(
                            color = SurfaceDark,
                            radius = radius - 3.dp.toPx(),
                            center = unitCenter
                        )

                        // Jet swept-wing indicator
                        if (unit.unitType == UnitType.JET) {
                            drawLine(
                                color = unitColor,
                                start = Offset(unit.posX - 10.dp.toPx(), unit.posY),
                                end = Offset(unit.posX + 10.dp.toPx(), unit.posY),
                                strokeWidth = 2.5f
                            )
                        }

                        // Mini HP Bar
                        val hpRatio = (unit.hp / unit.maxHp).coerceIn(0f, 1f)
                        val barW = 22.dp.toPx()
                        val barH = 3.dp.toPx()
                        val barTop = unit.posY - radius - 7.dp.toPx()
                        drawRect(
                            color = SpaceBlack,
                            topLeft = Offset(unit.posX - barW / 2f, barTop),
                            size = Size(barW, barH)
                        )
                        drawRect(
                            color = if (hpRatio > 0.4f) MatrixGreen else DangerRed,
                            topLeft = Offset(unit.posX - barW / 2f, barTop),
                            size = Size(barW * hpRatio, barH)
                        )
                    }

                    // 5. Draw Drag-to-Select Selection Box (Kare İçine Alarak Seçim)
                    if (dragStart != null && dragCurrent != null) {
                        val minX = Math.min(dragStart!!.x, dragCurrent!!.x)
                        val maxX = Math.max(dragStart!!.x, dragCurrent!!.x)
                        val minY = Math.min(dragStart!!.y, dragCurrent!!.y)
                        val maxY = Math.max(dragStart!!.y, dragCurrent!!.y)
                        val w = maxX - minX
                        val h = maxY - minY

                        drawRect(
                            color = MatrixGreen.copy(alpha = 0.15f),
                            topLeft = Offset(minX, minY),
                            size = Size(w, h)
                        )
                        drawRect(
                            color = MatrixGreen,
                            topLeft = Offset(minX, minY),
                            size = Size(w, h),
                            style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
                        )
                    }
                }

                // Control Overlay Tips
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceDark.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (state.selectedUnitIds.isNotEmpty())
                            "✅ ${state.selectedUnitIds.size} Birlik Seçildi. Haritaya dokunarak intikal emri verin."
                        else
                            "👆 Çoklu seçim için parmağınızı sürükleyin (Drag-to-Select).",
                        color = if (state.selectedUnitIds.isNotEmpty()) MatrixGreen else CyanNeon,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Unit count counter
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceDark.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${state.units.size} Aktif Birlik",
                        color = AmberGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Stress Test & Optimization Trigger Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated)))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "MOBİL RTS OPTİMİZASYON DENEYİ",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "100+ tank ve jet ile Spatial Partitioning & Object Pooling testi",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    Button(
                        onClick = onStressTest,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("stress_test_button")
                    ) {
                        Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "100 Birim Spawn", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Real-time Tactical Logs
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated)))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "CANLI SAVAŞ & TELEMETRİ GÜNLÜĞÜ",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(state.tacticalLogs) { log ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (log.isAlert) LaserCrimson.copy(alpha = 0.15f) else SurfaceElevated.copy(alpha = 0.4f))
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = log.time, color = TextMuted, fontSize = 8.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = log.text,
                                color = if (log.isAlert) LaserCrimson else TextSecondary,
                                fontSize = 9.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeneralsPowersBar(
    powers: List<GeneralsPower>,
    gold: Double,
    onTrigger: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(LaserCrimson.copy(alpha = 0.5f), AmberGold.copy(alpha = 0.4f))))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = LaserCrimson, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "GENERAL GÜÇLERİ (GENERALS POWERS)", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = "Çağa göre evrilir", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(powers) { power ->
                    val isReady = power.isReady
                    val canAfford = gold >= power.costGold

                    Surface(
                        color = if (isReady && canAfford) SurfaceElevated else SurfaceDark.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                if (isReady) listOf(LaserCrimson.copy(alpha = 0.7f), AmberGold.copy(alpha = 0.4f))
                                else listOf(SurfaceBorder, SurfaceBorder)
                            )
                        ),
                        modifier = Modifier
                            .width(170.dp)
                            .clickable(enabled = isReady && canAfford) { onTrigger(power.id) }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = power.name,
                                    color = if (isReady) TextPrimary else TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                if (!isReady) {
                                    Text(text = "${power.currentCooldownSeconds}s", color = DangerRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                } else {
                                    Text(text = "HAZIR", color = MatrixGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = power.description,
                                color = TextSecondary,
                                fontSize = 8.sp,
                                maxLines = 2,
                                lineHeight = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Maliyet: ${power.costGold}A", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "Bekleme: ${power.cooldownSeconds}s", color = CyanNeon, fontSize = 8.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
