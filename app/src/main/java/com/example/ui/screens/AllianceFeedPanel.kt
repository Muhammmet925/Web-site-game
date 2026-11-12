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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.AllianceDonationMilestone
import com.example.model.AllianceFeedCatalog
import com.example.model.AllianceFeedEvent
import com.example.model.AllianceFeedEventType
import com.example.model.AllianceState
import com.example.model.AllianceTechResearch
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
fun AllianceFeedPanel(
    initialState: AllianceState = remember { AllianceFeedCatalog.createInitialAllianceState() },
    onDonateGold: (amount: Int) -> Unit = {},
    onClose: () -> Unit
) {
    var allianceState by remember { mutableStateOf(initialState) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Canlı İttifak Akışı", "Ortak Teknoloji & Bağışlar", "50v50 Savaş Takvimi")

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("alliance_feed_panel"),
        color = SpaceBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(CyanNeon.copy(alpha = 0.35f), SurfaceDark)))
                            .border(1.5.dp, CyanNeon, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "İttifak",
                            tint = CyanNeon,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${allianceState.allianceName} ${allianceState.allianceTag}",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AmberGold)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Seviye ${allianceState.allianceLevel}",
                                    color = SpaceBlack,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SurfaceElevated)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Üyeler: ${allianceState.totalMembers}/${allianceState.maxMembers}",
                                    color = CyanNeon,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Text(
                            text = "Toplam İttifak Bağışı: ${allianceState.totalDonationsGold} 🪙 Altın • Aktif Muharebe Seferberliği",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_alliance_feed_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = TextSecondary
                    )
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = CyanNeon,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyanNeon,
                        height = 2.5.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CyanNeon else TextSecondary
                            )
                        },
                        modifier = Modifier.testTag("alliance_feed_tab_$index")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> AllianceFeedStreamTab(
                        events = allianceState.feedEvents
                    )
                    1 -> AllianceTechAndDonationTab(
                        state = allianceState,
                        onDonate = { amount ->
                            onDonateGold(amount)
                            val currentPoints = allianceState.activeTech.currentPoints + amount
                            val updatedTech = allianceState.activeTech.copy(
                                currentPoints = currentPoints.coerceAtMost(allianceState.activeTech.targetPoints)
                            )
                            val newEvent = AllianceFeedEvent(
                                timestampFormatted = "Şimdi",
                                authorName = "Komutan (Sen)",
                                authorRole = "General",
                                eventType = AllianceFeedEventType.DONATION,
                                message = "'${updatedTech.title}' araştırmasına $amount Altın bağışladı.",
                                rewardText = "+${amount / 4} İttifak Onur Puanı"
                            )
                            allianceState = allianceState.copy(
                                totalDonationsGold = allianceState.totalDonationsGold + amount,
                                activeTech = updatedTech,
                                feedEvents = listOf(newEvent) + allianceState.feedEvents
                            )
                        }
                    )
                    2 -> AllianceWarScheduleTab(
                        war = allianceState.upcomingWar,
                        onRegisterWar = {
                            allianceState = allianceState.copy(
                                upcomingWar = allianceState.upcomingWar.copy(
                                    registeredMembers = (allianceState.upcomingWar.registeredMembers + 1).coerceAtMost(allianceState.upcomingWar.maxMembers),
                                    warStatus = "Kayıt Yapıldı: 45/50 Asker Hazır!"
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AllianceFeedStreamTab(events: List<AllianceFeedEvent>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("alliance_feed_stream_list"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceDark)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(event.eventType.iconEmoji, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = event.authorName,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CyanNeon.copy(alpha = 0.15f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = event.authorRole,
                                        color = CyanNeon,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Text(
                                text = event.timestampFormatted,
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = event.message,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )

                        event.rewardText?.let { reward ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "✨ $reward",
                                color = AmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AllianceTechAndDonationTab(
    state: AllianceState,
    onDonate: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Left Column: Active Tech & Donation Controls
        Column(
            modifier = Modifier
                .weight(1.3f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceDark)
                .border(1.dp, CyanNeon.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(state.activeTech.iconEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = state.activeTech.title,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Seviye ${state.activeTech.currentLevel} / ${state.activeTech.maxLevel}",
                                color = AmberGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MatrixGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "%${(state.activeTech.progressPercent * 100).toInt()}",
                            color = MatrixGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.activeTech.description,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceElevated)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "🎁 Aktif İttifak Bonusu: ${state.activeTech.bonusDescription}",
                        color = MatrixGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { state.activeTech.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = MatrixGreen,
                    trackColor = SurfaceElevated
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Mevcut: ${state.activeTech.currentPoints} Puan",
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                    Text(
                        text = "Hedef: ${state.activeTech.targetPoints} Puan",
                        color = TextMuted,
                        fontSize = 9.sp
                    )
                }
            }

            // Interactive Donation Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "İTTİFAK TEKNOLOJİSİNE BAĞIŞ YAP",
                    color = AmberGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onDonate(500) },
                        colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("donate_button_500")
                    ) {
                        Text(
                            "+500 Altın",
                            color = SpaceBlack,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onDonate(2000) },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("donate_button_2000")
                    ) {
                        Text(
                            "+2,000 Altın",
                            color = SpaceBlack,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Right Column: Donation Milestones
        Column(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceDark)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "BAĞIŞ AŞAMALARI & ÖDÜLLER (MİLESTONES)",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.milestones) { milestone ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (milestone.isUnlocked) MatrixGreen.copy(alpha = 0.10f) else SpaceBlack.copy(alpha = 0.5f))
                            .border(
                                1.dp,
                                if (milestone.isUnlocked) MatrixGreen else SurfaceBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (milestone.isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (milestone.isUnlocked) MatrixGreen else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = milestone.title,
                                        color = if (milestone.isUnlocked) TextPrimary else TextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "(${milestone.requiredTotalDonation} 🪙)",
                                        color = AmberGold,
                                        fontSize = 9.sp
                                    )
                                }
                                Text(
                                    text = milestone.rewardBuff,
                                    color = if (milestone.isUnlocked) MatrixGreen else CyanNeon,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold
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
private fun AllianceWarScheduleTab(
    war: com.example.model.AllianceWarSchedule,
    onRegisterWar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceDark)
            .border(1.dp, DangerRed.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚔️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = war.warTitle,
                            color = DangerRed,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Hedef Bölge: ${war.targetSector}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(WarningOrange.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⏳ Başlamaya: ${war.remainingMinutes} Dakika",
                        color = WarningOrange,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Matchup Card: Friendly vs Enemy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SpaceBlack.copy(alpha = 0.6f))
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TURKISH GLADIATORS", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("[TURK]", color = TextSecondary, fontSize = 10.sp)
                    Text("Seviye 8 İttifak", color = MatrixGreen, fontSize = 9.sp)
                }

                Text("VS", color = DangerRed, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(war.enemyAllianceName, color = WarningOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(war.enemyAllianceTag, color = TextSecondary, fontSize = 10.sp)
                    Text("Seviye 9 İttifak", color = DangerRed, fontSize = 9.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Roster status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Savaşa Kayıtlı İttifak Askerleri: ${war.registeredMembers} / ${war.maxMembers}",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = war.scheduledTimeFormatted,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { war.registeredMembers.toFloat() / war.maxMembers.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = DangerRed,
                trackColor = SurfaceElevated
            )
        }

        // Action Bar
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceElevated,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Durum: ${war.warStatus}",
                    color = MatrixGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = onRegisterWar,
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("register_war_button")
                ) {
                    Text(
                        "Savaşa Katıl / Rezervasyon Yap",
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
