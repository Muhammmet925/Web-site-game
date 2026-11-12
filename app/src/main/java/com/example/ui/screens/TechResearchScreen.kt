package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.TechCategory
import com.example.model.TechNode
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
 * Technology Research Screen:
 * - Unlocks historical military units, structures, and tactical doctrines.
 * - Advances human civilization through 4 historical eras:
 *   1) 5. Yüzyıl (Orta Çağ Başlangıcı)
 *   2) Klasik & Barut Dönemi
 *   3) Endüstri Devrimi
 *   4) Modern Çağ (Modern Warfare)
 * - Real-time research progress tracking with live countdowns.
 */
@Composable
fun TechResearchScreen(
    techTree: List<TechNode>,
    currentEra: Era,
    currentTechPoints: Double,
    currentGold: Double,
    currentIron: Double,
    activeResearchId: String?,
    onStartResearch: (String) -> Unit,
    onCancelResearch: (String) -> Unit,
    onUpgradeEra: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedEraTab by remember { mutableStateOf(currentEra) }

    val filteredTechs = remember(techTree, selectedEraTab) {
        techTree.filter { it.era == selectedEraTab }
    }

    val activeResearchNode = remember(techTree, activeResearchId) {
        techTree.find { it.id == activeResearchId && it.isResearching }
    }

    // Era Progress statistics
    val totalInEra = filteredTechs.size
    val researchedInEra = filteredTechs.count { it.isResearched }

    val infiniteTransition = rememberInfiniteTransition(label = "tech_research_glow")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

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
                            .background(CyanNeon.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ASKERİ TEKNOLOJİ & AR-GE LABORATUVARI",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = MatrixGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "5. YY ➔ MODERN ÇAĞ",
                                    color = MatrixGreen,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Yeni askeri birimlerin ve savunma yapılarının kilitlerini açarak çağ atlayın.",
                            color = TextSecondary,
                            fontSize = 8.sp
                        )
                    }
                }

                // Balance and Close Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔬 ${currentTechPoints.toInt()} TP", color = CyanNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "💰 ${currentGold.toInt()}G", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "⚒️ ${currentIron.toInt()}D", color = Color(0xFFCBD5E1), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(28.dp).testTag("close_tech_research_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Active Research Progress Banner (If any research currently running)
            if (activeResearchNode != null) {
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(CyanNeon.copy(alpha = pulseGlow), AmberGold.copy(alpha = 0.5f))
                        )
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = activeResearchNode.iconEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "DEVAM EDEN ARAŞTIRMA: ${activeResearchNode.name}",
                                    color = CyanNeon,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Kalan Süre: %.0f saniye • İlerleme: %%%.0f".format(
                                        activeResearchNode.timeRemaining,
                                        activeResearchNode.progressPercent * 100f
                                    ),
                                    color = TextSecondary,
                                    fontSize = 7.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LinearProgressIndicator(
                                progress = { activeResearchNode.progressPercent },
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CyanNeon,
                                trackColor = SpaceBlack
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { onCancelResearch(activeResearchNode.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.25f)),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                modifier = Modifier.height(22.dp)
                            ) {
                                Text(text = "İptal", color = DangerRed, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Historical Era Selector Tabs (5. Yüzyıl ➔ Klasik ➔ Endüstri ➔ Modern)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Era.values()) { era ->
                    val isSelected = selectedEraTab == era
                    val isCurrentGameEra = currentEra == era
                    val eraResearched = techTree.count { it.era == era && it.isResearched }
                    val eraTotal = techTree.count { it.era == era }

                    Surface(
                        color = if (isSelected) CyanNeon else SurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                if (isCurrentGameEra) listOf(AmberGold, MatrixGreen)
                                else listOf(SurfaceBorder, SurfaceBorder)
                            )
                        ),
                        modifier = Modifier
                            .clickable { selectedEraTab = era }
                            .testTag("tech_era_tab_${era.name}")
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isCurrentGameEra) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) SpaceBlack else AmberGold)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = era.title.split("(").first(),
                                    color = if (isSelected) SpaceBlack else TextPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${era.yearTag} • $eraResearched/$eraTotal Tamamlandı",
                                color = if (isSelected) SpaceBlack.copy(alpha = 0.8f) else TextSecondary,
                                fontSize = 7.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tech Tree Nodes List for Selected Era
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredTechs, key = { it.id }) { tech ->
                    TechNodeCard(
                        tech = tech,
                        currentTechPoints = currentTechPoints,
                        currentGold = currentGold,
                        currentIron = currentIron,
                        isAnyResearchActive = activeResearchId != null,
                        allTechs = techTree,
                        onStartResearch = { onStartResearch(tech.id) }
                    )
                }

                // Era Advancement Action Card at the bottom of the list
                item {
                    EraAdvancementBottomBanner(
                        currentEra = currentEra,
                        viewedEra = selectedEraTab,
                        canAffordGold = currentGold >= (selectedEraTab.upgradeCostGold),
                        canAffordTech = currentTechPoints >= (selectedEraTab.upgradeCostTech),
                        onUpgradeEra = onUpgradeEra
                    )
                }
            }
        }
    }
}

