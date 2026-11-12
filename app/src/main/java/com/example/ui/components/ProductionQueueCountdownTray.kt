package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProductionOrder
import com.example.model.RtsBuilding
import com.example.model.RtsUnitTemplate
import com.example.model.UnitType
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
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
import com.example.ui.theme.WarningOrange

/**
 * C&C Generals / Rise of Kingdoms style Production Queue Countdown Tray:
 * - Real-time sub-second numerical countdown timers (e.g. 04.2s, 01.5s).
 * - Smooth glowing linear progress indicators for the active unit in slot 0.
 * - Queue slot indicators with unit type thumbnails.
 * - Quick order cancel buttons (✕) for every queue slot.
 * - Empty slot visual placeholders.
 * - Fast production queue shortcuts filtered by active building type.
 */
@Composable
fun ProductionQueueCountdownTray(
    building: RtsBuilding?,
    availableTemplates: List<RtsUnitTemplate>,
    currentGold: Double,
    currentOil: Double,
    onCancelOrder: (String, Int) -> Unit,
    onEnqueueUnit: (String, RtsUnitTemplate) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "queuePulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        color = SurfaceDark.copy(alpha = 0.95f),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.5f), CyanNeon.copy(alpha = 0.3f)))
        ),
        shadowElevation = 8.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header: Building Name & Queue Capacity Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = getQueueBuildingIcon(building),
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = building?.type?.title ?: "ÜRETİM FABRİKASI",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                val queueSize = building?.queue?.size ?: 0
                val maxCap = building?.maxQueueCapacity ?: 5
                Surface(
                    color = if (queueSize >= maxCap) DangerRed.copy(alpha = 0.25f) else SurfaceElevated,
                    shape = RoundedCornerShape(4.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            if (queueSize > 0) listOf(MatrixGreen, CyanNeon) else listOf(SurfaceBorder, SurfaceBorder)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = if (queueSize > 0) MatrixGreen else TextMuted,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$queueSize/$maxCap Sırada",
                            color = if (queueSize >= maxCap) LaserCrimson else if (queueSize > 0) MatrixGreen else TextMuted,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5 Queue Slots with Real-time Countdown & Cancel Buttons
            val queue = building?.queue ?: emptyList()
            val maxSlots = building?.maxQueueCapacity ?: 5

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (slotIndex in 0 until maxSlots) {
                    val order = queue.getOrNull(slotIndex)
                    QueueSlotCard(
                        slotIndex = slotIndex,
                        order = order,
                        pulseAlpha = if (slotIndex == 0) pulseAlpha else 1f,
                        onCancel = {
                            if (building != null && order != null) {
                                onCancelOrder(building.id, slotIndex)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Unit Production Action Bar
            if (building != null && building.type.allowedUnitTypes.isNotEmpty()) {
                val filterUnits = availableTemplates.filter { template ->
                    building.type.allowedUnitTypes.contains(template.unitType)
                }

                if (filterUnits.isNotEmpty()) {
                    Text(
                        text = "ÜRETİLEBİLİR ASKERİ BİRLİKLER",
                        color = TextSecondary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(filterUnits) { template ->
                            val canAffordGold = currentGold >= template.costGold
                            val isQueueFull = (building.queue.size >= building.maxQueueCapacity)
                            val canProduce = canAffordGold && !isQueueFull

                            Button(
                                onClick = { onEnqueueUnit(building.id, template) },
                                enabled = canProduce,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (canProduce) SurfaceElevated else SurfaceDark.copy(alpha = 0.5f),
                                    disabledContainerColor = SurfaceDark.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = if (canProduce) AmberGold.copy(alpha = 0.7f) else SurfaceBorder,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .testTag("enqueue_unit_${template.id}")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = getUnitVectorIcon(template.unitType),
                                        contentDescription = null,
                                        tint = if (canProduce) AmberGold else TextMuted,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Column {
                                        Text(
                                            text = template.name.take(14),
                                            color = if (canProduce) TextPrimary else TextMuted,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${template.costGold}G • ${template.productionTimeSeconds}s",
                                            color = if (canAffordGold) MatrixGreen else DangerRed,
                                            fontSize = 7.sp
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

@Composable
private fun QueueSlotCard(
    slotIndex: Int,
    order: ProductionOrder?,
    pulseAlpha: Float,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSlotActive = (slotIndex == 0 && order != null)

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (order != null) SurfaceElevated else SurfaceDark.copy(alpha = 0.45f))
            .border(
                width = if (isSlotActive) 1.5.dp else 1.dp,
                color = when {
                    isSlotActive -> AmberGold.copy(alpha = pulseAlpha)
                    order != null -> CyanNeon.copy(alpha = 0.5f)
                    else -> SurfaceBorder
                },
                shape = RoundedCornerShape(6.dp)
            )
            .padding(3.dp)
    ) {
        if (order != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top: Slot number and Cancel Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${slotIndex + 1}",
                        color = if (isSlotActive) AmberGold else TextMuted,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(DangerRed.copy(alpha = 0.2f))
                            .clickable { onCancel() }
                            .testTag("cancel_queue_slot_$slotIndex"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "İptal",
                            tint = DangerRed,
                            modifier = Modifier.size(9.dp)
                        )
                    }
                }

                // Middle: Unit Icon & Short Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = getUnitVectorIcon(order.unitTemplate.unitType),
                        contentDescription = null,
                        tint = if (isSlotActive) AmberLight else CyanNeon,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = order.unitTemplate.name.split(" ").first().take(6),
                        color = TextPrimary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                // Bottom: Real-Time Numeric Countdown or Waiting Status
                if (isSlotActive) {
                    val progress = (1f - (order.timeRemaining / order.totalTime)).coerceIn(0f, 1f)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .clip(RoundedCornerShape(1.5.dp)),
                            color = AmberGold,
                            trackColor = SpaceBlack
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = MatrixGreen,
                                modifier = Modifier.size(7.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "%.1fs".format(order.timeRemaining),
                                color = MatrixGreen,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Sırada (%d sn)".format(order.totalTime.toInt()),
                        color = AmberLight,
                        fontSize = 6.sp,
                        maxLines = 1
                    )
                }
            }
        } else {
            // Empty Slot Visual Placeholder
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "#${slotIndex + 1}",
                    color = TextMuted.copy(alpha = 0.4f),
                    fontSize = 7.sp
                )
                Text(
                    text = "Boş",
                    color = TextMuted,
                    fontSize = 7.sp
                )
            }
        }
    }
}

private fun getQueueBuildingIcon(building: RtsBuilding?): ImageVector {
    if (building == null) return Icons.Default.PrecisionManufacturing
    return when (building.type) {
        com.example.model.RtsBuildingType.HQ -> Icons.Default.PrecisionManufacturing
        com.example.model.RtsBuildingType.POWER_PLANT -> Icons.Default.PrecisionManufacturing
        com.example.model.RtsBuildingType.SUPPLY_DEPOT -> Icons.Default.PrecisionManufacturing
        com.example.model.RtsBuildingType.BARRACKS -> Icons.Default.Person
        com.example.model.RtsBuildingType.FACTORY -> Icons.Default.CarRepair
        com.example.model.RtsBuildingType.AIRFIELD -> Icons.Default.AirplanemodeActive
        com.example.model.RtsBuildingType.SHIPYARD -> Icons.Default.DirectionsBoat
    }
}

private fun getUnitVectorIcon(type: UnitType): ImageVector {
    return when (type) {
        UnitType.INFANTRY -> Icons.Default.Person
        UnitType.TANK -> Icons.Default.CarRepair
        UnitType.JET -> Icons.Default.AirplanemodeActive
        UnitType.SUBMARINE -> Icons.Default.DirectionsBoat
        UnitType.SIEGE -> Icons.Default.PrecisionManufacturing
    }
}
