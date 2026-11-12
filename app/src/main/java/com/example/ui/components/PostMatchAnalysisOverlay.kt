package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Nation
import com.example.ui.theme.AmberGold
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

data class PostMatchStats(
    val isVictory: Boolean = true,
    val playerFaction: Nation = Nation.TURKEY,
    val mapTitle: String = "Hürmüz Boğazı & Basra Körfezi",
    val matchDurationSeconds: Long = 480L,
    val mapControlPercentage: Int = 78,
    val totalGoldHarvested: Double = 12500.0,
    val totalIronHarvested: Double = 5400.0,
    val totalOilExtracted: Double = 1800.0,
    val enemyUnitsDestroyed: Int = 22,
    val friendlyUnitsLost: Int = 5
) {
    val mapControlPoints: Long = (mapControlPercentage * 10L)
    val resourceExtractionPoints: Long = ((totalGoldHarvested + totalIronHarvested + totalOilExtracted) / 30.0).toLong()
    val unitDestructionPoints: Long = (enemyUnitsDestroyed * 40L - friendlyUnitsLost * 15L).coerceAtLeast(0L)
    val totalVictoryPoints: Long = mapControlPoints + resourceExtractionPoints + unitDestructionPoints
}

@Composable
fun PostMatchAnalysisOverlay(
    stats: PostMatchStats,
    resourceHistory: List<ResourceDataPoint> = emptyList(),
    isSubmittingScore: Boolean = false,
    submissionFeedback: String? = null,
    onSubmitScoreToLeaderboard: (PostMatchStats) -> Unit,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Genel Puan Özeti, 1: Ekonomi Grafiği

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("post_match_analysis_overlay"),
        color = SpaceBlack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (stats.isVictory) MatrixGreen.copy(alpha = 0.25f) else LaserCrimson.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Zafer",
                            tint = if (stats.isVictory) AmberGold else LaserCrimson,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (stats.isVictory) "MUZAFFERİYET (VICTORY)" else "TAKTİKSEL YENİLGİ (DEFEAT)",
                                color = if (stats.isVictory) MatrixGreen else DangerRed,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${stats.playerFaction.flagEmoji} ${stats.playerFaction.title}",
                                color = TextPrimary,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Harita: ${stats.mapTitle} • Süre: ${stats.matchDurationSeconds / 60}dk ${stats.matchDurationSeconds % 60}sn",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Tab switch
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceDark)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                            .padding(2.dp)
                    ) {
                        TabButton(
                            title = "Zafer Puanı Detayları",
                            icon = Icons.Default.Assessment,
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )
                        TabButton(
                            title = "Ekonomi Akış Grafiği",
                            icon = Icons.Default.ShowChart,
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_post_match_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
                    }
                }
            }

            // Main Content Area
            if (selectedTab == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Box: Big Victory Points Summary Card
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.verticalGradient(listOf(SurfaceDark, SurfaceElevated)))
                            .border(1.dp, AmberGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "KAZANILAN ZAFER PUANI (VP)",
                                color = AmberGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "+${stats.totalVictoryPoints}",
                                color = Color.White,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = if (stats.totalVictoryPoints >= 1500) "🌟 EFSANEVİ BAŞARI (Rütbe: Mareşal)" else "🎖️ ÜSTÜN HAREKÂT (Rütbe: Orgeneral)",
                                color = MatrixGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Formula explanation
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceBlack.copy(alpha = 0.6f))
                                .padding(10.dp)
                        ) {
                            Text("Hesaplama Formülü:", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("• Bölge Hakimiyeti: %${stats.mapControlPercentage} x 10 = +${stats.mapControlPoints} VP", color = CyanNeon, fontSize = 9.sp)
                            Text("• Kaynak Çıkarımı: (Altın+Demir+Petrol)/30 = +${stats.resourceExtractionPoints} VP", color = AmberGold, fontSize = 9.sp)
                            Text("• Düşman İmhası: (${stats.enemyUnitsDestroyed}x40 - ${stats.friendlyUnitsLost}x15) = +${stats.unitDestructionPoints} VP", color = MatrixGreen, fontSize = 9.sp)
                        }

                        // Submit to Firestore Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            submissionFeedback?.let { feedback ->
                                Text(
                                    text = feedback,
                                    color = MatrixGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }

                            Button(
                                onClick = { onSubmitScoreToLeaderboard(stats) },
                                enabled = !isSubmittingScore,
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_victory_points_button")
                            ) {
                                if (isSubmittingScore) {
                                    CircularProgressIndicator(
                                        color = SpaceBlack,
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Firestore'a Yazılıyor...", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = "Kaydet",
                                        tint = SpaceBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Skoru Liderlik Tablosuna Kaydet", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Right Box: 3 Core Pillars Breakdown
                    Column(
                        modifier = Modifier
                            .weight(1.4f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pillar 1: Map Control
                        PillarCard(
                            title = "BÖLGE HAKİMİYETİ (MAP CONTROL)",
                            icon = Icons.Default.Flag,
                            points = "+${stats.mapControlPoints} VP",
                            progress = stats.mapControlPercentage / 100f,
                            accentColor = CyanNeon,
                            detailText = "Harita Yüzölçümü Kontrolü: %${stats.mapControlPercentage} (Sektörler ve Petrol Kuyuları Güvencede)"
                        )

                        // Pillar 2: Resource Extraction
                        PillarCard(
                            title = "KAYNAK ÇIKARIMI (RESOURCE EXTRACTION)",
                            icon = Icons.Default.MonetizationOn,
                            points = "+${stats.resourceExtractionPoints} VP",
                            progress = 0.85f,
                            accentColor = AmberGold,
                            detailText = "Altın: ${stats.totalGoldHarvested.toInt()} • Demir: ${stats.totalIronHarvested.toInt()} • Petrol: ${stats.totalOilExtracted.toInt()}"
                        )

                        // Pillar 3: Unit Destruction
                        PillarCard(
                            title = "ASKERİ İMHA ORANI (KILL / LOSS RATIO)",
                            icon = Icons.Default.MilitaryTech,
                            points = "+${stats.unitDestructionPoints} VP",
                            progress = 0.78f,
                            accentColor = MatrixGreen,
                            detailText = "İmha Edilen Düşman: ${stats.enemyUnitsDestroyed} Birlik • Kendi Kayıplarımız: ${stats.friendlyUnitsLost} Birlik"
                        )
                    }
                }
            } else {
                // Tab 1: Dynamic Economy Line Chart
                EconomyResourceLineChart(
                    dataPoints = resourceHistory,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PillarCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    points: String,
    progress: Float,
    accentColor: Color,
    detailText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = points, color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = accentColor,
                trackColor = SurfaceElevated
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = detailText, color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) CyanNeon else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) SpaceBlack else TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                color = if (isSelected) SpaceBlack else TextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
