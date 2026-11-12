package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.RtsResources
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EnergyYellow
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ResourceTopBar(
    resources: RtsResources,
    currentEra: Era,
    simulationSpeed: Float,
    isSimulationRunning: Boolean,
    onToggleRunning: (Boolean) -> Unit,
    onCycleSpeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark.copy(alpha = 0.95f))
            .border(width = 1.dp, color = SurfaceElevated)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gold Chip
            RtsChip(
                icon = Icons.Default.MonetizationOn,
                iconColor = AmberGold,
                label = "Altın",
                value = "${resources.gold.toInt()}",
                tag = "gold_chip"
            )

            // Tech Points Chip
            RtsChip(
                icon = Icons.Default.Science,
                iconColor = CyanNeon,
                label = "Teknoloji",
                value = "${resources.techPoints.toInt()} TP",
                tag = "tech_chip"
            )

            // Energy Chip
            val energyColor = if (resources.isEnergyDeficit) DangerRed else EnergyYellow
            RtsChip(
                icon = Icons.Default.Bolt,
                iconColor = energyColor,
                label = "Enerji",
                value = "${resources.energyProduced}/${resources.energyConsumed} MW",
                subText = if (resources.isEnergyDeficit) "! %${(resources.energyEfficiency * 100).toInt()}" else "OK",
                tag = "energy_chip"
            )

            // Era Chip
            RtsChip(
                icon = Icons.Default.AccountBalance,
                iconColor = MatrixGreen,
                label = "Dönem",
                value = currentEra.name.split("_").first(),
                tag = "era_chip"
            )

            // Speed & Pause Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .clickable { onToggleRunning(!isSimulationRunning) }
                        .testTag("sim_play_pause_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSimulationRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Simülasyon Durdur/Başlat",
                        tint = CyanNeon,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .clickable { onCycleSpeed() }
                        .testTag("sim_speed_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${simulationSpeed.toInt()}x",
                        color = AmberGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Energy Deficit Banner
        AnimatedVisibility(visible = resources.isEnergyDeficit) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(DangerRed.copy(alpha = 0.2f))
                    .border(1.dp, DangerRed.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = DangerRed,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "GÜÇ KESİNTİSİ! Binalar %${(resources.energyEfficiency * 100).toInt()} üretim hızıyla çalışıyor. Soğuk Füzyon Santrali kurun.",
                    color = DangerRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RtsChip(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    subText: String? = null,
    tag: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevated)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = label,
                fontSize = 8.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 11.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                if (subText != null) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = subText,
                        fontSize = 9.sp,
                        color = if (subText.startsWith("!")) DangerRed else TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
