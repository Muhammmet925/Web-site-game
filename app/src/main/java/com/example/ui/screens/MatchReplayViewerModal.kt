package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MatchReplayEntity
import com.example.data.local.ReplayJsonSerializer
import com.example.data.local.ReplayTimelineFrame
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
import com.example.ui.theme.WarningOrange
import kotlinx.coroutines.delay

@Composable
fun MatchReplayViewerModal(
    savedReplays: List<MatchReplayEntity>,
    onSaveCurrentSessionReplay: () -> Unit,
    onDeleteReplay: (String) -> Unit,
    onClose: () -> Unit
) {
    var selectedReplay by remember { mutableStateOf(savedReplays.firstOrNull()) }
    var parsedFrames by remember(selectedReplay) {
        mutableStateOf(
            selectedReplay?.let { ReplayJsonSerializer.deserializeFrames(it.framesJson) } ?: emptyList()
        )
    }

    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var currentFrameIndex by remember { mutableStateOf(0) }

    // Playback loop
    LaunchedEffect(isPlaying, playbackSpeed, parsedFrames) {
        if (isPlaying && parsedFrames.isNotEmpty()) {
            while (isPlaying) {
                delay((1000L / playbackSpeed).toLong())
                if (currentFrameIndex < parsedFrames.size - 1) {
                    currentFrameIndex++
                } else {
                    isPlaying = false
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("match_replay_viewer_modal"),
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
                            .background(CyanNeon.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Maç Tekrarı",
                            tint = CyanNeon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "YEREL ROOM MAÇ TEKRARLARI (REPLAY RECORDINGS)",
                                color = CyanNeon,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SurfaceElevated)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Room SQLite", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            text = "Birim hareket koordinatlarını ve çatışma anlarını adım adım yeniden izleyin.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = onSaveCurrentSessionReplay,
                        colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("save_current_replay_btn")
                    ) {
                        Icon(Icons.Default.FiberManualRecord, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mevcut Maçı Kaydet", color = SpaceBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_replay_viewer_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Kapat", tint = TextSecondary)
                    }
                }
            }

            // Two-column layout: Left saved replay list, Right interactive visual replay player
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Saved Replays List
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "KAYITLI KARŞILAŞMALAR (${savedReplays.size})",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(savedReplays) { replay ->
                            val isSelected = selectedReplay?.id == replay.id

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) SurfaceElevated else SpaceBlack.copy(alpha = 0.5f))
                                    .border(
                                        1.2.dp,
                                        if (isSelected) CyanNeon else SurfaceBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        selectedReplay = replay
                                        parsedFrames = ReplayJsonSerializer.deserializeFrames(replay.framesJson)
                                        currentFrameIndex = 0
                                        isPlaying = false
                                    }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = if (replay.isVictory) "🏆 ZAFER" else "💀 YENİLGİ",
                                                color = if (replay.isVictory) MatrixGreen else DangerRed,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = replay.title,
                                                color = TextPrimary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "Harita: ${replay.mapName} • ${replay.durationSeconds}sn • ${replay.totalUnitsInvolved} Birlik",
                                            color = TextMuted,
                                            fontSize = 9.sp
                                        )
                                    }

                                    IconButton(
                                        onClick = { onDeleteReplay(replay.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Sil",
                                            tint = LaserCrimson,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Right Column: Interactive Replay Visual Radar Player
                Column(
                    modifier = Modifier
                        .weight(1.6f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark)
                        .border(1.dp, CyanNeon.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val activeFrame: ReplayTimelineFrame? = parsedFrames.getOrNull(currentFrameIndex)

                    Column {
                        // Title bar of active replay
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedReplay?.title ?: "Bir tekrar seçin",
                                color = AmberGold,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Event Alert Banner
                            activeFrame?.eventAlert?.let { alert ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(WarningOrange.copy(alpha = 0.2f))
                                        .border(1.dp, WarningOrange, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(text = alert, color = WarningOrange, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Replay 2D Radar Canvas
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceBlack)
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height

                                // Draw tactical grid
                                for (gx in 0..10) {
                                    val x = (gx / 10f) * w
                                    drawLine(Color.White.copy(alpha = 0.05f), Offset(x, 0f), Offset(x, h), 1f)
                                }
                                for (gy in 0..6) {
                                    val y = (gy / 6f) * h
                                    drawLine(Color.White.copy(alpha = 0.05f), Offset(0f, y), Offset(w, y), 1f)
                                }

                                // River line through map
                                drawLine(
                                    color = Color(0xFF1E3A8A).copy(alpha = 0.4f),
                                    start = Offset(w * 0.45f, 0f),
                                    end = Offset(w * 0.55f, h),
                                    strokeWidth = 14f
                                )

                                // Draw Units from active frame
                                activeFrame?.units?.forEach { u ->
                                    // Scale coordinates from 1000x600 world to canvas
                                    val cx = (u.posX / 1000f) * w
                                    val cy = (u.posY / 600f) * h
                                    val uColor = if (u.isEnemy) LaserCrimson else CyanNeon

                                    // Outer tactical circle
                                    drawCircle(
                                        color = uColor.copy(alpha = 0.3f),
                                        radius = 10f,
                                        center = Offset(cx, cy)
                                    )
                                    // Solid dot
                                    drawCircle(
                                        color = uColor,
                                        radius = 5f,
                                        center = Offset(cx, cy)
                                    )
                                    // Health indicator arc
                                    drawCircle(
                                        color = MatrixGreen,
                                        radius = 12f,
                                        center = Offset(cx, cy),
                                        style = Stroke(width = 1.5f)
                                    )
                                }
                            }

                            // Frame timestamp badge
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SpaceBlack.copy(alpha = 0.8f),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = "Zaman: ${activeFrame?.timestampSeconds ?: 0}sn / ${parsedFrames.lastOrNull()?.timestampSeconds ?: 0}sn",
                                    color = TextSecondary,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Playback Controls Row & Scrubber Slider
                    Column {
                        // Slider Scrubber
                        Slider(
                            value = currentFrameIndex.toFloat(),
                            onValueChange = {
                                currentFrameIndex = it.toInt()
                                isPlaying = false
                            },
                            valueRange = 0f..(parsedFrames.size - 1).coerceAtLeast(1).toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = CyanNeon,
                                activeTrackColor = CyanNeon,
                                inactiveTrackColor = SurfaceElevated
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Play / Pause Button
                                IconButton(
                                    onClick = { isPlaying = !isPlaying },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(CyanNeon)
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Duraklat" else "Oynat",
                                        tint = SpaceBlack,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                // Speed Buttons (1x, 2x, 4x)
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SurfaceElevated)
                                        .padding(2.dp)
                                ) {
                                    listOf(1.0f, 2.0f, 4.0f).forEach { spd ->
                                        val isSel = playbackSpeed == spd
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(if (isSel) CyanNeon else Color.Transparent)
                                                .clickable { playbackSpeed = spd }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "${spd.toInt()}x",
                                                color = if (isSel) SpaceBlack else TextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Text(
                                text = "Kare: ${currentFrameIndex + 1} / ${parsedFrames.size}",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
