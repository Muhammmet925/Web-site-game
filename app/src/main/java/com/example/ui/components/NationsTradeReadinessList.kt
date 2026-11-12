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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.model.Nation
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

enum class DiplomacyFilterTab(val title: String, val icon: String) {
    ALL("Tümü (19)", "🌐"),
    TRADE_READY("Ticarete Açık (≥+50)", "🔓"),
    ALLIES("Müttefikler (≥+75)", "⭐"),
    EMBARGOED("Ambargolu (<+50)", "🔒")
}

/**
 * Dedicated UI Component:
 * - Lists the 19 other nations with their bilateral relationship scores (-100 to +100).
 * - Provides striking visual feedback for Trade Readiness when relationship score exceeds +50.
 * - Highlights available export equipment, specs, and direct import actions.
 * - Includes diplomatic gift actions (+25 score) to quickly reach trade readiness.
 */
@Composable
fun NationsTradeReadinessComponent(
    allNations: List<DiplomaticNation>,
    playerNation: Nation,
    currentGold: Double,
    currentOil: Double,
    onImproveDiplomacy: (String) -> Unit,
    onPurchaseHardware: (DiplomaticNation) -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(DiplomacyFilterTab.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter out the player's nation so exactly the 19 other nations are presented
    val other19Nations = remember(allNations, playerNation) {
        allNations.filter { nation ->
            val isPlayer = nation.name.equals(playerNation.title, ignoreCase = true) ||
                           nation.id.equals(playerNation.name, ignoreCase = true) ||
                           (playerNation == Nation.TURKEY && (nation.id == "turkey" || nation.name == "Türkiye")) ||
                           (playerNation == Nation.USA && (nation.id == "usa" || nation.name == "ABD")) ||
                           (playerNation == Nation.RUSSIA && (nation.id == "russia" || nation.name == "Rusya")) ||
                           (playerNation == Nation.CHINA && (nation.id == "china" || nation.name == "Çin"))
            !isPlayer
        }
    }

    // Filter by tab and search query
    val displayedNations = remember(other19Nations, selectedTab, searchQuery) {
        other19Nations.filter { nation ->
            val matchesTab = when (selectedTab) {
                DiplomacyFilterTab.ALL -> true
                DiplomacyFilterTab.TRADE_READY -> nation.relationshipScore >= 50
                DiplomacyFilterTab.ALLIES -> nation.relationshipScore >= 75
                DiplomacyFilterTab.EMBARGOED -> nation.relationshipScore < 50
            }
            val matchesSearch = searchQuery.isBlank() ||
                nation.name.contains(searchQuery, ignoreCase = true) ||
                nation.exportUnitName.contains(searchQuery, ignoreCase = true) ||
                nation.exportWeaponSystem.contains(searchQuery, ignoreCase = true)

            matchesTab && matchesSearch
        }
    }

    val tradeReadyCount = remember(other19Nations) { other19Nations.count { it.relationshipScore >= 50 } }
    val embargoCount = other19Nations.size - tradeReadyCount

    // Pulsing transition for trade-ready glow
    val infiniteTransition = rememberInfiniteTransition(label = "trade_readiness_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(14.dp),
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
                    Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "KÜRESEL DİPLOMASİ & TİCARET HAZIRLIK PANELİ",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = SurfaceElevated,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "19 DİĞER ÜLKE",
                                    color = AmberGold,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Ticaret İzni İçin Minimum +50 İtibar Puanı Gereklidir",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                    }
                }

                // Balance Summary & Optional Close Button
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
                            Text(text = "💰 ${currentGold.toInt()} Altın", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "🛢️ ${currentOil.toInt()} Petrol", color = Color(0xFFF59E0B), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (onClose != null) {
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.size(28.dp).testTag("close_nations_readiness_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Summary Metric Bar (Trade-Ready vs Embargoed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Metric 1: Trade Ready Count
                Surface(
                    color = MatrixGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(MatrixGreen.copy(alpha = 0.6f), CyanNeon.copy(alpha = 0.3f)))
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "$tradeReadyCount ÜLKE TİCARETE AÇIK", color = MatrixGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "İlişki Skoru ≥ +50 • Doğrudan İthalat İzni Verildi", color = TextSecondary, fontSize = 7.sp)
                        }
                    }
                }

                // Metric 2: Embargoed Count
                Surface(
                    color = LaserCrimson.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(8.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(LaserCrimson.copy(alpha = 0.5f), SurfaceBorder))
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "$embargoCount ÜLKE AMBARGOLU", color = DangerRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(text = "İlişki Skoru < +50 • Elçi/Hediye Göndererek Kilidi Açın", color = TextSecondary, fontSize = 7.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Tabs & Search Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Tabs
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(DiplomacyFilterTab.values()) { tab ->
                        val isSelected = selectedTab == tab
                        Surface(
                            color = if (isSelected) CyanNeon else SurfaceElevated,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.clickable { selectedTab = tab }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = tab.icon, fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tab.title,
                                    color = if (isSelected) SpaceBlack else TextPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Ülke veya Silah Ara...", fontSize = 8.sp, color = TextMuted) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated,
                        focusedBorderColor = CyanNeon,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.width(180.dp).height(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // The 19 Nations List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(displayedNations, key = { it.id }) { nation ->
                    NationTradeReadinessCard(
                        nation = nation,
                        currentGold = currentGold,
                        currentOil = currentOil,
                        pulseGlow = pulseGlow,
                        onImproveDiplomacy = { onImproveDiplomacy(nation.id) },
                        onPurchase = { onPurchaseHardware(nation) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NationTradeReadinessCard(
    nation: DiplomaticNation,
    currentGold: Double,
    currentOil: Double,
    pulseGlow: Float,
    onImproveDiplomacy: () -> Unit,
    onPurchase: () -> Unit
) {
    val isTradeReady = nation.relationshipScore >= 50
    val canAffordGift = currentGold >= 350
    val canAffordHardware = currentGold >= nation.exportCostGold && currentOil >= nation.exportCostOil
    val canPurchase = isTradeReady && canAffordHardware

    val relationColor = when {
        nation.relationshipScore >= 75 -> MatrixGreen
        nation.relationshipScore >= 50 -> CyanNeon
        nation.relationshipScore >= 0 -> AmberGold
        else -> DangerRed
    }

    val cardBorder = if (isTradeReady) {
        CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(MatrixGreen.copy(alpha = pulseGlow), CyanNeon.copy(alpha = pulseGlow * 0.8f))
            )
        )
    } else {
        CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceBorder))
        )
    }

    Surface(
        color = if (isTradeReady) SurfaceElevated.copy(alpha = 0.90f) else SurfaceElevated.copy(alpha = 0.65f),
        shape = RoundedCornerShape(10.dp),
        border = cardBorder,
        shadowElevation = if (isTradeReady) 6.dp else 2.dp,
        modifier = Modifier.fillMaxWidth().testTag("nation_trade_card_${nation.id}")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nation Rank & Flag Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(52.dp)
            ) {
                Surface(
                    color = if (isTradeReady) MatrixGreen.copy(alpha = 0.2f) else SurfaceDark,
                    shape = RoundedCornerShape(4.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceBorder)))
                ) {
                    Text(
                        text = "#${nation.militaryRank}",
                        color = if (isTradeReady) MatrixGreen else AmberGold,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = nation.flagEmoji, fontSize = 24.sp)
                Text(text = nation.name, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Main Info Column
            Column(modifier = Modifier.weight(1f)) {
                // Top Row: Weapon Name + Trade Readiness Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = nation.exportUnitName,
                            color = if (isTradeReady) Color(0xFF00F5D4) else TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${nation.exportUnitType.title.split("/").first()})",
                            color = TextSecondary,
                            fontSize = 8.sp
                        )
                    }

                    // TRADE READINESS VISUAL BADGE (CRITICAL REQUIREMENT)
                    if (isTradeReady) {
                        Surface(
                            color = MatrixGreen.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(MatrixGreen, CyanNeon))
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(MatrixGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "🔓 TİCARETE AÇIK (HAZIR)",
                                    color = MatrixGreen,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Surface(
                            color = LaserCrimson.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(6.dp),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.horizontalGradient(listOf(LaserCrimson, SurfaceBorder))
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = DangerRed, modifier = Modifier.size(9.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "🔒 TİCARET KİLİTLİ (GEREKLİ: +50)",
                                    color = DangerRed,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${nation.exportWeaponSystem} • ${nation.eraSpecialty}",
                    color = TextSecondary,
                    fontSize = 8.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Relationship Score Progress Bar with +50 Milestone Marker
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "İlişki Skoru: ", color = TextSecondary, fontSize = 8.sp)
                            Text(
                                text = if (nation.relationshipScore > 0) "+${nation.relationshipScore}" else "${nation.relationshipScore}",
                                color = relationColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "• ${nation.relationshipStatus}", color = relationColor, fontSize = 8.sp)
                        }

                        if (!isTradeReady) {
                            val deficit = 50 - nation.relationshipScore
                            Text(
                                text = "⚠️ Ticaret için +$deficit Puan Gerekli",
                                color = WarningOrange,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "✅ İthalat Protokolü Onaylandı",
                                color = MatrixGreen,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Relative score mapped from -100..+100 to 0f..1f
                    val scoreNorm = ((nation.relationshipScore + 100f) / 200f).coerceIn(0f, 1f)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        LinearProgressIndicator(
                            progress = { scoreNorm },
                            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                            color = relationColor,
                            trackColor = SpaceBlack
                        )
                        // +50 Threshold Marker (which is at 75% of the -100..+100 bar)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 140.dp) // Visual anchor for 75%
                                .size(2.dp, 8.dp)
                                .background(Color(0xFF00F5D4))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Specifications Row: Damage, HP, Stealth, Cost
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saldırı: ${nation.exportAttackPower.toInt()} • Can: ${nation.exportHp.toInt()} • Gizlilik: %.2fx".format(nation.stealthMultiplier),
                        color = TextMuted,
                        fontSize = 7.sp
                    )
                    Text(
                        text = "Maliyet: ${nation.exportCostGold} Altın / ${nation.exportCostOil} Petrol",
                        color = AmberGold,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Buttons Column
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Diplomatic Gift Button (+25 Score)
                Button(
                    onClick = onImproveDiplomacy,
                    enabled = canAffordGift && nation.relationshipScore < 100,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(26.dp)
                        .border(1.dp, if (canAffordGift) AmberGold else SurfaceBorder, RoundedCornerShape(6.dp))
                        .testTag("diplomatic_gift_btn_${nation.id}")
                ) {
                    Icon(imageVector = Icons.Default.Handshake, contentDescription = null, tint = AmberGold, modifier = Modifier.size(10.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "+25 Elçi (350G)", color = AmberGold, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                }

                // Purchase Hardware Button (Active only when Trade Ready >= +50)
                Button(
                    onClick = onPurchase,
                    enabled = canPurchase,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canPurchase) Color(0xFF00F5D4) else SurfaceDark.copy(alpha = 0.5f),
                        disabledContainerColor = SurfaceDark.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(28.dp)
                        .testTag("purchase_hardware_btn_${nation.id}")
                ) {
                    Icon(
                        imageVector = when (nation.exportUnitType) {
                            UnitType.JET -> Icons.Default.AirplanemodeActive
                            UnitType.TANK, UnitType.SIEGE -> Icons.Default.CarRepair
                            UnitType.SUBMARINE -> Icons.Default.DirectionsBoat
                            else -> Icons.Default.Security
                        },
                        contentDescription = null,
                        tint = if (canPurchase) SpaceBlack else TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (isTradeReady) "Sipariş Ver" else "Ambargo (≥+50)",
                        color = if (canPurchase) SpaceBlack else DangerRed,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
