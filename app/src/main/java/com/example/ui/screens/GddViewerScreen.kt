package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CrystalPurple
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EnergyYellow
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
fun GddViewerScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        "1. Dönem Geçişi",
        "2. Üretim Kuyruğu",
        "3. General Güçleri",
        "4. Dokunmatik & 100+ Birim",
        "5. C# MonoGame Kodu",
        "6. Yatay Mod & UI Çapa",
        "7. Kamera2D & Render Pipeline",
        "8. Asimetrik Ülke Seçimi & Minimap",
        "9. Skirmish AI & Asimetrik Fabrika",
        "10. 3D RTS Motoru & Raycasting",
        "11. Ses Yönetimi & Recharts Telemetri",
        "12. 3D Dünya Haritası & Kaynaklar",
        "13. 3D Varlıklar & GPU Instancing",
        "14. 20 Süper Güç & Gerçek Envanter",
        "15. Askeri Pazar & Diplomasi",
        "16. Birim XP, Rütbe & BattleLog"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceBlack)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = SurfaceDark,
            contentColor = CyanNeon,
            edgePadding = 12.dp,
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) CyanNeon else TextSecondary
                        )
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(14.dp)
        ) {
            when (selectedTab) {
                0 -> EraTransitionDesignSection()
                1 -> ProductionQueueDesignSection()
                2 -> GeneralsPowersDesignSection()
                3 -> TouchAndOptimizationSection()
                4 -> CsharpCodeSection()
                5 -> LandscapeAndAnchorSection()
                6 -> CameraAndRenderPipelineSection()
                7 -> FactionSelectAndMiniMapSection()
                8 -> SkirmishAiAndAsymmetricFactorySection()
                9 -> ThreeDRtsEngineAndRaycastingSection()
                10 -> SoundSystemAndTelemetrySection()
                11 -> InteractiveWorldMapAndResourceManagerSection()
                12 -> ThreeDAssetsAndInstancingSection()
                13 -> TwentySuperpowersAndInventorySection()
                14 -> MilitaryMarketAndDiplomacySection()
                15 -> UnitXpAndBattleLogSection()
            }
        }
    }
}

