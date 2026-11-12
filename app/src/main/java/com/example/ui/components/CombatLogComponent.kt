package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.model.CombatEngagementLog
import com.example.model.CombatLogType
import com.example.model.SessionCombatStats
import com.example.ui.theme.AmberGold
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

enum class CombatFilterTab(val title: String, val icon: String) {
    ALL("Tümü", "📋"),
    DAMAGE_DEALT("Verilen Hasar", "💥"),
    DAMAGE_TAKEN("Alınan Hasar", "⚠️"),
    CASUALTIES("Zayiat & İmha", "☠️"),
    CRITICAL("Kritik Vuruşlar", "⚡")
}

/**
 * Scrollable Combat Log UI Component:
 * - Displays real-time unit engagement outcomes.
 * - Tracks damage dealt, damage taken, and casualties for the current session.
 * - Live K/D ratio, casualty counters, and engagement audit log.
 */
@Composable
fun CombatLogComponent(
    logs: List<CombatEngagementLog>,
    stats: SessionCombatStats,
    onClearLogs: () -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(CombatFilterTab.ALL) }

    val filteredLogs = remember(logs, selectedTab) {
        when (selectedTab) {
            CombatFilterTab.ALL -> logs
            CombatFilterTab.DAMAGE_DEALT -> logs.filter { it.type == CombatLogType.DAMAGE_DEALT }
            CombatFilterTab.DAMAGE_TAKEN -> logs.filter { it.type == CombatLogType.DAMAGE_TAKEN }
            CombatFilterTab.CASUALTIES -> logs.filter { it.isCasualty || it.type == CombatLogType.ENEMY_KILLED || it.type == CombatLogType.FRIENDLY_LOST }
            CombatFilterTab.CRITICAL -> logs.filter { it.type == CombatLogType.CRITICAL_STRIKE }
        }
    }

    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(LaserCrimson.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚔️", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "GERÇEK ZAMANLI MUHAREBE & ÇATIŞMA KAYITLARI",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = LaserCrimson.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "CANLI SEANS",
                                    color = DangerRed,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Birim angajmanları, verilen hasarlar ve anlık muharebe zayiatları.",
                            color = TextSecondary,
                            fontSize = 8.sp
                        )
                    }
                }

                // Action buttons: Clear & Close
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onClearLogs,
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(26.dp).testTag("clear_combat_logs_btn")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Temizle", color = TextSecondary, fontSize = 8.sp)
                    }

                    if (onClose != null) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.size(28.dp).testTag("close_combat_log_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Session Combat Statistics Dashboard Card
            Surface(
                color = SurfaceElevated.copy(alpha = 0.85f),
                shape = RoundedCornerShape(10.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(LaserCrimson.copy(alpha = 0.4f), CyanNeon.copy(alpha = 0.4f)))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Metric 1: Total Engagements
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "ANGAJMAN", color = TextSecondary, fontSize = 7.sp)
                        Text(text = "${stats.totalEngagements}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Metric 2: Total Damage Dealt
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "VERİLEN HASAR", color = MatrixGreen, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        Text(text = "%.0f HP".format(stats.totalDamageDealt), color = MatrixGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Metric 3: Total Damage Taken
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "ALINAN HASAR", color = WarningOrange, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        Text(text = "%.0f HP".format(stats.totalDamageTaken), color = WarningOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Metric 4: Enemy Casualties (Kills)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "DÜŞMAN ZAYİATI", color = CyanNeon, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        Text(text = "☠️ ${stats.enemyCasualties}", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Metric 5: Friendly Casualties (Lost)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "DOST KAYIP", color = DangerRed, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        Text(text = "💔 ${stats.friendlyCasualties}", color = DangerRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Metric 6: Kill / Death Ratio
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "K/D ORANI", color = AmberGold, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "%.2f".format(stats.killDeathRatio),
                            color = if (stats.killDeathRatio >= 1.0f) MatrixGreen else DangerRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Tabs Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(CombatFilterTab.values()) { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        color = if (isSelected) LaserCrimson else SurfaceElevated,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable { selectedTab = tab }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = tab.icon, fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tab.title,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable Combat Log Entries
            if (filteredLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🛡️", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Henüz kayıtlı bir çatışma angajmanı bulunmuyor.",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "Düşman birimlerine yaklaşarak muharebeyi başlatın.",
                            color = TextMuted,
                            fontSize = 8.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredLogs, key = { it.id }) { log ->
                        CombatLogItemCard(log = log)
                    }
                }
            }
        }
    }
}

@Composable
private fun CombatLogItemCard(log: CombatEngagementLog) {
    val badgeColor = when (log.type) {
        CombatLogType.DAMAGE_DEALT -> MatrixGreen
        CombatLogType.DAMAGE_TAKEN -> WarningOrange
        CombatLogType.ENEMY_KILLED -> CyanNeon
        CombatLogType.FRIENDLY_LOST -> DangerRed
        CombatLogType.CRITICAL_STRIKE -> AmberGold
    }

    Surface(
        color = SurfaceElevated.copy(alpha = 0.70f),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (log.isCasualty) listOf(badgeColor.copy(alpha = 0.6f), SurfaceBorder)
                else listOf(SurfaceBorder, SurfaceBorder)
            )
        ),
        modifier = Modifier.fillMaxWidth().testTag("combat_log_item_${log.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Timestamp & Category Icon
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(48.dp)
            ) {
                Text(text = log.type.badgeIcon, fontSize = 14.sp)
                Text(text = log.timestamp, color = TextMuted, fontSize = 7.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Combat details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attacker ➔ Target
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = log.attackerName,
                            color = if (log.isAttackerPlayer) AmberGold else LaserCrimson,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = " ➔ ", color = TextMuted, fontSize = 8.sp)
                        Text(
                            text = log.targetName,
                            color = if (!log.isAttackerPlayer) AmberGold else LaserCrimson,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Damage dealt or casualty pill
                    if (log.isCasualty) {
                        Surface(
                            color = if (log.type == CombatLogType.ENEMY_KILLED) CyanNeon.copy(alpha = 0.2f) else DangerRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (log.type == CombatLogType.ENEMY_KILLED) "☠️ DÜŞMAN İMHA" else "💔 DOST KAYIP",
                                color = if (log.type == CombatLogType.ENEMY_KILLED) CyanNeon else DangerRed,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "-%.0f HP".format(log.damage),
                            color = if (log.isAttackerPlayer) MatrixGreen else DangerRed,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Silah: ${log.weaponSystem} • ${log.type.title}",
                        color = TextSecondary,
                        fontSize = 7.sp
                    )

                    if (log.isCasualty) {
                        Text(
                            text = if (log.type == CombatLogType.ENEMY_KILLED) "+150 Altın & +40 TP Kazanıldı" else "Kuvvetler Geri Çekildi",
                            color = AmberGold,
                            fontSize = 7.sp
                        )
                    }
                }
            }
        }
    }
}
