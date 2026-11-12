package com.example.model

enum class TacticalCommand(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val description: String
) {
    ATTACK_MOVE(
        id = "cmd_attack_move",
        title = "Toplu Taarruz (Attack Move)",
        iconEmoji = "⚔️",
        description = "Seçili tüm birlikleri hedef bölgeye hücum ettirir; karşılaşılan düşmanlara otomatik saldırır."
    ),
    RETREAT(
        id = "cmd_retreat",
        title = "Acil Geri Çekilme (Fall Back)",
        iconEmoji = "🛡️",
        description = "Birlikleri derhal gerideki ana üs veya güvenli toplanma alanına hızla geri çeker."
    ),
    PATROL(
        id = "cmd_patrol",
        title = "Sınır Devriyesi (Patrol)",
        iconEmoji = "🔄",
        description = "Birimlerin belirlenen iki nokta arasında devriye gezmesini ve düşmanları gözetlemesini sağlar."
    ),
    DEFENSIVE_STANCE(
        id = "cmd_defensive",
        title = "Siper Al & Savunma Duruşu",
        iconEmoji = "🧱",
        description = "Birimler mevzilenir, hasar azaltma katsayısı +%25 artar ve menziline giren düşmanı karşılar."
    ),
    SCATTER(
        id = "cmd_scatter",
        title = "Dağıl & Bombardımandan Kaç",
        iconEmoji = "💨",
        description = "Düşman topçu veya hava saldırılarına karşı birlikleri hızla çevreye dağıtır."
    ),
    RADAR_PING(
        id = "cmd_radar_ping",
        title = "Hızlı Sonar / Radar Taraması",
        iconEmoji = "📡",
        description = "Savaş sisi altındaki kritik bölgeyi 5 saniyeliğine açığa çıkarır."
    ),
    SELECT_ALL_COMBAT(
        id = "cmd_select_all",
        title = "Tüm Muharip Güçleri Seç",
        iconEmoji = "🎖️",
        description = "Haritada aktif tüm tank, jet ve piyade birliklerini anında seçili hale getirir."
    ),
    GENERALS_POWER(
        id = "cmd_generals_power",
        title = "Generals Süper Gücünü Tetikle",
        iconEmoji = "⚡",
        description = "Hazır durumdaki birincil Generals Power desteğini imleç noktasına uygular."
    )
}

enum class PhysicalGesture(
    val id: String,
    val label: String,
    val symbol: String,
    val gestureType: String
) {
    DOUBLE_TAP("gest_double_tap", "Çift Dokunma", "👆👆", "Ekran Hareketi"),
    LONG_PRESS("gest_long_press", "Uzun Basma (600ms)", "⏱️", "Ekran Hareketi"),
    TWO_FINGER_SWIPE_UP("gest_2f_up", "İki Parmak Yukarı", "✌️⬆️", "Çoklu Dokunuş"),
    TWO_FINGER_SWIPE_DOWN("gest_2f_down", "İki Parmak Aşağı", "✌️⬇️", "Çoklu Dokunuş"),
    PINCH_IN("gest_pinch_in", "Çimdikleme (Pinch In)", "🤏", "Çoklu Dokunuş"),
    THREE_FINGER_TAP("gest_3f_tap", "Üç Parmakla Dokunma", "🖐️", "Çoklu Dokunuş"),
    QUICK_SEQUENCE_A_B("gest_seq_ab", "Hızlı Buton Dizisi (A-B)", "🔲🔳", "Düğme Dizisi")
}

data class GestureBinding(
    val command: TacticalCommand,
    val gesture: PhysicalGesture,
    val isEnabled: Boolean = true
)

object TacticalGestureCatalog {
    fun createDefaultBindings(): List<GestureBinding> = listOf(
        GestureBinding(TacticalCommand.ATTACK_MOVE, PhysicalGesture.DOUBLE_TAP),
        GestureBinding(TacticalCommand.RETREAT, PhysicalGesture.TWO_FINGER_SWIPE_DOWN),
        GestureBinding(TacticalCommand.PATROL, PhysicalGesture.LONG_PRESS),
        GestureBinding(TacticalCommand.DEFENSIVE_STANCE, PhysicalGesture.TWO_FINGER_SWIPE_UP),
        GestureBinding(TacticalCommand.SCATTER, PhysicalGesture.PINCH_IN),
        GestureBinding(TacticalCommand.SELECT_ALL_COMBAT, PhysicalGesture.THREE_FINGER_TAP),
        GestureBinding(TacticalCommand.RADAR_PING, PhysicalGesture.QUICK_SEQUENCE_A_B)
    )
}
