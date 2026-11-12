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
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import com.example.model.DiplomaticNation
import com.example.model.GlobalTradeDeal
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
 * Global Military Market (Askeri Ticaret Pazarı):
 * - Buy high-tech foreign equipment from the top 20 military superpowers.
 * - Manage diplomatic relationship scores (-100 to +100).
 * - Minimum +50 relationship required to bypass military trade embargos.
 * - International logistics shipping countdowns with real-time progress.
 */
@Composable
fun MilitaryMarketScreen(
    nations: List<DiplomaticNation>,
    activeDeals: List<GlobalTradeDeal>,
    currentGold: Double,
    currentOil: Double,
    onImproveDiplomacy: (String) -> Unit,
    onPurchaseHardware: (DiplomaticNation) -> Unit,
    onCancelDeal: (String) -> Unit,
    onClose: () -> Unit,
    playerNation: com.example.model.Nation = com.example.model.Nation.TURKEY,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredNations = remember(nations, searchQuery) {
        if (searchQuery.isBlank()) nations
        else nations.filter { it.name.contains(searchQuery, ignoreCase = true) || it.exportUnitName.contains(searchQuery, ignoreCase = true) }
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
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "KÜRESEL ASKERİ TİCARET PAZARI (20 SÜPER GÜÇ)",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Diplomasi & İthalat • F-35, Su-57, K2 Black Panther, Leopard 2A7+",
                            color = TextSecondary,
                            fontSize = 9.sp
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
                            Text(text = "💰 ${currentGold.toInt()}G", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "🛢️ ${currentOil.toInt()}P", color = Color(0xFFF59E0B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(28.dp).testTag("close_military_market_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Active Logistics Deliveries Section (If any shipment in-transit)
            if (activeDeals.isNotEmpty()) {
                Surface(
                    color = SurfaceElevated.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(10.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.6f), CyanNeon.copy(alpha = 0.4f)))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = AmberGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ULUSLARARASI LOJİSTİK SEVKİYATLAR (${activeDeals.size} Sipariş Yolda)",
                                color = TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(activeDeals, key = { it.dealId }) { deal ->
                                InTransitDealCard(
                                    deal = deal,
                                    onCancel = { onCancelDeal(deal.dealId) }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // 19 Nations Superpower Catalog with Trade Readiness Indicators
            com.example.ui.components.NationsTradeReadinessComponent(
                allNations = nations,
                playerNation = playerNation,
                currentGold = currentGold,
                currentOil = currentOil,
                onImproveDiplomacy = onImproveDiplomacy,
                onPurchaseHardware = onPurchaseHardware,
                onClose = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun InTransitDealCard(
    deal: GlobalTradeDeal,
    onCancel: () -> Unit
) {
    val progress = (1f - (deal.deliveryTimeRemaining / deal.totalDeliveryTime)).coerceIn(0f, 1f)

    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.5f), SurfaceBorder))
        ),
        modifier = Modifier.width(220.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = deal.sellerFlag, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = deal.unitName, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DangerRed.copy(alpha = 0.2f))
                        .clickable { onCancel() }
                        .testTag("cancel_deal_${deal.dealId}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "İptal", tint = DangerRed, modifier = Modifier.size(10.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(1.5.dp)),
                color = AmberGold,
                trackColor = SpaceBlack
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Kargo Yolda", color = TextMuted, fontSize = 7.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(8.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "%.1fs".format(deal.deliveryTimeRemaining),
                        color = MatrixGreen,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SuperpowerNationCard(
    nation: DiplomaticNation,
    currentGold: Double,
    currentOil: Double,
    onImproveDiplomacy: () -> Unit,
    onPurchase: () -> Unit
) {
    val canAffordGift = currentGold >= 350
    val canAffordHardware = currentGold >= nation.exportCostGold && currentOil >= nation.exportCostOil
    val canBuy = nation.isAllianceReady && canAffordHardware

    val relationColor = when {
        nation.relationshipScore >= 75 -> MatrixGreen
        nation.relationshipScore >= 50 -> CyanNeon
        nation.relationshipScore >= 0 -> AmberGold
        else -> DangerRed
    }

    Surface(
        color = SurfaceElevated.copy(alpha = 0.75f),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (nation.isAllianceReady) listOf(CyanNeon.copy(alpha = 0.4f), SurfaceBorder)
                else listOf(SurfaceBorder, SurfaceBorder)
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Badge & Flag
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(46.dp)
            ) {
                Text(text = "#${nation.militaryRank}", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(text = nation.flagEmoji, fontSize = 22.sp)
                Text(text = nation.name, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Details and Diplomacy Progress
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nation.exportUnitName,
                        color = if (nation.isAllianceReady) AmberLight else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = nation.relationshipStatus,
                        color = relationColor,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${nation.exportWeaponSystem} • ${nation.eraSpecialty}",
                    color = TextSecondary,
                    fontSize = 8.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Diplomatic Relationship Meter (-100 to +100 mapped to 0f..1f)
                val relationNorm = ((nation.relationshipScore + 100f) / 200f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { relationNorm },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = relationColor,
                    trackColor = SpaceBlack
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Specs: Attack, HP, Stealth, Cost
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Saldırı: ${nation.exportAttackPower.toInt()} • Can: ${nation.exportHp.toInt()}", color = TextMuted, fontSize = 7.sp)
                    Text(
                        text = "Gizlilik: %.2fx • Maliyet: %dG / %dP".format(nation.stealthMultiplier, nation.exportCostGold, nation.exportCostOil),
                        color = AmberGold,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Diplomatic Gift Button
                Button(
                    onClick = onImproveDiplomacy,
                    enabled = canAffordGift && nation.relationshipScore < 100,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(24.dp)
                        .border(1.dp, if (canAffordGift) AmberGold else SurfaceBorder, RoundedCornerShape(6.dp))
                        .testTag("diplomacy_gift_${nation.id}")
                ) {
                    Icon(imageVector = Icons.Default.Handshake, contentDescription = null, tint = AmberGold, modifier = Modifier.size(10.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "+25 Elçi (350G)", color = AmberGold, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                }

                // Purchase Hardware Button
                Button(
                    onClick = onPurchase,
                    enabled = canBuy,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canBuy) CyanNeon else SurfaceDark.copy(alpha = 0.5f),
                        disabledContainerColor = SurfaceDark.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(26.dp)
                        .testTag("buy_hardware_${nation.id}")
                ) {
                    Icon(
                        imageVector = getUnitIcon(nation.exportUnitType),
                        contentDescription = null,
                        tint = if (canBuy) SpaceBlack else TextMuted,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (nation.isAllianceReady) "Sipariş Et" else "Ambargo (≥50)",
                        color = if (canBuy) SpaceBlack else DangerRed,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun getUnitIcon(type: UnitType): ImageVector {
    return when (type) {
        UnitType.INFANTRY -> Icons.Default.Security
        UnitType.TANK -> Icons.Default.CarRepair
        UnitType.JET -> Icons.Default.AirplanemodeActive
        UnitType.SUBMARINE -> Icons.Default.DirectionsBoat
        UnitType.SIEGE -> Icons.Default.PrecisionManufacturing
    }
}
