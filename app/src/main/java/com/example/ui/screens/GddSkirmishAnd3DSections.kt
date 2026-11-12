package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.LaserCrimson
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

// ==================== TAB 9: SKIRMISH AI & ASİMETRİK FABRİKA ====================
@Composable
fun SkirmishAiAndAsymmetricFactorySection() {
    val context = LocalContext.current

    val militaryUnitCode = """
using System;

namespace ZeroHourMobile.Production
{
    public enum UnitType
    {
        Land,   // Kara: Piyade, Tank, Topçu
        Air,    // Hava: SİHA, Jet, Helikopter
        Naval   // Deniz: Hücum Sandalı, Kalyon, Denizaltı, Uçak Gemisi
    }

    public enum Faction
    {
        Turkey,
        USA,
        Russia,
        China
    }

    /// <summary>
    /// Asimetrik askeri birim veri modeli
    /// </summary>
    public class MilitaryUnit
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public UnitType Type { get; set; }
        public int CostGold { get; set; }
        public int CostEnergy { get; set; }
        public float DurationSeconds { get; set; }
        public int RequiredCentury { get; set; } // 5: Orta Çağ, 18: Barut, 19: Endüstriyel, 21: Modern
        public Faction Faction { get; set; }
        public float MaxHp { get; set; }
        public float AttackPower { get; set; }
        public float Speed { get; set; }
        public string Description { get; set; }

        public MilitaryUnit(string id, string name, UnitType type, int costGold, int costEnergy, 
                            float duration, int century, Faction faction, float hp, float atk, float spd, string desc)
        {
            Id = id;
            Name = name;
            Type = type;
            CostGold = costGold;
            CostEnergy = costEnergy;
            DurationSeconds = duration;
            RequiredCentury = century;
            Faction = faction;
            MaxHp = hp;
            AttackPower = atk;
            Speed = spd;
            Description = desc;
        }
    }
}
""".trimIndent()

    val databaseCode = """
using System.Collections.Generic;

namespace ZeroHourMobile.Production
{
    /// <summary>
    /// Gerçekçi tarihsel ve modern asimetrik birim veri bankası
    /// </summary>
    public static class HistoricalUnitDatabase
    {
        public static readonly List<MilitaryUnit> Units = new List<MilitaryUnit>()
        {
            // ==================== TÜRKİYE ====================
            // 5. YY (Orta Çağ): Hun Süvarisi
            new MilitaryUnit("tr_hun_cavalry", "Hun Süvarisi (Atlı Bozkır Akıncısı)", UnitType.Land, 
                             90, 0, 3.5f, 5, Faction.Turkey, 110, 18, 68, "Bozkır taktiğiyle hareket halinde yaylım oku atan Hun süvarisi."),
            // Endüstriyel: Mancınık / Kuşatma Topçusu
            new MilitaryUnit("tr_siege_cannon", "Mancınık & Şahi Kuşatma Topu", UnitType.Land, 
                             280, 15, 7.0f, 19, Faction.Turkey, 240, 75, 25, "Sur ve tahkimatları yerle bir eden ağır topçu bataryası."),
            // Modern: Altay T2 Tankı
            new MilitaryUnit("tr_altay_t2", "Altay T2 Ana Muharebe Tankı", UnitType.Land, 
                             550, 35, 9.0f, 21, Faction.Turkey, 520, 120, 45, "AKKOR aktif koruma sistemi ve 120mm yivsiz yerli top."),
            // Modern: Bayraktar SİHA (Hava)
            new MilitaryUnit("tr_bayraktar_siha", "Bayraktar SİHA (TB2 & Kızılelma)", UnitType.Air, 
                             580, 45, 8.0f, 21, Faction.Turkey, 380, 140, 92, "Lazer güdümlü MAM-L mühimmatı ve radar görünmezliği."),
            // Modern: BORA / TAYFUN Balistik Füzesi (Kara/Kuşatma)
            new MilitaryUnit("tr_bora_missile", "BORA / TAYFUN Taktik Balistik Füzesi", UnitType.Land, 
                             650, 50, 11.0f, 21, Faction.Turkey, 350, 180, 35, "Derin stratejik hedeflere uzun menzilli hassas balistik darbe."),
            // Modern: TCG Anadolu (Tersane)
            new MilitaryUnit("tr_tcg_anadolu", "TCG Anadolu Amfibi Sancak Gemisi", UnitType.Naval, 
                             750, 60, 12.0f, 21, Faction.Turkey, 700, 150, 40, "SİHA ve helikopter konuşlu amfibi çıkarma ve sancak gemisi."),

            // ==================== ABD ====================
            // 18. YY: Milis Askeri (Minuteman)
            new MilitaryUnit("usa_minuteman", "18. YY Milis Askeri (Minuteman)", UnitType.Land, 
                             130, 0, 4.0f, 18, Faction.USA, 125, 26, 52, "Bağımsızlık savaşı yivli tüfekli milis piyadesi."),
            // Endüstriyel: M4 Sherman Tankı
            new MilitaryUnit("usa_sherman", "M4 Sherman Muharebe Tankı", UnitType.Land, 
                             390, 20, 7.0f, 19, Faction.USA, 350, 65, 44, "Seri üretim çelik efsane. 75mm top ve sağlam süspansiyon."),
            // Modern: M1A2 Abrams
            new MilitaryUnit("usa_abrams", "M1A2 Abrams SEPv3", UnitType.Land, 
                             580, 40, 10.0f, 21, Faction.USA, 560, 125, 44, "Seyreltilmiş uranyum zırh ve Trophy aktif koruma sistemi."),
            // Modern: F-22 Raptor (Hava)
            new MilitaryUnit("usa_f22", "F-22 Raptor Hayalet Jet", UnitType.Air, 
                             700, 60, 11.0f, 21, Faction.USA, 390, 155, 100, "5. Nesil hava üstünlüğü avcısı ve süper seyir kabiliyeti."),
            // Modern: Ohio Sınıfı Denizaltı (Tersane)
            new MilitaryUnit("usa_ohio_sub", "Ohio Sınıfı Nükleer Balistik Denizaltı", UnitType.Naval, 
                             720, 65, 12.0f, 21, Faction.USA, 680, 160, 42, "Trident II balistik füzeleri ve sessiz nükleer tahrik."),

            // ==================== RUSYA ====================
            // 5. YY: Slav Savaşçısı (Druzhina)
            new MilitaryUnit("ru_druzhina", "Slav Savaşçısı (Druzhina Muhafızı)", UnitType.Land, 
                             95, 0, 3.5f, 5, Faction.Russia, 130, 20, 52, "Ağır balta ve kalkan taşıyan fedakar 5. YY Slav öncü savaşçısı."),
            // Endüstriyel: T-34 Tankı
            new MilitaryUnit("ru_t34", "T-34/85 Efsanevi Tankı", UnitType.Land, 
                             370, 20, 6.5f, 19, Faction.Russia, 360, 68, 45, "Eğimli zırh, 85mm topu ve kitlesel cephe yarma gücü."),
            // Modern: T-14 Armata
            new MilitaryUnit("ru_t14", "T-14 Armata & T-90M Tankı", UnitType.Land, 
                             540, 35, 9.0f, 21, Faction.Russia, 550, 122, 45, "İnsansız taret, Relikt patlayıcı reaktif zırh ve Afganit APS."),
            // Modern: Su-57 Felon (Hava)
            new MilitaryUnit("ru_su57", "Su-57 Felon 5. Nesil Jet", UnitType.Air, 
                             640, 55, 10.0f, 21, Faction.Russia, 400, 148, 96, "3D itki vektörleme, süper-manevra kabiliyeti ve R-77 füzeleri."),
            // Modern: Akula Sınıfı Denizaltı (Tersane)
            new MilitaryUnit("ru_akula_sub", "Akula Sınıfı Nükleer Taarruz Denizaltısı", UnitType.Naval, 
                             690, 60, 11.0f, 21, Faction.Russia, 660, 150, 44, "Titanyum çift gövde ve Kalibr seyir füzeleri taşıyan dev avcı."),

            // ==================== ÇİN ====================
            // 5. YY: Han Hanedanı Okçusu
            new MilitaryUnit("cn_han_archer", "Han Hanedanı Okçusu", UnitType.Land, 
                             85, 0, 3.0f, 5, Faction.China, 95, 17, 58, "Bambu yaylar ve zehirli oklarla donatılmış kalabalık 5. YY Han piyadesi."),
            // Endüstriyel: Tip 59 Tankı
            new MilitaryUnit("cn_type59", "Tip 59 Ana Muharebe Tankı", UnitType.Land, 
                             340, 18, 6.0f, 19, Faction.China, 330, 62, 44, "100mm yivli top taşıyan dayanıklı ve seri üretilebilen tank."),
            // Modern: Tip 99 Tankı
            new MilitaryUnit("cn_type99", "Tip 99A2 Ana Muharebe Tankı", UnitType.Land, 
                             490, 30, 8.5f, 21, Faction.China, 510, 115, 46, "Lazer aktif savunma sistemi ve FY-4 reaktif zırhı."),
            // Modern: J-20 Jeti (Hava)
            new MilitaryUnit("cn_j20", "J-20 Mighty Dragon Jeti", UnitType.Air, 
                             620, 50, 10.0f, 21, Faction.China, 385, 142, 94, "Kanartlı hayalet jet, PL-15 uzun menzilli havadan havaya füzeler."),
            // Modern: Tip 094 Denizaltı (Tersane)
            new MilitaryUnit("cn_type094", "Tip 094 Jin Sınıfı Balistik Denizaltı", UnitType.Naval, 
                             660, 55, 11.0f, 21, Faction.China, 630, 145, 41, "JL-2 balistik füzeleri taşıyan nükleer caydırıcılık denizaltısı.")
        };
    }
}
""".trimIndent()

    val factoryCode = """
using System;
using System.Collections.Generic;
using System.Linq;

namespace ZeroHourMobile.Production
{
    public enum FactoryType
    {
        BarracksWarFactory, // Kara Birimleri
        Airfield,           // Hava Birimleri
        Shipyard            // Deniz Birimleri
    }

    /// <summary>
    /// Asimetrik ve filtrelenmiş üretim yapan dinamik fabrika sınıfı
    /// </summary>
    public class ProductionFactory
    {
        public string BuildingId { get; }
        public FactoryType Type { get; }
        public Faction CurrentFaction { get; private set; }
        public int CurrentCentury { get; private set; }

        public List<MilitaryUnit> ActiveAvailableUnits { get; private set; } = new List<MilitaryUnit>();
        public Queue<MilitaryUnit> ProductionQueue { get; } = new Queue<MilitaryUnit>();
        public const int MaxQueueSize = 5;

        // Çağ atlama olayı
        public event Action<int, List<MilitaryUnit>> OnCenturyUpgraded;

        public ProductionFactory(string id, FactoryType type, Faction faction, int initialCentury)
        {
            BuildingId = id;
            Type = type;
            CurrentFaction = faction;
            CurrentCentury = initialCentury;

            // İlk üretim listesini doldur
            RefreshAvailableUnits();
        }

        /// <summary>
        /// Fabrika türüne, ülkeye ve aktif yüzyıla göre SADECE üretilebilir birimleri döner
        /// </summary>
        public List<MilitaryUnit> GetAvailableProduction(FactoryType factoryType, Faction faction, int century)
        {
            return HistoricalUnitDatabase.Units.Where(unit =>
            {
                // 1. Ülke Eşleşmesi
                if (unit.Faction != faction) return false;

                // 2. Yüzyıl / Çağ Kilidi (Mevcut yüzyıldan küçük veya eşit olmalı)
                if (unit.RequiredCentury > century) return false;

                // 3. Fabrika Türü ve Birim Türü Uyumu (Kışla/Fabrika -> Kara, Hava Üssü -> Hava, Tersane -> Deniz)
                switch (factoryType)
                {
                    case FactoryType.BarracksWarFactory:
                        return unit.Type == UnitType.Land;
                    case FactoryType.Airfield:
                        return unit.Type == UnitType.Air;
                    case FactoryType.Shipyard:
                        return unit.Type == UnitType.Naval;
                    default:
                        return false;
                }
            }).ToList();
        }

        /// <summary>
        /// Yüzyıl yükseltildiğinde (Century++) fabrikayı otomatik güncelleyen tetikleyici
        /// </summary>
        public void OnCenturyUpgrade(int newCentury)
        {
            if (newCentury <= CurrentCentury) return;

            CurrentCentury = newCentury;
            RefreshAvailableUnits();

            // Dinleyici arayüzleri bilgilendir
            OnCenturyUpgraded?.Invoke(CurrentCentury, ActiveAvailableUnits);
        }

        private void RefreshAvailableUnits()
        {
            ActiveAvailableUnits = GetAvailableProduction(Type, CurrentFaction, CurrentCentury);
        }

        public bool EnqueueUnit(MilitaryUnit unit, int currentGold, out string error)
        {
            if (ProductionQueue.Count >= MaxQueueSize)
            {
                error = "Kuyruk dolu! (Maks 5 birim)";
                return false;
            }

            if (!ActiveAvailableUnits.Contains(unit))
            {
                error = "Bu birim bu fabrikada veya bu çağda üretilemez!";
                return false;
            }

            if (currentGold < unit.CostGold)
            {
                error = "Yetersiz Altın!";
                return false;
            }

            ProductionQueue.Enqueue(unit);
            error = null;
            return true;
        }
    }
}
""".trimIndent()

    val skirmishAiCode = """
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;

namespace ZeroHourMobile.AI
{
    public enum AiState
    {
        EcoBuild,       // Bina kurma ve üs genişletme
        MassProduce,    // Fabrikalardan ordu çıkarma
        AttackWave,     // Oyuncu üssüne koordineli taarruz
        Retreat         // Ağır hasar durumunda geri çekilme
    }

    /// <summary>
    /// Bilgisayarın otomatik bina kurmasını ve oyuncuya saldırmasını sağlayan Skirmish AI botu
    /// </summary>
    public class SkirmishAI
    {
        public AiState CurrentState { get; private set; } = AiState.EcoBuild;
        public int WaveNumber { get; private set; } = 1;
        public float DecisionTimer { get; private set; } = 0f;
        public const float DecisionInterval = 6.0f; // 6 saniyede bir karar al

        private readonly Vector2 _enemyBaseCenter = new Vector2(1150f, 320f);
        private readonly Vector2 _playerBaseCenter = new Vector2(460f, 280f);
        private readonly Vector2 _bridgeWaypoint = new Vector2(720f, 360f); // Nehir geçişi

        public List<Vector2> EnemyBuildingLocations { get; } = new List<Vector2>();
        public List<SimulatedUnit> BotUnits { get; } = new List<SimulatedUnit>();

        public void Update(float deltaTime, List<SimulatedUnit> playerUnits)
        {
            DecisionTimer += deltaTime;
            if (DecisionTimer >= DecisionInterval)
            {
                DecisionTimer = 0f;
                ExecuteAiCycle(playerUnits);
            }

            // Birimlerin Pathfinding ve hareketini güncelle
            UpdateUnitPathfinding(deltaTime, playerUnits);
        }

        private void ExecuteAiCycle(List<SimulatedUnit> playerUnits)
        {
            switch (CurrentState)
            {
                case AiState.EcoBuild:
                    // 1. Periyodik olarak eksik binaları kur
                    if (EnemyBuildingLocations.Count < 1)
                    {
                        EnemyBuildingLocations.Add(new Vector2(1120f, 420f)); // Tank Fabrikası
                    }
                    else if (EnemyBuildingLocations.Count < 2)
                    {
                        EnemyBuildingLocations.Add(new Vector2(1240f, 380f)); // Hava Üssü
                    }
                    CurrentState = AiState.MassProduce;
                    break;

                case AiState.MassProduce:
                    // 2. Fabrikalardan yeni taarruz dalgası birimleri üret
                    int unitsToSpawn = 2 + (WaveNumber / 2);
                    for (int i = 0; i < unitsToSpawn; i++)
                    {
                        Vector2 spawnPos = _enemyBaseCenter + new Vector2(i * 20f, i * 15f);
                        var unit = new SimulatedUnit("Bot_Tank_" + WaveNumber + "_" + i, spawnPos, 42f, 480f, 95f);
                        BotUnits.Add(unit);
                    }

                    WaveNumber++;
                    CurrentState = AiState.AttackWave;
                    break;

                case AiState.AttackWave:
                    // Eğer ordunun %70'i yok edildiyse tekrar ordu kurmaya dön
                    if (BotUnits.Count <= 1)
                    {
                        CurrentState = AiState.EcoBuild;
                    }
                    break;
            }
        }

        /// <summary>
        /// A* / Waypoint Mantığıyla birimleri oyuncunun üssüne doğru hareket ettiren Pathfinding
        /// </summary>
        private void UpdateUnitPathfinding(float dt, List<SimulatedUnit> playerUnits)
        {
            for (int i = BotUnits.Count - 1; i >= 0; i--)
            {
                var unit = BotUnits[i];
                if (unit.Hp <= 0)
                {
                    BotUnits.RemoveAt(i);
                    continue;
                }

                // En yakın oyuncu birimini tespit et
                SimulatedUnit targetPlayerUnit = null;
                float closestDist = float.MaxValue;
                foreach (var pu in playerUnits)
                {
                    float d = Vector2.Distance(unit.Position, pu.Position);
                    if (d < closestDist)
                    {
                        closestDist = d;
                        targetPlayerUnit = pu;
                    }
                }

                Vector2 destination;
                if (closestDist < 200f && targetPlayerUnit != null)
                {
                    // Oyuncu menzile girdiğinde doğrudan hedefe kilitlen (Attack Move)
                    destination = targetPlayerUnit.Position;

                    // Çatışma mesafesinde ateş et
                    if (closestDist < 120f)
                    {
                        targetPlayerUnit.Hp -= unit.AttackPower * dt * 0.4f;
                        unit.Hp -= targetPlayerUnit.AttackPower * dt * 0.35f;
                    }
                }
                else if (unit.Position.X > 740f)
                {
                    // Waypoint 1: Nehir Geçiş Köprüsü
                    destination = _bridgeWaypoint;
                }
                else
                {
                    // Waypoint 2: Oyuncu Karargahı (HQ)
                    destination = _playerBaseCenter;
                }

                // Hareket vektörü
                Vector2 dir = destination - unit.Position;
                if (dir.Length() > 6f)
                {
                    dir.Normalize();
                    unit.Position += dir * unit.Speed * dt;
                }
            }
        }
    }

    public class SimulatedUnit
    {
        public string Id { get; set; }
        public Vector2 Position { get; set; }
        public float Speed { get; set; }
        public float Hp { get; set; }
        public float AttackPower { get; set; }

        public SimulatedUnit(string id, Vector2 pos, float speed, float hp, float atk)
        {
            Id = id;
            Position = pos;
            Speed = speed;
            Hp = hp;
            AttackPower = atk;
        }
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(LaserCrimson, AmberGold))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.SmartToy, contentDescription = null, tint = LaserCrimson, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SKIRMISH AI & ASİMETRİK FABRİKA MİMARİSİ",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Bu modül; 4 ülkenin 5. Yüzyıldan Modern Çağa asimetrik birimlerini (Hun Süvarisi, Altay T2, Bayraktar SİHA, TCG Anadolu vb.) Kışla/Fabrika (Kara), Hava Üssü (Hava) ve Tersane (Deniz) binalarına filtreleyen motoru ve periyodik bina kurup oyuncu üssüne Waypoint Pathfinding ile saldıran Skirmish Botunu içerir.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "MilitaryUnit.cs (OOP Veri Modeli)",
            description = "Name, UnitType, Cost, Duration, RequiredCentury, Faction alanlarını tutar",
            code = militaryUnitCode,
            onCopy = {
                val clip = ClipData.newPlainText("MilitaryUnit.cs", militaryUnitCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "MilitaryUnit.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "HistoricalUnitDatabase.cs (Tarihsel & Modern Veri Bankası)",
            description = "Türkiye, ABD, Rusya ve Çin'in tüm çağlardaki gerçekçi birim listesi",
            code = databaseCode,
            onCopy = {
                val clip = ClipData.newPlainText("HistoricalUnitDatabase.cs", databaseCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "HistoricalUnitDatabase.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "ProductionFactory.cs (Filtrelenmiş Üretim & Çağ Atlama)",
            description = "GetAvailableProduction() ve OnCenturyUpgrade() fonksiyonları",
            code = factoryCode,
            onCopy = {
                val clip = ClipData.newPlainText("ProductionFactory.cs", factoryCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "ProductionFactory.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "SkirmishAI.cs (Yol Bulma & Otomatik İnşa Botu)",
            description = "Bina inşa döngüsü, Waypoint Pathfinding ve taarruz dalgası mekaniği",
            code = skirmishAiCode,
            onCopy = {
                val clip = ClipData.newPlainText("SkirmishAI.cs", skirmishAiCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "SkirmishAI.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==================== TAB 10: 3D RTS MOTORU & RAYCASTING ====================
@Composable
fun ThreeDRtsEngineAndRaycastingSection() {
    val context = LocalContext.current

    val camera3DCode = """
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input.Touch;
using System;

namespace ZeroHourMobile.Graphics3D
{
    /// <summary>
    /// C&C Generals tarzı RTS 3D Perspektif Kamera Sistemi.
    /// Dokunmatik sürüklemede X/Z zemin düzleminde pan, iki parmakla Y ekseninde Zoom yapar.
    /// </summary>
    public class Camera3D
    {
        public Vector3 Position { get; private set; }
        public Vector3 Target { get; private set; }
        public Vector3 Up { get; private set; } = Vector3.Up;

        public Matrix ViewMatrix { get; private set; }
        public Matrix ProjectionMatrix { get; private set; }

        public float PitchAngle { get; set; } = MathHelper.ToRadians(52f); // 52 derece açılı C&C Generals bakışı
        public float DistanceFromTarget { get; set; } = 450f; // Y ekseni yakınlaşma mesafesi (Zoom)
        public float MinDistance { get; set; } = 150f;
        public float MaxDistance { get; set; } = 800f;

        private float _previousPinchDistance = 0f;

        public Camera3D(GraphicsDevice graphicsDevice, Vector3 initialTarget)
        {
            Target = initialTarget;
            UpdateMatrices(graphicsDevice.Viewport);
        }

        public void Update(TouchCollection touches, Viewport viewport)
        {
            // 1 PARMAK: X ve Z Ekseninde Pan (Zemin üzerinde kamerayı kaydır)
            if (touches.Count == 1)
            {
                var touch = touches[0];
                if (touch.TryGetPreviousLocation(out var prevTouch))
                {
                    Vector2 delta = touch.Position - prevTouch.Position;
                    float panSpeed = DistanceFromTarget * 0.0025f;

                    // Kameranın baktığı yatay düzlem yönleri
                    Vector3 forward = new Vector3(0, 0, -1);
                    Vector3 right = new Vector3(1, 0, 0);

                    Target -= (right * delta.X - forward * delta.Y) * panSpeed;
                }
                _previousPinchDistance = 0f;
            }
            // 2 PARMAK: Y Ekseninde Pinch-to-Zoom (Yakınlaş / Uzaklaş)
            else if (touches.Count >= 2)
            {
                float currentDistance = Vector2.Distance(touches[0].Position, touches[1].Position);
                if (_previousPinchDistance > 0f)
                {
                    float delta = currentDistance - _previousPinchDistance;
                    DistanceFromTarget = MathHelper.Clamp(DistanceFromTarget - delta * 0.8f, MinDistance, MaxDistance);
                }
                _previousPinchDistance = currentDistance;
            }
            else
            {
                _previousPinchDistance = 0f;
            }

            UpdateMatrices(viewport);
        }

        public void CenterOn(Vector3 worldPos, Viewport viewport)
        {
            Target = worldPos;
            UpdateMatrices(viewport);
        }

        private void UpdateMatrices(Viewport viewport)
        {
            // Trigonometri ile hedefin arkasında ve yukarısında kamera pozisyonunu hesapla
            float height = (float)(DistanceFromTarget * Math.Sin(PitchAngle));
            float offsetZ = (float)(DistanceFromTarget * Math.Cos(PitchAngle));

            Position = new Vector3(Target.X, Target.Y + height, Target.Z + offsetZ);

            ViewMatrix = Matrix.CreateLookAt(Position, Target, Up);
            float aspectRatio = viewport.AspectRatio;
            ProjectionMatrix = Matrix.CreatePerspectiveFieldOfView(
                MathHelper.ToRadians(45f), 
                aspectRatio, 
                1.0f, 
                2500.0f
            );
        }
    }
}
""".trimIndent()

    val renderer3DCode = """
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System.Collections.Generic;

namespace ZeroHourMobile.Graphics3D
{
    /// <summary>
    /// 3D Model yükleme (.fbx / .obj), BasicEffect konfigürasyonu ve 3D çizim döngüsü
    /// </summary>
    public class Renderer3D
    {
        private readonly GraphicsDevice _graphicsDevice;
        private readonly BasicEffect _basicEffect;

        public Renderer3D(GraphicsDevice graphicsDevice)
        {
            _graphicsDevice = graphicsDevice;
            _basicEffect = new BasicEffect(graphicsDevice);
        }

        /// <summary>
        /// 3D Modeli World, View ve Projection matrisleriyle sahneye çizer
        /// </summary>
        public void Draw3DModel(Model model, Matrix worldMatrix, Camera3D camera)
        {
            // Depth-Buffer (Derinlik tamponu) etkinleştir
            _graphicsDevice.DepthStencilState = DepthStencilState.Default;
            _graphicsDevice.RasterizerState = RasterizerState.CullCounterClockwise;
            _graphicsDevice.BlendState = BlendState.Opaque;

            // Kemik (Bone) hiyerarşisi dönüşüm matrisleri
            Matrix[] boneTransforms = new Matrix[model.Bones.Count];
            model.CopyAbsoluteBoneTransformsTo(boneTransforms);

            foreach (ModelMesh mesh in model.Meshes)
            {
                foreach (BasicEffect effect in mesh.Effects)
                {
                    // Matrisler
                    effect.World = boneTransforms[mesh.ParentBone.Index] * worldMatrix;
                    effect.View = camera.ViewMatrix;
                    effect.Projection = camera.ProjectionMatrix;

                    // Askeri C&C Generals Güneş ve Çevre Aydınlatması
                    effect.LightingEnabled = true;
                    effect.EnableDefaultLighting();

                    // Ana Güneş Işığı (Key Light)
                    effect.DirectionalLight0.Enabled = true;
                    effect.DirectionalLight0.DiffuseColor = new Vector3(1.0f, 0.95f, 0.85f); // Sıcak gün ışığı
                    effect.DirectionalLight0.Direction = Vector3.Normalize(new Vector3(-1f, -1.8f, -1f));
                    effect.DirectionalLight0.SpecularColor = new Vector3(0.4f, 0.4f, 0.4f);

                    // Ortam Işığı (Ambient)
                    effect.AmbientLightColor = new Vector3(0.25f, 0.28f, 0.35f);
                }
                mesh.Draw();
            }
        }
    }
}
""".trimIndent()

    val raycastPickerCode = """
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System.Collections.Generic;

namespace ZeroHourMobile.Graphics3D
{
    public interface ISelectable3D
    {
        string Name { get; }
        Vector3 Position { get; }
        BoundingBox BoundingBox { get; }
        BoundingSphere BoundingSphere { get; }
    }

    /// <summary>
    /// 2D ekran dokunuşunu 3D uzayda bir Işına (Ray) çevirip hangi 3D tanka veya binaya dokunulduğunu bulur
    /// </summary>
    public static class RaycastPicker
    {
        public static ISelectable3D GetSelected3DObject(
            Vector2 touchPosition, 
            Viewport viewport, 
            Matrix viewMatrix, 
            Matrix projectionMatrix, 
            IEnumerable<ISelectable3D> objects)
        {
            // 1. Ekrandaki 2D dokunma noktasını Yakın (Near) ve Uzak (Far) 3D düzlemlere projekte et
            Vector3 nearSource = new Vector3(touchPosition.X, touchPosition.Y, 0f);
            Vector3 farSource = new Vector3(touchPosition.X, touchPosition.Y, 1f);

            Vector3 nearPoint = viewport.Unproject(nearSource, projectionMatrix, viewMatrix, Matrix.Identity);
            Vector3 farPoint = viewport.Unproject(farSource, projectionMatrix, viewMatrix, Matrix.Identity);

            // 2. Işın (Ray) oluştur
            Vector3 direction = farPoint - nearPoint;
            direction.Normalize();
            Ray pickRay = new Ray(nearPoint, direction);

            // 3. Sahnedeki 3D nesnelerle kesişim (Ray-Mesh/BoundingBox Intersection) testi
            ISelectable3D closestHitObject = null;
            float minDistance = float.MaxValue;

            foreach (var obj in objects)
            {
                // BoundingBox veya BoundingSphere ile ışın kesişimini kontrol et
                float? distance = pickRay.Intersects(obj.BoundingBox);
                if (distance.HasValue && distance.Value < minDistance)
                {
                    minDistance = distance.Value;
                    closestHitObject = obj;
                }
            }

            return closestHitObject;
        }

        /// <summary>
        /// Dokunulan noktanın Y = 0 zemin düzlemindeki 3D koordinatını bulur (Bina dikme veya birim hareket emri için)
        /// </summary>
        public static Vector3? GetGroundHitPoint(
            Vector2 touchPosition, 
            Viewport viewport, 
            Matrix viewMatrix, 
            Matrix projectionMatrix)
        {
            Vector3 nearPoint = viewport.Unproject(new Vector3(touchPosition.X, touchPosition.Y, 0f), projectionMatrix, viewMatrix, Matrix.Identity);
            Vector3 farPoint = viewport.Unproject(new Vector3(touchPosition.X, touchPosition.Y, 1f), projectionMatrix, viewMatrix, Matrix.Identity);

            Vector3 direction = farPoint - nearPoint;
            direction.Normalize();
            Ray ray = new Ray(nearPoint, direction);

            // Y = 0 Zemin Düzlemi
            Plane groundPlane = new Plane(Vector3.Up, 0f);
            float? distance = ray.Intersects(groundPlane);

            if (distance.HasValue)
            {
                return ray.Position + ray.Direction * distance.Value;
            }

            return null;
        }
    }
}
""".trimIndent()

    val terrain3DCode = """
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ZeroHourMobile.Graphics3D
{
    /// <summary>
    /// 3D uzayda Y = 0 düzlemine oturan, binaların yerleştirileceği 3D zemin ve ızgara (Grid) sınıfı
    /// </summary>
    public class Terrain3D
    {
        private readonly GraphicsDevice _graphicsDevice;
        private readonly BasicEffect _effect;
        private VertexBuffer _vertexBuffer;
        private IndexBuffer _indexBuffer;

        public int GridWidth { get; } = 40;
        public int GridHeight { get; } = 40;
        public float CellSize { get; } = 35f;

        public Terrain3D(GraphicsDevice graphicsDevice)
        {
            _graphicsDevice = graphicsDevice;
            _effect = new BasicEffect(graphicsDevice)
            {
                VertexColorEnabled = true
            };

            BuildTerrainMesh();
        }

        private void BuildTerrainMesh()
        {
            int vertexCount = (GridWidth + 1) * (GridHeight + 1);
            VertexPositionColor[] vertices = new VertexPositionColor[vertexCount];

            float startX = -(GridWidth * CellSize) / 2f;
            float startZ = -(GridHeight * CellSize) / 2f;

            int idx = 0;
            for (int z = 0; z <= GridHeight; z++)
            {
                for (int x = 0; x <= GridWidth; x++)
                {
                    float px = startX + x * CellSize;
                    float pz = startZ + z * CellSize;
                    float py = 0f; // Y = 0 düzlemi

                    // C&C Generals çöl/çim kamuflaj zemin rengi
                    Color cellColor = ((x + z) % 2 == 0) ? new Color(38, 55, 34) : new Color(32, 48, 28);
                    vertices[idx++] = new VertexPositionColor(new Vector3(px, py, pz), cellColor);
                }
            }

            // İndeksler (Triangles)
            int indexCount = GridWidth * GridHeight * 6;
            short[] indices = new short[indexCount];
            int iIdx = 0;

            for (int z = 0; z < GridHeight; z++)
            {
                for (int x = 0; x < GridWidth; x++)
                {
                    short topLeft = (short)(z * (GridWidth + 1) + x);
                    short topRight = (short)(topLeft + 1);
                    short bottomLeft = (short)((z + 1) * (GridWidth + 1) + x);
                    short bottomRight = (short)(bottomLeft + 1);

                    // 1. Üçgen
                    indices[iIdx++] = topLeft;
                    indices[iIdx++] = bottomLeft;
                    indices[iIdx++] = topRight;

                    // 2. Üçgen
                    indices[iIdx++] = topRight;
                    indices[iIdx++] = bottomLeft;
                    indices[iIdx++] = bottomRight;
                }
            }

            _vertexBuffer = new VertexBuffer(_graphicsDevice, typeof(VertexPositionColor), vertices.Length, BufferUsage.WriteOnly);
            _vertexBuffer.SetData(vertices);

            _indexBuffer = new IndexBuffer(_graphicsDevice, IndexElementSize.SixteenBits, indices.Length, BufferUsage.WriteOnly);
            _indexBuffer.SetData(indices);
        }

        public void Draw(Camera3D camera)
        {
            _effect.World = Matrix.Identity;
            _effect.View = camera.ViewMatrix;
            _effect.Projection = camera.ProjectionMatrix;

            _graphicsDevice.SetVertexBuffer(_vertexBuffer);
            _graphicsDevice.Indices = _indexBuffer;

            foreach (EffectPass pass in _effect.CurrentTechnique.Passes)
            {
                pass.Apply();
                _graphicsDevice.DrawIndexedPrimitives(PrimitiveType.TriangleList, 0, 0, _indexBuffer.IndexCount / 3);
            }
        }
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(CyanNeon, AmberGold))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ViewInAr, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "3D RTS PERSPEKTİF MOTORU & RAYCASTING (C# MONOGAME)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "C&C Generals: Zero Hour gibi 3D araziler, 3D binalar ve hareket eden askeri modeller (Altay tankı, SİHA vb.) için 3D Perspektif Kamera, Model yükleme/çizim pipeline'ı, Viewport.Unproject ile dokunmatik Raycasting ve Y=0 3D zemin ızgara altyapısı.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "Camera3D.cs (RTS 3D Perspektif Kamera)",
            description = "X/Z Pan, Y Ekseninde Pinch-to-Zoom ve C&C Generals 52° Pitch açısı",
            code = camera3DCode,
            onCopy = {
                val clip = ClipData.newPlainText("Camera3D.cs", camera3DCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Camera3D.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "Renderer3D.cs (3D Model Yükleme & Draw3D Döngüsü)",
            description = ".fbx/.obj modelleri BasicEffect, World, View, Projection ve güneş ışığıyla çizer",
            code = renderer3DCode,
            onCopy = {
                val clip = ClipData.newPlainText("Renderer3D.cs", renderer3DCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Renderer3D.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "RaycastPicker.cs (3D Dünyaya Dokunma & Raycasting)",
            description = "2D ekran dokunuşunu 3D Işına (Ray) çevirip tank veya binayı seçme",
            code = raycastPickerCode,
            onCopy = {
                val clip = ClipData.newPlainText("RaycastPicker.cs", raycastPickerCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "RaycastPicker.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "Terrain3D.cs (Y=0 Düzlemi 3D Zemin ve Izgara Mesh)",
            description = "VertexBuffer/IndexBuffer ile 3D arazi zemin ızgarası ve bina yerleşim tabanı",
            code = terrain3DCode,
            onCopy = {
                val clip = ClipData.newPlainText("Terrain3D.cs", terrain3DCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Terrain3D.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==================== TAB 11: SES YÖNETİMİ & RECHARTS SAVAŞ TELEMETRİSİ ====================
@Composable
fun SoundSystemAndTelemetrySection() {
    val context = LocalContext.current

    val csharpAudioCode = """
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;

namespace ZeroHourMobile.Audio
{
    public enum UnitSoundAction
    {
        Move,
        Attack,
        Death
    }

    /// <summary>
    /// Birim türlerine göre farklılaştırılmış ses ID'leri ve 3D konumsal ses yöneticisi
    /// </summary>
    public class RtsAudioManager
    {
        public static RtsAudioManager Instance { get; private set; }

        private readonly Dictionary<string, SoundEffect> _soundEffects = new Dictionary<string, SoundEffect>();
        private readonly AudioListener _cameraListener = new AudioListener();
        private float _masterVolume = 1.0f;
        public bool IsMuted { get; set; } = false;

        public RtsAudioManager()
        {
            Instance = this;
        }

        public void LoadContent(ContentManager content)
        {
            // 1. TANK SESLERİ
            RegisterSound("tank_engine_rev", content.Load<SoundEffect>("Audio/Units/Tank/engine_rev"));
            RegisterSound("tank_cannon_120mm", content.Load<SoundEffect>("Audio/Units/Tank/cannon_120mm"));
            RegisterSound("tank_explosion_hull", content.Load<SoundEffect>("Audio/Units/Tank/explosion_hull"));

            // 2. JET VE SİHA SESLERİ
            RegisterSound("jet_afterburner_whoosh", content.Load<SoundEffect>("Audio/Units/Air/afterburner"));
            RegisterSound("missile_launch_cirit", content.Load<SoundEffect>("Audio/Units/Air/missile_launch"));
            RegisterSound("airframe_breakup", content.Load<SoundEffect>("Audio/Units/Air/airframe_breakup"));

            // 3. PİYADE VE ASKER SESLERİ
            RegisterSound("infantry_boots_march", content.Load<SoundEffect>("Audio/Units/Infantry/boots_march"));
            RegisterSound("rifle_burst_mpt76", content.Load<SoundEffect>("Audio/Units/Infantry/rifle_burst"));
            RegisterSound("soldier_down", content.Load<SoundEffect>("Audio/Units/Infantry/soldier_down"));

            // 4. DENİZ VE DENİZALTI SESLERİ
            RegisterSound("sonar_ping_active", content.Load<SoundEffect>("Audio/Units/Naval/sonar_ping"));
            RegisterSound("heavy_torpedo_launch", content.Load<SoundEffect>("Audio/Units/Naval/torpedo_launch"));
            RegisterSound("bulkhead_implosion", content.Load<SoundEffect>("Audio/Units/Naval/bulkhead_implosion"));

            // 5. KUŞATMA VE BALİSTİK FÜZE SESLERİ
            RegisterSound("heavy_tread_groan", content.Load<SoundEffect>("Audio/Units/Siege/heavy_tread"));
            RegisterSound("artillery_earthquake_blast", content.Load<SoundEffect>("Audio/Units/Siege/ballistic_blast"));
            RegisterSound("magazine_detonation", content.Load<SoundEffect>("Audio/Units/Siege/magazine_detonation"));

            // 6. TAKTİK HUD & ÇAĞ SESLERİ
            RegisterSound("radio_order_ack", content.Load<SoundEffect>("Audio/UI/radio_ack"));
            RegisterSound("era_transition_fanfare", content.Load<SoundEffect>("Audio/UI/era_fanfare"));
        }

        private void RegisterSound(string id, SoundEffect sfx)
        {
            if (sfx != null) _soundEffects[id] = sfx;
        }

        /// <summary>
        /// Birim türü ve aksiyona göre tetiklenebilir ses yürütücü
        /// </summary>
        public void PlayUnitSound(string unitCategory, UnitSoundAction action, Vector3? worldPosition = null)
        {
            if (IsMuted) return;

            string soundKey = GetSoundKey(unitCategory, action);
            if (!_soundEffects.TryGetValue(soundKey, out SoundEffect effect)) return;

            if (worldPosition.HasValue)
            {
                // 3D Konumsal Ses (Stereo Panning & Mesafe Zayıflaması)
                AudioEmitter emitter = new AudioEmitter { Position = worldPosition.Value };
                SoundEffectInstance instance = effect.CreateInstance();
                instance.Apply3D(_cameraListener, emitter);
                instance.Volume = _masterVolume;
                instance.Play();
            }
            else
            {
                effect.Play(_masterVolume, 0f, 0f);
            }
        }

        private string GetSoundKey(string category, UnitSoundAction action)
        {
            switch (category.ToLower())
            {
                case "tank":
                    return action switch {
                        UnitSoundAction.Move => "tank_engine_rev",
                        UnitSoundAction.Attack => "tank_cannon_120mm",
                        UnitSoundAction.Death => "tank_explosion_hull",
                        _ => "tank_engine_rev"
                    };
                case "jet":
                case "drone":
                    return action switch {
                        UnitSoundAction.Move => "jet_afterburner_whoosh",
                        UnitSoundAction.Attack => "missile_launch_cirit",
                        UnitSoundAction.Death => "airframe_breakup",
                        _ => "jet_afterburner_whoosh"
                    };
                case "infantry":
                    return action switch {
                        UnitSoundAction.Move => "infantry_boots_march",
                        UnitSoundAction.Attack => "rifle_burst_mpt76",
                        UnitSoundAction.Death => "soldier_down",
                        _ => "infantry_boots_march"
                    };
                case "naval":
                case "submarine":
                    return action switch {
                        UnitSoundAction.Move => "sonar_ping_active",
                        UnitSoundAction.Attack => "heavy_torpedo_launch",
                        UnitSoundAction.Death => "bulkhead_implosion",
                        _ => "sonar_ping_active"
                    };
                case "siege":
                    return action switch {
                        UnitSoundAction.Move => "heavy_tread_groan",
                        UnitSoundAction.Attack => "artillery_earthquake_blast",
                        UnitSoundAction.Death => "magazine_detonation",
                        _ => "artillery_earthquake_blast"
                    };
                default:
                    return "radio_order_ack";
            }
        }

        public void UpdateCameraListener(Vector3 cameraPos, Vector3 forward, Vector3 up)
        {
            _cameraListener.Position = cameraPos;
            _cameraListener.Forward = forward;
            _cameraListener.Up = up;
        }
    }
}
""".trimIndent()

    val rechartsCode = """
import React, { useState } from 'react';
import { 
  AreaChart, Area, BarChart, Bar, XAxis, YAxis, 
  CartesianGrid, Tooltip, ResponsiveContainer, Legend 
} from 'recharts';

/**
 * Global Savaş Paneli - Recharts Veri Grafiği
 * Oyuncuların toplam zafer sayısını ve küresel çatışma yoğunluğunu zamana bağlı gösterir.
 */
export const GlobalWarRoomDashboard = () => {
  const [timeRange, setTimeRange] = useState('24h');

  // Küresel Çatışma Yoğunluğu Zaman Serisi
  const conflictDensityData = [
    { time: '00:00', density: 14, playerVictories: 5, casualties: 9 },
    { time: '02:00', density: 32, playerVictories: 18, casualties: 14 },
    { time: '04:00', density: 68, playerVictories: 42, casualties: 26 },
    { time: '06:00', density: 110, playerVictories: 75, casualties: 35 },
    { time: '08:00', density: 165, playerVictories: 115, casualties: 50 },
    { time: '10:00', density: 245, playerVictories: 180, casualties: 65 },
    { time: '12:00', density: 210, playerVictories: 155, casualties: 55 },
    { time: '14:00', density: 285, playerVictories: 215, casualties: 70 },
  ];

  // Ülke Bazlı Toplam Zafer ve Başarı Oranları
  const factionWinData = [
    { nation: 'Türkiye 🇹🇷', victories: 485, winRate: 72.4, color: '#FF3366' },
    { nation: 'ABD 🇺🇸', victories: 390, winRate: 64.1, color: '#00F5D4' },
    { nation: 'Rusya 🇷🇺', victories: 345, winRate: 58.7, color: '#F59E0B' },
    { nation: 'Çin 🇨🇳', victories: 310, winRate: 55.2, color: '#10B981' },
  ];

  return (
    <div style={{ background: '#090d14', color: '#fff', padding: '24px', borderRadius: '12px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h2 style={{ margin: 0, color: '#00F5D4', fontSize: '20px', fontWeight: 'bold' }}>
            GLOBAL SAVAŞ ODASI (WAR TELEMETRY DASHBOARD)
          </h2>
          <p style={{ margin: '4px 0 0 0', color: '#94a3b8', fontSize: '12px' }}>
            Canlı Çatışma Yoğunluğu, Birim Zayiatları ve Ülke Zafer Dağılımı
          </p>
        </div>
        <div style={{ background: 'rgba(255, 51, 102, 0.2)', border: '1px solid #FF3366', padding: '6px 14px', borderRadius: '20px', color: '#FF3366', fontSize: '11px', fontWeight: 'bold' }}>
          CANLI CEPHE AKTİF
        </div>
      </div>

      {/* 1. KÜRESEL ÇATIŞMA YOĞUNLUĞU GRAFİĞİ (Area Chart) */}
      <div style={{ background: 'rgba(15, 23, 42, 0.7)', padding: '16px', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.08)', marginBottom: '20px' }}>
        <h3 style={{ margin: '0 0 12px 0', fontSize: '13px', color: '#F59E0B', fontWeight: 'bold' }}>
          ZAMANA BAĞLI KÜRESEL ÇATIŞMA YOĞUNLUĞU EĞRİSİ (Conflict Density)
        </h3>
        <div style={{ height: '220px' }}>
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={conflictDensityData}>
              <defs>
                <linearGradient id="colorDensity" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#00F5D4" stopOpacity={0.8}/>
                  <stop offset="95%" stopColor="#00F5D4" stopOpacity={0}/>
                </linearGradient>
                <linearGradient id="colorWins" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#FF3366" stopOpacity={0.8}/>
                  <stop offset="95%" stopColor="#FF3366" stopOpacity={0}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
              <XAxis dataKey="time" stroke="#64748b" fontSize={11} />
              <YAxis stroke="#64748b" fontSize={11} />
              <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155' }} />
              <Legend />
              <Area type="monotone" dataKey="density" stroke="#00F5D4" fillOpacity={1} fill="url(#colorDensity)" name="Çatışma Yoğunluğu" />
              <Area type="monotone" dataKey="playerVictories" stroke="#FF3366" fillOpacity={1} fill="url(#colorWins)" name="Kazanılan Zaferler" />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* 2. ÜLKE BAZLI ZAFER DAĞILIMI (Bar Chart) */}
      <div style={{ background: 'rgba(15, 23, 42, 0.7)', padding: '16px', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.08)' }}>
        <h3 style={{ margin: '0 0 12px 0', fontSize: '13px', color: '#00F5D4', fontWeight: 'bold' }}>
          ÜLKELERE GÖRE KÜRESEL ZAFER SAYISI & BAŞARI ORANI
        </h3>
        <div style={{ height: '180px' }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={factionWinData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
              <XAxis dataKey="nation" stroke="#64748b" fontSize={11} />
              <YAxis stroke="#64748b" fontSize={11} />
              <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#334155' }} />
              <Bar dataKey="victories" fill="#F59E0B" name="Toplam Zafer" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(CyanNeon, LaserCrimson))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SES YÖNETİMİ & RECHARTS KÜRESEL SAVAŞ PANELİ",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tank, SİHA, Piyade, Denizaltı ve Kuşatma birimlerinin hareket, saldırı ve ölüm anlarında tetiklenen ses ID mimarisi ile web/masaüstü için Recharts React veri grafiği bileşeni.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "RtsAudioManager.cs (MonoGame 3D Ses Motoru & ID Sistemi)",
            description = "Tank paleti, top ateşleme, SİHA türbini, piyade tüfeği ve 3D konumsal ses",
            code = csharpAudioCode,
            onCopy = {
                val clip = ClipData.newPlainText("RtsAudioManager.cs", csharpAudioCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "RtsAudioManager.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "GlobalWarRoomDashboard.jsx (Recharts Veri Grafiği)",
            description = "ResponsiveContainer, AreaChart ve BarChart ile çatışma yoğunluğu ve zafer grafiği",
            code = rechartsCode,
            onCopy = {
                val clip = ClipData.newPlainText("GlobalWarRoomDashboard.jsx", rechartsCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Recharts Kodu Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

