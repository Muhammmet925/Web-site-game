package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.data.model.LeaderboardEntryDto
import com.example.data.model.MatchHistoryRecordDto
import com.example.model.Nation
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DangerRed
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
fun GlobalLeaderboardScreen(
    leaderboardEntries: List<LeaderboardEntryDto>,
    matchHistory: List<MatchHistoryRecordDto>,
    currentUserName: String,
    currentUserFaction: Nation,
    isAuthenticated: Boolean = false,
    onSignInGoogle: () -> Unit = {},
    onSubmitCurrentSessionScore: () -> Unit = {},
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Liderlik Sıralaması, 1: Maç Geçmişim

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("global_leaderboard_screen"),
        color = SpaceBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AmberGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Liderlik",
                            tint = AmberGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KÜRESEL LİDERLİK TABLOSU (FIRESTORE CLOUD)",
                                color = AmberGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Bulut Senkronize",
                                tint = MatrixGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Tüm dünyadaki oyuncuların zafer puanları (VP) ve rütbe sıralamaları canlı senkronize edilir.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Google Sign-In Status / Button
                    if (isAuthenticated) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MatrixGreen.copy(alpha = 0.15f))
                                .border(1.dp, MatrixGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(currentUserName, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = onSignInGoogle,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("google_signin_button")
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Google ile Giriş", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onSubmitCurrentSessionScore,
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("sync_score_button")
                    ) {
                        Icon(Icons.Default.CloudSync, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Skor Gönder", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_leaderboard_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
                    }
                }
            }

            // Tabs Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TabChip(
                    title = "🏆 Küresel Oyuncu Sıralaması",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                TabChip(
                    title = "📜 Maç Geçmişim (${matchHistory.size})",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }

            // Tab Content
            if (selectedTab == 0) {
                // Table of Leaders
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceElevated)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sıra", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(44.dp))
                            Text("Komutan & Ülke", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f))
                            Text("Askeri Rütbe", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f))
                            Text("Kazanma", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(80.dp))
                            Text("Zafer Puanı (VP)", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(110.dp))
                        }
                    }

                    itemsIndexed(leaderboardEntries) { index, entry ->
                        val rankNumber = index + 1
                        val isTop3 = rankNumber <= 3
                        val rankColor = when (rankNumber) {
                            1 -> AmberGold
                            2 -> Color(0xFFE0E0E0) // Silver
                            3 -> Color(0xFFCD7F32) // Bronze
                            else -> TextSecondary
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isTop3) rankColor.copy(alpha = 0.08f) else SpaceBlack.copy(alpha = 0.4f))
                                .border(
                                    1.dp,
                                    if (isTop3) rankColor.copy(alpha = 0.35f) else Color.Transparent,
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank Number / Badge
                            Box(
                                modifier = Modifier.width(44.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (isTop3) {
                                    Text(
                                        text = when (rankNumber) {
                                            1 -> "🥇 1"
                                            2 -> "🥈 2"
                                            else -> "🥉 3"
                                        },
                                        color = rankColor,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                } else {
                                    Text(
                                        text = "#$rankNumber",
                                        color = TextMuted,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Commander & Faction
                            Row(
                                modifier = Modifier.weight(1.5f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = entry.playerName,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceElevated)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = entry.playerFaction,
                                        color = CyanNeon,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Rank Title
                            Text(
                                text = entry.rankTitle.ifEmpty { "Tuğgeneral" },
                                color = if (isTop3) rankColor else WarningOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1.2f)
                            )

                            // Win Rate
                            Text(
                                text = "%${entry.winRatePct} (${entry.matchesWon}/${entry.matchesPlayed})",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                modifier = Modifier.width(80.dp)
                            )

                            // Victory Points
                            Text(
                                text = "${entry.totalVictoryPoints} VP",
                                color = AmberGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.width(110.dp)
                            )
                        }
                    }
                }
            } else {
                // Match History List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (matchHistory.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.History, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Henüz kayıtlı maç geçmişi bulunmuyor. Bir RTS skirmish karşılaşması tamamlandığında sonuçlar burada saklanacaktır.",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    } else {
                        itemsIndexed(matchHistory) { _, record ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpaceBlack)
                                    .border(1.dp, if (record.isVictory) MatrixGreen.copy(alpha = 0.4f) else DangerRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = if (record.isVictory) "🏆 ZAFER" else "💀 YENİLGİ",
                                                color = if (record.isVictory) MatrixGreen else DangerRed,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = record.skirmishMapTitle,
                                                color = TextPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Bölge Kontrolü: +${record.mapControlPoints} VP • Kaynak: +${record.resourceExtractionPoints} VP • İmha: +${record.unitDestructionPoints} VP",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "+${record.totalVictoryPoints} VP",
                                            color = AmberGold,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                        Text(
                                            text = "Süre: ${record.matchDurationSeconds / 60}dk",
                                            color = TextMuted,
                                            fontSize = 10.sp
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
private fun TabChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AmberGold else SurfaceDark)
            .border(1.dp, if (isSelected) AmberGold else SurfaceBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) SpaceBlack else TextPrimary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
