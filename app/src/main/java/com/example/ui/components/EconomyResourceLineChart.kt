package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class ResourceDataPoint(
    val timeLabel: String,
    val minute: Float,
    val gold: Double,
    val iron: Double,
    val oil: Double
)

@Composable
fun EconomyResourceLineChart(
    dataPoints: List<ResourceDataPoint>,
    modifier: Modifier = Modifier
) {
    var showGold by remember { mutableStateOf(true) }
    var showIron by remember { mutableStateOf(true) }
    var showOil by remember { mutableStateOf(true) }
    var scrubberX by remember { mutableStateOf<Float?>(null) }

    val points = if (dataPoints.isEmpty()) {
        listOf(
            ResourceDataPoint("00:00", 0f, 1000.0, 500.0, 100.0),
            ResourceDataPoint("02:00", 2f, 2400.0, 1100.0, 120.0),
            ResourceDataPoint("04:00", 4f, 4800.0, 1950.0, 150.0),
            ResourceDataPoint("06:00", 6f, 7500.0, 2900.0, 180.0),
            ResourceDataPoint("08:00", 8f, 10500.0, 3600.0, 200.0),
            ResourceDataPoint("10:00", 10f, 14200.0, 4800.0, 220.0)
        )
    } else dataPoints

    val maxVal = maxOf(
        points.maxOfOrNull { it.gold } ?: 1000.0,
        points.maxOfOrNull { it.iron } ?: 500.0,
        points.maxOfOrNull { it.oil } ?: 200.0
    ).coerceAtLeast(100.0)

    val currentPoint = points.lastOrNull()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("economy_resource_line_chart")
    ) {
        // Chart Header & Metric Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DİNAMİK EKONOMİ & KAYNAK AKIŞ GRAFİĞİ",
                    color = CyanNeon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Zaman serisi boyunca Altın, Maden (Demir/Wood) ve Petrol birikim eğrileri",
                    color = TextMuted,
                    fontSize = 9.sp
                )
            }

            // Series Filters / Toggles
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                SeriesChip(
                    label = "Altın: ${(currentPoint?.gold ?: 0.0).toInt()}",
                    color = AmberGold,
                    isActive = showGold,
                    onClick = { showGold = !showGold }
                )
                SeriesChip(
                    label = "Maden/Demir: ${(currentPoint?.iron ?: 0.0).toInt()}",
                    color = CyanNeon,
                    isActive = showIron,
                    onClick = { showIron = !showIron }
                )
                SeriesChip(
                    label = "Petrol: ${(currentPoint?.oil ?: 0.0).toInt()}",
                    color = MatrixGreen,
                    isActive = showOil,
                    onClick = { showOil = !showOil }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Canvas Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SpaceBlack)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        scrubberX = offset.x
                    }
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { scrubberX = it.x },
                        onDrag = { change, _ ->
                            change.consume()
                            scrubberX = change.position.x
                        },
                        onDragEnd = { scrubberX = null }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val paddingLeft = 40f
                val paddingBottom = 24f
                val chartW = w - paddingLeft - 10f
                val chartH = h - paddingBottom - 10f

                // Draw Horizontal Grid Lines
                val gridLines = 4
                for (i in 0..gridLines) {
                    val y = chartH - (i.toFloat() / gridLines * chartH) + 5f
                    drawLine(
                        color = Color.White.copy(alpha = 0.08f),
                        start = Offset(paddingLeft, y),
                        end = Offset(w - 10f, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )
                }

                // Helper to map index & value to canvas Offset
                fun getPointOffset(index: Int, value: Double): Offset {
                    val x = paddingLeft + (index.toFloat() / (points.size - 1).coerceAtLeast(1)) * chartW
                    val normalized = (value / maxVal).coerceIn(0.0, 1.0).toFloat()
                    val y = (chartH - (normalized * chartH)) + 5f
                    return Offset(x, y)
                }

                // Draw Curves
                fun drawSeriesLine(
                    selector: (ResourceDataPoint) -> Double,
                    color: Color,
                    gradientColors: List<Color>
                ) {
                    if (points.size < 2) return

                    val path = Path()
                    val fillPath = Path()

                    val firstOffset = getPointOffset(0, selector(points[0]))
                    path.moveTo(firstOffset.x, firstOffset.y)
                    fillPath.moveTo(firstOffset.x, chartH + 5f)
                    fillPath.lineTo(firstOffset.x, firstOffset.y)

                    for (i in 1 until points.size) {
                        val prev = getPointOffset(i - 1, selector(points[i - 1]))
                        val curr = getPointOffset(i, selector(points[i]))

                        // Cubic Bezier curve for ultra smooth line
                        val control1 = Offset(prev.x + (curr.x - prev.x) / 2f, prev.y)
                        val control2 = Offset(prev.x + (curr.x - prev.x) / 2f, curr.y)
                        path.cubicTo(control1.x, control1.y, control2.x, control2.y, curr.x, curr.y)
                        fillPath.cubicTo(control1.x, control1.y, control2.x, control2.y, curr.x, curr.y)
                    }

                    val lastOffset = getPointOffset(points.size - 1, selector(points.last()))
                    fillPath.lineTo(lastOffset.x, chartH + 5f)
                    fillPath.close()

                    // Gradient underlay
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(gradientColors)
                    )

                    // Line
                    drawPath(
                        path = path,
                        color = color,
                        style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                    )

                    // Dots on vertices
                    points.forEachIndexed { idx, pt ->
                        val dotOffset = getPointOffset(idx, selector(pt))
                        drawCircle(color = color, radius = 3.5f, center = dotOffset)
                        drawCircle(color = SpaceBlack, radius = 1.5f, center = dotOffset)
                    }
                }

                if (showOil) {
                    drawSeriesLine(
                        { it.oil },
                        MatrixGreen,
                        listOf(MatrixGreen.copy(alpha = 0.25f), Color.Transparent)
                    )
                }
                if (showIron) {
                    drawSeriesLine(
                        { it.iron },
                        CyanNeon,
                        listOf(CyanNeon.copy(alpha = 0.25f), Color.Transparent)
                    )
                }
                if (showGold) {
                    drawSeriesLine(
                        { it.gold },
                        AmberGold,
                        listOf(AmberGold.copy(alpha = 0.35f), Color.Transparent)
                    )
                }

                // Scrubber Line & Highlight
                scrubberX?.let { sx ->
                    val clampedX = sx.coerceIn(paddingLeft, w - 10f)
                    drawLine(
                        color = Color.White.copy(alpha = 0.7f),
                        start = Offset(clampedX, 5f),
                        end = Offset(clampedX, chartH + 5f),
                        strokeWidth = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                    )
                }
            }

            // Scrubber tooltip overlay
            scrubberX?.let { sx ->
                val ratio = ((sx - 40f) / 500f).coerceIn(0f, 1f)
                val idx = ((points.size - 1) * ratio).toInt().coerceIn(0, points.size - 1)
                val pt = points[idx]

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SurfaceElevated.copy(alpha = 0.95f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        Text("Zaman: ${pt.timeLabel}", color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("Altın: ${pt.gold.toInt()}", color = AmberGold, fontSize = 9.sp)
                        Text("Maden: ${pt.iron.toInt()}", color = CyanNeon, fontSize = 9.sp)
                        Text("Petrol: ${pt.oil.toInt()}", color = MatrixGreen, fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeriesChip(
    label: String,
    color: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (isActive) color.copy(alpha = 0.18f) else SurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isActive) color else SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (isActive) color else TextMuted)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = if (isActive) color else TextMuted,
                fontSize = 9.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
