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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CarRepair
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.model.RtsBuildingType
import com.example.model.RtsCatalog
import com.example.model.RtsUnitTemplate
import com.example.model.UnitType
import com.example.simulation.RtsUiState
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EnergyYellow
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseGameScreen(
    state: RtsUiState,
    onSelectBuilding: (RtsBuilding?) -> Unit,
    onEnqueueUnit: (String, RtsUnitTemplate) -> Unit,
    onCancelOrder: (String, Int) -> Unit,
    onUpgradeEra: () -> Unit,
    onSelectNation: (Nation) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Nation & Era Header
        RtsNationAndEraCard(
            selectedNation = state.selectedNation,
            currentEra = state.currentEra,
            gold = state.resources.gold,
            tech = state.resources.techPoints,
            onSelectNation = onSelectNation,
            onUpgradeEra = onUpgradeEra
        )

        // Building Grid Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "C&C GENERALS ÜS İNŞASI & ÜRETİM TESİSLERİ",
                    color = CyanNeon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Üretim kuyruğunu açmak için bir fabrikaya dokunun (Maks. 5 Sipariş)",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${state.buildings.size} Tesis Aktif",
                    color = AmberGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // C&C Generals Production Facilities Grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            state.buildings.chunked(2).forEach { rowBuildings ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowBuildings.forEach { building ->
                        Box(modifier = Modifier.weight(1f)) {
                            RtsBuildingCard(
                                building = building,
                                efficiency = state.resources.energyEfficiency,
                                onClick = { onSelectBuilding(building) }
                            )
                        }
                    }
                    if (rowBuildings.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Tactical Constraint Info Box
        TacticalConstraintCard()
    }

    // Modal Sheet for Building Production Queue
    if (state.selectedBuilding != null) {
        ModalBottomSheet(
            onDismissRequest = { onSelectBuilding(null) },
            sheetState = sheetState,
            containerColor = SurfaceDark
        ) {
            ProductionQueueSheet(
                building = state.selectedBuilding,
                currentEra = state.currentEra,
                nation = state.selectedNation,
                gold = state.resources.gold,
                onEnqueue = { template -> onEnqueueUnit(state.selectedBuilding.id, template) },
                onCancelOrder = { index -> onCancelOrder(state.selectedBuilding.id, index) },
                onClose = { onSelectBuilding(null) }
            )
        }
    }
}

