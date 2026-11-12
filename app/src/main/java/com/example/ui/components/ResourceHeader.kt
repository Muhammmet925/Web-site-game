package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PropaneTank
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.RtsResources
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

/**
 * ResourceHeader UI Component:
 * Clean, minimal header design displaying current Food (Yiyecek), Gold (Altın), and Oil (Petrol) counts.
 * Replaces cluttered overlay elements with a neat, compact tactical status bar.
 */
@Composable
fun ResourceHeader(
    resources: RtsResources,
    nation: Nation? = null,
    currentEra: Era? = null,
    onOpenBaseManagement: () -> Unit = {},
    onOpenTechResearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Pulsing critical warning animation for low Gold (< 400) or low Oil (< 200)
    val isGoldCritical = resources.gold < 400.0
    val isOilCritical = resources.oil < 200.0

    val infiniteTransition = rememberInfiniteTransition(label = "resource_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "critical_resource_pulse"
    )

    Surface(
        color = SurfaceDark.copy(alpha = 0.92f),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isGoldCritical || isOilCritical)
                    listOf(DangerRed.copy(alpha = pulseAlpha), WarningOrange.copy(alpha = pulseAlpha))
                else
                    listOf(CyanNeon.copy(alpha = 0.5f), SurfaceBorder)
            )
        ),
        shadowElevation = 6.dp,
        modifier = modifier
            .testTag("resource_header")
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Optional Nation + Era identity pill
            if (nation != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onOpenBaseManagement() }
                        .padding(end = 4.dp)
                ) {
                    Text(text = nation.flagEmoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Column {
                        Text(
                            text = nation.title,
                            color = TextPrimary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (currentEra != null) {
                            Text(
                                text = currentEra.title.split("(").first().take(9),
                                color = MatrixGreen,
                                fontSize = 6.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Box(modifier = Modifier.size(1.dp, 16.dp).background(SurfaceBorder))
            }

            // 1. FOOD (YİYECEK / GIDA) ITEM - Distinct Agriculture / Harvest Vector Icon
            ResourcePillItem(
                icon = androidx.compose.material.icons.Icons.Default.Agriculture,
                iconTint = Color(0xFF4ADE80),
                amount = resources.food.toInt(),
                maxAmount = resources.maxFood.toInt(),
                label = "Gıda",
                isCritical = false,
                pulseAlpha = 1f,
                testTag = "resource_food_pill"
            )

            // 2. GOLD (ALTIN / HAZİNE) ITEM - Distinct Gold Coin / Monetization Vector Icon
            ResourcePillItem(
                icon = androidx.compose.material.icons.Icons.Default.MonetizationOn,
                iconTint = AmberGold,
                amount = resources.gold.toInt(),
                maxAmount = resources.maxGold.toInt(),
                label = if (isGoldCritical) "Altın (Kritik!)" else "Altın",
                isCritical = isGoldCritical,
                pulseAlpha = pulseAlpha,
                testTag = "resource_gold_pill"
            )

            // 3. OIL (PETROL / YAKIT) ITEM - Distinct Industrial Propane & Fuel Tank Vector Icon
            ResourcePillItem(
                icon = androidx.compose.material.icons.Icons.Default.PropaneTank,
                iconTint = Color(0xFF38BDF8),
                amount = resources.oil.toInt(),
                maxAmount = resources.maxOil.toInt(),
                label = if (isOilCritical) "Petrol (Kritik!)" else "Petrol",
                isCritical = isOilCritical,
                pulseAlpha = pulseAlpha,
                testTag = "resource_oil_pill"
            )

            // Quick tech button shortcut
            Surface(
                onClick = onOpenTechResearch,
                shape = RoundedCornerShape(6.dp),
                color = SurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(0.8.dp, CyanNeon.copy(alpha = 0.4f)),
                modifier = Modifier.testTag("resource_header_tech_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Science,
                        contentDescription = "Araştırma",
                        tint = CyanNeon,
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Teknoloji",
                        color = CyanNeon,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ResourcePillItem(
    icon: ImageVector,
    iconTint: Color,
    amount: Int,
    maxAmount: Int,
    label: String,
    isCritical: Boolean = false,
    pulseAlpha: Float = 1f,
    testTag: String
) {
    val bgModifier = if (isCritical) {
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(DangerRed.copy(alpha = 0.22f * pulseAlpha))
            .border(1.dp, DangerRed.copy(alpha = pulseAlpha), RoundedCornerShape(6.dp))
    } else {
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceElevated)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = bgModifier
            .padding(horizontal = 5.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (isCritical) DangerRed.copy(alpha = 0.35f * pulseAlpha) else iconTint.copy(alpha = 0.20f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isCritical) DangerRed else iconTint,
                modifier = Modifier.size(10.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = amount.toString(),
                color = if (isCritical) DangerRed else TextPrimary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = if (isCritical) DangerRed else TextMuted,
                fontSize = 6.5.sp,
                fontWeight = if (isCritical) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
