package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Nation
import com.example.simulation.GlobalWarStats
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

@Composable
fun GlobalWarDashboardScreen(
    stats: GlobalWarStats,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("📊 Küresel Savaş Telemetrisi", "💻 Web Recharts Bileşeni (React)", "⚙️ C# Telemetry API")

    val rechartsCode = """
import React from 'react';
import { 
  AreaChart, Area, BarChart, Bar, XAxis, YAxis, 
  CartesianGrid, Tooltip, ResponsiveContainer, Legend, PieChart, Pie, Cell 
} from 'recharts';

// Global Savaş Paneli Recharts Veri Grafiği
export const GlobalWarRoom = ({ warData }) => {
  const conflictDensityData = [
    { time: '00:00', intensity: 12, turkeyKills: 4, enemyLosses: 8 },
    { time: '02:00', intensity: 28, turkeyKills: 14, enemyLosses: 14 },
    { time: '04:00', intensity: 65, turkeyKills: 35, enemyLosses: 30 },
    { time: '06:00', intensity: 88, turkeyKills: 52, enemyLosses: 36 },
    { time: '08:00', intensity: 142, turkeyKills: 85, enemyLosses: 57 },
    { time: '10:00', intensity: 210, turkeyKills: 130, enemyLosses: 80 },
    { time: '12:00', intensity: 175, turkeyKills: 110, enemyLosses: 65 },
  ];

  const factionWinRates = [
    { name: 'Türkiye 🇹🇷', victories: 342, winRate: 68.4, color: '#FF3366' },
    { name: 'ABD 🇺🇸', victories: 280, winRate: 59.2, color: '#00F5D4' },
    { name: 'Rusya 🇷🇺', victories: 245, winRate: 54.1, color: '#F59E0B' },
    { name: 'Çin 🇨🇳', victories: 215, winRate: 51.0, color: '#A78BFA' },
  ];

  return (
    <div className="p-6 bg-slate-950 text-white rounded-xl border border-cyan-500/30">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-bold tracking-wider text-cyan-400">
          GLOBAL SAVAŞ KONTROL ODASI (WAR TELEMETRY)
        </h2>
        <span className="px-3 py-1 bg-red-600/30 border border-red-500 text-red-400 text-xs rounded-full">
          CANLI CEPHE AKTİF
        </span>
      </div>

      {/* 1. KÜRESEL ÇATIŞMA YOĞUNLUĞU GRAFİĞİ (Area Chart) */}
      <div className="h-64 mb-8 bg-slate-900/60 p-4 rounded-lg border border-slate-800">
        <h3 className="text-sm font-semibold text-amber-400 mb-3">
          Zamana Bağlı Çatışma Yoğunluğu & Zayiat Eğrisi
        </h3>
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={conflictDensityData}>
            <defs>
              <linearGradient id="colorIntensity" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#00F5D4" stopOpacity={0.8}/>
                <stop offset="95%" stopColor="#00F5D4" stopOpacity={0}/>
              </linearGradient>
              <linearGradient id="colorKills" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#FF3366" stopOpacity={0.8}/>
                <stop offset="95%" stopColor="#FF3366" stopOpacity={0}/>
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
            <XAxis dataKey="time" stroke="#64748b" />
            <YAxis stroke="#64748b" />
            <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155' }} />
            <Legend />
            <Area type="monotone" dataKey="intensity" stroke="#00F5D4" fillOpacity={1} fill="url(#colorIntensity)" name="Çatışma Yoğunluğu" />
            <Area type="monotone" dataKey="turkeyKills" stroke="#FF3366" fillOpacity={1} fill="url(#colorKills)" name="İmha Edilen Düşman" />
          </AreaChart>
        </ResponsiveContainer>
      </div>

      {/* 2. ÜLKE ZAFER DAĞILIMI (Bar Chart) */}
      <div className="h-56 bg-slate-900/60 p-4 rounded-lg border border-slate-800">
        <h3 className="text-sm font-semibold text-cyan-400 mb-3">
          Ülke Bazlı Toplam Zafer Dağılımı
        </h3>
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={factionWinRates}>
            <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
            <XAxis dataKey="name" stroke="#64748b" />
            <YAxis stroke="#64748b" />
            <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155' }} />
            <Bar dataKey="victories" fill="#F59E0B" name="Toplam Zafer" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};
""".trimIndent()

    val csharpTelemetryCode = """
using System;
using System.Collections.Generic;

namespace ZeroHourMobile.Telemetry
{
    public class GlobalWarTelemetry
    {
        public int TotalPlayerVictories { get; set; } = 412;
        public int TotalPlayerDefeats { get; set; } = 89;
        public float OverallWinRate => (TotalPlayerVictories / (float)(TotalPlayerVictories + TotalPlayerDefeats)) * 100f;

        public List<ConflictPoint> History { get; } = new List<ConflictPoint>()
        {
            new ConflictPoint("00:00", 15),
            new ConflictPoint("02:00", 35),
            new ConflictPoint("04:00", 72),
            new ConflictPoint("06:00", 120),
            new ConflictPoint("08:00", 185),
            new ConflictPoint("10:00", 240),
            new ConflictPoint("12:00", 195)
        };

        public void RecordCombatClash(int intensity)
        {
            History.Add(new ConflictPoint(DateTime.Now.ToString("HH:mm"), intensity));
        }
    }

    public class ConflictPoint
    {
        public string Timestamp { get; set; }
        public int Intensity { get; set; }
        public ConflictPoint(string t, int i) { Timestamp = t; Intensity = i; }
    }
}
""".trimIndent()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
            .padding(14.dp)
            .testTag("global_war_dashboard")
    ) {
        // Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "GLOBAL SAVAŞ KONTROL ODASI (WAR DASHBOARD)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Küresel Çatışma Yoğunluğu, Zafer İstatistikleri & Cephe Telemetrisi",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tabs
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = SurfaceDark,
            contentColor = CyanNeon,
            divider = {}
        ) {
            tabTitles.forEachIndexed { idx, title ->
                Tab(
                    selected = activeTab == idx,
                    onClick = { activeTab = idx },
                    text = {
                        Text(
                            text = title,
                            color = if (activeTab == idx) CyanNeon else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (activeTab == idx) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeTab) {
            0 -> LiveWarTelemetryTab(stats)
            1 -> CodeViewerCard(
                title = "GlobalWarRoom.jsx (Recharts React Bileşeni)",
                description = "Recharts kütüphanesiyle web veya mobil tarayıcıda küresel çatışma yoğunluğu grafiği",
                code = rechartsCode,
                onCopy = {
                    val clip = ClipData.newPlainText("GlobalWarRoom.jsx", rechartsCode)
                    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                    Toast.makeText(context, "Recharts Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
                }
            )
            2 -> CodeViewerCard(
                title = "GlobalWarTelemetry.cs (C# MonoGame Telemetry Servisi)",
                description = "Arka planda çatışma yoğunluğu ve zaferleri sunucuya raporlayan telemetry modeli",
                code = csharpTelemetryCode,
                onCopy = {
                    val clip = ClipData.newPlainText("GlobalWarTelemetry.cs", csharpTelemetryCode)
                    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                    Toast.makeText(context, "C# Telemetry Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
private fun LiveWarTelemetryTab(stats: GlobalWarStats) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // KPI Stat Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KpiCard(
                title = "Toplam Zafer",
                value = stats.totalVictories.toString(),
                subtitle = "%${stats.winRatePercent} Başarı Oranı",
                accentColor = MatrixGreen,
                icon = Icons.Default.MilitaryTech,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Küresel Çatışma",
                value = stats.totalBattles.toString(),
                subtitle = "${stats.activeBattlefieldUnits} Aktif Birlik",
                accentColor = AmberGold,
                icon = Icons.Default.Timeline,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Düşman Zayiatı",
                value = stats.totalEnemiesDestroyed.toString(),
                subtitle = "+${stats.totalGoldLooted} Altın Ganimet",
                accentColor = LaserCrimson,
                icon = Icons.Default.Shield,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Skirmish Dalgası",
                value = "Dalga ${stats.skirmishWave}",
                subtitle = "Bot Savunuldu",
                accentColor = CyanNeon,
                icon = Icons.Default.TrendingUp,
                modifier = Modifier.weight(1f)
            )
        }

        // Live Canvas Conflict Density Chart (Recharts Equivalent on Native Android Compose)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.5f), SurfaceBorder))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoGraph, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "KÜRESEL ÇATIŞMA YOĞUNLUĞU EĞRİSİ (ZAMAN SERİSİ)",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        LegendIndicator(label = "Çatışma Yoğunluğu", color = CyanNeon)
                        LegendIndicator(label = "Düşman Kayıpları", color = LaserCrimson)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Native Canvas Area Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(SpaceBlack, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    val points = stats.conflictDensityPoints
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        if (points.isEmpty()) return@Canvas

                        val w = size.width
                        val h = size.height
                        val maxVal = (points.maxOfOrNull { it.intensity } ?: 100).toFloat().coerceAtLeast(50f)

                        // Grid lines
                        for (i in 1..3) {
                            val y = h * (i / 4f)
                            drawLine(
                                color = SurfaceBorder.copy(alpha = 0.4f),
                                start = Offset(0f, y),
                                end = Offset(w, y),
                                strokeWidth = 1f
                            )
                        }

                        // Path 1: Conflict Intensity Area
                        val path = Path()
                        val strokePath = Path()
                        val stepX = w / (points.size - 1).coerceAtLeast(1)

                        points.forEachIndexed { i, pt ->
                            val x = i * stepX
                            val y = h - (pt.intensity / maxVal) * (h * 0.85f)
                            if (i == 0) {
                                path.moveTo(x, y)
                                strokePath.moveTo(x, y)
                            } else {
                                val prevX = (i - 1) * stepX
                                val prevY = h - (points[i - 1].intensity / maxVal) * (h * 0.85f)
                                val cx = (prevX + x) / 2f
                                path.cubicTo(cx, prevY, cx, y, x, y)
                                strokePath.cubicTo(cx, prevY, cx, y, x, y)
                            }
                        }

                        // Area Gradient fill
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                listOf(CyanNeon.copy(alpha = 0.35f), Color.Transparent)
                            )
                        )
                        drawPath(
                            path = strokePath,
                            color = CyanNeon,
                            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                        )

                        // Red casualty dots
                        points.forEachIndexed { i, pt ->
                            val x = i * stepX
                            val y = h - (pt.enemyKills / maxVal) * (h * 0.85f)
                            drawCircle(
                                color = LaserCrimson,
                                radius = 3.5f,
                                center = Offset(x, y)
                            )
                        }
                    }
                }
            }
        }

        // Faction Breakdown Bar Charts
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.5f), SurfaceBorder)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ÜLKELERİN KÜRESEL ZAFER DAĞILIMI (ASİMETRİK GÜÇ DENGESİ)",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                FactionWinBar(nationName = "Türkiye (SİHA & Hızlı Zırhlı)", flag = "🇹🇷", winCount = 380, totalBattles = 500, color = LaserCrimson)
                FactionWinBar(nationName = "ABD (Hava Hakimiyeti & Lojistik)", flag = "🇺🇸", winCount = 310, totalBattles = 500, color = CyanNeon)
                FactionWinBar(nationName = "Rusya (Ağır Zırh & Topçu)", flag = "🇷🇺", winCount = 275, totalBattles = 500, color = AmberGold)
                FactionWinBar(nationName = "Çin (Seri Üretim & Kitle)", flag = "🇨🇳", winCount = 250, totalBattles = 500, color = MatrixGreen)
            }
        }
    }
}

@Composable
private fun FactionWinBar(nationName: String, flag: String, winCount: Int, totalBattles: Int, color: Color) {
    val ratio = (winCount.toFloat() / totalBattles).coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$flag $nationName", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(text = "$winCount Zafer (%${(ratio * 100).toInt()})", color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { ratio },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = SpaceBlack
        )
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceDark,
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.5f), SurfaceBorder))),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = title, color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = subtitle, color = accentColor, fontSize = 8.sp)
        }
    }
}

@Composable
private fun LegendIndicator(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = TextSecondary, fontSize = 9.sp)
    }
}

@Composable
private fun CodeViewerCard(title: String, description: String, code: String, onCopy: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.5f), SurfaceBorder)))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = title, color = AmberGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = description, color = TextSecondary, fontSize = 9.sp)
                }
                Button(
                    onClick = onCopy,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Kopyala", color = CyanNeon, fontSize = 9.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(SpaceBlack, RoundedCornerShape(8.dp))
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = code,
                    color = TextPrimary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 13.sp
                )
            }
        }
    }
}