@Composable
private fun RtsNationAndEraCard(
    selectedNation: Nation,
    currentEra: Era,
    gold: Double,
    tech: Double,
    onSelectNation: (Nation) -> Unit,
    onUpgradeEra: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.5f), AmberGold.copy(alpha = 0.4f)))
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Nation Selector Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SEÇİLİ ÜLKE DOKTRİNİ",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Nation.values().forEach { nation ->
                        val isSelected = nation == selectedNation
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) CyanNeon.copy(alpha = 0.25f) else SurfaceElevated)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) CyanNeon else SurfaceBorder,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { onSelectNation(nation) }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                .testTag("select_nation_${nation.name}")
                        ) {
                            Text(
                                text = "${nation.flagEmoji} ${nation.title}",
                                color = if (isSelected) CyanNeon else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Era Banner & Upgrade Button
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = currentEra.title, color = MatrixGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = currentEra.description,
                            color = TextSecondary,
                            fontSize = 10.sp,
                            maxLines = 2,
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val nextEra = when (currentEra) {
                        Era.MEDIEVAL_5TH -> Era.CLASSICAL_GUNPOWDER
                        Era.CLASSICAL_GUNPOWDER -> Era.INDUSTRIAL
                        Era.INDUSTRIAL -> Era.MODERN
                        Era.MODERN -> null
                    }

                    if (nextEra != null) {
                        val canAfford = gold >= nextEra.upgradeCostGold && tech >= nextEra.upgradeCostTech
                        Button(
                            onClick = onUpgradeEra,
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("upgrade_era_button")
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Çağ Atla ➔", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "${nextEra.upgradeCostGold}A / ${nextEra.upgradeCostTech}TP",
                                    color = SpaceBlack.copy(alpha = 0.8f),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MatrixGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Son Çağ", color = MatrixGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RtsBuildingCard(
    building: RtsBuilding,
    efficiency: Float,
    onClick: () -> Unit
) {
    val icon = getBuildingIcon(building.type)
    val color = getBuildingColor(building.type)
    val activeOrder = building.queue.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("building_card_${building.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (activeOrder != null) listOf(AmberGold.copy(alpha = 0.6f), SurfaceBorder)
                else listOf(SurfaceBorder, SurfaceElevated)
            )
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }

                // Queue Counter Badge
                if (building.queue.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AmberGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${building.queue.size}/5 Kuyruk",
                            color = SpaceBlack,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = building.type.title.split("(").first(),
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = when (building.type) {
                    RtsBuildingType.FACTORY -> "Sadece Tank Üretir"
                    RtsBuildingType.AIRFIELD -> "Sadece Jet Üretir"
                    RtsBuildingType.SHIPYARD -> "Sadece Denizaltı Üretir"
                    RtsBuildingType.BARRACKS -> "Sadece Piyade Eğitir"
                    RtsBuildingType.POWER_PLANT -> "+150 MW Enerji Üretir"
                    RtsBuildingType.SUPPLY_DEPOT -> "+35 Altın/s Akışı"
                    RtsBuildingType.HQ -> "Operasyon & Dönem Geçişi"
                },
                color = TextSecondary,
                fontSize = 9.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Active Queue Progress Bar
            if (activeOrder != null) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = activeOrder.unitTemplate.name.split(" ").first(),
                            color = AmberLight,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "%.1fs".format(activeOrder.timeRemaining),
                            color = AmberGold,
                            fontSize = 9.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    LinearProgressIndicator(
                        progress = { 1f - (activeOrder.timeRemaining / activeOrder.totalTime) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = AmberGold,
                        trackColor = SurfaceElevated
                    )
                }
            } else if (building.type.allowedUnitTypes.isNotEmpty()) {
                Text(
                    text = "• Boşta (Üretim İçin Dokun)",
                    color = CyanNeon,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ProductionQueueSheet(
    building: RtsBuilding,
    currentEra: Era,
    nation: Nation,
    gold: Double,
    onEnqueue: (RtsUnitTemplate) -> Unit,
    onCancelOrder: (Int) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Sheet Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = building.type.title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kısıtlama: ${building.type.description}",
                    color = AmberGold,
                    fontSize = 11.sp
                )
            }
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // C&C GENERALS 5-SLOT PRODUCTION QUEUE TRAY
        Text(
            text = "ÜRETİM SİPARİŞ KUYRUĞU (${building.queue.size}/5)",
            color = CyanNeon,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in 0 until 5) {
                val order = building.queue.getOrNull(i)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (order != null) SurfaceElevated else SurfaceDark.copy(alpha = 0.5f))
                        .border(
                            width = 1.dp,
                            color = if (i == 0 && order != null) AmberGold else SurfaceBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(4.dp)
                ) {
                    if (order != null) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "#${i + 1}", color = TextMuted, fontSize = 8.sp)
                                // Cancel button
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "İptal Et",
                                    tint = DangerRed,
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clickable { onCancelOrder(i) }
                                )
                            }
                            Text(
                                text = order.unitTemplate.name.split(" ").first(),
                                color = TextPrimary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            if (i == 0) {
                                LinearProgressIndicator(
                                    progress = { 1f - (order.timeRemaining / order.totalTime) },
                                    modifier = Modifier.fillMaxWidth().height(2.dp),
                                    color = AmberGold,
                                    trackColor = SpaceBlack
                                )
                            } else {
                                Text(text = "Bekliyor", color = AmberLight, fontSize = 7.sp)
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Boş", color = TextMuted, fontSize = 8.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AVAILABLE UNITS FOR THIS BUILDING & CURRENT ERA
        Text(
            text = "ÜRETİLEBİLİR BİRİMLER (${currentEra.name})",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        val availableUnits = RtsCatalog.getUnitTemplates().filter { template ->
            building.type.allowedUnitTypes.contains(template.unitType) &&
            template.era == currentEra &&
            (template.nation == nation || currentEra != Era.MODERN)
        }

        if (availableUnits.isEmpty()) {
            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bu çağda (${currentEra.title}) bu tesis türü için üretilebilir birim bulunmuyor. Çağ atlayın veya diğer tesisleri kullanın.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                availableUnits.forEach { unitTemplate ->
                    val canAfford = gold >= unitTemplate.costGold
                    val hasQueueSpace = building.queue.size < 5

                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = canAfford && hasQueueSpace) { onEnqueue(unitTemplate) }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(CyanNeon.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getUnitIcon(unitTemplate.unitType),
                                    contentDescription = null,
                                    tint = CyanNeon,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = unitTemplate.name,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = unitTemplate.description,
                                    color = TextSecondary,
                                    fontSize = 9.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "Maliyet: ${unitTemplate.costGold} Altın", color = if (canAfford) AmberGold else DangerRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Süre: ${unitTemplate.productionTimeSeconds}s", color = CyanNeon, fontSize = 10.sp)
                                    Text(text = "HP: ${unitTemplate.maxHp.toInt()}", color = MatrixGreen, fontSize = 10.sp)
                                    Text(text = "Saldırı: ${unitTemplate.attackPower.toInt()}", color = DangerRed, fontSize = 10.sp)
                                }
                            }

                            Button(
                                onClick = { onEnqueue(unitTemplate) },
                                enabled = canAfford && hasQueueSpace,
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("enqueue_unit_${unitTemplate.id}")
                            ) {
                                Text(text = "Üret", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TacticalConstraintCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.4f), SurfaceBorder)))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "C&C Generals Mimari İlkesi: Tip Güvenli Üretim",
                    color = CyanNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• Tank Fabrikası (War Factory) yalnızca zırhlı araçları (M1 Abrams, Altay, T-90);\n• Hava Üssü (Airfield) yalnızca jetleri (F-22, KAAN, Su-57);\n• Tersane (Shipyard) ise yalnızca denizaltı ve gemileri üretir. C# nesne yönelimli miras (OOP) mimarisi bu kısıtlamaları derleme ve çalışma anında garanti eder.",
                color = TextSecondary,
                fontSize = 10.sp,
                lineHeight = 15.sp
            )
        }
    }
}

