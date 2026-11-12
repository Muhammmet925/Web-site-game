package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SurfaceDark
import kotlin.math.max
import kotlin.math.min

/**
 * SelectionBoxOverlay:
 * Allows players to drag a tactical selection box over multiple units on the game world map.
 * Visualizes a semi-transparent laser green fill, dashed tactical boundary, laser corner brackets (┌ ┐ └ ┘),
 * and a floating badge displaying the live count of targeted units.
 */
@Composable
fun SelectionBoxOverlay(
    dragStart: Offset?,
    dragCurrent: Offset?,
    selectedUnitsCount: Int = 0,
    modifier: Modifier = Modifier
) {
    if (dragStart == null || dragCurrent == null) return

    val minX = min(dragStart.x, dragCurrent.x)
    val maxX = max(dragStart.x, dragCurrent.x)
    val minY = min(dragStart.y, dragCurrent.y)
    val maxY = max(dragStart.y, dragCurrent.y)
    val w = maxX - minX
    val h = maxY - minY

    if (w < 4f && h < 4f) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("selection_box_overlay")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 1. Semi-transparent laser green fill
            drawRect(
                color = MatrixGreen.copy(alpha = 0.15f),
                topLeft = Offset(minX, minY),
                size = Size(w, h)
            )

            // 2. Dashed tactical boundary
            drawRect(
                color = MatrixGreen,
                topLeft = Offset(minX, minY),
                size = Size(w, h),
                style = Stroke(
                    width = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                )
            )

            // 3. Four Tactical Laser Corner Brackets: ┌ ┐ └ ┘
            val bLen = 14.dp.toPx().coerceAtMost(w / 3f).coerceAtMost(h / 3f)
            val strokeW = 3f

            // Top-Left ┌
            drawLine(MatrixGreen, Offset(minX, minY), Offset(minX + bLen, minY), strokeW)
            drawLine(MatrixGreen, Offset(minX, minY), Offset(minX, minY + bLen), strokeW)
            // Top-Right ┐
            drawLine(MatrixGreen, Offset(maxX, minY), Offset(maxX - bLen, minY), strokeW)
            drawLine(MatrixGreen, Offset(maxX, minY), Offset(maxX, minY + bLen), strokeW)
            // Bottom-Left └
            drawLine(MatrixGreen, Offset(minX, maxY), Offset(minX + bLen, maxY), strokeW)
            drawLine(MatrixGreen, Offset(minX, maxY), Offset(minX, maxY - bLen), strokeW)
            // Bottom-Right ┘
            drawLine(MatrixGreen, Offset(maxX, maxY), Offset(maxX - bLen, maxY), strokeW)
            drawLine(MatrixGreen, Offset(maxX, maxY), Offset(maxX, maxY - bLen), strokeW)
        }

        // Floating Target Counter Badge
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (minX + w / 2f - 60f).toInt().coerceAtLeast(10),
                        (minY - 26f).toInt().coerceAtLeast(10)
                    )
                }
                .clip(RoundedCornerShape(4.dp))
                .background(SurfaceDark.copy(alpha = 0.92f))
                .border(1.dp, MatrixGreen, RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (selectedUnitsCount > 0) "🎯 $selectedUnitsCount BİRLİK HEDEFTE" else "🎯 SEÇİM KUTUSU",
                color = MatrixGreen,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
