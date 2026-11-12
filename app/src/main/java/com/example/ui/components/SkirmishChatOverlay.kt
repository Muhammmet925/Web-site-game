package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SkirmishChatMessageDto
import com.example.model.Nation
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

@Composable
fun SkirmishChatOverlay(
    messages: List<SkirmishChatMessageDto>,
    currentFaction: Nation,
    currentCommanderName: String = "Komutan Gazi",
    onSendMessage: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    var filterMode by remember { mutableStateOf(0) } // 0: Tümü, 1: Telsiz, 2: Sistem
    val listState = rememberLazyListState()

    val quickPhrases = listOf(
        "⚠️ Düşman taarruzu başladı!",
        "🚀 Hava desteği intikal ediyor!",
        "🛡️ Savunma hattına çekilin!",
        "🎯 Stratejik tesis hedefte!",
        "🤝 Müttefiklik korundu!"
    )

    val filteredMessages = messages.filter { msg ->
        when (filterMode) {
            1 -> !msg.isSystemNotification
            2 -> msg.isSystemNotification
            else -> true
        }
    }

    LaunchedEffect(filteredMessages.size) {
        if (filteredMessages.isNotEmpty() && isExpanded) {
            listState.animateScrollToItem(filteredMessages.size - 1)
        }
    }

    Box(
        modifier = modifier
            .testTag("skirmish_chat_overlay")
    ) {
        if (!isExpanded) {
            // Collapsed Floating Button with recent message preview
            Surface(
                onClick = { isExpanded = true },
                shape = RoundedCornerShape(20.dp),
                color = SurfaceDark.copy(alpha = 0.92f),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanNeon.copy(alpha = 0.5f)),
                modifier = Modifier.testTag("expand_chat_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Telsiz",
                            tint = CyanNeon,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Telsiz & Savaş Sohbeti",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(AmberGold)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${messages.size}",
                            color = SpaceBlack,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        } else {
            // Expanded Chat Overlay Box
            Column(
                modifier = Modifier
                    .width(360.dp)
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark.copy(alpha = 0.96f))
                    .border(1.dp, CyanNeon.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = "Telsiz",
                            tint = CyanNeon,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SKIRMISH ÇOK OYUNCULU TELSİZ",
                            color = CyanNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row {
                        IconButton(
                            onClick = { isExpanded = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Küçült",
                                tint = TextSecondary
                            )
                        }
                    }
                }

                // Filter Tabs Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip("Tümü (${messages.size})", filterMode == 0) { filterMode = 0 }
                    FilterChip("Telsiz", filterMode == 1) { filterMode = 1 }
                    FilterChip("Savaş Bildirimi", filterMode == 2) { filterMode = 2 }
                }

                // Messages Scroll List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SpaceBlack.copy(alpha = 0.6f))
                        .padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (filteredMessages.isEmpty()) {
                        item {
                            Text(
                                text = "Henüz telsiz mesajı yok. Taktik mesaj gönderin.",
                                color = TextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    } else {
                        items(filteredMessages) { msg ->
                            ChatMessageRow(msg = msg)
                        }
                    }
                }

                // Quick Tactical Radio Phrases
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(quickPhrases) { phrase ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceElevated)
                                .border(1.dp, SurfaceBorder, RoundedCornerShape(6.dp))
                                .clickable {
                                    onSendMessage(phrase, false)
                                }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(text = phrase, color = TextPrimary, fontSize = 9.sp)
                        }
                    }
                }

                // Input Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Telsiz mesajı yaz...", fontSize = 10.sp, color = TextMuted) },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("chat_input_field"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedContainerColor = SpaceBlack,
                            unfocusedContainerColor = SpaceBlack,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText.trim(), false)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyanNeon)
                            .testTag("send_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Gönder",
                            tint = SpaceBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessageRow(msg: SkirmishChatMessageDto) {
    val isSystem = msg.isSystemNotification

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSystem) DangerRed.copy(alpha = 0.12f) else SurfaceElevated.copy(alpha = 0.4f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isSystem) "⚠️ [SİSTEM / SAVAŞ]" else "[${msg.senderFaction}] ${msg.senderName}",
                    color = if (isSystem) WarningOrange else CyanNeon,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = msg.messageText,
            color = if (isSystem) Color.White else TextPrimary,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 1.dp)
        )
    }
}

@Composable
private fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) CyanNeon else SurfaceElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) SpaceBlack else TextSecondary,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
