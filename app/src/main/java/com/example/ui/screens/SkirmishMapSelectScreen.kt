package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Era
import com.example.model.SkirmishMap
import com.example.model.SkirmishMapCatalog
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

enum class MapEraFilter(val title: String) {
    ALL("Tüm Haritalar (6)"),
    MEDIEVAL("5. Yüzyıl"),
    CLASSICAL("Klasik & Barut"),
    INDUSTRIAL("Endüstri Devrimi"),
    MODERN("Modern Çağ")
}

/**
 * Historical Geographic Map Selection Screen:
 * - Allows players to choose between 6 distinct historical geographic maps for their skirmish battles.
 * - Displays tactical terrain modifiers, recommended battle doctrines, and strategic base coordinates.
 */
@Composable
fun SkirmishMapSelectScreen(
    maps: List<SkirmishMap> = SkirmishMapCatalog.maps,
    selectedMapId: String,
    onSelectMap: (SkirmishMap) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(MapEraFilter.ALL) }

    val filteredMaps = remember(maps, selectedFilter) {
        when (selectedFilter) {
            MapEraFilter.ALL -> maps
            MapEraFilter.MEDIEVAL -> maps.filter { it.historicalEra == Era.MEDIEVAL_5TH }
            MapEraFilter.CLASSICAL -> maps.filter { it.historicalEra == Era.CLASSICAL_GUNPOWDER }
            MapEraFilter.INDUSTRIAL -> maps.filter { it.historicalEra == Era.INDUSTRIAL }
            MapEraFilter.MODERN -> maps.filter { it.historicalEra == Era.MODERN }
        }
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
                            .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Map, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TARİHİ COĞRAFİ SAVAŞ HARİTALARI",
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
                                    text = "SKIRMISH SEÇİMİ",
                                    color = AmberGold,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Farklı dönemlerin coğrafi arazilerinde ve taktik koşullarında üs kurun.",
                            color = TextSecondary,
                            fontSize = 8.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(28.dp).testTag("close_map_select_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Historical Era Filter Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(MapEraFilter.values()) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        color = if (isSelected) Color(0xFF38BDF8) else SurfaceElevated,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.clickable { selectedFilter = filter }
                    ) {
                        Text(
                            text = filter.title,
                            color = if (isSelected) SpaceBlack else TextPrimary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Map Cards List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(filteredMaps, key = { it.id }) { skirmishMap ->
                    val isSelected = skirmishMap.id == selectedMapId
                    SkirmishMapCard(
                        map = skirmishMap,
                        isSelected = isSelected,
                        onSelect = { onSelectMap(skirmishMap) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SkirmishMapCard(
    map: SkirmishMap,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val accentColor = Color(map.accentColorHex)

    Surface(
        color = if (isSelected) SurfaceElevated.copy(alpha = 0.95f) else SurfaceElevated.copy(alpha = 0.65f),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isSelected) listOf(accentColor, CyanNeon)
                else listOf(SurfaceBorder, SurfaceBorder)
            )
        ),
        shadowElevation = if (isSelected) 8.dp else 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("skirmish_map_card_${map.id}")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Map Ambient Icon & Era Badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(1.dp, if (isSelected) accentColor else SurfaceBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = map.ambientIcon, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Surface(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = map.historicalEra.title.split("(").first().take(9),
                        color = accentColor,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Main Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = map.title,
                        color = if (isSelected) accentColor else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (isSelected) {
                        Surface(
                            color = MatrixGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "AKTİF HARİTA", color = MatrixGreen, fontSize = 7.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Text(
                    text = "${map.geographicRegion} • ${map.terrainTheme}",
                    color = TextSecondary,
                    fontSize = 8.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = map.description,
                    color = TextSecondary,
                    fontSize = 8.sp,
                    maxLines = 2,
                    lineHeight = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tactical Trait
                Surface(
                    color = accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.4f), SurfaceBorder)))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = accentColor, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Taktik Avantaj: ${map.tacticalTrait}",
                            color = accentColor,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Terrain Modifiers Chips
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    map.terrainModifiers.forEach { mod ->
                        Surface(
                            color = SurfaceDark,
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "• $mod",
                                color = TextMuted,
                                fontSize = 7.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Button
            Button(
                onClick = onSelect,
                enabled = !isSelected,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    disabledContainerColor = SurfaceDark.copy(alpha = 0.5f)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .height(30.dp)
                    .testTag("select_map_btn_${map.id}")
            ) {
                Text(
                    text = if (isSelected) "Seçili" else "Haritayı Seç",
                    color = if (isSelected) TextMuted else SpaceBlack,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
