package com.example.model

data class SkirmishMap(
    val id: String,
    val title: String,
    val geographicRegion: String,
    val historicalEra: Era,
    val description: String,
    val tacticalTrait: String,
    val terrainTheme: String,
    val ambientIcon: String,
    val accentColorHex: Long,
    val recommendedDoctrine: String,
    val baseWorldX: Float,
    val baseWorldY: Float,
    val enemyBaseWorldX: Float,
    val enemyBaseWorldY: Float,
    val terrainModifiers: List<String>
)

object SkirmishMapCatalog {
    val maps: List<SkirmishMap> = listOf(
        SkirmishMap(
            id = "map_mesopotamia",
            title = "Mezopotamya & Fırat-Dicle Vadisi",
            geographicRegion = "Bereketli Hilal • Ortadoğu",
            historicalEra = Era.MEDIEVAL_5TH,
            description = "Antik nehir yatakları, hurma vahaları ve çamurlu kıyı geçitleri. Açık bozkır arazisi geniş süvari akınlarına ve hızlı manevraya uygundur.",
            tacticalTrait = "Bozkır Süvarisi & Kuşatma Birimleri +%20 Hareket Hızı",
            terrainTheme = "Çöl Vahası & Çamur Nehir Yatağı",
            ambientIcon = "🏜️",
            accentColorHex = 0xFFF59E0B, // Amber
            recommendedDoctrine = "Atlı Akıncılar & Ağır Mancınık Kuşatması",
            baseWorldX = 460f,
            baseWorldY = 280f,
            enemyBaseWorldX = 1100f,
            enemyBaseWorldY = 320f,
            terrainModifiers = listOf("Hızlı Nehir Geçişi", "Geniş Manevra Sahası", "Düşük Siper Örtüsü")
        ),
        SkirmishMap(
            id = "map_constantinople",
            title = "Konstantinopolis & Boğaz Savunması",
            geographicRegion = "Marmara & Haliç • Boğaziçi",
            historicalEra = Era.CLASSICAL_GUNPOWDER,
            description = "Tarihi çifte sur hatları, deniz zinciri ve sahil tabyaları. Savunma tabyaları düşman kalyonlarına ve kuşatma toplarına karşı aşılmaz bir kalkan sunar.",
            tacticalTrait = "Sahil Tabyaları ve Karargah Zırhı +%30 Can (HP)",
            terrainTheme = "Tarihi Taş Duvarlar, Boğaz Suyu & Tabyalar",
            ambientIcon = "🏰",
            accentColorHex = 0xFF00E5FF, // Cyan
            recommendedDoctrine = "Şahi Topları & Zırhlı Donanma Kalyonları",
            baseWorldX = 420f,
            baseWorldY = 260f,
            enemyBaseWorldX = 1150f,
            enemyBaseWorldY = 360f,
            terrainModifiers = listOf("Boğaz Deniz Hattı", "Tahkimatlı Taş Siperler", "Dar Kuşatma Koridoru")
        ),
        SkirmishMap(
            id = "map_gallipoli",
            title = "Çanakkale & Gelibolu Yarımadası (1915)",
            geographicRegion = "Gelibolu Sırtları & Seddülbahir",
            historicalEra = Era.INDUSTRIAL,
            description = "Conkbayırı ve Anafartalar'ın dik sırtları, sahil çıkarma koyları ve mayınlı sular. Yüksek irtifa topçuları vadiye hakim atış avantajı sağlar.",
            tacticalTrait = "Tepe Topçuları +%25 Kritik Hasar • Savunma Direnci +%20",
            terrainTheme = "Yalçın Tepeler, Siper Hatları & Mayınlı Boğaz",
            ambientIcon = "🎖️",
            accentColorHex = 0xFFFF453A, // Crimson
            recommendedDoctrine = "Ağır Batarya Topçusu & Siper Piyadesi",
            baseWorldX = 440f,
            baseWorldY = 300f,
            enemyBaseWorldX = 1080f,
            enemyBaseWorldY = 240f,
            terrainModifiers = listOf("Yüksek İrtifa Siperleri", "Denizaltı Mayın Tarlası", "Dar Çıkarma Sahili")
        ),
        SkirmishMap(
            id = "map_normandy",
            title = "Normandiya Sahili & Atlantik Duvarı (1944)",
            geographicRegion = "Kuzey Fransa Kıyıları • Manş Denizi",
            historicalEra = Era.INDUSTRIAL,
            description = "Beton koruganlar (bunker), kumsal tanksavar engelleri ve bocage çitleri. Müttefik amfibi çıkarması ve tank fabrikaları için amansız bir zırhlı muharebe.",
            tacticalTrait = "Tank ve Zırhlı Birlik Üretim Süresi -%20 Hızlanır",
            terrainTheme = "Kumsal Bunkerları, Çit Tarlaları & Geniş Deniz",
            ambientIcon = "🌊",
            accentColorHex = 0xFF10B981, // Emerald
            recommendedDoctrine = "M1 Abrams / Sherman Zırhlı Hücumu & Hava Desteği",
            baseWorldX = 400f,
            baseWorldY = 320f,
            enemyBaseWorldX = 1120f,
            enemyBaseWorldY = 380f,
            terrainModifiers = listOf("Sahil Çıkarma Bölgesi", "Bocage Zırh Engelleri", "Açık Hava Koridoru")
        ),
        SkirmishMap(
            id = "map_caucasus",
            title = "Kafkasya & Dağlık Karabağ Boğazı",
            geographicRegion = "Sarp Kafkas Sıradağları • Aras Vadisi",
            historicalEra = Era.MODERN,
            description = "Sisli dağ geçitleri, derin nehir kanyonları ve radar gözetleme tepeleri. Derin vadiler, alçaktan uçan Bayraktar SİHA ve hayalet jetler için radar kör noktaları oluşturur.",
            tacticalTrait = "SİHA ve 5. Nesil Jet Gizlilik Katsayısı +%35 • Hava Saldırısı +%15",
            terrainTheme = "Sisli Dağ Zirveleri, Kanyonlar & Radar İstasyonları",
            ambientIcon = "⛰️",
            accentColorHex = 0xFF00F5D4, // Teal/Neon
            recommendedDoctrine = "Bayraktar TB3 / KIZILELMA Sürü Taarruzu & KAAN",
            baseWorldX = 480f,
            baseWorldY = 260f,
            enemyBaseWorldX = 1050f,
            enemyBaseWorldY = 340f,
            terrainModifiers = listOf("Kanyon Radar Kör Noktaları", "Dağ Geçidi Pusuları", "Köprü Boğum Noktası")
        ),
        SkirmishMap(
            id = "map_hormuz",
            title = "Hürmüz Boğazı & Basra Körfezi",
            geographicRegion = "Stratejik Petrol Geçidi • Umman Denizi",
            historicalEra = Era.MODERN,
            description = "Küresel petrol ticaretinin şahdamarı olan dar boğaz, yapay ada üsleri ve sığ resifler. Denizaltılar ve sahil füzeleri için kritik deniz muharebesi sahası.",
            tacticalTrait = "Petrol & Enerji Geliri +%40 • Reis Denizaltıları +%25 Menzil",
            terrainTheme = "Petrol Platformları, Tropik Adalar & Turkuaz Boğaz",
            ambientIcon = "🛢️",
            accentColorHex = 0xFF38BDF8, // Sky Blue
            recommendedDoctrine = "Reis & Virginia Sınıfı AIP Denizaltı & Balistik Füze",
            baseWorldX = 430f,
            baseWorldY = 350f,
            enemyBaseWorldX = 1140f,
            enemyBaseWorldY = 280f,
            terrainModifiers = listOf("Derin Denizaltı Hendekleri", "Petrol Platformu Gelirleri", "Ada Radar Kalkanı")
        )
    )
}
