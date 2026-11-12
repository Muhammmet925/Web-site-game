package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BattleLogCategory
import com.example.model.BattleLogEntry
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
 * BattleLog Screen:
 * Interactive scrollable tactical telemetry panel that records in-game events:
 * - Unit deaths & kills
 * - Building completions
 * - Regional ballistic strikes
 * - International military trade deliveries
 * - Unit XP rank promotions
 */
@Composable
fun BattleLogScreen(
    logs: List<BattleLogEntry>,
    onClearLogs: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<BattleLogCategory>(BattleLogCategory.ALL) }

    val filteredLogs = remember(logs, selectedCategory) {
        if (selectedCategory == BattleLogCategory.ALL) logs
        else logs.filter { it.category == selectedCategory }
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
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = CyanNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "MUHAREBE VE SAVAŞ GÜNLÜĞÜ (BATTLE LOG)",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${logs.size} Olay Kaydedildi • Çatışma, Terfi, Üretim & Ticaret",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = onClearLogs,
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.2f)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(28.dp).testTag("clear_battle_logs_btn")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = DangerRed, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Temizle", color = DangerRed, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(28.dp).testTag("close_battle_log_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(BattleLogCategory.values()) { cat ->
                    val isSelected = (selectedCategory == cat)
                    val count = if (cat == BattleLogCategory.ALL) logs.size else logs.count { it.category == cat }

                    Surface(
                        color = if (isSelected) CyanNeon else SurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                if (isSelected) listOf(CyanNeon, AmberGold) else listOf(SurfaceBorder, SurfaceBorder)
                            )
                        ),
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("filter_log_${cat.name}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = cat.icon, fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${cat.title} ($count)",
                                color = if (isSelected) SpaceBlack else TextPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrollable Log List
            if (filteredLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = TextMuted, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Henüz bu kategoride kaydedilmiş muharebe olayı bulunmuyor.",
                            color = TextMuted,
                            fontSize = 10.sp
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
                        BattleLogCard(entry = log)
                    }
                }
            }
        }
    }
}

@Composable
private fun BattleLogCard(entry: BattleLogEntry) {
    val badgeColor = when (entry.category) {
        BattleLogCategory.COMBAT -> LaserCrimson
        BattleLogCategory.RANK -> AmberGold
        BattleLogCategory.TRADE -> Color(0xFF00F5D4)
        BattleLogCategory.BUILDING -> CyanNeon
        BattleLogCategory.TERRITORY -> Color(0xFFF59E0B)
        BattleLogCategory.ALL -> TextSecondary
    }

    Surface(
        color = SurfaceElevated.copy(alpha = 0.85f),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(badgeColor.copy(alpha = 0.4f), SurfaceBorder))
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon Badge
            Surface(
                color = badgeColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "${entry.category.icon} ${entry.category.title}",
                    color = badgeColor,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Timestamp
            Text(
                text = entry.timestamp,
                color = TextMuted,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Event Message
            Text(
                text = entry.message,
                color = TextPrimary,
                fontSize = 9.sp,
                lineHeight = 12.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
