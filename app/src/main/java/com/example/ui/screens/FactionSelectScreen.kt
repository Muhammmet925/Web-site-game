package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.FactionCatalog
import com.example.model.FactionData
import com.example.model.Nation
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CyanGlow
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
 * Landscape Fullscreen Faction Selection Screen:
 * Presents Turkey, USA, Russia, China with asymmetric starting parameters:
 * - Starting Era (e.g. Modern, Industrial, Classical Gunpowder)
 * - Starting Gold, Tech Points & Megawatt Energy
 * - Unique Faction Passive Doctrine & Unit Bonuses
 */
@Composable
fun FactionSelectScreen(
    currentNation: Nation,
    onSelectFaction: (FactionData) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val factions = FactionCatalog.factions

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack.copy(alpha = 0.96f))
            .padding(16.dp)
            .testTag("faction_select_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.2f))
                            .border(1.5.dp, CyanNeon, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "KÜRESEL DOKTRİN VE ÜLKE SEÇİMİ (FACTION SELECTION)",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Her ülkenin başlangıç çağı, teknoloji rezervi ve askeri pasif bonusları asimetriktir.",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Faction Cards Laid Out Horizontally in Landscape
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(factions) { faction ->
                    val isCurrent = faction.nation == currentNation
                    FactionCard(
                        faction = faction,
                        isSelected = isCurrent,
                        onChoose = { onSelectFaction(faction) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FactionCard(
    faction: FactionData,
    isSelected: Boolean,
    onChoose: () -> Unit
) {
    val nation = faction.nation

    val accentColor = when (nation) {
        Nation.TURKEY -> LaserCrimson
        Nation.USA -> CyanNeon
        Nation.RUSSIA -> WarningOrange
        Nation.CHINA -> AmberGold
    }

    Card(
        modifier = Modifier
            .width(260.dp)
            .fillMaxSize()
            .clickable { onChoose() }
            .testTag("faction_card_${nation.name}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                if (isSelected) listOf(accentColor, MatrixGreen)
                else listOf(accentColor.copy(alpha = 0.6f), SurfaceBorder)
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Card Header: Flag & Nation Title & Starting Era Badge
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = nation.flagEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = nation.title,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Starting Era Chip
                    Surface(
                        color = accentColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor)
                    ) {
                        Text(
                            text = faction.startingEra.title.split("(").first().take(14),
                            color = accentColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Asymmetric Starting Resources Strip
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Altın", color = TextMuted, fontSize = 8.sp)
                            Text(text = faction.startingGold.toInt().toString(), color = AmberGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Teknoloji", color = TextMuted, fontSize = 8.sp)
                            Text(text = "${faction.startingTechPoints.toInt()} TP", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Enerji", color = TextMuted, fontSize = 8.sp)
                            Text(text = "${faction.startingEnergyProduced} MW", color = EnergyYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Faction Passive Doctrine Box
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = accentColor, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = faction.factionPassive,
                            color = accentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = faction.passiveDescription,
                        color = TextSecondary,
                        fontSize = 9.sp,
                        lineHeight = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Unique Signature Arsenal
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(text = "ÖZEL BİRİMLER & SİSTEMLER:", color = TextMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CarRepair, contentDescription = null, tint = AmberLight, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = nation.uniqueTankName, color = TextPrimary, fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AirplanemodeActive, contentDescription = null, tint = CyanGlow, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = nation.uniqueJetName, color = TextPrimary, fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DirectionsBoat, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = nation.uniqueSubmarineName, color = TextPrimary, fontSize = 9.sp)
                    }
                }
            }

            // Choose Button
            Button(
                onClick = onChoose,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MatrixGreen else accentColor
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("choose_faction_btn_${nation.name}")
            ) {
                if (isSelected) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "SEÇİLDİ • SEFERDE", color = SpaceBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text(text = "BU ÜLKEYLE BAŞLA", color = SpaceBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
