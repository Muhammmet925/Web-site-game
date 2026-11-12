package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
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

@Composable
fun EconomySandboxScreen(modifier: Modifier = Modifier) {
    // Designer Interactive Sliders
    var goldRatePerSec by remember { mutableFloatStateOf(15f) }
    var powerPlantOutput by remember { mutableFloatStateOf(100f) }
    var costMultiplier by remember { mutableFloatStateOf(1.35f) }
    var baseUpgradeCost by remember { mutableFloatStateOf(150f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        SandboxHeaderCard()

        // Sliders Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.5f), SurfaceBorder)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DENGELEME PARAMETRELERİ (SLIDERS)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Slider 1: Gold Rate
                SliderSetting(
                    title = "Baz Maden Altın Üretimi",
                    valueText = "${goldRatePerSec.toInt()} Altın/saniye",
                    value = goldRatePerSec,
                    range = 5f..60f,
                    accentColor = AmberGold,
                    onValueChange = { goldRatePerSec = it },
                    tag = "slider_gold_rate"
                )

                // Slider 2: Power Output
                SliderSetting(
                    title = "Reaktör Enerji Çıktısı",
                    valueText = "${powerPlantOutput.toInt()} MW / Santral",
                    value = powerPlantOutput,
                    range = 50f..250f,
                    accentColor = EnergyYellow,
                    onValueChange = { powerPlantOutput = it },
                    tag = "slider_power_output"
                )

                // Slider 3: Cost Multiplier
                SliderSetting(
                    title = "Maliyet Üstel Çarpanı (r)",
                    valueText = "x%.2f kat artış / Seviye".format(costMultiplier),
                    value = costMultiplier,
                    range = 1.15f..1.55f,
                    accentColor = CyanNeon,
                    onValueChange = { costMultiplier = it },
                    tag = "slider_cost_multiplier"
                )

                // Slider 4: Base Cost
                SliderSetting(
                    title = "Başlangıç Bina Maliyeti (C0)",
                    valueText = "${baseUpgradeCost.toInt()} Altın",
                    value = baseUpgradeCost,
                    range = 50f..400f,
                    accentColor = MatrixGreen,
                    onValueChange = { baseUpgradeCost = it },
                    tag = "slider_base_cost"
                )
            }
        }

        // Real-time Idle Projection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "BOŞTA (IDLE) KAZANÇ PROJEKSİYONU",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Oyuncu oyunda değilken birikecek teorik kaynak miktarları:",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val sec10m = 600
                    val sec1h = 3600
                    val sec6h = 21600
                    val sec24h = 86400

                    ProjectionChip(period = "10 Dakika", amount = "${(goldRatePerSec * sec10m).toInt()}", color = CyanNeon)
                    ProjectionChip(period = "1 Saat", amount = "${(goldRatePerSec * sec1h).toInt()}", color = AmberGold)
                    ProjectionChip(period = "6 Saat", amount = "${(goldRatePerSec * sec6h / 1000).toInt()}k", color = MatrixGreen)
                    ProjectionChip(period = "24 Saat", amount = "${(goldRatePerSec * sec24h / 1000).toInt()}k", color = AmberLight)
                }
            }
        }

        // Level 1-7 Building Cost Curve Table
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "BİNA YÜKSELTMESİ MALİYET & SÜRE EĞRİSİ",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Formül: Cost(L) = $baseUpgradeCost × (${"%.2f".format(costMultiplier)})^(L-1)",
                    color = AmberGold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Seviye", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Maliyet (Altın)", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Tahmini Süre", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Gereken Enerji", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Table Rows (Level 1 to 7)
                for (lvl in 1..7) {
                    val cost = (baseUpgradeCost * Math.pow(costMultiplier.toDouble(), (lvl - 1).toDouble())).toInt()
                    val durationSec = 10 + (lvl - 1) * 15
                    val energyNeed = 20 + (lvl - 1) * 12

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Seviye $lvl", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "$cost", color = AmberGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = formatDuration(durationSec), color = CyanNeon, fontSize = 11.sp)
                        Text(text = "$energyNeed MW", color = EnergyYellow, fontSize = 11.sp)
                    }
                }
            }
        }

        // Game Designer Recommendation & Health Check
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(MatrixGreen.copy(alpha = 0.5f), SurfaceBorder)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "OYUN TASARIMCISI DEĞERLENDİRMESİ",
                        color = MatrixGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                val feedback = when {
                    costMultiplier < 1.25f -> "⚠️ Maliyet çarpanı çok düşük (r < 1.25). Oyuncu çok hızlı seviye atlayacak ve içerik 2 gün içinde tükenebilir."
                    costMultiplier > 1.45f -> "⚠️ Maliyet çarpanı çok yüksek (r > 1.45). Seviye 5'ten sonra oyuncu aşırı bekleme yaşayıp oyunu terk edebilir (churn riski)."
                    else -> "✅ Mükemmel Denge! 1.30 - 1.38 arası çarpan mobil strateji türünde D7 ve D30 tutundurması için altın standarttır."
                }

                Text(
                    text = feedback,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun SandboxHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.5f), CyanNeon.copy(alpha = 0.3f))))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AmberGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = AmberGold, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "İNTERAKTİF EKONOMİ DENGELEYİCİ (SANDBOX)",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Üretim ve maliyet katsayılarını değiştirerek canlı dengeyi test edin",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun SliderSetting(
    title: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    accentColor: Color,
    onValueChange: (Float) -> Unit,
    tag: String
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = TextSecondary, fontSize = 11.sp)
            Text(text = valueText, color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = SurfaceElevated
            ),
            modifier = Modifier.testTag(tag)
        )
    }
}

@Composable
private fun ProjectionChip(period: String, amount: String, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevated)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = period, color = TextSecondary, fontSize = 9.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = amount, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

private fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}dk ${s}s" else "${s}s"
}
