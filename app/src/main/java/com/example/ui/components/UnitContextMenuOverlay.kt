package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.UnitStance
import com.example.model.UnitTacticalCommand
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
 * Interactive Floating Context Menu:
 * - Appears when clicking an individual unit on the RTS battlefield.
 * - Allows specific commands: 'Patrol', 'Guard', 'Repair', 'Hold Fire' dynamically filtered by UnitType.
 * - Displays active stance badges, current HP progress, and unit rank.
 */
@Composable
fun UnitContextMenuOverlay(
    unit: RtsUnit,
    onExecuteCommand: (UnitTacticalCommand) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hpRatio = (unit.hp / unit.maxHp).coerceIn(0f, 1f)
    val availableCommands = UnitTacticalCommand.values().filter { it.isAvailableFor(unit.unitType) }

    Surface(
        color = SurfaceDark.copy(alpha = 0.94f),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(CyanNeon.copy(alpha = 0.65f), AmberGold.copy(alpha = 0.65f))
            )
        ),
        shadowElevation = 12.dp,
        modifier = modifier
            .width(260.dp)
            .testTag("unit_floating_context_menu")
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            // Header: Unit Name, Stance Badge & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = unit.name,
                        color = CyanNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Stance Badge
                    Surface(
                        color = when (unit.activeStance) {
                            UnitStance.PATROL -> Color(0xFF0284C7).copy(alpha = 0.3f)
                            UnitStance.GUARD -> AmberGold.copy(alpha = 0.3f)
                            UnitStance.HOLD_FIRE -> Color(0xFFA855F7).copy(alpha = 0.3f)
                            UnitStance.REPAIRING -> MatrixGreen.copy(alpha = 0.3f)
                            else -> LaserCrimson.copy(alpha = 0.3f)
                        },
                        shape = RoundedCornerShape(4.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(AmberGold.copy(alpha = 0.5f), SurfaceBorder)
                            )
                        )
                    ) {
                        Text(
                            text = "${unit.activeStance.badge} ${unit.activeStance.title}",
                            color = TextPrimary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .clickable { onDismiss() }
                            .testTag("dismiss_unit_context_menu_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kapat",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Unit Quick Stats: HP & Rank
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HP: ${unit.hp.toInt()}/${unit.maxHp.toInt()}",
                    color = if (hpRatio > 0.4f) TextSecondary else DangerRed,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = unit.rankBadge, color = AmberGold, fontSize = 9.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = unit.rank.title, color = AmberGold, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            LinearProgressIndicator(
                progress = { hpRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp)),
                color = if (hpRatio > 0.5f) MatrixGreen else if (hpRatio > 0.25f) AmberGold else DangerRed,
                trackColor = SpaceBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⚡ TAKTİK BİRİM KOMUTLARI:",
                color = TextMuted,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Command Grid: 2 columns
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                availableCommands.chunked(2).forEach { rowCommands ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowCommands.forEach { cmd ->
                            val isCurrentStance = when (cmd) {
                                UnitTacticalCommand.PATROL -> unit.activeStance == UnitStance.PATROL
                                UnitTacticalCommand.GUARD -> unit.activeStance == UnitStance.GUARD
                                UnitTacticalCommand.HOLD_FIRE -> unit.isHoldingFire
                                UnitTacticalCommand.REPAIR -> unit.isRepairing
                            }

                            UnitCommandActionChip(
                                command = cmd,
                                isActive = isCurrentStance,
                                onClick = { onExecuteCommand(cmd) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowCommands.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UnitCommandActionChip(
    command: UnitTacticalCommand,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = when (command) {
        UnitTacticalCommand.PATROL -> Color(0xFF0284C7) // Sky
        UnitTacticalCommand.GUARD -> AmberGold // Amber
        UnitTacticalCommand.REPAIR -> MatrixGreen // Green
        UnitTacticalCommand.HOLD_FIRE -> Color(0xFFA855F7) // Purple
    }

    Surface(
        color = if (isActive) accentColor.copy(alpha = 0.25f) else SurfaceElevated,
        shape = RoundedCornerShape(6.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isActive) listOf(accentColor, CyanNeon)
                else listOf(SurfaceBorder, SurfaceBorder)
            )
        ),
        modifier = modifier
            .height(34.dp)
            .clickable { onClick() }
            .testTag("unit_cmd_${command.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isActive) accentColor else SurfaceDark),
                contentAlignment = Alignment.Center
            ) {
                Text(text = command.iconEmoji, fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.width(5.dp))

            Column {
                Text(
                    text = command.title,
                    color = if (isActive) CyanNeon else TextPrimary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = if (isActive) "Aktif" else "Emir Ver",
                    color = if (isActive) MatrixGreen else TextMuted,
                    fontSize = 7.sp
                )
            }
        }
    }
}