private fun getBuildingIcon(type: RtsBuildingType): ImageVector {
    return when (type) {
        RtsBuildingType.HQ -> Icons.Default.Security
        RtsBuildingType.POWER_PLANT -> Icons.Default.Bolt
        RtsBuildingType.SUPPLY_DEPOT -> Icons.Default.Inventory2
        RtsBuildingType.BARRACKS -> Icons.Default.Person
        RtsBuildingType.FACTORY -> Icons.Default.CarRepair
        RtsBuildingType.AIRFIELD -> Icons.Default.AirplanemodeActive
        RtsBuildingType.SHIPYARD -> Icons.Default.DirectionsBoat
    }
}

private fun getBuildingColor(type: RtsBuildingType): Color {
    return when (type) {
        RtsBuildingType.HQ -> CyanNeon
        RtsBuildingType.POWER_PLANT -> EnergyYellow
        RtsBuildingType.SUPPLY_DEPOT -> AmberGold
        RtsBuildingType.BARRACKS -> MatrixGreen
        RtsBuildingType.FACTORY -> AmberLight
        RtsBuildingType.AIRFIELD -> CyanGlow
        RtsBuildingType.SHIPYARD -> Color(0xFF38BDF8)
    }
}

private fun getUnitIcon(type: UnitType): ImageVector {
    return when (type) {
        UnitType.INFANTRY -> Icons.Default.Person
        UnitType.TANK -> Icons.Default.CarRepair
        UnitType.JET -> Icons.Default.AirplanemodeActive
        UnitType.SUBMARINE -> Icons.Default.DirectionsBoat
        UnitType.SIEGE -> Icons.Default.PrecisionManufacturing
    }
}
