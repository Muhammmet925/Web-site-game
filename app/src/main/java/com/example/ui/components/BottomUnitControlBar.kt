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
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.RtsUnit
import com.example.model.UnitStance
import com.example.model.UnitTacticalCommand
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

/**
 * Bottom-anchored tactical control bar that dynamically displays available commands
 * (Move, Attack, Guard, Ability/Stance) based on the currently selected units in the RTS game.
 */
@Composable
fun BottomUnitControlBar(
    selectedUnits: List<RtsUnit>,
    onExecuteCommand: (UnitTacticalCommand) -> Unit,
    onMoveOrder: () -> Unit = {},
    onAttackOrder: () -> Unit = {},
    onDeselectAll: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (selectedUnits.isEmpty()) return

    val unitCount = selectedUnits.size
    val firstUnit = selectedUnits.first()
    val isSingle = unitCount == 1

    // Determine available commands based on unit types present in the selection
    val selectedTypes = selectedUnits.map { it.unitType }.toSet()
    val hasVehicles = selectedTypes.any { it == UnitType.TANK || it == UnitType.SIEGE || it == UnitType.SUBMARINE }
    val hasStealthOrInfantry = selectedTypes.any { it == UnitType.INFANTRY || it == UnitType.SUBMARINE || it == UnitType.JET }

    Surface(
        color = SurfaceDark.copy(alpha = 0.94f),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(CyanNeon.copy(alpha = 0.6f), AmberGold.copy(alpha = 0.6f))
            )
        ),
        shadowElevation = 10.dp,
        modifier = modifier
            .testTag("bottom_unit_control_bar")
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Left Unit Identity / Multi-selection counter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (isSingle) firstUnit.activeStance.badge else "🎖️",
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = if (isSingle) firstUnit.name.take(13) else "$unitCount Birlik",
                        color = CyanNeon,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isSingle) "HP: ${firstUnit.hp.toInt()}/${firstUnit.maxHp.toInt()}" else "Karma Müfreze",
                        color = MatrixGreen,
                        fontSize = 6.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(modifier = Modifier.size(1.dp, 20.dp).background(SurfaceBorder))

            // 1. MOVE COMMAND (Hareket Et)
            CommandActionButton(
                icon = Icons.Default.NearMe,
                label = "İlerle",
                accentColor = MatrixGreen,
                onClick = onMoveOrder,
                testTag = "cmd_move_btn"
            )

            // 2. ATTACK / ATTACK-MOVE COMMAND (Saldır)
            CommandActionButton(
                icon = Icons.Default.FlashOn,
                label = "Taarruz",
                accentColor = LaserCrimson,
                onClick = onAttackOrder,
                testTag = "cmd_attack_btn"
            )

            // 3. GUARD COMMAND (Muhafız / Savun)
            CommandActionButton(
                icon = Icons.Default.Shield,
                label = "Koru",
                accentColor = AmberGold,
                onClick = { onExecuteCommand(UnitTacticalCommand.GUARD) },
                testTag = "cmd_guard_btn"
            )

            // 4. PATROL COMMAND (Devriye)
            CommandActionButton(
                icon = Icons.Default.Speed,
                label = "Devriye",
                accentColor = Color(0xFF38BDF8),
                onClick = { onExecuteCommand(UnitTacticalCommand.PATROL) },
                testTag = "cmd_patrol_btn"
            )

            // 5. ABILITY / STANCE COMMAND (Repair if vehicles present, else Hold Fire / Pusu)
            if (hasVehicles) {
                CommandActionButton(
                    icon = Icons.Default.Security,
                    label = "Onarım",
                    accentColor = Color(0xFFA78BFA),
                    onClick = { onExecuteCommand(UnitTacticalCommand.REPAIR) },
                    testTag = "cmd_repair_btn"
                )
            } else if (hasStealthOrInfantry) {
                CommandActionButton(
                    icon = Icons.Default.Security,
                    label = "Ateş Kes",
                    accentColor = Color(0xFF94A3B8),
                    onClick = { onExecuteCommand(UnitTacticalCommand.HOLD_FIRE) },
                    testTag = "cmd_hold_fire_btn"
                )
            }

            // Deselect / Close button
            Surface(
                onClick = onDeselectAll,
                shape = CircleShape,
                color = SurfaceElevated,
                modifier = Modifier.size(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Seçimi Kaldır",
                        tint = TextMuted,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommandActionButton(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        color = SurfaceElevated,
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(accentColor.copy(alpha = 0.5f), SurfaceBorder)
            )
        ),
        modifier = Modifier.testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
