package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CommanderCatalog
import com.example.model.HistoricalCommander
import com.example.model.PassiveSkill
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
 * Clean, playable Commander Gallery Screen:
 * - Left pane: 2-column Grid of unlocked historical generals with status and ratings.
 * - Right pane: General dossier with detailed biography, doctrine quote, and an interactive 3-slot loadout system.
 */
@Composable
fun CommanderGalleryScreen(
    commanders: List<HistoricalCommander> = CommanderCatalog.createInitialCommanders(),
    activeCommanderId: String = "cmd_ataturk",
    onSelectActiveCommander: (String) -> Unit,
    onToggleEquipSkill: (commanderId: String, skillId: String) -> Unit,
    onClose: () -> Unit
) {
    var commanderList by remember(commanders) { mutableStateOf(commanders) }
    var selectedCommander by remember {
        mutableStateOf(commanderList.find { it.id == activeCommanderId } ?: commanderList.first())
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("commander_gallery_screen"),
        color = SpaceBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // ==================== CLEAN TOP HEADER ====================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(AmberGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Komutan Galerisi",
                            tint = AmberGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TARİHİ KOMUTAN GALERİSİ",
                                color = AmberGold,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MatrixGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "3 Doktrin Yuvası",
                                    color = MatrixGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Tarihi generalleri seçin, biyografilerini inceleyin ve savaşa girmeden önce 3 adet pasif yetenek kuşanın.",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_commander_gallery_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // ==================== TWO-COLUMN MASTER-DETAIL LAYOUT ====================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ==================== LEFT PANE: GRID OF HISTORICAL GENERALS ====================
                Column(
                    modifier = Modifier
                        .weight(1.25f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KOMUTAN KADROSU (IZGARA)",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${commanderList.count { it.isUnlocked }} Açık / ${commanderList.size} Toplam",
                            color = CyanNeon,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(commanderList) { cmd ->
                            val isSelected = selectedCommander.id == cmd.id
                            val isActive = activeCommanderId == cmd.id

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) SurfaceElevated else SpaceBlack.copy(alpha = 0.55f))
                                    .border(
                                        1.5.dp,
                                        if (isActive) AmberGold else if (isSelected) CyanNeon else SurfaceBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedCommander = cmd }
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Commander Portrait Circle
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    Brush.radialGradient(
                                                        listOf(
                                                            if (cmd.isUnlocked) AmberGold.copy(alpha = 0.35f) else SurfaceElevated,
                                                            SurfaceDark
                                                        )
                                                    )
                                                )
                                                .border(1.dp, if (cmd.isUnlocked) AmberGold else SurfaceBorder, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (cmd.isUnlocked) {
                                                Text(cmd.avatarEmoji, fontSize = 18.sp)
                                            } else {
                                                Icon(
                                                    Icons.Default.Lock,
                                                    contentDescription = "Kilitli",
                                                    tint = TextMuted,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = cmd.flagEmoji,
                                                    fontSize = 11.sp
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = cmd.name,
                                                    color = if (cmd.isUnlocked) TextPrimary else TextMuted,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Text(
                                                text = cmd.title,
                                                color = if (cmd.isUnlocked) CyanNeon else TextMuted,
                                                fontSize = 8.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row {
                                            repeat(cmd.starRating) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = AmberGold,
                                                    modifier = Modifier.size(9.dp)
                                                )
                                            }
                                        }

                                        if (isActive) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(AmberGold)
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text("AKTİF", color = SpaceBlack, fontSize = 7.sp, fontWeight = FontWeight.ExtraBold)
                                            }
                                        } else if (!cmd.isUnlocked) {
                                            Text("KİLİTLİ", color = DangerRed, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                                        } else {
                                            Text(
                                                text = "${cmd.equippedSkillIds.size}/3 Yuva",
                                                color = MatrixGreen,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ==================== RIGHT PANE: DETAIL VIEW & LOADOUT SLOT SYSTEM ====================
                Column(
                    modifier = Modifier
                        .weight(1.75f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(1.dp, AmberGold.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. General Header with Equip Button
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${selectedCommander.flagEmoji} ${selectedCommander.name}",
                                            color = AmberGold,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "• ${selectedCommander.eraTitle}",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                    Text(
                                        text = selectedCommander.title,
                                        color = CyanNeon,
                                        fontSize = 10.sp
                                    )
                                }

                                if (selectedCommander.isUnlocked) {
                                    val isCurrentlyActive = activeCommanderId == selectedCommander.id
                                    Button(
                                        onClick = { onSelectActiveCommander(selectedCommander.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isCurrentlyActive) MatrixGreen else AmberGold
                                        ),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("activate_commander_button")
                                    ) {
                                        if (isCurrentlyActive) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text("Başkomutan Seçili", color = SpaceBlack, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        } else {
                                            Text("Orduya Komutan Ata", color = SpaceBlack, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // 2. Biography & Strategic Doctrine Detail View
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpaceBlack.copy(alpha = 0.65f))
                                    .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "TARİHİ BİYOGRAFİ & DOKTRİN",
                                        color = TextSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = selectedCommander.biography,
                                        color = TextPrimary,
                                        fontSize = 10.sp,
                                        lineHeight = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "\"${selectedCommander.strategicDoctrine}\"",
                                        color = WarningOrange,
                                        fontSize = 9.sp,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        // 3. Loadout Slot System (3 Distinct Slots)
                        item {
                            Text(
                                text = "PASİF YETENEK LOADOUT YUVALARI (${selectedCommander.equippedSkillIds.size} / 3)",
                                color = MatrixGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                repeat(3) { slotIndex ->
                                    val equippedSkillId = selectedCommander.equippedSkillIds.getOrNull(slotIndex)
                                    val equippedSkill = selectedCommander.availableSkills.find { it.id == equippedSkillId }

                                    LoadoutSlotCard(
                                        slotNumber = slotIndex + 1,
                                        equippedSkill = equippedSkill,
                                        onUnequip = {
                                            if (equippedSkillId != null) {
                                                onToggleEquipSkill(selectedCommander.id, equippedSkillId)
                                                val updatedList = selectedCommander.equippedSkillIds.filter { it != equippedSkillId }
                                                selectedCommander = selectedCommander.copy(equippedSkillIds = updatedList)
                                                commanderList = commanderList.map { if (it.id == selectedCommander.id) selectedCommander else it }
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // 4. Available Ability Modifiers List
                        item {
                            Text(
                                text = "KULLANILABİLİR PASİF YETENEK DOKTRİNLERİ",
                                color = CyanNeon,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        items(selectedCommander.availableSkills) { skill ->
                            val isEquipped = selectedCommander.equippedSkillIds.contains(skill.id)

                            Surface(
                                color = if (isEquipped) MatrixGreen.copy(alpha = 0.12f) else SpaceBlack.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isEquipped) MatrixGreen else SurfaceBorder
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(skill.iconEmoji, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = skill.name,
                                                    color = if (isEquipped) MatrixGreen else TextPrimary,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = skill.combatBonusText,
                                                    color = AmberGold,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                            Text(
                                                text = skill.description,
                                                color = TextMuted,
                                                fontSize = 8.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            onToggleEquipSkill(selectedCommander.id, skill.id)
                                            val currentList = selectedCommander.equippedSkillIds.toMutableList()
                                            if (isEquipped) {
                                                currentList.remove(skill.id)
                                            } else if (currentList.size < 3) {
                                                currentList.add(skill.id)
                                            }
                                            val updated = selectedCommander.copy(equippedSkillIds = currentList)
                                            selectedCommander = updated
                                            commanderList = commanderList.map { if (it.id == updated.id) updated else it }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isEquipped) DangerRed.copy(alpha = 0.8f) else CyanNeon
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier
                                            .height(26.dp)
                                            .testTag("toggle_skill_${skill.id}")
                                    ) {
                                        Text(
                                            text = if (isEquipped) "Çıkar" else "Kuşan",
                                            color = SpaceBlack,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Status
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Aktif Doktrinler: ${selectedCommander.equippedSkillIds.size}/3 Yuva Donatıldı",
                                color = TextSecondary,
                                fontSize = 9.sp
                            )
                            Text(
                                text = if (selectedCommander.equippedSkillIds.isNotEmpty()) "⚡ Doktrinler Aktif" else "⚠️ Yuvalar Boş",
                                color = if (selectedCommander.equippedSkillIds.isNotEmpty()) MatrixGreen else WarningOrange,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadoutSlotCard(
    slotNumber: Int,
    equippedSkill: PassiveSkill?,
    onUnequip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (equippedSkill != null) MatrixGreen.copy(alpha = 0.10f) else SpaceBlack.copy(alpha = 0.45f))
            .border(
                1.dp,
                if (equippedSkill != null) MatrixGreen else SurfaceBorder,
                RoundedCornerShape(8.dp)
            )
            .padding(6.dp)
    ) {
        if (equippedSkill != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("YUVA $slotNumber", color = TextMuted, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(DangerRed.copy(alpha = 0.2f))
                            .clickable { onUnequip() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Kaldır", tint = DangerRed, modifier = Modifier.size(10.dp))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(equippedSkill.iconEmoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = equippedSkill.name,
                        color = MatrixGreen,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = equippedSkill.combatBonusText,
                    color = AmberGold,
                    fontSize = 7.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.height(2.dp))
                Text("Yuva $slotNumber (Boş)", color = TextMuted, fontSize = 8.sp)
            }
        }
    }
}
