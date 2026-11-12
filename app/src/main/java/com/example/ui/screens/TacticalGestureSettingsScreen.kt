package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GestureBinding
import com.example.model.PhysicalGesture
import com.example.model.TacticalCommand
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
fun TacticalGestureSettingsScreen(
    currentBindings: List<GestureBinding>,
    onSaveBindings: (List<GestureBinding>) -> Unit,
    onClose: () -> Unit
) {
    var bindings by remember { mutableStateOf(currentBindings) }
    var selectedCommandForEdit by remember { mutableStateOf<TacticalCommand?>(null) }
    var detectedGestureText by remember { mutableStateOf("Pad üzerinde hareketinizi deneyin...") }
    var lastExecutedAction by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("tactical_gesture_settings_screen"),
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
                            imageVector = Icons.Default.Gesture,
                            contentDescription = "Jest Ayarları",
                            tint = CyanNeon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "TAKTIK EKRAN HAREKETLERI & DÜĞME DİZİLERİ",
                            color = CyanNeon,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Fiziksel ekran jestlerini (Double-tap, Swipe, Pinch) doğrudan taktik komutlara bağlayın.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = {
                            bindings = com.example.model.TacticalGestureCatalog.createDefaultBindings()
                            onSaveBindings(bindings)
                            detectedGestureText = "Varsayılan taktik jestler yüklendi!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("reset_gestures_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sıfırla",
                            tint = WarningOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Varsayılana Dön", color = TextPrimary, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_gesture_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kapat",
                            tint = TextSecondary
                        )
                    }
                }
            }

            // Split Layout: Left list of command bindings, Right interactive touch test pad
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Gesture Bindings Table
                LazyColumn(
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(bindings) { binding ->
                        val isEditing = selectedCommandForEdit == binding.command

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isEditing) SurfaceElevated else SurfaceDark)
                                .border(
                                    1.dp,
                                    if (isEditing) CyanNeon else SurfaceBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedCommandForEdit = if (isEditing) null else binding.command
                                }
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(binding.command.iconEmoji, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = binding.command.title,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = binding.command.description,
                                                color = TextSecondary,
                                                fontSize = 10.sp,
                                                maxLines = 2
                                            )
                                        }
                                    }

                                    // Gesture Badge
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.2f), AmberGold.copy(alpha = 0.15f))))
                                            .border(1.dp, CyanNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(binding.gesture.symbol, fontSize = 14.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = binding.gesture.label,
                                                color = CyanNeon,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }

                                // Quick Rebind Options Row (Visible when clicked)
                                AnimatedVisibility(visible = isEditing) {
                                    Column(modifier = Modifier.padding(top = 10.dp)) {
                                        Text(
                                            text = "Bağlamak istediğiniz jesti seçin:",
                                            color = WarningOrange,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            PhysicalGesture.values().forEach { candidateGesture ->
                                                val isSelected = binding.gesture == candidateGesture
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(if (isSelected) CyanNeon else SurfaceDark)
                                                        .border(1.dp, if (isSelected) Color.White else SurfaceBorder, RoundedCornerShape(6.dp))
                                                        .clickable {
                                                            val updated = bindings.map { b ->
                                                                if (b.command == binding.command) {
                                                                    b.copy(gesture = candidateGesture)
                                                                } else if (b.gesture == candidateGesture) {
                                                                    // Swap if already assigned
                                                                    b.copy(gesture = binding.gesture)
                                                                } else b
                                                            }
                                                            bindings = updated
                                                            onSaveBindings(updated)
                                                            selectedCommandForEdit = null
                                                            detectedGestureText = "${binding.command.title} -> ${candidateGesture.label} olarak atandı!"
                                                        }
                                                        .padding(horizontal = 8.dp, vertical = 5.dp)
                                                ) {
                                                    Text(
                                                        text = "${candidateGesture.symbol} ${candidateGesture.label}",
                                                        color = if (isSelected) SpaceBlack else TextPrimary,
                                                        fontSize = 9.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Right Column: Interactive Gesture Testing Pad
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark)
                        .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CANLI JEST TEST PEDİ",
                                color = MatrixGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Test Pad",
                                tint = MatrixGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "Dokunun, çift tıklayın veya uzun basın. Atanan taktik komutun anında tetiklenmesini izleyin.",
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                        )

                        // Interactive Touch Canvas Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(SpaceBlack)
                                .border(1.dp, MatrixGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            detectedGestureText = "Tek Dokunuş algılandı (Birim Seçimi)"
                                            lastExecutedAction = "🎯 Hedef Konum Seçildi"
                                        },
                                        onDoubleTap = {
                                            val cmd = bindings.find { it.gesture == PhysicalGesture.DOUBLE_TAP }?.command
                                            val cmdTitle = cmd?.title ?: "Komut Yok"
                                            detectedGestureText = "Çift Dokunma Algılandı! (Double Tap)"
                                            lastExecutedAction = "⚔️ Tetiklenen: $cmdTitle"
                                        },
                                        onLongPress = {
                                            val cmd = bindings.find { it.gesture == PhysicalGesture.LONG_PRESS }?.command
                                            val cmdTitle = cmd?.title ?: "Komut Yok"
                                            detectedGestureText = "Uzun Basma Algılandı (600ms)"
                                            lastExecutedAction = "🔄 Tetiklenen: $cmdTitle"
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("👆", fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = detectedGestureText,
                                    color = MatrixGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                lastExecutedAction?.let { action ->
                                    Text(
                                        text = action,
                                        color = AmberGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Button sequences test bar
                    Column {
                        Text(
                            text = "Hızlı Düğme Dizisi Kısayolu:",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val cmd = bindings.find { it.gesture == PhysicalGesture.QUICK_SEQUENCE_A_B }?.command
                                    detectedGestureText = "Dizi: A -> B Buton Tetiklemesi Gerçekleşti"
                                    lastExecutedAction = "⚡ Tetiklenen: ${cmd?.title ?: "Radar Taraması"}"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("simulate_sequence_button")
                            ) {
                                Text("[A-B] Dizisini Test Et", color = SpaceBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    onSaveBindings(bindings)
                                    onClose()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MatrixGreen),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = "Kaydet", tint = SpaceBlack, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Kaydet", color = SpaceBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