@Composable
private fun TechNodeCard(
    tech: TechNode,
    currentTechPoints: Double,
    currentGold: Double,
    currentIron: Double,
    isAnyResearchActive: Boolean,
    allTechs: List<TechNode>,
    onStartResearch: () -> Unit
) {
    val prereq = tech.prerequisiteTechId?.let { pid -> allTechs.find { it.id == pid } }
    val isPrereqMet = prereq == null || prereq.isResearched

    val canAfford = currentTechPoints >= tech.costTechPoints &&
                    currentGold >= tech.costGold &&
                    currentIron >= tech.costIron

    val canStart = isPrereqMet && canAfford && !tech.isResearched && !tech.isResearching && !isAnyResearchActive

    val cardBorder = when {
        tech.isResearched -> CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MatrixGreen.copy(alpha = 0.7f), SurfaceBorder)))
        tech.isResearching -> CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyanNeon, AmberGold)))
        canStart -> CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.4f), SurfaceBorder)))
        else -> CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceBorder)))
    }

    Surface(
        color = if (tech.isResearched) SurfaceElevated.copy(alpha = 0.85f) else SurfaceElevated.copy(alpha = 0.65f),
        shape = RoundedCornerShape(10.dp),
        border = cardBorder,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tech_card_${tech.id}")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon & Category Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(54.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                tech.isResearched -> MatrixGreen.copy(alpha = 0.2f)
                                tech.isResearching -> CyanNeon.copy(alpha = 0.2f)
                                else -> SurfaceDark
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = tech.iconEmoji, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tech.category.badge,
                    fontSize = 10.sp
                )
                Text(
                    text = tech.category.title.take(8),
                    color = TextSecondary,
                    fontSize = 7.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Tech Info Column
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tech.name,
                        color = when {
                            tech.isResearched -> MatrixGreen
                            tech.isResearching -> CyanNeon
                            else -> TextPrimary
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Research Status Pill
                    Surface(
                        color = when {
                            tech.isResearched -> MatrixGreen.copy(alpha = 0.2f)
                            tech.isResearching -> CyanNeon.copy(alpha = 0.2f)
                            !isPrereqMet -> LaserCrimson.copy(alpha = 0.15f)
                            else -> AmberGold.copy(alpha = 0.15f)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = when {
                                tech.isResearched -> "✅ ARAŞTIRILDI"
                                tech.isResearching -> "⏳ %${(tech.progressPercent * 100).toInt()}"
                                !isPrereqMet -> "🔒 KİLİTLİ"
                                canAfford -> "🔓 HAZIR"
                                else -> "⚠️ KAYNAK YETERSİZ"
                            },
                            color = when {
                                tech.isResearched -> MatrixGreen
                                tech.isResearching -> CyanNeon
                                !isPrereqMet -> DangerRed
                                else -> AmberGold
                            },
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = tech.description,
                    color = TextSecondary,
                    fontSize = 8.sp,
                    lineHeight = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // What it unlocks (Unit / Structure / Buff)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (tech.unlocksUnitName != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (tech.unlocksUnitType) {
                                    UnitType.JET -> Icons.Default.AirplanemodeActive
                                    UnitType.TANK, UnitType.SIEGE -> Icons.Default.CarRepair
                                    UnitType.SUBMARINE -> Icons.Default.DirectionsBoat
                                    else -> Icons.Default.Person
                                },
                                contentDescription = null,
                                tint = AmberLight,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Açar: ${tech.unlocksUnitName}",
                                color = AmberLight,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (tech.unlocksStructureTitle != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Açar: ${tech.unlocksStructureTitle}",
                                color = MatrixGreen,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (tech.passiveBonusText != null) {
                        Text(
                            text = "Bonus: ${tech.passiveBonusText}",
                            color = CyanNeon,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Cost Row
                    Text(
                        text = "${tech.costTechPoints}TP • ${tech.costGold}G • ${tech.costIron}D • ${tech.durationSeconds}s",
                        color = TextMuted,
                        fontSize = 7.sp
                    )
                }

                // If researching: show live progress indicator
                if (tech.isResearching) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { tech.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = CyanNeon,
                        trackColor = SpaceBlack
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Button
            Button(
                onClick = onStartResearch,
                enabled = canStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanNeon,
                    disabledContainerColor = SurfaceDark.copy(alpha = 0.4f)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .height(28.dp)
                    .testTag("research_btn_${tech.id}")
            ) {
                Text(
                    text = when {
                        tech.isResearched -> "Açıldı"
                        tech.isResearching -> "Sürüyor"
                        !isPrereqMet -> "Kilitli"
                        else -> "Araştır"
                    },
                    color = if (canStart) SpaceBlack else TextMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EraAdvancementBottomBanner(
    currentEra: Era,
    viewedEra: Era,
    canAffordGold: Boolean,
    canAffordTech: Boolean,
    onUpgradeEra: () -> Unit
) {
    val nextEra = when (currentEra) {
        Era.MEDIEVAL_5TH -> Era.CLASSICAL_GUNPOWDER
        Era.CLASSICAL_GUNPOWDER -> Era.INDUSTRIAL
        Era.INDUSTRIAL -> Era.MODERN
        Era.MODERN -> null
    }

    if (nextEra != null && viewedEra == currentEra) {
        Surface(
            color = SurfaceDark,
            shape = RoundedCornerShape(10.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(AmberGold, MatrixGreen))
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Upgrade, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "TARİHİ ÇAĞ ATLAMA: ${nextEra.title}",
                            color = AmberGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Gereksinim: ${nextEra.upgradeCostGold} Altın & ${nextEra.upgradeCostTech} Teknoloji Puanı",
                            color = TextSecondary,
                            fontSize = 8.sp
                        )
                    }
                }

                Button(
                    onClick = onUpgradeEra,
                    enabled = canAffordGold && canAffordTech,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberGold,
                        disabledContainerColor = SurfaceElevated
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp).testTag("upgrade_era_screen_btn")
                ) {
                    Text(
                        text = if (canAffordGold && canAffordTech) "Çağ Atla ➔" else "Kaynak Yetersiz",
                        color = if (canAffordGold && canAffordTech) SpaceBlack else DangerRed,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
