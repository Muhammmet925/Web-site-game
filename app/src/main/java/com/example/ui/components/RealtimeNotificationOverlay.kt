package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InGameNotification
import com.example.model.NotificationSeverity
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
fun RealtimeNotificationOverlay(
    notifications: List<InGameNotification>,
    onFocusLocation: (Float, Float) -> Unit,
    onDismissNotification: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .testTag("realtime_notification_overlay"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        notifications.take(1).forEach { notif ->
            NotificationToastCard(
                notification = notif,
                onFocus = {
                    if (notif.targetWorldX != null && notif.targetWorldY != null) {
                        onFocusLocation(notif.targetWorldX, notif.targetWorldY)
                    }
                },
                onDismiss = { onDismissNotification(notif.id) }
            )
        }
    }
}

@Composable
private fun NotificationToastCard(
    notification: InGameNotification,
    onFocus: () -> Unit,
    onDismiss: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(1.0f) }

    LaunchedEffect(notification.id) {
        val totalMs = notification.durationMs
        val steps = 40
        val stepMs = totalMs / steps
        for (i in steps downTo 0) {
            delay(stepMs)
            progress = i.toFloat() / steps.toFloat()
        }
        onDismiss()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "notif_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alarm_pulse"
    )

    val (accentColor, headerBg) = when (notification.type.severity) {
        NotificationSeverity.CRITICAL -> Pair(LaserCrimson, LaserCrimson.copy(alpha = 0.25f * pulseAlpha))
        NotificationSeverity.WARNING -> Pair(WarningOrange, WarningOrange.copy(alpha = 0.20f))
        NotificationSeverity.TRIUMPH -> Pair(MatrixGreen, MatrixGreen.copy(alpha = 0.20f))
        NotificationSeverity.INFO -> Pair(CyanNeon, CyanNeon.copy(alpha = 0.18f))
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceDark.copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor),
        shadowElevation = 8.dp,
        modifier = Modifier
            .widthIn(max = 420.dp)
            .testTag("notification_toast_${notification.id}")
    ) {
        Column {
            Row(
                modifier = Modifier
                    .background(headerBg)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(notification.type.iconEmoji, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = notification.message,
                    color = TextPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (notification.targetWorldX != null && notification.targetWorldY != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        onClick = onFocus,
                        shape = RoundedCornerShape(12.dp),
                        color = accentColor.copy(alpha = 0.35f),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, accentColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = "Odaklan",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Git",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Auto-dismiss countdown bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = accentColor,
                trackColor = SurfaceElevated
            )
        }
    }
}
