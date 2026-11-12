package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ScatterPlot
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RtsUnit
import com.example.model.TacticalControlGroup
import com.example.model.TacticalGroupCommand
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

/**
 * Control Panel UI Overlay:
 * - Allows RTS players to create and save tactical unit groups (G1 to G5).
 * - Quick selection of assigned groups and group command execution (Attack-Move, Hold, Retreat, Scatter).
 * - Real-time composition display and health progress bars.
 * - Collapsible dock placed seamlessly at the bottom center of the landscape battlefield.
 */
@Composable
fun TacticalControlGroupsOverlay(
    groups: List<TacticalControlGroup>,
    units: List<RtsUnit>,
    selectedUnitIds: Set<String>,
    onSelectGroup: (Int) -> Unit,
    onAssignSelectedToGroup: (Int) -> Unit,
    onClearGroup: (Int) -> Unit,
    onExecuteGroupCommand: (Int, TacticalGroupCommand) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var activeGroupCommandsId by remember { mutableStateOf<Int?>(null) }

    Surface(
        color = SurfaceDark.copy(alpha = 0.94f),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(CyanNeon.copy(alpha = 0.4f), AmberGold.copy(alpha = 0.4f))
            )
        ),
        shadowElevation = 10.dp,
        modifier = modifier
            .testTag("tactical_control_groups_overlay")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Title & Collapse/Expand Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎖️ TAKTİK BİRLİK GRUPLARI",
                        color = CyanNeon,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (selectedUnitIds.isNotEmpty()) {
                        Surface(
                            color = AmberGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(AmberGold, AmberGold))
                            )
                        ) {
                            Text(
                                text = "${selectedUnitIds.size} Birlik Seçili",
                                color = AmberGold,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isExpanded) "Gizle" else "Genişlet",
                        color = TextSecondary,
                        fontSize = 8.sp
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = "Genişlet/Daralt",
                        tint = CyanNeon,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))

                    // Group Slots (G1 to G5)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        groups.forEach { group ->
                            val aliveUnits = group.getAliveUnits(units)
                            val isAssigned = aliveUnits.isNotEmpty()
                            val avgHp = group.getAverageHpPercent(units)
                            val isCurrentlySelected = isAssigned && aliveUnits.all { selectedUnitIds.contains(it.id) } && selectedUnitIds.isNotEmpty()

                            ControlGroupSlotCard(
                                group = group,
                                aliveUnitsCount = aliveUnits.size,
                                avgHp = avgHp,
                                compositionSummary = group.getCompositionSummary(units),
                                isAssigned = isAssigned,
                                isSelected = isCurrentlySelected,
                                hasSelectedUnitsToAssign = selectedUnitIds.isNotEmpty(),
                                onSelect = {
                                    if (isAssigned) {
                                        onSelectGroup(group.id)
                                        activeGroupCommandsId = group.id
                                    } else if (selectedUnitIds.isNotEmpty()) {
                                        onAssignSelectedToGroup(group.id)
                                        activeGroupCommandsId = group.id
                                    }
                                },
                                onAssign = {
                                    onAssignSelectedToGroup(group.id)
                                    activeGroupCommandsId = group.id
                                },
                                onClear = {
                                    onClearGroup(group.id)
                                    if (activeGroupCommandsId == group.id) activeGroupCommandsId = null
                                }
                            )
                        }
                    }

                    // Tactical Command Bar for Active Group
                    val currentActiveGroup = groups.find { it.id == activeGroupCommandsId }
                    if (currentActiveGroup != null && currentActiveGroup.getAliveUnits(units).isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceElevated)
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currentActiveGroup.iconEmoji} ${currentActiveGroup.name} Komutları:",
                                color = TextPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                GroupCommandButton(
                                    command = TacticalGroupCommand.ATTACK_MOVE,
                                    accentColor = LaserCrimson,
                                    testTag = "group_cmd_attack",
                                    onClick = { onExecuteGroupCommand(currentActiveGroup.id, TacticalGroupCommand.ATTACK_MOVE) }
                                )
                                GroupCommandButton(
                                    command = TacticalGroupCommand.HOLD_POSITION,
                                    accentColor = AmberGold,
                                    testTag = "group_cmd_hold",
                                    onClick = { onExecuteGroupCommand(currentActiveGroup.id, TacticalGroupCommand.HOLD_POSITION) }
                                )
                                GroupCommandButton(
                                    command = TacticalGroupCommand.RETREAT_TO_HQ,
                                    accentColor = Color(0xFF38BDF8),
                                    testTag = "group_cmd_retreat",
                                    onClick = { onExecuteGroupCommand(currentActiveGroup.id, TacticalGroupCommand.RETREAT_TO_HQ) }
                                )
                                GroupCommandButton(
                                    command = TacticalGroupCommand.SCATTER,
                                    accentColor = Color(0xFFA78BFA),
                                    testTag = "group_cmd_scatter",
                                    onClick = { onExecuteGroupCommand(currentActiveGroup.id, TacticalGroupCommand.SCATTER) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlGroupSlotCard(
    group: TacticalControlGroup,
    aliveUnitsCount: Int,
    avgHp: Float,
    compositionSummary: String,
    isAssigned: Boolean,
    isSelected: Boolean,
    hasSelectedUnitsToAssign: Boolean,
    onSelect: () -> Unit,
    onAssign: () -> Unit,
    onClear: () -> Unit
) {
    Surface(
        color = if (isSelected) SurfaceElevated else SurfaceDark,
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isSelected) listOf(CyanNeon, AmberGold)
                else if (isAssigned) listOf(Color(group.accentColorHex).copy(alpha = 0.6f), SurfaceBorder)
                else listOf(SurfaceBorder, SurfaceBorder)
            )
        ),
        modifier = Modifier
            .width(88.dp)
            .clickable { onSelect() }
            .testTag("control_group_${group.id}")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Group Header: G# badge and Clear or Add action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "G${group.id}",
                        color = if (isSelected) CyanNeon else Color(group.accentColorHex),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = group.iconEmoji,
                        fontSize = 9.sp
                    )
                }

                if (isAssigned) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .clickable { onClear() }
                            .testTag("control_group_clear_${group.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Grubu Boşalt",
                            tint = TextMuted,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                } else if (hasSelectedUnitsToAssign) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(AmberGold.copy(alpha = 0.2f))
                            .clickable { onAssign() }
                            .testTag("control_group_assign_${group.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Gruba Ata",
                            tint = AmberGold,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Unit count or Assign prompt
            if (isAssigned) {
                Text(
                    text = "$aliveUnitsCount Birlik",
                    color = if (isSelected) AmberGold else TextPrimary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = compositionSummary.take(12),
                    color = TextSecondary,
                    fontSize = 7.sp,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Average Health gauge
                LinearProgressIndicator(
                    progress = { avgHp },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp)),
                    color = if (avgHp > 0.5f) MatrixGreen else if (avgHp > 0.25f) AmberGold else DangerRed,
                    trackColor = SpaceBlack
                )
            } else {
                Text(
                    text = if (hasSelectedUnitsToAssign) "+ Ata" else "Boş",
                    color = if (hasSelectedUnitsToAssign) AmberGold else TextMuted,
                    fontSize = 8.sp,
                    fontWeight = if (hasSelectedUnitsToAssign) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
private fun GroupCommandButton(
    command: TacticalGroupCommand,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(4.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.5f), SurfaceBorder))
        ),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = command.iconEmoji, fontSize = 8.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = command.title,
                color = TextPrimary,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
