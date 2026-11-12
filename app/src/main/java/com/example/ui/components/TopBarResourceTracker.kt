package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.Nation
import com.example.model.RtsBuilding
import com.example.model.RtsResources
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

/**
 * Top-Bar Resource Tracker Component:
 * - Displays real-time reserves for Gold (Altın), Iron (Demir/Çelik), and Energy (Enerji MW).
 * - Real-time depletion indicators based on active production queue activity.
 * - Shows dynamic rate badges (+gain vs -depletion drain arrows).
 * - Integrated quick-action buttons in a single compact horizontal scrollable strip.
 */
@Composable
fun TopBarResourceTracker(
    resources: RtsResources,
    nation: Nation,
    currentEra: Era,
    buildings: List<RtsBuilding>,
    simulationSpeed: Float,
    is3DMode: Boolean,
    isSkirmishAiActive: Boolean,
    skirmishWave: Int,
    isAudioMuted: Boolean,
    isHudVisible: Boolean,
    onCycleSpeed: () -> Unit,
    onToggle3D: () -> Unit,
    onToggleSkirmishAi: () -> Unit,
    onToggleAudio: () -> Unit,
    onToggleHudVisibility: () -> Unit,
    onOpenTechResearch: () -> Unit,
    onOpenDiplomacy: () -> Unit,
    onOpenBattleLog: () -> Unit,
    onOpenMilitaryMarket: () -> Unit,
    onOpenWorldMap: () -> Unit,
    onOpenWarDashboard: () -> Unit,
    onOpenFactionSelect: () -> Unit,
    onOpenBaseManagement: () -> Unit,
    onOpenCombatLog: () -> Unit = {},
    onOpenSkirmishMapSelect: () -> Unit = {},
    onOpenLeaderboard: () -> Unit = {},
    onOpenPostMatch: () -> Unit = {},
    onOpenGestureSettings: () -> Unit = {},
    onOpenReplays: () -> Unit = {},
    onOpenCommanderGallery: () -> Unit = {},
    onOpenAllianceFeed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Count active production queues across all factories, airfields, shipyards, barracks
    val activeQueuesCount = buildings.sumOf { it.queue.size }
    val isProducingUnits = activeQueuesCount > 0

    // Compute dynamic production depletion drain based on active queue count
    val effectiveGoldDrain = if (isProducingUnits) activeQueuesCount * 22.0 else 0.0
    val effectiveIronDrain = if (isProducingUnits) activeQueuesCount * 14.0 else 0.0

    val netGold = resources.goldIncomeRate - effectiveGoldDrain
    val netIron = resources.ironIncomeRate - effectiveIronDrain

    val isGoldDepleting = netGold < 0
    val isIronDepleting = netIron < 0

    // Pulsing animation for depletion warnings and energy deficits
    val infiniteTransition = rememberInfiniteTransition(label = "depletion_anim")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Surface(
        color = SurfaceDark.copy(alpha = 0.94f),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isGoldDepleting || isIronDepleting || resources.isEnergyDeficit)
                    listOf(DangerRed.copy(alpha = pulseAlpha), SurfaceBorder)
                else
                    listOf(CyanNeon.copy(alpha = 0.5f), SurfaceBorder)
            )
        ),
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_bar_resource_tracker")
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LEFT: Nation Flag + Era Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onOpenBaseManagement() }
                    .padding(end = 2.dp)
            ) {
                Text(text = nation.flagEmoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(3.dp))
                Column {
                    Text(text = nation.title, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = currentEra.title.split("(").first().take(9),
                        color = MatrixGreen,
                        fontSize = 6.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Divider
            Box(modifier = Modifier.size(1.dp, 16.dp).background(SurfaceBorder))

            // ==================== 1. GOLD (ALTIN) TRACKER WITH REAL-TIME DEPLETION ====================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isGoldDepleting) DangerRed.copy(alpha = 0.12f) else Color.Transparent)
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(AmberGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Inventory2, contentDescription = "Altın", tint = AmberGold, modifier = Modifier.size(10.dp))
                }
                Spacer(modifier = Modifier.width(3.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = resources.gold.toInt().toString(),
                            color = AmberGold,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = if (isGoldDepleting) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = if (isGoldDepleting) DangerRed else MatrixGreen,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                    Text(
                        text = if (isGoldDepleting) "%.0f/s".format(netGold) else "+%.0f/s".format(netGold),
                        color = if (isGoldDepleting) DangerRed else TextMuted,
                        fontSize = 6.5.sp,
                        fontWeight = if (isGoldDepleting) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            // ==================== 2. IRON (DEMİR / ÇELİK) TRACKER WITH DEPLETION ====================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isIronDepleting) WarningOrange.copy(alpha = 0.12f) else Color.Transparent)
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF64748B).copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = "Demir/Çelik", tint = Color(0xFF94A3B8), modifier = Modifier.size(10.dp))
                }
                Spacer(modifier = Modifier.width(3.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = resources.iron.toInt().toString(),
                            color = Color(0xFFCBD5E1),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = if (isIronDepleting) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = if (isIronDepleting) WarningOrange else MatrixGreen,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                    Text(
                        text = if (isIronDepleting) "%.0f/s".format(netIron) else "+%.0f/s".format(netIron),
                        color = if (isIronDepleting) WarningOrange else TextMuted,
                        fontSize = 6.5.sp,
                        fontWeight = if (isIronDepleting) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            // ==================== 3. ENERGY (ENERJİ MW) TRACKER ====================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (resources.isEnergyDeficit) DangerRed.copy(alpha = 0.15f) else Color.Transparent)
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(EnergyYellow.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Bolt, contentDescription = "Enerji", tint = EnergyYellow, modifier = Modifier.size(10.dp))
                }
                Spacer(modifier = Modifier.width(3.dp))
                Column {
                    Text(
                        text = "${resources.energyProduced}/${resources.energyConsumed}",
                        color = if (resources.isEnergyDeficit) DangerRed else EnergyYellow,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (resources.isEnergyDeficit) "⚠️ Defisit" else "100%",
                        color = if (resources.isEnergyDeficit) DangerRed else MatrixGreen,
                        fontSize = 6.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ==================== 4. REAL-TIME PRODUCTION QUEUE DEPLETION BADGE ====================
            if (isProducingUnits) {
                Surface(
                    color = LaserCrimson.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(4.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(LaserCrimson.copy(alpha = pulseAlpha), SurfaceBorder))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Construction, contentDescription = null, tint = LaserCrimson, modifier = Modifier.size(9.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "$activeQueuesCount Üretiyor",
                            color = LaserCrimson,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Divider
            Box(modifier = Modifier.size(1.dp, 16.dp).background(SurfaceBorder))

            // ==================== 5. CORE ACTION BUTTONS + MENU MODAL ====================
            var showMoreMenu by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. TECHNOLOGY RESEARCH BUTTON
                ActionButtonChip(
                    icon = Icons.Default.Science,
                    label = "🔬 Araştır",
                    accentColor = CyanNeon,
                    onClick = onOpenTechResearch,
                    testTag = "open_tech_research_btn"
                )

                // 2. HISTORICAL COMMANDERS GALLERY BUTTON
                ActionButtonChip(
                    icon = Icons.Default.MilitaryTech,
                    label = "🎖️ Komutan",
                    accentColor = AmberGold,
                    onClick = onOpenCommanderGallery,
                    testTag = "open_commanders_btn"
                )

                // 3. ALLIANCE FEED & TECH DONATION BUTTON
                ActionButtonChip(
                    icon = Icons.Default.Security,
                    label = "🛡️ İttifak",
                    accentColor = Color(0xFF00F5D4),
                    onClick = onOpenAllianceFeed,
                    testTag = "open_alliance_feed_btn"
                )

                // 4. SIMULATION SPEED
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.clickable { onCycleSpeed() }
                ) {
                    Text(
                        text = "⚡%.0fx".format(simulationSpeed),
                        color = CyanNeon,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                // 5. MORE / MENÜ BUTTON
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(4.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.5f), SurfaceBorder))
                    ),
                    modifier = Modifier
                        .clickable { showMoreMenu = true }
                        .testTag("open_more_menu_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "☰", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "Menü", color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Clean Popup Dialog for Secondary Options
            if (showMoreMenu) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showMoreMenu = false }) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.6f)),
                        shadowElevation = 16.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚙️ Komuta & Sistem Menüsü",
                                    color = CyanNeon,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    onClick = { showMoreMenu = false },
                                    shape = CircleShape,
                                    color = SurfaceElevated,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "✕", color = TextMuted, fontSize = 11.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Grid of Quick Actions
                            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.height(200.dp)
                            ) {
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.Handshake,
                                        label = "19 Ülke Diplomasi",
                                        color = Color(0xFF00F5D4),
                                        onClick = { showMoreMenu = false; onOpenDiplomacy() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.History,
                                        label = "Savaş Günlüğü",
                                        color = AmberGold,
                                        onClick = { showMoreMenu = false; onOpenBattleLog() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.FlashOn,
                                        label = "Çatışma Raporu",
                                        color = LaserCrimson,
                                        onClick = { showMoreMenu = false; onOpenCombatLog() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.Terrain,
                                        label = "Harita Değiştir",
                                        color = MatrixGreen,
                                        onClick = { showMoreMenu = false; onOpenSkirmishMapSelect() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.ShoppingCart,
                                        label = "Askeri Pazar",
                                        color = Color(0xFF38BDF8),
                                        onClick = { showMoreMenu = false; onOpenMilitaryMarket() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.Public,
                                        label = "3D Dünya Haritası",
                                        color = CyanNeon,
                                        onClick = { showMoreMenu = false; onOpenWorldMap() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.Public,
                                        label = "🏆 Liderlik Tablosu",
                                        color = AmberGold,
                                        onClick = { showMoreMenu = false; onOpenLeaderboard() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.Construction,
                                        label = "📊 VP Analiz Raporu",
                                        color = Color(0xFF10B981),
                                        onClick = { showMoreMenu = false; onOpenPostMatch() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.FlashOn,
                                        label = "🎮 Jest Ayarları",
                                        color = CyanNeon,
                                        onClick = { showMoreMenu = false; onOpenGestureSettings() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = Icons.Default.History,
                                        label = "📹 Maç Tekrarları",
                                        color = MatrixGreen,
                                        onClick = { showMoreMenu = false; onOpenReplays() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = if (is3DMode) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        label = if (is3DMode) "Görünüm: 3D" else "Görünüm: 2D",
                                        color = AmberGold,
                                        onClick = { onToggle3D() }
                                    )
                                }
                                item {
                                    MenuGridButton(
                                        icon = if (isAudioMuted) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        label = if (isAudioMuted) "Ses: Kapalı" else "Ses: Açık",
                                        color = if (isAudioMuted) DangerRed else MatrixGreen,
                                        onClick = { onToggleAudio() }
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

@Composable
private fun ActionButtonChip(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(6.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.4f), SurfaceBorder))
        ),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(11.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = label, color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MenuGridButton(
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = SurfaceElevated,
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(color.copy(alpha = 0.4f), SurfaceBorder))
        ),
        modifier = Modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