@Composable
private fun EraTransitionDesignSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RtsGddHeaderCard(
            title = "DÖNEM GEÇİŞİ (ERA TRANSITION) SİSTEMİ",
            subtitle = "5. Yüzyıl ➔ Klasik/Barut ➔ Endüstri Devrimi ➔ Modern Çağ",
            icon = Icons.Default.HistoryEdu,
            accentColor = MatrixGreen
        )

        RtsGddCard(title = "1. Çağ Yapısı ve Dinamik Yükseltme Mimarisi") {
            Text(
                text = "Oyunda çağ atlama işlemi Komuta Merkezi üzerinden tetiklenir. Her çağ belirli binaları ve birimleri açar:",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            EraTierItem(
                eraName = "I. 5. Yüzyıl (Orta Çağ Başlangıcı)",
                units = "• Piyade: Atlı Okçu, Şövalye, Mızraklı\n• Kuşatma: Karataş Mancınığı, Koçbaşı\n• Deniz: Kürekli Kadırga ve Hücum Sandalı",
                cost = "Başlangıç Çağı (Ücretsiz)",
                color = AmberGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            EraTierItem(
                eraName = "II. Klasik & Barut Dönemi (14. - 18. Yy)",
                units = "• Piyade: Yeniçeri Tüfekçisi, Amerikan Milisi, Streltsiy\n• Topçu: Şahi Kuşatma Topu, Döküm Bronz Top\n• Deniz: Zırhlı Kalyon, Kalyata, Bordalı Fırkateyn",
                cost = "800 Altın + 400 Teknoloji Puanı",
                color = EnergyYellow
            )
            Spacer(modifier = Modifier.height(6.dp))
            EraTierItem(
                eraName = "III. Endüstri Devrimi (I. & II. Dünya Savaşı)",
                units = "• Zırhlı: T-26, M4 Sherman, T-34/85, Type 59\n• Uçak: Spitfire, P-51 Mustang, Yak-9, IL-2\n• Deniz: Dumlupınar I (Alman Tip IX), Gato Denizaltısı",
                cost = "1800 Altın + 1000 Teknoloji Puanı",
                color = CyanNeon
            )
            Spacer(modifier = Modifier.height(6.dp))
            EraTierItem(
                eraName = "IV. Modern Çağ (Modern Warfare - 21. Yy)",
                units = "• Tank: Altay T2 (TR), M1A2 Abrams (ABD), T-90M/T-14 (RU), Type 99A2 (Çin), Leopard 2\n• 5. Nesil Jet: KAAN / KIZILELMA, F-22 Raptor, F-35, Su-57 Felon, J-20\n• Denizaltı: Reis Sınıfı (TCG Piri Reis AIP), Virginia Nükleer, Yasen-M",
                cost = "3500 Altın + 2200 Teknoloji Puanı",
                color = MatrixGreen
            )
        }

        RtsGddCard(title = "2. Otomatik Birim & Bina Evrimi (Auto-Evolution)") {
            Text(
                text = "Oyuncu çağ atladığında haritada bulunan eski çağ birimleri yok edilmez veya gereksiz kılınmaz:\n" +
                       "• Atlı Okçular ➔ Otomatik olarak Yeniçeriye, ardından Modern Özel Kuvvetlere evrilir.\n" +
                       "• Mancınıklar ➔ Şahi Topuna, ardından Modern Ana Muharebe Tankına (Altay/Abrams) dönüşür.\n" +
                       "• Kürekli Kadırgalar ➔ Zırhlı Kalyona, ardından Reis/Virginia sınıfı denizaltılara dönüştürülür.\n" +
                       "Bu mekanizma C&C Generals ve Empire Earth tarzı akıcı bir teknolojik ilerleme hissi yaşatır.",
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ProductionQueueDesignSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RtsGddHeaderCard(
            title = "C&C GENERALS ÜRETİM KUYRUĞU MİMARİSİ",
            subtitle = "5 Birimlik Dairesel Kuyruk, Timer ve Tip Kısıtlamaları",
            icon = Icons.Default.PrecisionManufacturing,
            accentColor = AmberGold
        )

        RtsGddCard(title = "1. Üretim Binası Kısıtlama Kuralları") {
            Text(
                text = "C&C Generals: Zero Hour mimarisinde her tesis kesin bir askeri uzmanlığa sahiptir:",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            RtsBullet(
                title = "🚜 Tank Fabrikası (War Factory):",
                desc = "YALNIZCA TANK VE ZIRHLI ARAÇ ÜRETİR. (Leopard 2, M1 Abrams, Altay T2, T-90M). Asla uçak veya denizaltı siparişi kabul etmez."
            )
            RtsBullet(
                title = "✈️ Hava Üssü (Airfield):",
                desc = "YALNIZCA SAVAŞ UÇAĞI VE JET ÜRETİR. (F-22 Raptor, F-35, Su-57, KAAN, KIZILELMA). Uçaklar piste iner, bakım görür ve kalkış yapar."
            )
            RtsBullet(
                title = "⚓ Tersane (Shipyard):",
                desc = "YALNIZCA DENİZALTI VE SAVAŞ GEMİSİ ÜRETİR. (Reis Sınıfı, Virginia, Yasen-M)."
            )
            RtsBullet(
                title = "🎖️ Kışla (Barracks):",
                desc = "YALNIZCA PİYADE VE KOMANDO BİRLİKLERİ EĞİTİR. (Bordo Bereliler, SEALs, Spetsnaz)."
            )
        }

        RtsGddCard(title = "2. Kuyruk (Queue) ve Zamanlayıcı (Timer) Dinamiği") {
            Text(
                text = "• Maksimum 5 Birim: Her bina en fazla 5 adet üretim emrini hafızada tutabilir.\n" +
                       "• FIFO (First-In, First-Out): Kuyruğun en başındaki (Peek) birim deltaTime ile düşürülür.\n" +
                       "• İade Mekanizması: Oyuncu kuyruktaki herhangi bir siparişi iptal ettiğinde harcanan altın anında oyuncu kasasına iade edilir.\n" +
                       "• Enerji Cezası: Güç santrali yetersizse (Brownout) üretim süresi 2 katına çıkar.",
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun GeneralsPowersDesignSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RtsGddHeaderCard(
            title = "GERÇEK ÜLKELER & GENERAL GÜÇLERİ (GENERALS POWERS)",
            subtitle = "Türkiye, ABD, Rusya, Çin yetenek ağacı ve dengeleme tablosu",
            icon = Icons.Default.MilitaryTech,
            accentColor = LaserCrimson
        )

        RtsGddCard(title = "1. Ülkelere Göre Çağlar Arası Yetenek Ağacı") {
            CountryPowerItem(
                country = "🇹🇷 TÜRKİYE",
                power5th = "Atlı Akını (Bozkır Süvarisi Taarruzu)",
                powerClassical = "Şahi Topu Kuşatma Barajı (Bronz Top Bombardımanı)",
                powerIndustrial = "Kuvayı Milliye Seferberliği (Anında Takviye)",
                powerModern = "Bayraktar TB2 / Akıncı SİHA Lazer Bombardımanı & TAYFUN Balistik Füzesi",
                accentColor = DangerRed
            )
            Spacer(modifier = Modifier.height(8.dp))
            CountryPowerItem(
                country = "🇺🇸 ABD",
                power5th = "Paralı Şövalye Lejyonu",
                powerClassical = "Donanma Topçu Desteği (Kıyı Bombardımanı)",
                powerIndustrial = "101. Hava İndirme İntikali (Paraşütçü Taburu)",
                powerModern = "A-10 Thunderbolt II (BRRRRT!) & B-2 Spirit MOAB Halı Bombardımanı",
                accentColor = CyanNeon
            )
            Spacer(modifier = Modifier.height(8.dp))
            CountryPowerItem(
                country = "🇷🇺 RUSYA",
                power5th = "Bojar Ağır Muhafız Hattı",
                powerClassical = "Çar Bombardıman Topu",
                powerIndustrial = "Katyuşa Roket Yaylım Ateşi (Stalin'in Orgu)",
                powerModern = "Su-57 Termobarik Vakum Bombası & İskender Hipersonik Füzesi",
                accentColor = AmberGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            CountryPowerItem(
                country = "🇨🇳 ÇİN",
                power5th = "Bambu Ateş Oku Barajı",
                powerClassical = "Ejder Savaş Topları",
                powerIndustrial = "Kızıl Muhafız İnsan Dalgası Seferberliği",
                powerModern = "EMP Nükleer Şok Dalgası (Tesis Felci) & DF-21D Uçak Gemisi Katili",
                accentColor = MatrixGreen
            )
        }

        RtsGddCard(title = "2. Cooldown ve Kaynak Maliyeti Dengeleme Tablosu") {
            TableDataRow(title = "Hafif Taarruz (SİHA / A-10 CAS):", cd = "35 Saniye", cost = "400 Altın", impact = "Tekil Zırhlı ve Binalara Kritik Hasar")
            TableDataRow(title = "Geniş Alan Barajı (Katyuşa / Topçu):", cd = "50 Saniye", cost = "450 Altın", impact = "Geniş Alana Yayılmış Piyade ve Hafif Araçlar")
            TableDataRow(title = "Stratejik Felç (EMP Şok Dalgası):", cd = "40 Saniye", cost = "380 Altın", impact = "15 Saniyeliğine Düşman Tesis ve Radarını Kapatma")
            TableDataRow(title = "Süper Silah (B-2 MOAB / TAYFUN / İskender):", cd = "65 Saniye", cost = "700 Altın", impact = "Üs Merkezini ve Ağır Tesisleri Yıkıcı Darbe")
        }
    }
}

@Composable
private fun TouchAndOptimizationSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RtsGddHeaderCard(
            title = "DOKUNMATİK SEÇİM & 100+ BİRİM OPTİMİZASYONLARI",
            subtitle = "Drag-to-select, Spatial Hash Grid ve Sıfır GC Nesne Havuzu",
            icon = Icons.Default.PhoneAndroid,
            accentColor = CyanNeon
        )

        RtsGddCard(title = "1. Android Dokunmatik Kontrolleri") {
            RtsBullet(
                title = "Kare İçine Alarak Çoklu Seçim (Drag-to-Select):",
                desc = "Kullanıcı ekrana dokunup parmağını sürüklediğinde yeşil seçim dikdörtgeni çizilir. Parmağını kaldırdığında MinX, MaxX, MinY, MaxY sınırları içindeki tüm dost tank, uçak ve denizaltılar seçilir."
            )
            RtsBullet(
                title = "Haritaya Dokunarak Hareket (Right-Click Taşınması):",
                desc = "Seçili birimler varken ekranda boş bir noktaya dokunulduğunda intikal hedefi (waypoint) oluşur. Birimler üst üste binmesin diye 'Formation Angle/Radius' formülüyle yayılır."
            )
        }

        RtsGddCard(title = "2. 100+ Birimde CPU ve Batarya Optimizasyon İlkeleri") {
            RtsBullet(
                title = "1. Uzamsal Izgara (Spatial Partitioning / Spatial Hash Grid):",
                desc = "100 birim birbirini kontrol ettiğinde O(N²) = 10,000 mesafe kontrolü CPU'yu kilitler. Harita 64x64 piksellik ızgaralara bölünür; birim sadece kendi ızgarasındaki 3-4 komşuyu kontrol eder. Karmaşıklık O(N) seviyesine iner."
            )
            RtsBullet(
                title = "2. Obje Havuzu (Object Pooling - Zero GC):",
                desc = "Ateşlenen mermiler, roketler ve patlama efektleri 'new' ve 'delete' edilmez. Önceden oluşturulmuş 'ObjectPool<Projectile>' havuzundan çekilip geri bırakılır. Android Garbage Collector takılmaları tamamen önlenir."
            )
            RtsBullet(
                title = "3. Dinamik Tick & Ekran Dışı LOD:",
                desc = "Kamera kadrajında olmayan birimlerin fizik ve çarpışma hesaplamaları saniyede 60 kare yerine saniyede 5 kareye çekilir. Bu işlemci ısınmasını ve batarya tüketimini %50 düşürür."
            )
        }
    }
}

@Composable
private fun CsharpCodeSection() {
    val context = LocalContext.current

    val eraManagerCode = """
// ============================================================================
// Proje: C&C Generals Epoch RTS - Dönem Geçişi Sistemi
// Dosya: EraManager.cs
// Motor: MonoGame (Android) & C# (Zero-GC Optimized)
// ============================================================================

using System;
using System.Collections.Generic;

namespace EpochRTS.Managers
{
    public enum EraType
    {
        Medieval5th,       // 5. Yüzyıl (Orta Çağ Başlangıcı)
        ClassicalGunpowder,// Klasik & Barut Dönemi
        Industrial,        // Endüstri Devrimi (Tanklar, Uçaklar)
        Modern             // Modern Çağ (5. Nesil Jetler, Nükleer Denizaltılar)
    }

    public class EraData
    {
        public EraType Era { get; set; }
        public string Title { get; set; }
        public int UpgradeCostGold { get; set; }
        public int UpgradeCostTech { get; set; }
        public List<string> AllowedStructures { get; set; } = new List<string>();
        public List<string> AllowedUnits { get; set; } = new List<string>();
    }

    public class EraManager
    {
        public EraType CurrentEra { get; private set; }
        private readonly Dictionary<EraType, EraData> _eras = new Dictionary<EraType, EraData>();

        // Otomatik modernizasyon tablosu (Eski birim -> Yeni birim ID)
        private readonly Dictionary<string, string> _unitEvolutionMap = new Dictionary<string, string>();

        public event Action<EraType> OnEraUpgraded;

        public EraManager()
        {
            InitializeEras();
        }

        private void InitializeEras()
        {
            CurrentEra = EraType.Medieval5th;

            // 1. 5. Yüzyıl
            _eras[EraType.Medieval5th] = new EraData
            {
                Era = EraType.Medieval5th,
                Title = "5. Yüzyıl (Orta Çağ Başlangıcı)",
                UpgradeCostGold = 0,
                UpgradeCostTech = 0,
                AllowedStructures = { "HQ", "Barracks", "SiegeWorkshop", "Dock" },
                AllowedUnits = { "HorseArcher", "Catapult", "Galley" }
            };

            // 2. Klasik Dönem
            _eras[EraType.ClassicalGunpowder] = new EraData
            {
                Era = EraType.ClassicalGunpowder,
                Title = "Klasik & Barut Dönemi",
                UpgradeCostGold = 800,
                UpgradeCostTech = 400,
                AllowedStructures = { "HQ", "Barracks", "CannonFoundry", "Shipyard" },
                AllowedUnits = { "Janissary", "GreatBombard", "Galleon" }
            };

            // 3. Endüstri Devrimi
            _eras[EraType.Industrial] = new EraData
            {
                Era = EraType.Industrial,
                Title = "Endüstri Devrimi",
                UpgradeCostGold = 1800,
                UpgradeCostTech = 1000,
                AllowedStructures = { "HQ", "PowerPlant", "Factory", "Airfield", "Shipyard" },
                AllowedUnits = { "ShermanTank", "Spitfire", "SubmarineIX" }
            };

            // 4. Modern Çağ
            _eras[EraType.Modern] = new EraData
            {
                Era = EraType.Modern,
                Title = "Modern Çağ (Modern Warfare)",
                UpgradeCostGold = 3500,
                UpgradeCostTech = 2200,
                AllowedStructures = { "HQ", "FusionPowerPlant", "WarFactory", "Airfield", "NavalShipyard" },
                AllowedUnits = { "M1Abrams", "AltayTank", "F22Raptor", "KAAN", "Su57", "VirginiaSubmarine", "ReisSubmarine" }
            };

            // Evrim Haritası
            _unitEvolutionMap["HorseArcher"] = "Janissary";
            _unitEvolutionMap["Janissary"] = "InfantryModern";
            _unitEvolutionMap["Catapult"] = "GreatBombard";
            _unitEvolutionMap["GreatBombard"] = "M1Abrams";
            _unitEvolutionMap["Galley"] = "Galleon";
            _unitEvolutionMap["Galleon"] = "VirginiaSubmarine";
            _unitEvolutionMap["ShermanTank"] = "M1Abrams";
            _unitEvolutionMap["Spitfire"] = "F22Raptor";
        }

        public bool CanUpgradeToNextEra(int playerGold, int playerTech)
        {
            EraType? next = GetNextEra(CurrentEra);
            if (!next.HasValue) return false;

            var nextData = _eras[next.Value];
            return playerGold >= nextData.UpgradeCostGold && playerTech >= nextData.UpgradeCostTech;
        }

        public bool UpgradeToNextEra(ref int playerGold, ref int playerTech, List<Unit> existingUnits)
        {
            EraType? next = GetNextEra(CurrentEra);
            if (!next.HasValue) return false;

            var nextData = _eras[next.Value];
            if (playerGold < nextData.UpgradeCostGold || playerTech < nextData.UpgradeCostTech) return false;

            playerGold -= nextData.UpgradeCostGold;
            playerTech -= nextData.UpgradeCostTech;
            CurrentEra = next.Value;

            // Haritadaki tüm eski birimleri yeni dönemin muadillerine otomatik dönüştür
            AutoEvolveUnits(existingUnits);

            OnEraUpgraded?.Invoke(CurrentEra);
            return true;
        }

        private void AutoEvolveUnits(List<Unit> units)
        {
            for (int i = 0; i < units.Count; i++)
            {
                var u = units[i];
                if (_unitEvolutionMap.TryGetValue(u.Name, out string newName))
                {
                    u.UpgradeToModernEquivalent(newName, CurrentEra);
                }
            }
        }

        private EraType? GetNextEra(EraType current)
        {
            return current switch
            {
                EraType.Medieval5th => EraType.ClassicalGunpowder,
                EraType.ClassicalGunpowder => EraType.Industrial,
                EraType.Industrial => EraType.Modern,
                _ => null
            };
        }
    }
}
""".trimIndent()

    val productionQueueCode = """
// ============================================================================
// Proje: C&C Generals Üretim Kuyruğu ve Bina Mimarisi
// Dosya: ProductionQueue.cs
// Motor: MonoGame (Android) & C# (OOP & Zero-GC)
// ============================================================================

using System;
using System.Collections.Generic;

namespace EpochRTS.Buildings
{
    public enum UnitType
    {
        Infantry,
        Tank,
        Jet,
        Submarine,
        Siege
    }

    public class Unit
    {
        public string Name { get; set; }
        public int Cost { get; set; }
        public float ProductionTime { get; set; }
        public UnitType Type { get; set; }
        public float Health { get; set; }
        public float MaxHealth { get; set; }
        public float Speed { get; set; }

        public void UpgradeToModernEquivalent(string newName, Managers.EraType era)
        {
            Name = newName;
            MaxHealth *= 1.4f;
            Health = MaxHealth;
            Speed *= 1.2f;
        }
    }

    public class ProductionOrder
    {
        public Unit UnitData { get; private set; }
        public float TimeRemaining { get; set; }
        public float TotalTime { get; private set; }

        public ProductionOrder(Unit unit)
        {
            UnitData = unit;
            TimeRemaining = unit.ProductionTime;
            TotalTime = unit.ProductionTime;
        }
    }

    /// <summary>
    /// C&C Generals tarzı temel bina sınıfı. Maksimum 5 siparişlik kuyruk içerir.
    /// </summary>
    public abstract class Building
    {
        public string BuildingName { get; protected set; }
        public const int MaxQueueCapacity = 5;

        // GC oluşturmamak için dairesel liste kullanımı
        protected readonly List<ProductionOrder> _queue = new List<ProductionOrder>(MaxQueueCapacity);

        // Bu binanın üretebileceği birim tipi kısıtlaması
        public abstract UnitType AllowedUnitType { get; }

        // Birim tamamlandığında haritaya intikal tetikleyicisi
        public event Action<Unit> OnUnitSpawned;

        public int QueueCount => _queue.Count;
        public IReadOnlyList<ProductionOrder> Queue => _queue;

        /// <summary>
        /// Kuyruğa birim siparişi ekler (Maks 5 kontrolü, tip kontrolü ve fon kontrolü)
        /// </summary>
        public bool EnqueueUnit(Unit unit, ref int playerMoney)
        {
            if (unit.Type != AllowedUnitType)
            {
                // Tip Kısıtlaması: Tank Fabrikası uçak üretemez!
                return false;
            }

            if (_queue.Count >= MaxQueueCapacity)
            {
                return false;
            }

            if (playerMoney < unit.Cost)
            {
                return false;
            }

            playerMoney -= unit.Cost;
            _queue.Add(new ProductionOrder(unit));
            return true;
        }

        /// <summary>
        /// MonoGame ana Update döngüsünden deltaTime ile çağrılır.
        /// Kuyruğun en başındaki siparişin süresini eritir.
        /// </summary>
        public virtual void UpdateProduction(float deltaTime, float powerEfficiency = 1.0f)
        {
            if (_queue.Count == 0) return;

            var currentOrder = _queue[0];
            currentOrder.TimeRemaining -= (deltaTime * powerEfficiency);

            if (currentOrder.TimeRemaining <= 0f)
            {
                _queue.RemoveAt(0);
                OnUnitSpawned?.Invoke(currentOrder.UnitData);
            }
        }

        /// <summary>
        /// Oyuncu kuyruktaki bir üretime sağ tıkladığında/iptal ettiğinde parayı iade eder.
        /// </summary>
        public bool CancelOrder(int index, ref int playerMoney)
        {
            if (index < 0 || index >= _queue.Count) return false;

            var canceledOrder = _queue[index];
            playerMoney += canceledOrder.UnitData.Cost; // İade et
            _queue.RemoveAt(index);
            return true;
        }
    }

    // ==================== TÜRETİLMİŞ ÖZEL BİNA SINIFLARI ====================

    /// <summary>
    /// SADECE TANK ÜRETİR (Leopard 2, M1 Abrams, Altay, T-90)
    /// </summary>
    public class WarFactory : Building
    {
        public override UnitType AllowedUnitType => UnitType.Tank;

        public WarFactory()
        {
            BuildingName = "Tank Fabrikası (War Factory)";
        }
    }

    /// <summary>
    /// SADECE SAVAŞ UÇAĞI VE JET ÜRETİR (F-22, F-35, Su-57, KAAN)
    /// </summary>
    public class Airfield : Building
    {
        public override UnitType AllowedUnitType => UnitType.Jet;

        public Airfield()
        {
            BuildingName = "Hava Üssü (Airfield)";
        }
    }

    /// <summary>
    /// SADECE DENİZALTI VE GEMİ ÜRETİR (Reis Sınıfı, Virginia, Yasen-M)
    /// </summary>
    public class Shipyard : Building
    {
        public override UnitType AllowedUnitType => UnitType.Submarine;

        public Shipyard()
        {
            BuildingName = "Tersane (Shipyard)";
        }
    }
}
""".trimIndent()

    val touchOptimizationCode = """
// ============================================================================
// Proje: Android Dokunmatik Seçim & 100+ Birim Spatial Partitioning
// Dosya: TouchSelectionAndSpatialGrid.cs
// Motor: MonoGame (Android) & C# (60 FPS Battery Saver)
// ============================================================================

using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Input.Touch;

namespace EpochRTS.Optimization
{
    /// <summary>
    /// Android çoklu birim sürükleyerek seçme (Drag-to-Select) ve hareket ettirme sistemi
    /// </summary>
    public class TouchRtsController
    {
        private Vector2? _dragStart;
        private Vector2? _dragCurrent;
        public Rectangle SelectionBox { get; private set; }
        public bool IsDragging { get; private set; }

        public List<int> SelectedUnitIds { get; } = new List<int>();

        public void UpdateTouch(TouchCollection touches, SpatialGrid grid)
        {
            if (touches.Count == 0)
            {
                if (IsDragging && _dragStart.HasValue && _dragCurrent.HasValue)
                {
                    // Parmak kalktı: Seçim kutusunu hesapla
                    SelectionBox = CalculateBox(_dragStart.Value, _dragCurrent.Value);
                    SelectedUnitIds.Clear();

                    // Spatial grid üzerinden kutu içindeki birimleri hızlıca çek
                    grid.QueryBox(SelectionBox, SelectedUnitIds);
                }

                IsDragging = false;
                _dragStart = null;
                _dragCurrent = null;
                return;
            }

            TouchLocation touch = touches[0];

            if (touch.State == TouchLocationState.Pressed)
            {
                _dragStart = touch.Position;
                _dragCurrent = touch.Position;
                IsDragging = true;
            }
            else if (touch.State == TouchLocationState.Moved && IsDragging)
            {
                _dragCurrent = touch.Position;
                SelectionBox = CalculateBox(_dragStart.Value, _dragCurrent.Value);
            }
        }

        private Rectangle CalculateBox(Vector2 p1, Vector2 p2)
        {
            int minX = (int)Math.Min(p1.X, p2.X);
            int maxX = (int)Math.Max(p1.X, p2.X);
            int minY = (int)Math.Min(p1.Y, p2.Y);
            int maxY = (int)Math.Max(p1.Y, p2.Y);
            return new Rectangle(minX, minY, maxX - minX, maxY - minY);
        }
    }

    /// <summary>
    /// 100+ birimde O(N²) mesafe kontrollerini O(N) seviyesine çeken Uzamsal Izgara
    /// </summary>
    public class SpatialGrid
    {
        private readonly int _cellSize = 64; // Hücre piksel boyutu
        private readonly Dictionary<int, List<int>> _cells = new Dictionary<int, List<int>>();

        public void Clear()
        {
            foreach (var cell in _cells.Values)
            {
                cell.Clear();
            }
        }

        public void InsertUnit(int unitId, Vector2 position)
        {
            int cellHash = GetHash((int)position.X, (int)position.Y);
            if (!_cells.TryGetValue(cellHash, out var list))
            {
                list = new List<int>(8);
                _cells[cellHash] = list;
            }
            list.Add(unitId);
        }

        public void QueryBox(Rectangle box, List<int> results)
        {
            int startX = box.Left / _cellSize;
            int endX = box.Right / _cellSize;
            int startY = box.Top / _cellSize;
            int endY = box.Bottom / _cellSize;

            for (int x = startX; x <= endX; x++)
            {
                for (int y = startY; y <= endY; y++)
                {
                    int key = (x * 73856093) ^ (y * 19349663);
                    if (_cells.TryGetValue(key, out var cellList))
                    {
                        results.AddRange(cellList);
                    }
                }
            }
        }

        private int GetHash(int x, int y)
        {
            return ((x / _cellSize) * 73856093) ^ ((y / _cellSize) * 19349663);
        }
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        RtsGddHeaderCard(
            title = "C# MONOGAME KOD KÜTÜPHANESİ",
            subtitle = "Android optimize edilmiş üretim kuyruğu, dönem yöneticisi ve dokunmatik kontrol mimarisi",
            icon = Icons.Default.Code,
            accentColor = CyanNeon
        )

        // Module 1: EraManager.cs
        CodeDisplayCard(
            fileName = "EraManager.cs (Dönem Geçişi & Otomatik Birim Evrimi)",
            description = "4 Çağ tanımları, AllowedStructures, AllowedUnits ve UpgradeToNextEra mantığı",
            code = eraManagerCode,
            onCopy = {
                val clip = ClipData.newPlainText("EraManager.cs", eraManagerCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "EraManager.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Module 2: ProductionQueue.cs
        CodeDisplayCard(
            fileName = "ProductionQueue.cs (C&C Generals Üretim Kuyruğu)",
            description = "Unit, ProductionOrder, 5'lik kuyruk, WarFactory, Airfield, Shipyard kısıtlamaları",
            code = productionQueueCode,
            onCopy = {
                val clip = ClipData.newPlainText("ProductionQueue.cs", productionQueueCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "ProductionQueue.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Module 3: TouchSelectionAndSpatialGrid.cs
        CodeDisplayCard(
            fileName = "TouchSelectionAndSpatialGrid.cs (Dokunmatik & 100+ Birim)",
            description = "Drag-to-select, parmak hareket ettirme ve Spatial Hash Grid optimizasyonu",
            code = touchOptimizationCode,
            onCopy = {
                val clip = ClipData.newPlainText("TouchSelectionAndSpatialGrid.cs", touchOptimizationCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "TouchSelectionAndSpatialGrid.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// Reusable Helper Components for GDD Screen
@Composable
private fun RtsGddHeaderCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.5f), SurfaceBorder)))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = TextSecondary, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun RtsGddCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SurfaceBorder, SurfaceElevated)))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun EraTierItem(eraName: String, units: String, cost: String, color: Color) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = eraName, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = cost, color = AmberLight, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = units, color = TextPrimary, fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun CountryPowerItem(
    country: String,
    power5th: String,
    powerClassical: String,
    powerIndustrial: String,
    powerModern: String,
    accentColor: Color
) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = country, color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "• 5. Yy: $power5th", color = TextSecondary, fontSize = 10.sp)
            Text(text = "• Klasik: $powerClassical", color = TextSecondary, fontSize = 10.sp)
            Text(text = "• Endüstri: $powerIndustrial", color = TextSecondary, fontSize = 10.sp)
            Text(text = "• Modern Çağ: $powerModern", color = MatrixGreen, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TableDataRow(title: String, cd: String, cost: String, impact: String) {
    Surface(
        color = SurfaceElevated,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = impact, color = TextSecondary, fontSize = 9.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = cost, color = AmberGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(text = cd, color = CyanNeon, fontSize = 9.sp)
            }
        }
    }
}

@Composable
private fun RtsBullet(title: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(text = title, color = AmberLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(text = desc, color = TextSecondary, fontSize = 10.sp, lineHeight = 15.sp)
    }
}

@Composable
fun CodeDisplayCard(
    fileName: String,
    description: String,
    code: String,
    onCopy: () -> Unit
) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = fileName, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = description, color = CyanNeon, fontSize = 9.sp)
                }

                Button(
                    onClick = onCopy,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanNeon),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("copy_${fileName.split(".").first()}")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = SpaceBlack, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Kopyala", color = SpaceBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(SpaceBlack)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(6.dp))
                    .padding(8.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    text = code,
                    color = Color(0xFF80D8FF),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
private fun LandscapeAndAnchorSection() {
    val context = LocalContext.current
    var selectedAspect by remember { mutableStateOf("20:9 (2400x1080)") }

    val activity1Code = """
// ============================================================================
// Dosya: Activity1.cs (Android Yatay Başlatma ve Tam Ekran Kilitleme)
// Motor: MonoGame (Android) & C#
// ============================================================================

using Android.App;
using Android.Content.PM;
using Android.OS;
using Android.Views;
using Microsoft.Xna.Framework;

namespace EpochRTS
{
    [Activity(
        Label = "EpochRTS",
        MainLauncher = true,
        Icon = "@drawable/icon",
        AlwaysRetainTaskState = true,
        LaunchMode = LaunchMode.SingleInstance,
        // Cihaz döndürülse bile yalnızca Yatay (LandscapeLeft veya LandscapeRight) modda kilitler:
        ScreenOrientation = ScreenOrientation.SensorLandscape,
        // Oryantasyon ve ekran boyutu değiştiğinde Activity'nin yeniden başlatılmasını önler:
        ConfigurationChanges = ConfigChanges.Orientation | 
                               ConfigChanges.Keyboard | 
                               ConfigChanges.KeyboardHidden | 
                               ConfigChanges.ScreenSize | 
                               ConfigChanges.ScreenLayout |
                               ConfigChanges.UiMode
    )]
    public class Activity1 : AndroidGameActivity
    {
        private Game1 _game;
        private View _view;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            _game = new Game1();
            _view = _game.Services.GetService(typeof(View)) as View;
            SetContentView(_view);

            // Çentik (Cutout) ve Navigasyon Çubuğunu Gizleyerek Gerçek Tam Ekran Yap
            ApplyImmersiveFullScreen();

            _game.Run();
        }

        public override void OnWindowFocusChanged(bool hasFocus)
        {
            base.OnWindowFocusChanged(hasFocus);
            if (hasFocus)
            {
                ApplyImmersiveFullScreen();
            }
        }

        private void ApplyImmersiveFullScreen()
        {
            if (Window == null) return;

            // Android Immersive Sticky: Kullanıcı ekranı kaydırsa bile barlar 3 saniye sonra otomatik kaybolur
            Window.DecorView.SystemUiVisibility = (StatusBarVisibility)(
                SystemUiFlags.LayoutStable |
                SystemUiFlags.LayoutHideNavigation |
                SystemUiFlags.LayoutFullScreen |
                SystemUiFlags.HideNavigation |
                SystemUiFlags.FullScreen |
                SystemUiFlags.ImmersiveSticky
            );

            // Android 9+ (API 28) Çentik (Notch/Cutout) Alanına Taşma Desteği
            if (Build.VERSION.SdkInt >= BuildVersionCodes.P)
            {
                Window.Attributes.LayoutInDisplayCutoutMode = LayoutInDisplayCutoutMode.ShortEdges;
            }
        }
    }
}
""".trimIndent()

    val game1Code = """
// ============================================================================
// Dosya: Game1.cs (Donanımsal Yatay Çözünürlük ve GraphicsDeviceManager)
// Motor: MonoGame (Android) & C#
// ============================================================================

using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace EpochRTS
{
    public class Game1 : Game
    {
        private GraphicsDeviceManager _graphics;
        private SpriteBatch _spriteBatch;

        public Game1()
        {
            _graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
            IsMouseVisible = true;

            // 1. Desteklenen Oryantasyonları Yatay Olarak Belirle
            _graphics.SupportedOrientations = DisplayOrientation.LandscapeLeft | DisplayOrientation.LandscapeRight;

            // 2. Cihazın Gerçek Tam Ekran Donanım Çözünürlüğünü Yakala
            // (GraphicsAdapter.DefaultAdapter.CurrentDisplayMode doğrudan telefonun fiziksel panel çözünürlüğünü verir)
            DisplayMode displayMode = GraphicsAdapter.DefaultAdapter.CurrentDisplayMode;
            _graphics.PreferredBackBufferWidth = displayMode.Width;
            _graphics.PreferredBackBufferHeight = displayMode.Height;
            _graphics.IsFullScreen = true;

            // 3. Değişiklikleri Grafik Kartına Uygula
            _graphics.ApplyChanges();
        }

        protected override void Initialize()
        {
            // Ekran boyutu değiştiğinde (örn: katlanabilir telefonlar açıldığında)
            Window.ClientSizeChanged += OnClientSizeChanged;

            // UI Yöneticisine donanım çözünürlüğünü bildir
            UIManager.Instance.UpdateScreenDimensions(GraphicsDevice.Viewport.Width, GraphicsDevice.Viewport.Height);

            base.Initialize();
        }

        private void OnClientSizeChanged(object sender, EventArgs e)
        {
            UIManager.Instance.UpdateScreenDimensions(GraphicsDevice.Viewport.Width, GraphicsDevice.Viewport.Height);
        }

        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.Black);

            // 1. SAVAŞ ALANI (Taktik Harita - Virtual Matrix ile Orantılı Çizim)
            _spriteBatch.Begin(
                SpriteSortMode.Deferred, 
                BlendState.AlphaBlend, 
                SamplerState.PointClamp, 
                null, null, null, 
                UIManager.Instance.WorldMatrix
            );
            // Harita ve tank/jet birimlerini çiz...
            _spriteBatch.End();

            // 2. KULLANICI ARAYÜZÜ (Çapa/Anchor Tabanlı HUD - Ekrana Tam Yayılan Çizim)
            _spriteBatch.Begin(SpriteSortMode.Deferred, BlendState.AlphaBlend);
            UIManager.Instance.DrawHUD(_spriteBatch);
            _spriteBatch.End();

            base.Draw(gameTime);
        }
    }
}
""".trimIndent()

    val uiManagerCode = """
// ============================================================================
// Dosya: UIManager.cs (Dinamik Çapa / Anchor ve Virtual Matrix Sistemi)
// Motor: MonoGame (Android) & C#
// ============================================================================

using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace EpochRTS
{
    public enum UIAnchor
    {
        TopLeft,      // Kaynak Sayaçları (Altın, Enerji, Teknoloji)
        TopCenter,    // Dönem Başlığı & Zaman
        TopRight,     // General Güçleri Butonları & Ayarlar
        BottomLeft,   // Mini-Harita & Taktik Radar
        BottomCenter, // Çoklu Birim Komuta Paneli
        BottomRight,  // Hızlı İnşaat & Fabrika Üretim Kuyruğu
        Center        // Menü & Modal Pencereleri
    }

    public class UIManager
    {
        public static UIManager Instance { get; } = new UIManager();

        public int ScreenWidth { get; private set; }
        public int ScreenHeight { get; private set; }

        // Sanal Referans Çözünürlük (Tasarım 1920x1080 baz alınarak hazırlanır)
        public const int VirtualWidth = 1920;
        public const int VirtualHeight = 1080;

        public Matrix WorldMatrix { get; private set; }
        public float AspectRatio => (float)ScreenWidth / ScreenHeight;

        public void UpdateScreenDimensions(int width, int height)
        {
            ScreenWidth = width;
            ScreenHeight = height;

            // 1. Savaş Alanı İçin Orantılı Ölçekleme Matrisi (Hiçbir şekilde basıklık/esneme yapmaz)
            float scaleX = (float)width / VirtualWidth;
            float scaleY = (float)height / VirtualHeight;
            float uniformScale = Math.Min(scaleX, scaleY);

            WorldMatrix = Matrix.CreateScale(uniformScale, uniformScale, 1.0f);
        }

        /// <summary>
        /// Ekran 16:9, 18:9 veya 20:9 olsa dahi bileşenleri ekran kenarlarına sabitler
        /// </summary>
        public Vector2 GetAnchorPosition(UIAnchor anchor, Vector2 offsetFromCorner)
        {
            // Çentik / Ekran Kenar Boşluğu (Safe Margin)
            float safeMarginX = 40f;
            float safeMarginY = 24f;

            return anchor switch
            {
                UIAnchor.TopLeft => new Vector2(safeMarginX + offsetFromCorner.X, safeMarginY + offsetFromCorner.Y),
                UIAnchor.TopCenter => new Vector2(ScreenWidth / 2f + offsetFromCorner.X, safeMarginY + offsetFromCorner.Y),
                UIAnchor.TopRight => new Vector2(ScreenWidth - safeMarginX - offsetFromCorner.X, safeMarginY + offsetFromCorner.Y),
                UIAnchor.BottomLeft => new Vector2(safeMarginX + offsetFromCorner.X, ScreenHeight - safeMarginY - offsetFromCorner.Y),
                UIAnchor.BottomCenter => new Vector2(ScreenWidth / 2f + offsetFromCorner.X, ScreenHeight - safeMarginY - offsetFromCorner.Y),
                UIAnchor.BottomRight => new Vector2(ScreenWidth - safeMarginX - offsetFromCorner.X, ScreenHeight - safeMarginY - offsetFromCorner.Y),
                UIAnchor.Center => new Vector2(ScreenWidth / 2f + offsetFromCorner.X, ScreenHeight / 2f + offsetFromCorner.Y),
                _ => offsetFromCorner
            };
        }

        public void DrawHUD(SpriteBatch spriteBatch)
        {
            // Örnek: Kaynak Barı her zaman sol üst köşeye (TopLeft) sabitlenir
            Vector2 resourcePos = GetAnchorPosition(UIAnchor.TopLeft, Vector2.Zero);
            // DrawResourceBar(spriteBatch, resourcePos);

            // Örnek: Hızlı İnşa Menüsü her zaman sağ alt köşeye (BottomRight) sabitlenir
            Vector2 buildMenuPos = GetAnchorPosition(UIAnchor.BottomRight, new Vector2(160, 80));
            // DrawBuildMenu(spriteBatch, buildMenuPos);

            // Örnek: General Güçleri sağ üst köşeye (TopRight) sabitlenir
            Vector2 generalPowersPos = GetAnchorPosition(UIAnchor.TopRight, new Vector2(280, 0));
            // DrawGeneralsPowers(spriteBatch, generalPowersPos);
        }
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RtsGddHeaderCard(
            title = "MONOGAME ANDROID YATAY (LANDSCAPE) MOD & UI ÇAPA REHBERİ",
            subtitle = "Clash of Clans & Rise of Kingdoms tarzı tam ekran yatay kurgu ve çözünürlük ölçekleme",
            icon = Icons.Default.Devices,
            accentColor = CyanNeon
        )

        // Interactive Aspect Ratio Simulator Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AmberGold.copy(alpha = 0.5f), CyanNeon.copy(alpha = 0.3f))))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "CANLI YATAY ÇÖZÜNÜRLÜK & ÇAPA (ANCHOR) SİMÜLATÖRÜ",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Farklı telefon oranlarını seçerek UI elemanlarının kenarlara nasıl kilitlendiğini inceleyin:",
                    color = TextSecondary,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Aspect Ratio Selector Pills
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("16:9 (1920x1080)", "18:9 (2160x1080)", "20:9 (2400x1080)").forEach { aspect ->
                        val isSel = aspect == selectedAspect
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) CyanNeon.copy(alpha = 0.25f) else SurfaceElevated)
                                .border(1.dp, if (isSel) CyanNeon else SurfaceBorder, RoundedCornerShape(6.dp))
                                .clickable { selectedAspect = aspect }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = aspect,
                                color = if (isSel) CyanNeon else TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Visual Mockup of Landscape Screen with Anchors
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpaceBlack)
                        .border(1.5.dp, SurfaceBorder, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    // TopLeft Anchor (Resources)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(4.dp))
                            .background(AmberGold.copy(alpha = 0.25f))
                            .border(1.dp, AmberGold, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(text = "TopLeft: 🟡 4500A | ⚡ OK", color = AmberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    // TopRight Anchor (Generals Powers)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(4.dp))
                            .background(LaserCrimson.copy(alpha = 0.25f))
                            .border(1.dp, LaserCrimson, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(text = "TopRight: 🚀 SİHA / A-10", color = LaserCrimson, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    // Center Battlefield
                    Box(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⚔️ Taktik Savaş Alanı ($selectedAspect)", color = TextSecondary, fontSize = 10.sp)
                            Text(text = "Virtual Matrix: %0 Esneme, Genişletilmiş Görüş Alanı", color = MatrixGreen, fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // BottomLeft Anchor (Minimap)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(38.dp, 28.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(CyanNeon.copy(alpha = 0.2f))
                            .border(1.dp, CyanNeon, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Radar", color = CyanNeon, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }

                    // BottomRight Anchor (Build & Factory Queue)
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MatrixGreen.copy(alpha = 0.25f))
                            .border(1.dp, MatrixGreen, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(text = "BottomRight: 🏭 Fabrika (5/5)", color = MatrixGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Code Card 1: Activity1.cs
        CodeDisplayCard(
            fileName = "Activity1.cs (Yatay Mod Kilitleme & Immersive FullScreen)",
            description = "ScreenOrientation.SensorLandscape ve LayoutInDisplayCutoutMode ile tam ekran",
            code = activity1Code,
            onCopy = {
                val clip = ClipData.newPlainText("Activity1.cs", activity1Code)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Activity1.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 2: Game1.cs
        CodeDisplayCard(
            fileName = "Game1.cs (GraphicsDeviceManager Yatay Çözünürlük)",
            description = "SupportedOrientations, PreferredBackBufferWidth/Height ve IsFullScreen",
            code = game1Code,
            onCopy = {
                val clip = ClipData.newPlainText("Game1.cs", game1Code)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Game1.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 3: UIManager.cs
        CodeDisplayCard(
            fileName = "UIManager.cs (Dinamik Çapa / Anchor & Virtual Matrix)",
            description = "16:9, 18:9, 20:9 ekranlarda kayma yapmayan UI çapa sistemi ve Matrix",
            code = uiManagerCode,
            onCopy = {
                val clip = ClipData.newPlainText("UIManager.cs", uiManagerCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "UIManager.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun CameraAndRenderPipelineSection() {
    val context = LocalContext.current

    val camera2dCode = """
// ============================================================================
// Camera2D.cs - Android Çoklu Dokunmatik Destekli RTS Kamera Sistemi (MonoGame)
// Destekler: 1-Parmak Pan (Sürükleme), 2-Parmak Pinch (Yakınlaştırma), Sınır Kilitleme
// ============================================================================
using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input.Touch;

namespace NovaBase.Engine
{
    public class Camera2D
    {
        private float _zoom;
        private float _minZoom = 0.5f;
        private float _maxZoom = 2.0f;
        private Vector2 _position;
        private Rectangle? _worldBounds;
        private readonly Viewport _viewport;

        // Dokunmatik Takip Değişkenleri
        private float _previousPinchDistance = 0f;
        private Vector2 _lastPanPosition = Vector2.Zero;
        private bool _isPanning = false;

        public Vector2 Position
        {
            get => _position;
            set
            {
                _position = value;
                ClampToBounds();
            }
        }

        public float Zoom
        {
            get => _zoom;
            set
            {
                _zoom = MathHelper.Clamp(value, _minZoom, _maxZoom);
                ClampToBounds();
            }
        }

        public Camera2D(Viewport viewport, Rectangle? worldBounds = null)
        {
            _viewport = viewport;
            _worldBounds = worldBounds;
            _zoom = 1.0f;
            _position = new Vector2(viewport.Width / 2f, viewport.Height / 2f);

            // Android Dokunma Jestlerini Aktif Et
            TouchPanel.EnabledGestures = GestureType.FreeDrag | GestureType.Pinch | GestureType.PinchComplete | GestureType.Tap;
        }

        public void Update(GameTime gameTime)
        {
            TouchCollection touchCollection = TouchPanel.GetState();

            // 1. PINCH-TO-ZOOM (İki parmakla yakınlaştırma/uzaklaştırma)
            if (touchCollection.Count == 2)
            {
                Vector2 touch1 = touchCollection[0].Position;
                Vector2 touch2 = touchCollection[1].Position;
                float currentPinchDistance = Vector2.Distance(touch1, touch2);

                if (_previousPinchDistance > 0f)
                {
                    float deltaDistance = currentPinchDistance - _previousPinchDistance;
                    float zoomDelta = deltaDistance * 0.0035f;
                    Zoom += zoomDelta;
                }
                _previousPinchDistance = currentPinchDistance;
                _isPanning = false;
            }
            // 2. PAN (Tek parmakla haritada kaydırma)
            else if (touchCollection.Count == 1)
            {
                _previousPinchDistance = 0f;
                TouchLocation touch = touchCollection[0];

                if (touch.State == TouchLocationState.Pressed)
                {
                    _lastPanPosition = touch.Position;
                    _isPanning = true;
                }
                else if (touch.State == TouchLocationState.Moved && _isPanning)
                {
                    Vector2 delta = (_lastPanPosition - touch.Position) / _zoom;
                    Position += delta;
                    _lastPanPosition = touch.Position;
                }
                else if (touch.State == TouchLocationState.Released)
                {
                    _isPanning = false;
                }
            }
            else
            {
                _previousPinchDistance = 0f;
                _isPanning = false;
            }
        }

        // Kamera Matrisi: Dünya koordinatlarını ekran koordinatlarına dönüştürür
        public Matrix GetViewMatrix()
        {
            return Matrix.CreateTranslation(new Vector3(-_position.X, -_position.Y, 0f)) *
                   Matrix.CreateScale(new Vector3(_zoom, _zoom, 1f)) *
                   Matrix.CreateTranslation(new Vector3(_viewport.Width * 0.5f, _viewport.Height * 0.5f, 0f));
        }

        // Ekrandaki dokunma noktasını oyun dünyasındaki koordinatlara çevirir (Raycasting/Picking)
        public Vector2 ScreenToWorld(Vector2 screenPosition)
        {
            return Vector2.Transform(screenPosition, Matrix.Invert(GetViewMatrix()));
        }

        // Dünya koordinatını ekran pikseline çevirir (HUD ve mini-sağlık çubukları için)
        public Vector2 WorldToScreen(Vector2 worldPosition)
        {
            return Vector2.Transform(worldPosition, GetViewMatrix());
        }

        private void ClampToBounds()
        {
            if (!_worldBounds.HasValue) return;

            Rectangle bounds = _worldBounds.Value;
            float visibleWidth = _viewport.Width / _zoom;
            float visibleHeight = _viewport.Height / _zoom;

            float minX = bounds.Left + visibleWidth * 0.5f;
            float maxX = bounds.Right - visibleWidth * 0.5f;
            float minY = bounds.Top + visibleHeight * 0.5f;
            float maxY = bounds.Bottom - visibleHeight * 0.5f;

            if (minX > maxX) minX = maxX = (bounds.Left + bounds.Right) * 0.5f;
            if (minY > maxY) minY = maxY = (bounds.Top + bounds.Bottom) * 0.5f;

            _position.X = MathHelper.Clamp(_position.X, minX, maxX);
            _position.Y = MathHelper.Clamp(_position.Y, minY, maxY);
        }
    }
}
""".trimIndent()

    val mapGridCode = """
// ============================================================================
// MapGrid.cs - RTS Harita, Izgara (Tilemap) ve Bina Yerleşim Sistemi
// Binaların X, Y ayak izlerini ve zemin engellerini yönetir.
// ============================================================================
using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace NovaBase.Engine
{
    public enum TileType
    {
        Grass,      // Çimen: Binalar ve kara birimleri için uygun
        Road,       // Yol: Tanklar için +%30 hareket hızı sağlar
        River,      // Nehir: Yalnızca Tersane ve Denizaltılar kullanabilir
        Concrete    // Beton zemin: Üs inşa alanı
    }

    public class MapGrid
    {
        public const int TileSize = 64; // Her kare 64x64 piksel
        public int Columns { get; }
        public int Rows { get; }

        private readonly TileType[,] _tiles;
        private readonly bool[,] _occupied;

        public MapGrid(int columns, int rows)
        {
            Columns = columns;
            Rows = rows;
            _tiles = new TileType[columns, rows];
            _occupied = new bool[columns, rows];

            InitializeDefaultTerrain();
        }

        private void InitializeDefaultTerrain()
        {
            for (int x = 0; x < Columns; x++)
            {
                for (int y = 0; y < Rows; y++)
                {
                    _tiles[x, y] = TileType.Grass;
                    _occupied[x, y] = false;

                    // Sağ taraftaki dikey nehir kanalı (Denizaltı rotası)
                    if (x >= Columns - 6)
                        _tiles[x, y] = TileType.River;

                    // Ana üs merkezindeki beton zemin
                    if (x >= 4 && x <= 16 && y >= 4 && y <= 14)
                        _tiles[x, y] = TileType.Concrete;
                }
            }
        }

        // BİNANIN HARİTAYA KOYULUP KOYULAMAYACAĞINI DENETLE
        public bool CanPlaceStructure(int gridX, int gridY, int widthInTiles, int heightInTiles, bool requiresWater = false)
        {
            if (gridX < 0 || gridY < 0 || gridX + widthInTiles > Columns || gridY + heightInTiles > Rows)
                return false;

            for (int x = gridX; x < gridX + widthInTiles; x++)
            {
                for (int y = gridY; y < gridY + heightInTiles; y++)
                {
                    if (_occupied[x, y]) return false;

                    if (requiresWater && _tiles[x, y] != TileType.River)
                        return false; // Tersane sadece suya kurulabilir

                    if (!requiresWater && _tiles[x, y] == TileType.River)
                        return false; // Kara binaları nehre kurulamaz
                }
            }
            return true;
        }

        // BİNAYI HARİTAYA KAYDET VE AYAK İZİNİ DOLDUR
        public void OccupyCells(int gridX, int gridY, int widthInTiles, int heightInTiles)
        {
            for (int x = gridX; x < gridX + widthInTiles; x++)
                for (int y = gridY; y < gridY + heightInTiles; y++)
                    _occupied[x, y] = true;
        }

        // Dünya piksel koordinatını ızgara indeksine dönüştür
        public Point WorldToGrid(Vector2 worldPos)
        {
            return new Point((int)(worldPos.X / TileSize), (int)(worldPos.Y / TileSize));
        }

        // Izgara indeksini dünya piksel merkezine dönüştür
        public Vector2 GridToWorld(int gridX, int gridY)
        {
            return new Vector2((gridX + 0.5f) * TileSize, (gridY + 0.5f) * TileSize);
        }
    }
}
""".trimIndent()

    val renderPipelineCode = """
// ============================================================================
// RtsRenderPipeline.cs - C&C Generals Katmanlı Çizim Sırası (Game1.Draw)
// 1. Zemin -> 2. Gölgeler -> 3. Binalar -> 4. Kara Birimleri -> 5. Jetler -> 6. HUD
// ============================================================================
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace NovaBase.Engine
{
    public class RtsRenderPipeline
    {
        private readonly SpriteBatch _spriteBatch;
        private readonly Camera2D _camera;
        private readonly Texture2D _terrainTexture;
        private readonly Matrix _uiVirtualMatrix;

        public RtsRenderPipeline(SpriteBatch spriteBatch, Camera2D camera, Texture2D terrainTexture, Matrix uiVirtualMatrix)
        {
            _spriteBatch = spriteBatch;
            _camera = camera;
            _terrainTexture = terrainTexture;
            _uiVirtualMatrix = uiVirtualMatrix;
        }

        public void RenderGame(GameTime gameTime, RtsWorld world, RtsHud hud, EraTransitionOverlay eraOverlay)
        {
            GraphicsDevice graphics = _spriteBatch.GraphicsDevice;
            graphics.Clear(Color.Black);

            // ====================================================================
            // 1. DÜNYA GEÇİŞİ (WORLD PASS) - Camera Matrix Kullanır
            // ====================================================================
            _spriteBatch.Begin(
                SpriteSortMode.Deferred,
                BlendState.AlphaBlend,
                SamplerState.LinearClamp,
                DepthStencilState.None,
                RasterizerState.CullNone,
                null,
                _camera.GetViewMatrix() // <-- Kameranın Matrix'i dünyaya uygulanır
            );

            // KATMAN 0: Harita Zemini (Çimen, Yollar, Su Kanalı)
            _spriteBatch.Draw(_terrainTexture, Vector2.Zero, Color.White);

            // KATMAN 1: Yerleşim Izgarası ve Ayak İzi Gölgeleri
            world.DrawGridLinesAndFootprints(_spriteBatch);

            // KATMAN 2: Binalar (Komuta Merkezi, Tank Fabrikası, Hava Üssü, Tersane)
            foreach (var building in world.Buildings)
            {
                building.Draw(_spriteBatch);
                // Bina üzerindeki aktif üretim halkası
                building.DrawProductionIndicator(_spriteBatch);
            }

            // KATMAN 3: Birim Gölgeleri (Uçaklar için 25px ofsetli zemin gölgesi)
            foreach (var unit in world.Units)
            {
                unit.DrawGroundShadow(_spriteBatch);
            }

            // KATMAN 4: Kara ve Deniz Birimleri (Tanklar, Denizaltılar, Piyadeler)
            foreach (var unit in world.Units)
            {
                if (unit.IsGroundOrNaval)
                {
                    unit.Draw(_spriteBatch);
                }
            }

            // KATMAN 5: Hava Birimleri (KAAN, F-22, Su-57 Jetleri yüksek irtifada uçar)
            foreach (var unit in world.Units)
            {
                if (unit.IsAirborne)
                {
                    unit.Draw(_spriteBatch);
                }
            }

            // KATMAN 6: Savaş Efektleri, SİHA Lazerleri ve Patlama Partikülleri
            world.CombatFxManager.Draw(_spriteBatch);

            _spriteBatch.End(); // Dünya çizimi kapandı

            // ====================================================================
            // 2. ARAYÜZ GEÇİŞİ (HUD PASS) - Screen-Space Matrix Kullanır
            // Menüler haritayı kaplamaz, Clash of Clans gibi köşelere sabitlenir!
            // ====================================================================
            _spriteBatch.Begin(
                SpriteSortMode.Deferred,
                BlendState.AlphaBlend,
                SamplerState.LinearClamp,
                DepthStencilState.None,
                RasterizerState.CullNone,
                null,
                _uiVirtualMatrix // <-- Ekran çözünürlüğüne adapte olan UI matrisi
            );

            // RTS Yüzen HUD Bileşenleri:
            // Sol Üst: Kaynaklar (Altın, TP, Enerji)
            hud.DrawTopLeftResourceBar(_spriteBatch);

            // Sağ Üst: General Güçleri (SİHA, Baraj, EMP)
            hud.DrawTopRightGeneralsPowers(_spriteBatch);

            // Sağ Alt: C&C Generals 5'li Üretim Konsolu & Çağ Atla Butonu
            hud.DrawBottomRightCommandConsole(_spriteBatch);

            // Sol Alt: Seçili Birim Müfettişi (HP, Zırh, Agresif/Savunma)
            hud.DrawBottomLeftUnitInspector(_spriteBatch);

            // DÖNEM GEÇİŞİ EFEKTİ (Era Transition Overlay)
            if (eraOverlay.IsActive)
            {
                eraOverlay.Draw(_spriteBatch, graphics.Viewport.Bounds);
            }

            _spriteBatch.End(); // HUD çizimi kapandı
        }
    }
}
""".trimIndent()

    val eraOverlayCode = """
// ============================================================================
// EraTransitionOverlay.cs - Dönem Geçişi Atmosfer Efekti (MonoGame C#)
// Orta Çağ: Sis ve Küller | Modern Çağ: Siber Glitch ve Uydu Radarı
// ============================================================================
using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace NovaBase.Engine
{
    public class EraTransitionOverlay
    {
        public bool IsActive { get; private set; }
        private Era _targetEra;
        private float _timer;
        private const float Duration = 4.0f; // 4 saniye tam ekran kalır
        private readonly Texture2D _pixel;

        public EraTransitionOverlay(GraphicsDevice graphics)
        {
            _pixel = new Texture2D(graphics, 1, 1);
            _pixel.SetData(new[] { Color.White });
        }

        public void Trigger(Era nextEra)
        {
            _targetEra = nextEra;
            _timer = Duration;
            IsActive = true;
        }

        public void Update(GameTime gameTime)
        {
            if (!IsActive) return;

            _timer -= (float)gameTime.ElapsedGameTime.TotalSeconds;
            if (_timer <= 0f)
            {
                IsActive = false;
            }
        }

        public void Draw(SpriteBatch spriteBatch, Rectangle screenBounds)
        {
            if (!IsActive) return;

            float alpha = MathHelper.Clamp(_timer / Duration, 0f, 1f);

            // 1. Yarı Saydam Koyu Arka Plan
            spriteBatch.Draw(_pixel, screenBounds, Color.Black * 0.85f * alpha);

            // 2. Çağa Özel Efekt Çizimi
            switch (_targetEra)
            {
                case Era.Medieval5th:
                    DrawMedievalFog(spriteBatch, screenBounds, alpha);
                    break;

                case Era.ClassicalGunpowder:
                    DrawGunpowderSmoke(spriteBatch, screenBounds, alpha);
                    break;

                case Era.Industrial:
                    DrawIndustrialGears(spriteBatch, screenBounds, alpha);
                    break;

                case Era.Modern:
                    DrawModernCyberGlitch(spriteBatch, screenBounds, alpha);
                    break;
            }
        }

        private void DrawModernCyberGlitch(SpriteBatch spriteBatch, Rectangle screenBounds, float alpha)
        {
            // Glitch Scanlines & Yeşil Radar Çizgileri
            int scanlineCount = screenBounds.Height / 10;
            for (int i = 0; i < scanlineCount; i++)
            {
                if (i % 2 == 0)
                {
                    Rectangle line = new Rectangle(0, i * 10, screenBounds.Width, 2);
                    spriteBatch.Draw(_pixel, line, Color.Cyan * (0.15f * alpha));
                }
            }

            // Radar Çemberi ve Taktik Bilgi
            // SpriteBatch font veya efekt çizimleri buraya eklenir
        }

        private void DrawMedievalFog(SpriteBatch spriteBatch, Rectangle screenBounds, float alpha)
        {
            // Hareket eden feodal gri sis katmanı
            spriteBatch.Draw(_pixel, screenBounds, new Color(40, 50, 60) * (0.45f * alpha));
        }

        private void DrawGunpowderSmoke(SpriteBatch spriteBatch, Rectangle screenBounds, float alpha)
        {
            // Barut dumanı ve kıvılcım parçacıkları
            spriteBatch.Draw(_pixel, screenBounds, new Color(30, 30, 35) * (0.6f * alpha));
        }

        private void DrawIndustrialGears(SpriteBatch spriteBatch, Rectangle screenBounds, float alpha)
        {
            // Buhar ve dönen çark silüetleri
            spriteBatch.Draw(_pixel, screenBounds, new Color(180, 83, 9) * (0.35f * alpha));
        }
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Architecture Intro Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.6f), MatrixGreen.copy(alpha = 0.5f)))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MONOGAME C# KAMERA, GRID & RENDER MİMARİSİ",
                        color = CyanNeon,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Dikey buton menüleri yerine C&C Generals ve Clash of Clans gibi yaşayan yatay bir dünya oluşturmak için kurulan tam donanımlı C# sınıfları. Kamerayı Matrix üzerinden kaydırır, binaları MapGrid koordinatlarına çizer ve arayüzü ekranın kenarlarına sabitler.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Code Card 1: Camera2D.cs
        CodeDisplayCard(
            fileName = "Camera2D.cs (1-Parmak Pan & 2-Parmak Pinch Zoom)",
            description = "Android TouchPanel jestleri, Matrix.CreateTranslation ve ScreenToWorld raycast",
            code = camera2dCode,
            onCopy = {
                val clip = ClipData.newPlainText("Camera2D.cs", camera2dCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Camera2D.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 2: MapGrid.cs
        CodeDisplayCard(
            fileName = "MapGrid.cs (Bina Ayak İzi & Zemin Izgarası)",
            description = "TileType (Çimen, Yol, Nehir, Beton) ve bina yerleştirme (CanPlaceStructure) kontrolü",
            code = mapGridCode,
            onCopy = {
                val clip = ClipData.newPlainText("MapGrid.cs", mapGridCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "MapGrid.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 3: RtsRenderPipeline.cs
        CodeDisplayCard(
            fileName = "RtsRenderPipeline.cs (Katmanlı Çizim & İki SpriteBatch Geçişi)",
            description = "Zemin -> Gölgeler -> Binalar -> Kara Birimleri -> Jetler -> HUD sırası",
            code = renderPipelineCode,
            onCopy = {
                val clip = ClipData.newPlainText("RtsRenderPipeline.cs", renderPipelineCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "RtsRenderPipeline.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 4: EraTransitionOverlay.cs
        CodeDisplayCard(
            fileName = "EraTransitionOverlay.cs (Tam Ekran Atmosfer Efekti)",
            description = "Orta Çağ sis ve külleri, Modern Çağ dijital glitch ve radar taraması",
            code = eraOverlayCode,
            onCopy = {
                val clip = ClipData.newPlainText("EraTransitionOverlay.cs", eraOverlayCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "EraTransitionOverlay.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
private fun FactionSelectAndMiniMapSection() {
    val context = LocalContext.current

    val factionDataCode = """
// ============================================================================
// FactionData.cs - Asimetrik Ülke Başlangıç Parametreleri ve Pasif Doktrinler
// Türkiye, ABD, Rusya ve Çin için çağ, kaynak ve bonus modellemesi
// ============================================================================
namespace NovaBase.Engine
{
    public enum Nation
    {
        Turkey,
        USA,
        Russia,
        China
    }

    public class FactionData
    {
        public Nation Nation { get; set; }
        public string Title { get; set; }
        public string FlagEmoji { get; set; }
        public Era StartingEra { get; set; }
        public double StartingGold { get; set; }
        public double StartingTechPoints { get; set; }
        public int StartingEnergy { get; set; }
        public string FactionPassive { get; set; }
        public string PassiveDescription { get; set; }
        public string UniqueTank { get; set; }
        public string UniqueJet { get; set; }
        public string UniqueSubmarine { get; set; }
        public float ProductionSpeedMultiplier { get; set; } = 1.0f;
        public float CostDiscountMultiplier { get; set; } = 0.0f;
        public float ArmorBonusMultiplier { get; set; } = 0.0f;

        public static FactionData GetFaction(Nation nation)
        {
            return nation switch
            {
                Nation.Turkey => new FactionData
                {
                    Nation = Nation.Turkey,
                    Title = "Türkiye",
                    FlagEmoji = "🇹🇷",
                    StartingEra = Era.Modern,
                    StartingGold = 4500.0,
                    StartingTechPoints = 1400.0,
                    StartingEnergy = 220,
                    FactionPassive = "SİHA Doktrini & Asimetrik Manevra",
                    PassiveDescription = "Bayraktar SİHA ve KAAN jetleri %25 daha hızlı üretilir. SİHA akını bekleme süresi 15s daha kısadır.",
                    UniqueTank = "Altay T2",
                    UniqueJet = "KAAN / Bayraktar KIZILELMA",
                    UniqueSubmarine = "Reis Sınıfı (TCG Piri Reis)",
                    ProductionSpeedMultiplier = 1.25f
                },
                Nation.USA => new FactionData
                {
                    Nation = Nation.USA,
                    Title = "ABD",
                    FlagEmoji = "🇺🇸",
                    StartingEra = Era.Modern,
                    StartingGold = 6000.0,
                    StartingTechPoints = 2500.0,
                    StartingEnergy = 280,
                    FactionPassive = "Hava Üstünlüğü & Yüksek Teknoloji",
                    PassiveDescription = "F-22 ve hava üssü alımlarında %15 maliyet indirimi. +60 MW nükleer reaktör gücü.",
                    UniqueTank = "M1A2 Abrams SEPv3",
                    UniqueJet = "F-22 Raptor / F-35",
                    UniqueSubmarine = "Virginia Sınıfı Nükleer",
                    CostDiscountMultiplier = 0.15f
                },
                Nation.Russia => new FactionData
                {
                    Nation = Nation.Russia,
                    Title = "Rusya",
                    FlagEmoji = "🇷🇺",
                    StartingEra = Era.Industrial,
                    StartingGold = 4800.0,
                    StartingTechPoints = 900.0,
                    StartingEnergy = 190,
                    FactionPassive = "Ağır Zırh Çeliği & Ağır Topçu Barajı",
                    PassiveDescription = "T-90 tankları ve topçu birimleri +%20 daha fazla cana (HP) ve zırh direncine sahiptir.",
                    UniqueTank = "T-90M Proryv / T-14",
                    UniqueJet = "Su-57 Felon",
                    UniqueSubmarine = "Yasen-M Sınıfı",
                    ArmorBonusMultiplier = 0.20f
                },
                Nation.China => new FactionData
                {
                    Nation = Nation.China,
                    Title = "Çin",
                    FlagEmoji = "🇨🇳",
                    StartingEra = Era.ClassicalGunpowder,
                    StartingGold = 5500.0,
                    StartingTechPoints = 700.0,
                    StartingEnergy = 230,
                    FactionPassive = "Seri İmalat & Sayısal Üstünlük",
                    PassiveDescription = "Kışla ve fabrika üretim süreleri %30 daha kısadır. Tedarik lojistiği +%20 Altın/s sağlar.",
                    UniqueTank = "Type 99A2",
                    UniqueJet = "J-20 Mighty Dragon",
                    UniqueSubmarine = "Type 093B Shang",
                    ProductionSpeedMultiplier = 1.30f
                },
                _ => GetFaction(Nation.Turkey)
            };
        }
    }
}
""".trimIndent()

    val factionSelectScreenCode = """
// ============================================================================
// FactionSelectScreen.cs - 1920x1080 Yatay Ülke Seçim Arayüzü (MonoGame)
// 4 Ülke Kartını Yatay Olarak Ekrana Konumlandırır ve Dokunmaları Dinler
// ============================================================================
using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input.Touch;

namespace NovaBase.Engine
{
    public class FactionSelectScreen
    {
        private readonly SpriteBatch _spriteBatch;
        private readonly SpriteFont _font;
        private readonly Texture2D _pixel;
        private readonly Rectangle[] _cardRectangles;
        private readonly FactionData[] _factions;

        public event Action<FactionData> OnFactionSelected;

        public FactionSelectScreen(SpriteBatch spriteBatch, SpriteFont font)
        {
            _spriteBatch = spriteBatch;
            _font = font;

            _pixel = new Texture2D(_spriteBatch.GraphicsDevice, 1, 1);
            _pixel.SetData(new[] { Color.White });

            _factions = new[]
            {
                FactionData.GetFaction(Nation.Turkey),
                FactionData.GetFaction(Nation.USA),
                FactionData.GetFaction(Nation.Russia),
                FactionData.GetFaction(Nation.China)
            };

            _cardRectangles = new Rectangle[4];
            ComputeCardLayout(1920, 1080);
        }

        public void ComputeCardLayout(int screenWidth, int screenHeight)
        {
            int cardWidth = 420;
            int cardHeight = 680;
            int spacing = 32;
            int totalWidth = (4 * cardWidth) + (3 * spacing);
            int startX = (screenWidth - totalWidth) / 2;
            int startY = (screenHeight - cardHeight) / 2 + 40;

            for (int i = 0; i < 4; i++)
            {
                _cardRectangles[i] = new Rectangle(startX + i * (cardWidth + spacing), startY, cardWidth, cardHeight);
            }
        }

        public void Update(GameTime gameTime)
        {
            TouchCollection touches = TouchPanel.GetState();
            foreach (var touch in touches)
            {
                if (touch.State == TouchLocationState.Released)
                {
                    Point p = new Point((int)touch.Position.X, (int)touch.Position.Y);
                    for (int i = 0; i < 4; i++)
                    {
                        if (_cardRectangles[i].Contains(p))
                        {
                            OnFactionSelected?.Invoke(_factions[i]);
                            return;
                        }
                    }
                }
            }
        }

        public void Draw(int screenWidth, int screenHeight)
        {
            // 1. Koyu Askeri Komuta Odası Arka Planı
            _spriteBatch.Draw(_pixel, new Rectangle(0, 0, screenWidth, screenHeight), new Color(8, 12, 20));

            // Başlık
            // "KÜRESEL DOKTRİN VE ÜLKE SEÇİMİ (FACTION SELECTION)"
            // Her ülkenin kartını Vector2 pozisyonunda çiz:
            for (int i = 0; i < 4; i++)
            {
                Rectangle rect = _cardRectangles[i];
                FactionData faction = _factions[i];

                // Kart Arka Planı
                _spriteBatch.Draw(_pixel, rect, new Color(18, 26, 40) * 0.95f);

                // Kart Çerçevesi
                DrawOutline(rect, Color.Cyan * 0.5f, 2);

                // Bayrak, Başlangıç Çağı ve Pasif Doktrin Metinleri
                Vector2 pos = new Vector2(rect.X + 20, rect.Y + 24);
                // "🇹🇷 TÜRKİYE"
                // "Başlangıç: Modern Çağ"
                // "Rezerv: 4500A | 1400TP | 220MW"
                // "Pasif: SİHA Doktrini (%25 Hızlı Üretim)"
            }
        }

        private void DrawOutline(Rectangle rect, Color color, int thickness)
        {
            _spriteBatch.Draw(_pixel, new Rectangle(rect.X, rect.Y, rect.Width, thickness), color);
            _spriteBatch.Draw(_pixel, new Rectangle(rect.X, rect.Bottom - thickness, rect.Width, thickness), color);
            _spriteBatch.Draw(_pixel, new Rectangle(rect.X, rect.Y, thickness, rect.Height), color);
            _spriteBatch.Draw(_pixel, new Rectangle(rect.Right - thickness, rect.Y, thickness, rect.Height), color);
        }
    }
}
""".trimIndent()

    val gameInitializerCode = """
// ============================================================================
// GameInitializer.cs - Seçilen Ülkeye Göre Harita ve Kaynakların Otomatik Kurulumu
// ============================================================================
namespace NovaBase.Engine
{
    public static class GameInitializer
    {
        public static void InitializeFromFaction(
            FactionData faction,
            EraManager eraManager,
            ResourceManager resourceManager,
            UnitManager unitManager,
            GeneralsPowersManager powersManager)
        {
            // 1. ÇAĞ YÖNETİCİSİNİ KUR (StartingEra)
            eraManager.SetInitialEra(faction.StartingEra);

            // 2. ASİMETRİK EKONOMİ VE ENERJİYİ KUR
            resourceManager.SetResources(
                gold: faction.StartingGold,
                techPoints: faction.StartingTechPoints,
                energyProduced: faction.StartingEnergy,
                energyConsumed: 100
            );

            // 3. ÜLKEYE ÖZEL BAŞLANGIÇ BİRLİKLERİNİ KONUŞLANDIR
            unitManager.ClearAllUnits();

            switch (faction.Nation)
            {
                case Nation.Turkey:
                    unitManager.SpawnUnit("Altay T2", UnitType.Tank, worldX: 260, worldY: 340);
                    unitManager.SpawnUnit("KAAN Hayalet Jet", UnitType.Jet, worldX: 680, worldY: 180);
                    unitManager.SpawnUnit("TCG Piri Reis", UnitType.Submarine, worldX: 900, worldY: 420);
                    break;

                case Nation.USA:
                    unitManager.SpawnUnit("M1A2 Abrams", UnitType.Tank, worldX: 260, worldY: 340);
                    unitManager.SpawnUnit("F-22 Raptor", UnitType.Jet, worldX: 680, worldY: 180);
                    break;

                case Nation.Russia:
                    unitManager.SpawnUnit("T-90M Proryv", UnitType.Tank, worldX: 260, worldY: 340);
                    unitManager.SpawnUnit("Ağır Topçu Bataryası", UnitType.Siege, worldX: 300, worldY: 380);
                    break;

                case Nation.China:
                    unitManager.SpawnUnit("Type 99A2", UnitType.Tank, worldX: 260, worldY: 340);
                    unitManager.SpawnUnit("Piyade Tüfek Bölüğü", UnitType.Infantry, worldX: 240, worldY: 300);
                    break;
            }

            // 4. GENERAL GÜÇLERİNİ ÜLKE DOKTRİNİNE GÖRE YENİLE
            powersManager.LoadPowersForNationAndEra(faction.Nation, faction.StartingEra);
        }
    }
}
""".trimIndent()

    val miniMapCode = """
// ============================================================================
// MiniMap.cs - İnteraktif Kuşbakışı Radar Bileşeni (MonoGame C#)
// Dokunulan noktaya ana kamerayı anında odaklar (ScreenToWorld Raycast)
// ============================================================================
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input.Touch;

namespace NovaBase.Engine
{
    public class MiniMap
    {
        private readonly Rectangle _bounds; // Örn: (1680, 16, 220, 140)
        private readonly int _worldWidth;   // Örn: 2400
        private readonly int _worldHeight;  // Örn: 1600
        private readonly Camera2D _camera;
        private readonly Texture2D _pixel;

        public MiniMap(Rectangle bounds, int worldWidth, int worldHeight, Camera2D camera, GraphicsDevice graphics)
        {
            _bounds = bounds;
            _worldWidth = worldWidth;
            _worldHeight = worldHeight;
            _camera = camera;

            _pixel = new Texture2D(graphics, 1, 1);
            _pixel.SetData(new[] { Color.White });
        }

        // İNTERAKTİF DOKUNMA: Mini haritaya dokunulduğunda kamerayı o dünya koordinatına merkezler
        public void HandleTouch(TouchCollection touches)
        {
            foreach (var touch in touches)
            {
                if (touch.State == TouchLocationState.Pressed || touch.State == TouchLocationState.Moved)
                {
                    Point p = new Point((int)touch.Position.X, (int)touch.Position.Y);
                    if (_bounds.Contains(p))
                    {
                        // Mini harita içi yerel koordinat (0 .. bounds.Width)
                        float localX = p.X - _bounds.X;
                        float localY = p.Y - _bounds.Y;

                        // Dünya koordinatına ölçekle
                        float targetWorldX = (localX / _bounds.Width) * _worldWidth;
                        float targetWorldY = (localY / _bounds.Height) * _worldHeight;

                        // Ana kamerayı hedefe taşı!
                        _camera.Position = new Vector2(targetWorldX, targetWorldY);
                    }
                }
            }
        }

        public void Draw(SpriteBatch spriteBatch, RtsWorld world)
        {
            // 1. Radar Arka Planı
            spriteBatch.Draw(_pixel, _bounds, new Color(10, 20, 30) * 0.90f);

            float scaleX = (float)_bounds.Width / _worldWidth;
            float scaleY = (float)_bounds.Height / _worldHeight;

            // 2. Binaları çiz (Mavi/Sarı noktalar)
            foreach (var b in world.Buildings)
            {
                int bx = _bounds.X + (int)(b.WorldX * scaleX);
                int by = _bounds.Y + (int)(b.WorldY * scaleY);
                spriteBatch.Draw(_pixel, new Rectangle(bx - 3, by - 3, 6, 6), Color.Cyan);
            }

            // 3. Birimleri çiz (Dost: Yeşil, Düşman: Kırmızı)
            foreach (var u in world.Units)
            {
                int ux = _bounds.X + (int)(u.Position.X * scaleX);
                int uy = _bounds.Y + (int)(u.Position.Y * scaleY);
                Color uColor = u.IsPlayerSide ? Color.LimeGreen : Color.Red;
                spriteBatch.Draw(_pixel, new Rectangle(ux - 2, uy - 2, 4, 4), uColor);
            }

            // 4. Ana Kameranın Görüntü Çerçevesi (Viewport Karesi)
            Vector2 camTopLeft = _camera.ScreenToWorld(Vector2.Zero);
            Vector2 camBottomRight = _camera.ScreenToWorld(new Vector2(1920, 1080));

            int vx = _bounds.X + (int)(camTopLeft.X * scaleX);
            int vy = _bounds.Y + (int)(camTopLeft.Y * scaleY);
            int vw = (int)((camBottomRight.X - camTopLeft.X) * scaleX);
            int vh = (int)((camBottomRight.Y - camTopLeft.Y) * scaleY);

            // Sarı kamera çerçevesi
            DrawOutline(spriteBatch, new Rectangle(vx, vy, vw, vh), Color.Gold, 1);
        }

        private void DrawOutline(SpriteBatch sb, Rectangle r, Color c, int t)
        {
            sb.Draw(_pixel, new Rectangle(r.X, r.Y, r.Width, t), c);
            sb.Draw(_pixel, new Rectangle(r.X, r.Bottom - t, r.Width, t), c);
            sb.Draw(_pixel, new Rectangle(r.X, r.Y, t, r.Height), c);
            sb.Draw(_pixel, new Rectangle(r.Right - t, r.Y, t, r.Height), c);
        }
    }
}
""".trimIndent()

    val selectionMarkerCode = """
// ============================================================================
// SelectionBox & UnitSelectionMarker.cs - Çoklu Seçim ve Taktik Nişangah
// ============================================================================
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace NovaBase.Engine
{
    public class SelectionSystem
    {
        private Vector2? _dragStart;
        private Vector2? _dragCurrent;
        private readonly Texture2D _pixel;

        public SelectionSystem(GraphicsDevice graphics)
        {
            _pixel = new Texture2D(graphics, 1, 1);
            _pixel.SetData(new[] { Color.White });
        }

        public void StartDrag(Vector2 screenPos) => _dragStart = _dragCurrent = screenPos;
        public void UpdateDrag(Vector2 screenPos) => _dragCurrent = screenPos;

        public Rectangle? EndDrag()
        {
            if (!_dragStart.HasValue || !_dragCurrent.HasValue) return null;
            int x = (int)Math.Min(_dragStart.Value.X, _dragCurrent.Value.X);
            int y = (int)Math.Min(_dragStart.Value.Y, _dragCurrent.Value.Y);
            int w = (int)Math.Abs(_dragStart.Value.X - _dragCurrent.Value.X);
            int h = (int)Math.Abs(_dragStart.Value.Y - _dragCurrent.Value.Y);
            _dragStart = _dragCurrent = null;
            return new Rectangle(x, y, w, h);
        }

        // ÇOKLU SEÇİM KUTUSUNU LAZER KÖŞE PARANTEZLERİYLE ÇİZ
        public void DrawSelectionBox(SpriteBatch sb)
        {
            if (!_dragStart.HasValue || !_dragCurrent.HasValue) return;

            int minX = (int)Math.Min(_dragStart.Value.X, _dragCurrent.Value.X);
            int maxX = (int)Math.Max(_dragStart.Value.X, _dragCurrent.Value.X);
            int minY = (int)Math.Min(_dragStart.Value.Y, _dragCurrent.Value.Y);
            int maxY = (int)Math.Max(_dragStart.Value.Y, _dragCurrent.Value.Y);
            Rectangle rect = new Rectangle(minX, minY, maxX - minX, maxY - minY);

            // Yarı saydam yeşil lazer dolgu
            sb.Draw(_pixel, rect, Color.LimeGreen * 0.15f);

            // 4 Köşe Lazer Çentiği (┌ ┐ └ ┘)
            int bLen = Math.Min(16, Math.Min(rect.Width / 3, rect.Height / 3));
            sb.Draw(_pixel, new Rectangle(minX, minY, bLen, 2), Color.LimeGreen);
            sb.Draw(_pixel, new Rectangle(minX, minY, 2, bLen), Color.LimeGreen);

            sb.Draw(_pixel, new Rectangle(maxX - bLen, minY, bLen, 2), Color.LimeGreen);
            sb.Draw(_pixel, new Rectangle(maxX - 2, minY, 2, bLen), Color.LimeGreen);

            sb.Draw(_pixel, new Rectangle(minX, maxY - 2, bLen, 2), Color.LimeGreen);
            sb.Draw(_pixel, new Rectangle(minX, maxY - bLen, 2, bLen), Color.LimeGreen);

            sb.Draw(_pixel, new Rectangle(maxX - bLen, maxY - 2, bLen, 2), Color.LimeGreen);
            sb.Draw(_pixel, new Rectangle(maxX - 2, maxY - bLen, 2, bLen), Color.LimeGreen);
        }

        // SEÇİLİ BİRİM TAKTİK NİŞANGAH İŞARETÇİSİ (Unit Selection Marker)
        public static void DrawUnitMarker(SpriteBatch sb, Texture2D pixel, Vector2 unitScreenPos, float radius, float rotAngle)
        {
            Color markerColor = Color.LimeGreen;

            // 1. İç ve Dış Halka
            // 2. 4 Yönlü Çapraz Çentik (+)
            sb.Draw(pixel, new Rectangle((int)unitScreenPos.X - 1, (int)(unitScreenPos.Y - radius - 6), 2, 6), markerColor);
            sb.Draw(pixel, new Rectangle((int)unitScreenPos.X - 1, (int)(unitScreenPos.Y + radius), 2, 6), markerColor);
            sb.Draw(pixel, new Rectangle((int)(unitScreenPos.X - radius - 6), (int)unitScreenPos.Y - 1, 6, 2), markerColor);
            sb.Draw(pixel, new Rectangle((int)(unitScreenPos.X + radius), (int)unitScreenPos.Y - 1, 6, 2), markerColor);
        }
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Architecture Intro Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(LaserCrimson.copy(alpha = 0.6f), AmberGold.copy(alpha = 0.5f)))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = LaserCrimson, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ASİMETRİK ÜLKE SEÇİMİ, MINIMAP & SEÇİM NİŞANGAHI",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Türkiye, ABD, Rusya ve Çin asimetrik başlangıç çağı (Modern, Endüstri, Barut), kaynak ve pasif bonuslarla başlar. Ayrıca 1920x1080 ekranında parmakla dokunulan yere kamerayı anında odaklayan interaktif MiniMap ve lazer köşe parantezli çoklu birim seçim kutusu mimarisi kuruldu.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Code Card 1: FactionData.cs
        CodeDisplayCard(
            fileName = "FactionData.cs (Asimetrik Ülke Modeli & Başlangıç Parametreleri)",
            description = "StartingEra, StartingTechPoints, StartingEnergy ve FactionPassive tanımları",
            code = factionDataCode,
            onCopy = {
                val clip = ClipData.newPlainText("FactionData.cs", factionDataCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "FactionData.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 2: FactionSelectScreen.cs
        CodeDisplayCard(
            fileName = "FactionSelectScreen.cs (1920x1080 Yatay Ülke Seçim Ekranı)",
            description = "4 Ülke kartını yatayda yan yana dizer ve dokunma girdilerini yakalar",
            code = factionSelectScreenCode,
            onCopy = {
                val clip = ClipData.newPlainText("FactionSelectScreen.cs", factionSelectScreenCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "FactionSelectScreen.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 3: GameInitializer.cs
        CodeDisplayCard(
            fileName = "GameInitializer.cs (Asimetrik Savaş Haritasını Başlatıcı)",
            description = "Seçilen ülkeye göre EraManager, ResourceManager ve birlikleri otomatik kurar",
            code = gameInitializerCode,
            onCopy = {
                val clip = ClipData.newPlainText("GameInitializer.cs", gameInitializerCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "GameInitializer.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 4: MiniMap.cs
        CodeDisplayCard(
            fileName = "MiniMap.cs (İnteraktif Kuşbakışı Radar & Dokun-Odakla)",
            description = "Mini haritaya dokunulan dünya koordinatına Camera2D'yi otomatik merkezler",
            code = miniMapCode,
            onCopy = {
                val clip = ClipData.newPlainText("MiniMap.cs", miniMapCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "MiniMap.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        // Code Card 5: SelectionBox.cs & Marker
        CodeDisplayCard(
            fileName = "SelectionBox.cs & UnitSelectionMarker.cs (Lazer Köşeli Çoklu Seçim)",
            description = "Kare içine alarak çoklu seçim kutusu ve dönen çentikli taktik işaretçi",
            code = selectionMarkerCode,
            onCopy = {
                val clip = ClipData.newPlainText("SelectionBox.cs", selectionMarkerCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "SelectionBox.cs Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}



