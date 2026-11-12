package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Upgrade
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
import com.example.model.Era
import com.example.model.EraProgressCalculator
import com.example.model.RtsResources
import com.example.model.TechNode
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

/**
 * Visual Timeline Progress Bar Component:
 * - Shows current historical era and dynamic progress toward the next age requirement.
 * - Updates dynamically in real-time as research completes and resources accumulate.
 * - Displays milestone nodes with connected timeline tracks for all 4 epochs.
 * - Interactive upgrade trigger when age requirements are fulfilled.
 */
@Composable
fun EraTimelineProgressBar(
    currentEra: Era,
    techTree: List<TechNode>,
    resources: RtsResources,
    onUpgradeEra: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressState = remember(currentEra, techTree, resources.gold, resources.techPoints) {
        EraProgressCalculator.calculate(currentEra, techTree, resources)
    }

    var isDetailsExpanded by remember { mutableStateOf(false) }

    // Pulsing animation for the current era milestone
    val infiniteTransition = rememberInfiniteTransition(label = "timeline_node_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Surface(
        color = SurfaceDark.copy(alpha = 0.92f),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(CyanNeon.copy(alpha = 0.35f), AmberGold.copy(alpha = 0.35f))
            )
        ),
        modifier = modifier.testTag("era_timeline_progress_bar")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            // Header Row: Current Era Title & Dynamic Next Age Progress Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isDetailsExpanded = !isDetailsExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⏳ ÇAĞ ZAMAN ÇİZELGESİ",
                        color = AmberGold,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${currentEra.title}",
                        color = TextPrimary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (progressState.nextEra != null) {
                        Text(
                            text = "Sonraki Çağ: %${(progressState.progressPercent * 100).toInt()}",
                            color = if (progressState.isReadyToUpgrade) MatrixGreen else CyanNeon,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "👑 ZİRVE ÇAĞI",
                            color = MatrixGreen,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isDetailsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Detaylar",
                        tint = CyanNeon,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ==================== 4 ERA MILESTONES & CONNECTED TIMELINE ====================
            val allEras = listOf(
                Era.MEDIEVAL_5TH to "5. YY",
                Era.CLASSICAL_GUNPOWDER to "Barut",
                Era.INDUSTRIAL to "Endüstri",
                Era.MODERN to "Modern"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                allEras.forEachIndexed { index, (era, shortLabel) ->
                    val isPast = era.ordinal < currentEra.ordinal
                    val isCurrent = era == currentEra
                    val isFuture = era.ordinal > currentEra.ordinal

                    // Milestone Node
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(if (isCurrent) 20.dp else 16.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isPast -> MatrixGreen.copy(alpha = 0.25f)
                                    isCurrent -> AmberGold.copy(alpha = 0.35f)
                                    else -> SurfaceElevated
                                }
                            )
                            .border(
                                width = if (isCurrent) 1.5.dp else 1.dp,
                                color = when {
                                    isPast -> MatrixGreen
                                    isCurrent -> AmberGold
                                    else -> SurfaceBorder
                                },
                                shape = CircleShape
                            )
                    ) {
                        when {
                            isPast -> Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MatrixGreen,
                                modifier = Modifier.size(10.dp)
                            )
                            isCurrent -> Text(
                                text = "★",
                                color = AmberGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            else -> Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = shortLabel,
                        color = when {
                            isCurrent -> AmberGold
                            isPast -> MatrixGreen
                            else -> TextMuted
                        },
                        fontSize = 8.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                    )

                    // Connecting Line Track to Next Era
                    if (index < allEras.lastIndex) {
                        Spacer(modifier = Modifier.width(4.dp))
                        val trackProgress = when {
                            isPast -> 1f
                            isCurrent -> progressState.progressPercent
                            else -> 0f
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(SurfaceElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(trackProgress)
                                    .height(3.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            if (isPast) listOf(MatrixGreen, MatrixGreen)
                                            else listOf(AmberGold, CyanNeon)
                                        )
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }

            // Real-Time Progress Bar toward Next Era Requirement
            if (progressState.nextEra != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "🔬 AR-GE: ${progressState.researchedCountInCurrentEra}/${progressState.totalTechsInCurrentEra} Tamamlandı",
                        color = CyanNeon,
                        fontSize = 8.sp
                    )

                    Text(
                        text = "İlerleme: %${(progressState.progressPercent * 100).toInt()}",
                        color = if (progressState.isReadyToUpgrade) MatrixGreen else AmberGold,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                LinearProgressIndicator(
                    progress = { progressState.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp)),
                    color = if (progressState.isReadyToUpgrade) MatrixGreen else CyanNeon,
                    trackColor = SurfaceElevated
                )
            }

            // Expandable Requirement Details & Upgrade Button
            AnimatedVisibility(
                visible = isDetailsExpanded || progressState.isReadyToUpgrade,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    if (progressState.nextEra != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Gereksinimler (${progressState.nextEra.title}):",
                                    color = TextSecondary,
                                    fontSize = 7.sp
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "💰 Altın: ${resources.gold.toInt()} / ${progressState.nextEra.upgradeCostGold}",
                                        color = if (progressState.hasRequiredGold) MatrixGreen else DangerRed,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "🧪 TP: ${resources.techPoints.toInt()} / ${progressState.nextEra.upgradeCostTech}",
                                        color = if (progressState.hasRequiredTech) MatrixGreen else DangerRed,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            if (progressState.isReadyToUpgrade) {
                                Button(
                                    onClick = onUpgradeEra,
                                    colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier
                                        .height(24.dp)
                                        .testTag("timeline_upgrade_era_btn")
                                ) {
                                    Icon(imageVector = Icons.Default.Upgrade, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "ÇAĞ ATLA",
                                        color = SpaceBlack,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Tüm çağlar başarıyla tamamlandı. Ordunuz en üst düzey modern askeri teknolojilerle donatıldı.",
                            color = MatrixGreen,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        }
    }
}
