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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

// ==================== TAB 14: 20 SÜPER GÜÇ & GERÇEK ASKERİ ENVANTER ====================
@Composable
fun TwentySuperpowersAndInventorySection() {
    val context = LocalContext.current

    val weaponSystemAndBlueprintCode = """
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;

namespace ZeroHourMobile.MilitaryData
{
    /// <summary>
    /// Gerçek askeri doktrinlere göre ayrılmış silah sistemi rolleri
    /// </summary>
    public enum WeaponSystem
    {
        AirSuperiorityFighter, // F-22 Raptor, Su-57, KAAN
        MultiRoleStealthJet,   // F-35 Lightning II, J-20
        StrikeAttackerJet,     // A-10 Thunderbolt II, Su-25 Frogfoot
        HeavyStrategicBomber,  // B-2 Spirit, Tu-160
        ReconArmedDrone,       // Bayraktar TB2, ANKA-3
        HeavyCombatDrone,      // Bayraktar KIZILELMA, Akıncı
        MainBattleTank,        // Altay T2, M1A2 Abrams, T-90M Proryv, Type 99A
        ArmoredInfantryCombat, // BMP-3, Stryker, Type 05
        SelfPropelledHowitzer, // T-155 Fırtına, M109 Paladin
        BallisticMissileSystem,// BORA, TAYFUN, Iskander, Tomahawk
        AircraftCarrier,       // TCG Anadolu, Gerald R. Ford, Amiral Kuznetsov, Fujian
        GuidedMissileFrigate,  // İstif Sınıfı (İ), Arleigh Burke, Udaloy, Type 055
        AttackSubmarine        // Reis Sınıfı (AIP), Virginia, Yasen, Type 094
    }

    /// <summary>
    /// GC (Garbage Collection) baskısını sıfıra indiren hafif C# struct veri modeli
    /// </summary>
    public struct UnitBlueprint
    {
        public string Id;
        public string Name;
        public string Nation;
        public WeaponSystem Role;
        public int RequiredEraCentury; // 5: Orta Çağ, 18: Barut, 19: Endüstri, 21: Modern
        public int CostGold;
        public int CostOil;
        public float ProductionDuration;
        public float Speed;
        public float AttackPower;
        public float ArmorHp;
        public float RadarStealthMultiplier; // 0.20: Hayalet Uçak (Radara %80 görünmez), 1.0: Standart

        public UnitBlueprint(string id, string name, string nation, WeaponSystem role, int century,
                             int gold, int oil, float duration, float speed, float attack, float hp, float stealth = 1.0f)
        {
            Id = id;
            Name = name;
            Nation = nation;
            Role = role;
            RequiredEraCentury = century;
            CostGold = gold;
            CostOil = oil;
            ProductionDuration = duration;
            Speed = speed;
            AttackPower = attack;
            ArmorHp = hp;
            RadarStealthMultiplier = stealth;
        }
    }
}
""".trimIndent()

    val factionDatabase20NationsCode = """
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;

namespace ZeroHourMobile.MilitaryData
{
    public class SuperpowerFactionInfo
    {
        public string NationKey { get; set; }
        public string DisplayName { get; set; }
        public string FlagEmoji { get; set; }
        public int GlobalMilitaryRank { get; set; }
        public Vector3 WorldSpawnPosition { get; set; }
        public int MinEraCentury { get; set; } = 5;
        public int MaxEraCentury { get; set; } = 21;
        public string NationalDoctrinePassive { get; set; }
        public List<UnitBlueprint> SpecialUnits { get; } = new List<UnitBlueprint>();
    }

    /// <summary>
    /// En Güçlü 20 Ülkenin 3D Dünya Koordinatları ve Gerçek Askeri Envanter Veritabanı
    /// </summary>
    public static class FactionInventoryDatabase
    {
        public static Dictionary<string, SuperpowerFactionInfo> Nations { get; private set; }
        public static List<UnitBlueprint> MasterUnitCatalog { get; private set; }

        static FactionInventoryDatabase()
        {
            InitializeDatabase();
        }

        public static void InitializeDatabase()
        {
            Nations = new Dictionary<string, SuperpowerFactionInfo>(20);
            MasterUnitCatalog = new List<UnitBlueprint>(120);

            // ==================== 1. TÜRKİYE (🇹🇷) ====================
            var turkey = new SuperpowerFactionInfo
            {
                NationKey = "TURKEY",
                DisplayName = "Türkiye",
                FlagEmoji = "🇹🇷",
                GlobalMilitaryRank = 8,
                WorldSpawnPosition = new Vector3(390f, 0f, 350f), // Anadolu (39°N, 35°E)
                NationalDoctrinePassive = "SİHA Sürü Doktrini: Bayraktar ve ANKA-3 %25 daha ucuz üretilir."
            };
            // Türkiye Gerçek Envanter Girdileri
            AddUnit(turkey, new UnitBlueprint("tr_f16", "F-16 Blok 70 Viper", "TURKEY", WeaponSystem.AirSuperiorityFighter, 21, 1200, 280, 7f, 95f, 180f, 520f, 0.65f));
            AddUnit(turkey, new UnitBlueprint("tr_f4", "F-4E Phantom II Terminator", "TURKEY", WeaponSystem.StrikeAttackerJet, 21, 650, 180, 5f, 75f, 150f, 440f, 0.90f));
            AddUnit(turkey, new UnitBlueprint("tr_tb2", "Bayraktar TB2 SİHA", "TURKEY", WeaponSystem.ReconArmedDrone, 21, 450, 100, 4f, 60f, 120f, 260f, 0.40f));
            AddUnit(turkey, new UnitBlueprint("tr_tb3", "Bayraktar TB3 (Katlanır Kanat)", "TURKEY", WeaponSystem.ReconArmedDrone, 21, 550, 120, 4.5f, 65f, 135f, 300f, 0.35f));
            AddUnit(turkey, new UnitBlueprint("tr_anka3", "TUSAŞ ANKA-3 Uçan Kanat", "TURKEY", WeaponSystem.HeavyCombatDrone, 21, 950, 220, 6f, 85f, 175f, 480f, 0.20f));
            AddUnit(turkey, new UnitBlueprint("tr_akinci", "Bayraktar Akıncı TİHA", "TURKEY", WeaponSystem.HeavyCombatDrone, 21, 850, 200, 5.5f, 70f, 160f, 420f, 0.30f));
            AddUnit(turkey, new UnitBlueprint("tr_kizilelma", "Bayraktar KIZILELMA", "TURKEY", WeaponSystem.MultiRoleStealthJet, 21, 1400, 320, 7f, 105f, 210f, 620f, 0.18f));
            AddUnit(turkey, new UnitBlueprint("tr_altay", "Altay T2 Ana Muharebe Tankı", "TURKEY", WeaponSystem.MainBattleTank, 21, 900, 220, 6f, 45f, 195f, 880f, 1.0f));
            AddUnit(turkey, new UnitBlueprint("tr_leo2a4", "Leopard 2A4 TİYK", "TURKEY", WeaponSystem.MainBattleTank, 21, 800, 190, 5.5f, 42f, 175f, 780f, 1.0f));
            AddUnit(turkey, new UnitBlueprint("tr_sabra", "M60T Sabra", "TURKEY", WeaponSystem.MainBattleTank, 21, 620, 150, 5f, 38f, 155f, 690f, 1.0f));
            AddUnit(turkey, new UnitBlueprint("tr_firtina", "T-155 Fırtına II Obüsü", "TURKEY", WeaponSystem.SelfPropelledHowitzer, 21, 750, 160, 5f, 35f, 230f, 520f, 1.0f));
            AddUnit(turkey, new UnitBlueprint("tr_bora", "BORA / TAYFUN Balistik Füzesi", "TURKEY", WeaponSystem.BallisticMissileSystem, 21, 1500, 400, 9f, 140f, 380f, 300f, 0.5f));
            AddUnit(turkey, new UnitBlueprint("tr_anadolu", "TCG Anadolu SİHA Gemisi", "TURKEY", WeaponSystem.AircraftCarrier, 21, 2800, 800, 14f, 28f, 250f, 2400f, 0.8f));
            AddUnit(turkey, new UnitBlueprint("tr_istif", "TCG İstanbul (İstif Sınıfı)", "TURKEY", WeaponSystem.GuidedMissileFrigate, 21, 1600, 450, 8f, 34f, 210f, 1400f, 0.5f));
            AddUnit(turkey, new UnitBlueprint("tr_ada", "Ada Sınıfı Milgem Korveti", "TURKEY", WeaponSystem.GuidedMissileFrigate, 21, 1200, 320, 6.5f, 36f, 170f, 1100f, 0.45f));
            AddUnit(turkey, new UnitBlueprint("tr_reis", "TCG Piri Reis (AIP Denizaltı)", "TURKEY", WeaponSystem.AttackSubmarine, 21, 1700, 420, 8.5f, 30f, 220f, 950f, 0.15f));
            Nations.Add("TURKEY", turkey);

            // ==================== 2. ABD (🇺🇸) ====================
            var usa = new SuperpowerFactionInfo
            {
                NationKey = "USA",
                DisplayName = "Amerika Birleşik Devletleri",
                FlagEmoji = "🇺🇸",
                GlobalMilitaryRank = 1,
                WorldSpawnPosition = new Vector3(-770f, 0f, 380f), // Washington (38°N, 77°W)
                NationalDoctrinePassive = "Global Hava Hakimiyeti: F-22 ve F-35 radara %80 daha az yakalanır."
            };
            AddUnit(usa, new UnitBlueprint("us_f22", "F-22 Raptor", "USA", WeaponSystem.AirSuperiorityFighter, 21, 1900, 450, 8.5f, 115f, 240f, 680f, 0.10f));
            AddUnit(usa, new UnitBlueprint("us_f35", "F-35A Lightning II", "USA", WeaponSystem.MultiRoleStealthJet, 21, 1750, 400, 8f, 100f, 215f, 650f, 0.15f));
            AddUnit(usa, new UnitBlueprint("us_f15ex", "F-15EX Eagle II", "USA", WeaponSystem.AirSuperiorityFighter, 21, 1450, 350, 7f, 110f, 230f, 620f, 0.60f));
            AddUnit(usa, new UnitBlueprint("us_a10", "A-10C Thunderbolt II Warthog", "USA", WeaponSystem.StrikeAttackerJet, 21, 950, 240, 5.5f, 65f, 220f, 750f, 0.85f));
            AddUnit(usa, new UnitBlueprint("us_b2", "B-2 Spirit Hayalet Bombardıman", "USA", WeaponSystem.HeavyStrategicBomber, 21, 3200, 900, 15f, 80f, 420f, 950f, 0.05f));
            AddUnit(usa, new UnitBlueprint("us_m1a2", "M1A2 Abrams SEPv3", "USA", WeaponSystem.MainBattleTank, 21, 1200, 300, 7f, 44f, 210f, 950f, 1.0f));
            AddUnit(usa, new UnitBlueprint("us_stryker", "M1126 Stryker ICV", "USA", WeaponSystem.ArmoredInfantryCombat, 21, 650, 160, 4.5f, 55f, 140f, 540f, 1.0f));
            AddUnit(usa, new UnitBlueprint("us_paladin", "M109A7 Paladin Obüsü", "USA", WeaponSystem.SelfPropelledHowitzer, 21, 850, 200, 5.5f, 36f, 240f, 560f, 1.0f));
            AddUnit(usa, new UnitBlueprint("us_tomahawk", "Tomahawk Seyir Füzesi Rampası", "USA", WeaponSystem.BallisticMissileSystem, 21, 1600, 420, 9f, 130f, 390f, 320f, 0.3f));
            AddUnit(usa, new UnitBlueprint("us_ford", "Gerald R. Ford Nükleer Uçak Gemisi", "USA", WeaponSystem.AircraftCarrier, 21, 4500, 1200, 20f, 26f, 320f, 3800f, 0.9f));
            AddUnit(usa, new UnitBlueprint("us_burke", "Arleigh Burke Flight III Muhrip", "USA", WeaponSystem.GuidedMissileFrigate, 21, 2100, 550, 10f, 32f, 260f, 1800f, 0.45f));
            AddUnit(usa, new UnitBlueprint("us_virginia", "Virginia Sınıfı Nükleer Taarruz", "USA", WeaponSystem.AttackSubmarine, 21, 2300, 600, 11f, 33f, 280f, 1250f, 0.12f));
            Nations.Add("USA", usa);

            // ==================== 3. RUSYA (🇷🇺) ====================
            var russia = new SuperpowerFactionInfo
            {
                NationKey = "RUSSIA",
                DisplayName = "Rusya Federasyonu",
                FlagEmoji = "🇷🇺",
                GlobalMilitaryRank = 2,
                WorldSpawnPosition = new Vector3(370f, 0f, 550f), // Moskova (55°N, 37°E)
                NationalDoctrinePassive = "Ağır Zırh ve Roket Barajı: T-90M ve İskender füzeleri +%25 patlama hasarı verir."
            };
            AddUnit(russia, new UnitBlueprint("ru_su57", "Su-57 Felon", "RUSSIA", WeaponSystem.AirSuperiorityFighter, 21, 1800, 430, 8f, 110f, 230f, 670f, 0.20f));
            AddUnit(russia, new UnitBlueprint("ru_su35", "Su-35S Flanker-E", "RUSSIA", WeaponSystem.AirSuperiorityFighter, 21, 1350, 320, 6.5f, 105f, 205f, 610f, 0.55f));
            AddUnit(russia, new UnitBlueprint("ru_mig31", "MiG-31BM Foxhound (Kinjal)", "RUSSIA", WeaponSystem.AirSuperiorityFighter, 21, 1500, 380, 7f, 130f, 260f, 590f, 0.65f));
            AddUnit(russia, new UnitBlueprint("ru_su25", "Su-25SM3 Frogfoot", "RUSSIA", WeaponSystem.StrikeAttackerJet, 21, 850, 210, 5f, 65f, 195f, 720f, 0.85f));
            AddUnit(russia, new UnitBlueprint("ru_tu160", "Tu-160M Beyaz Kuğu", "RUSSIA", WeaponSystem.HeavyStrategicBomber, 21, 3100, 850, 14f, 95f, 400f, 900f, 0.40f));
            AddUnit(russia, new UnitBlueprint("ru_t14", "T-14 Armata", "RUSSIA", WeaponSystem.MainBattleTank, 21, 1300, 310, 7.5f, 46f, 220f, 980f, 0.90f));
            AddUnit(russia, new UnitBlueprint("ru_t90m", "T-90M Proryv-3", "RUSSIA", WeaponSystem.MainBattleTank, 21, 1050, 260, 6.5f, 42f, 205f, 920f, 1.0f));
            AddUnit(russia, new UnitBlueprint("ru_bmp3", "BMP-3M Piyade Muharebe", "RUSSIA", WeaponSystem.ArmoredInfantryCombat, 21, 600, 150, 4.5f, 48f, 160f, 510f, 1.0f));
            AddUnit(russia, new UnitBlueprint("ru_iskander", "9K720 İskender-M Balistik", "RUSSIA", WeaponSystem.BallisticMissileSystem, 21, 1700, 450, 9f, 135f, 410f, 310f, 0.35f));
            AddUnit(russia, new UnitBlueprint("ru_kuznetsov", "Amiral Kuznetsov Ağır Kruvazör", "RUSSIA", WeaponSystem.AircraftCarrier, 21, 3800, 1000, 17f, 25f, 290f, 3200f, 0.85f));
            AddUnit(russia, new UnitBlueprint("ru_udaloy", "Udaloy Sınıfı Anti-Denizaltı Muhrip", "RUSSIA", WeaponSystem.GuidedMissileFrigate, 21, 1950, 500, 9.5f, 31f, 240f, 1650f, 0.50f));
            AddUnit(russia, new UnitBlueprint("ru_yasen", "Yasen-M Sınıfı Nükleer Denizaltı", "RUSSIA", WeaponSystem.AttackSubmarine, 21, 2400, 620, 11f, 32f, 290f, 1350f, 0.10f));
            Nations.Add("RUSSIA", russia);

            // ==================== 4. ÇİN (🇨🇳) ====================
            var china = new SuperpowerFactionInfo
            {
                NationKey = "CHINA",
                DisplayName = "Çin Halk Cumhuriyeti",
                FlagEmoji = "🇨🇳",
                GlobalMilitaryRank = 3,
                WorldSpawnPosition = new Vector3(1160f, 0f, 390f), // Pekin (39°N, 116°E)
                NationalDoctrinePassive = "Seri Fabrikasyon: Kara ve deniz üretim süreleri %20 daha hızlıdır."
            };
            AddUnit(china, new UnitBlueprint("cn_j20", "J-20 Mighty Dragon", "CHINA", WeaponSystem.MultiRoleStealthJet, 21, 1800, 410, 8f, 105f, 220f, 660f, 0.15f));
            AddUnit(china, new UnitBlueprint("cn_j16", "J-16 Çok Rollü Taarruz Uçağı", "CHINA", WeaponSystem.AirSuperiorityFighter, 21, 1300, 300, 6.5f, 100f, 195f, 590f, 0.60f));
            AddUnit(china, new UnitBlueprint("cn_h6k", "H-6K Stratejik Füze Bombardıman", "CHINA", WeaponSystem.HeavyStrategicBomber, 21, 2600, 700, 12f, 75f, 340f, 820f, 0.70f));
            AddUnit(china, new UnitBlueprint("cn_type99a", "Tip 99A Ana Muharebe Tankı", "CHINA", WeaponSystem.MainBattleTank, 21, 1100, 270, 6.5f, 45f, 200f, 910f, 1.0f));
            AddUnit(china, new UnitBlueprint("cn_type05", "Tip 05 Amfibi Zırhlı Taarruz", "CHINA", WeaponSystem.ArmoredInfantryCombat, 21, 620, 150, 4.5f, 50f, 145f, 520f, 1.0f));
            AddUnit(china, new UnitBlueprint("cn_phl16", "PHL-16 Ağır ÇNRA Sistemi", "CHINA", WeaponSystem.SelfPropelledHowitzer, 21, 900, 220, 6f, 38f, 250f, 540f, 1.0f));
            AddUnit(china, new UnitBlueprint("cn_df21d", "DF-21D Gemi-Savar Balistik Füze", "CHINA", WeaponSystem.BallisticMissileSystem, 21, 1850, 480, 9.5f, 145f, 440f, 320f, 0.40f));
            AddUnit(china, new UnitBlueprint("cn_fujian", "Tip 003 Fujian EMALS Uçak Gemisi", "CHINA", WeaponSystem.AircraftCarrier, 21, 4200, 1100, 18f, 27f, 310f, 3600f, 0.85f));
            AddUnit(china, new UnitBlueprint("cn_type055", "Tip 055 Gizli Güdümlü Füze Muhribi", "CHINA", WeaponSystem.GuidedMissileFrigate, 21, 2200, 580, 10.5f, 32f, 270f, 1900f, 0.35f));
            AddUnit(china, new UnitBlueprint("cn_type094", "Tip 094 Jin Sınıfı Nükleer Denizaltı", "CHINA", WeaponSystem.AttackSubmarine, 21, 2500, 650, 12f, 30f, 290f, 1300f, 0.15f));
            Nations.Add("CHINA", china);

            // ==================== DİĞER 16 DÜNYA DEVİ ====================
            RegisterNation("INDIA", "Hindistan", "🇮🇳", 4, new Vector3(770f, 0f, 280f), "Tejas Mk2 & BrahMos Süpersonik Füzeleri", "Tejas Mk2", WeaponSystem.AirSuperiorityFighter, 1300, 280, 175f, 580f);
            RegisterNation("SOUTH_KOREA", "Güney Kore", "🇰🇷", 5, new Vector3(1270f, 0f, 370f), "K2 Black Panther & KF-21 Boramae", "K2 Black Panther", WeaponSystem.MainBattleTank, 1550, 350, 190f, 890f);
            RegisterNation("UK", "İngiltere", "🇬🇧", 6, new Vector3(-10f, 0f, 510f), "Challenger 3 & Astute Denizaltısı", "Challenger 3", WeaponSystem.MainBattleTank, 1600, 380, 185f, 950f);
            RegisterNation("JAPAN", "Japonya", "🇯🇵", 7, new Vector3(1390f, 0f, 350f), "Taigei Denizaltısı & Aegis Kalkanı", "Taigei Sınıfı Denizaltı", WeaponSystem.AttackSubmarine, 1700, 420, 200f, 820f);
            RegisterNation("PAKISTAN", "Pakistan", "🇵🇰", 9, new Vector3(730f, 0f, 330f), "JF-17 Block 3 & Al-Khalid Tankı", "JF-17 Thunder Block 3", WeaponSystem.AirSuperiorityFighter, 1100, 220, 160f, 540f);
            RegisterNation("ITALY", "İtalya", "🇮🇹", 10, new Vector3(120f, 0f, 410f), "Todaro AIP Denizaltısı & Fremm Fırkateyn", "Todaro (Tip 212A)", WeaponSystem.AttackSubmarine, 1450, 310, 180f, 750f);
            RegisterNation("FRANCE", "Fransa", "🇫🇷", 11, new Vector3(20f, 0f, 460f), "Dassault Rafale F4 & Suffren Denizaltı", "Dassault Rafale F4", WeaponSystem.AirSuperiorityFighter, 1850, 420, 215f, 660f);
            RegisterNation("BRAZIL", "Brezilya", "🇧🇷", 12, new Vector3(-470f, 0f, -150f), "Gripen E & Riachuelo Denizaltı", "JAS 39 Gripen E", WeaponSystem.AirSuperiorityFighter, 1350, 270, 170f, 570f);
            RegisterNation("INDONESIA", "Endonezya", "🇮🇩", 13, new Vector3(1060f, 0f, -60f), "Harimau Tankı & Nagapasa Sınıfı", "Harimau Orta Tank", WeaponSystem.MainBattleTank, 1200, 250, 165f, 740f);
            RegisterNation("IRAN", "İran", "🇮🇷", 14, new Vector3(510f, 0f, 350f), "Şahid-136 Kamikaze & Karrar Tankı", "Şahid-136 Sürüsü", WeaponSystem.ReconArmedDrone, 750, 150, 140f, 320f);
            RegisterNation("EGYPT", "Mısır", "🇪🇬", 15, new Vector3(310f, 0f, 300f), "M1A1 Abrams Nil & Mistral Gemisi", "M1A1 Abrams Nil", WeaponSystem.MainBattleTank, 1300, 260, 175f, 850f);
            RegisterNation("AUSTRALIA", "Avustralya", "🇦🇺", 16, new Vector3(1330f, 0f, -250f), "F/A-18F Super Hornet & Collins Sınıfı", "F/A-18F Super Hornet", WeaponSystem.AirSuperiorityFighter, 1650, 360, 195f, 640f);
            RegisterNation("ISRAEL", "İsrail", "🇮🇱", 17, new Vector3(350f, 0f, 310f), "Merkava Mk 4M Trophy & F-35I Adir", "Merkava Mk 4M Windbreaker", WeaponSystem.MainBattleTank, 1700, 340, 210f, 960f);
            RegisterNation("UKRAINE", "Ukrayna", "🇺🇦", 18, new Vector3(300f, 0f, 500f), "T-84 Oplot-M & Neptun Füzesi", "T-84 Oplot-M", WeaponSystem.MainBattleTank, 1350, 280, 185f, 870f);
            RegisterNation("GERMANY", "Almanya", "🇩🇪", 19, new Vector3(100f, 0f, 520f), "Leopard 2A7+ & Tip 212A Denizaltı", "Leopard 2A7+", WeaponSystem.MainBattleTank, 1750, 380, 215f, 980f);
            RegisterNation("SPAIN", "İspanya", "🇪🇸", 20, new Vector3(-30f, 0f, 400f), "S-80 Plus Denizaltısı & Leopard 2E", "S-80 Plus Denizaltı", WeaponSystem.AttackSubmarine, 1500, 330, 185f, 790f);
        }

        private static void AddUnit(SuperpowerFactionInfo nation, UnitBlueprint unit)
        {
            nation.SpecialUnits.Add(unit);
            MasterUnitCatalog.Add(unit);
        }

        private static void RegisterNation(string key, string name, string flag, int rank, Vector3 coords, string passive,
                                           string unitName, WeaponSystem role, int gold, int oil, float atk, float hp)
        {
            var nat = new SuperpowerFactionInfo
            {
                NationKey = key,
                DisplayName = name,
                FlagEmoji = flag,
                GlobalMilitaryRank = rank,
                WorldSpawnPosition = coords,
                NationalDoctrinePassive = passive
            };
            var unit = new UnitBlueprint(key.ToLower() + "_prime", unitName, key, role, 21, gold, oil, 7f, 45f, atk, hp);
            AddUnit(nat, unit);
            Nations.Add(key, nat);
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
                    Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = LaserCrimson, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "20 SÜPER GÜÇ & GERÇEK ASKERİ ENVANTER MİMARİSİ",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Dünyanın askeri açıdan en güçlü 20 ülkesinin 3D Dünya Haritasındaki Vector3 koordinatları, dönem sınırları ve Türkiye (KAAN, Altay, TB3, Kızılelma), ABD (F-22, F-35, Abrams, Virginia), Rusya (Su-57, T-14, Yasen), Çin (J-20, Type 99A) ve diğer 16 ülkenin gerçek hava, kara ve deniz envanter modelleri.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "WeaponSystem.cs & UnitBlueprint.cs (Hafif C# Struct)",
            description = "Garbage Collector'ı yormayan struct yapısı, radar gizlilik çarpanı (Stealth) ve silah sistem rolleri",
            code = weaponSystemAndBlueprintCode,
            onCopy = {
                val clip = ClipData.newPlainText("WeaponSystem.cs", weaponSystemAndBlueprintCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "WeaponSystem.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "FactionInventoryDatabase.cs (20 Ülke Statik Veritabanı)",
            description = "20 ülkenin Vector3 SpawnPosition koordinatları ve tam kapsamlı modern askeri araç envanter kataloğu",
            code = factionDatabase20NationsCode,
            onCopy = {
                val clip = ClipData.newPlainText("FactionInventoryDatabase.cs", factionDatabase20NationsCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "FactionInventoryDatabase.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==================== TAB 15: ASKERİ TİCARET PAZARI & DİPLOMASİ ====================
@Composable
fun MilitaryMarketAndDiplomacySection() {
    val context = LocalContext.current

    val diplomacyAndMarketCode = """
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using ZeroHourMobile.MilitaryData;

namespace ZeroHourMobile.Trade
{
    /// <summary>
    /// Ülkeler arasındaki diplomatik ilişki puanını (-100 ile +100) yöneten sistem
    /// Askeri donanım satışı için en az +50 puan gereklidir.
    /// </summary>
    public class DiplomacyManager
    {
        private Dictionary<string, int> _relationshipScores = new Dictionary<string, int>();

        public const int RequiredAllyScoreForTrade = 50;

        public DiplomacyManager()
        {
            InitializeRelations();
        }

        private void InitializeRelations()
        {
            _relationshipScores["USA"] = 60;
            _relationshipScores["RUSSIA"] = 45;
            _relationshipScores["CHINA"] = 50;
            _relationshipScores["PAKISTAN"] = 85;
            _relationshipScores["SOUTH_KOREA"] = 75;
            _relationshipScores["UK"] = 65;
            _relationshipScores["JAPAN"] = 70;
            _relationshipScores["GERMANY"] = 55;
            _relationshipScores["UKRAINE"] = 70;
            _relationshipScores["SPAIN"] = 60;
            _relationshipScores["IRAN"] = -20;
        }

        public int GetRelationship(string nationKey)
        {
            return _relationshipScores.TryGetValue(nationKey, out int score) ? score : 0;
        }

        public bool CanTradeWith(string nationKey)
        {
            return GetRelationship(nationKey) >= RequiredAllyScoreForTrade;
        }

        public void ImproveRelation(string nationKey, int amount = 25)
        {
            int current = GetRelationship(nationKey);
            _relationshipScores[nationKey] = Math.Min(100, current + amount);
        }

        public void DeteriorateRelation(string nationKey, int amount = 30)
        {
            int current = GetRelationship(nationKey);
            _relationshipScores[nationKey] = Math.Max(-100, current - amount);
        }
    }

    /// <summary>
    /// Aktif uluslararası silah alım satım emri
    /// </summary>
    public class TradeDeal
    {
        public string DealId { get; set; }
        public string SellerNation { get; set; }
        public string BuyerNation { get; set; }
        public UnitBlueprint PurchasedBlueprint { get; set; }
        public int OrderQuantity { get; set; }
        public float DeliveryTimeRemaining { get; set; }
        public float TotalDeliveryDuration { get; set; }

        public TradeDeal(string id, string seller, string buyer, UnitBlueprint blueprint, int quantity = 1)
        {
            DealId = id;
            SellerNation = seller;
            BuyerNation = buyer;
            PurchasedBlueprint = blueprint;
            OrderQuantity = quantity;

            // Uluslararası lojistik sevkiyat süresi: Normal üretim süresinden %50 daha uzun
            TotalDeliveryDuration = blueprint.ProductionDuration * 1.5f;
            DeliveryTimeRemaining = TotalDeliveryDuration;
        }
    }

    /// <summary>
    /// Global Askeri Pazar Yöneticisi:
    /// İthalat kataloğunu listeler, sevkiyat sürelerini sayar ve üsse otomatik spawn eder.
    /// </summary>
    public class MilitaryMarketSystem
    {
        public DiplomacyManager Diplomacy { get; }
        public List<TradeDeal> ActiveDeliveries { get; } = new List<TradeDeal>();

        public event Action<TradeDeal, Vector3> OnUnitDeliveredAndSpawned;

        public MilitaryMarketSystem(DiplomacyManager diplomacy)
        {
            Diplomacy = diplomacy;
        }

        /// <summary>
        /// Oyuncunun mevcut çağına ve diplomatik ilişkilerine göre satın alabileceği yabancı silahları filtreler
        /// </summary>
        public List<UnitBlueprint> GetImportCatalog(string playerNation, int currentCentury)
        {
            var availableImports = new List<UnitBlueprint>();

            foreach (var nationEntry in FactionInventoryDatabase.Nations)
            {
                if (nationEntry.Key == playerNation) continue; // Kendi ülkesinden ithalat yapamaz

                // +50 İlişki Şartı (Ambargo kontrolü)
                if (Diplomacy.CanTradeWith(nationEntry.Key))
                {
                    foreach (var unit in nationEntry.Value.SpecialUnits)
                    {
                        if (unit.RequiredEraCentury <= currentCentury)
                        {
                            availableImports.Add(unit);
                        }
                    }
                }
            }

            return availableImports;
        }

        /// <summary>
        /// MonoGame ana güncelleme döngüsünden çağrılır.
        /// Sevkiyat süresi bittiğinde yabancı birimi oyuncunun üssünde spawn eder.
        /// </summary>
        public void UpdateTradeDeliveries(float deltaTime, Vector3 playerHqPosition, Vector3 playerShipyardPosition)
        {
            for (int i = ActiveDeliveries.Count - 1; i >= 0; i--)
            {
                var deal = ActiveDeliveries[i];
                deal.DeliveryTimeRemaining -= deltaTime;

                if (deal.DeliveryTimeRemaining <= 0f)
                {
                    // Lojistik teslimat tamamlandı!
                    ActiveDeliveries.RemoveAt(i);

                    // Birim tipine göre spawn noktası seç
                    Vector3 spawnLocation = (deal.PurchasedBlueprint.Role == WeaponSystem.AttackSubmarine ||
                                             deal.PurchasedBlueprint.Role == WeaponSystem.AircraftCarrier)
                                            ? playerShipyardPosition
                                            : playerHqPosition + new Vector3(30f, 0f, 30f);

                    OnUnitDeliveredAndSpawned?.Invoke(deal, spawnLocation);
                }
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
                brush = Brush.horizontalGradient(listOf(Color(0xFF00F5D4), AmberGold))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF00F5D4), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ASKERİ TİCARET PAZARI & DİPLOMASİ SİSTEMİ (C#)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Diğer 19 ülkeden F-35, Su-57, Leopard 2A7+ satın alma, +50 dostluk ilişki eşiği, DeliveryTimer (normal üretimden %50 daha uzun lojistik sevkiyat) ve teslimat bitince Komuta Merkezi veya Tersanede otomatik spawn mantığı.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "MilitaryMarketSystem.cs (Diplomasi & İthalat Motoru)",
            description = "DiplomacyManager, TradeDeal ve GetImportCatalog / UpdateTradeDeliveries MonoGame döngüsü",
            code = diplomacyAndMarketCode,
            onCopy = {
                val clip = ClipData.newPlainText("MilitaryMarketSystem.cs", diplomacyAndMarketCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "MilitaryMarketSystem.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==================== TAB 16: BİRİM XP, RÜTBE & BATTLELOG ====================
@Composable
fun UnitXpAndBattleLogSection() {
    val context = LocalContext.current

    val xpAndBattleLogCode = """
using System;
using System.Collections.Generic;

namespace ZeroHourMobile.Combat
{
    public enum UnitRank
    {
        Recruit,  // Acemi: Standart İstatistikler (★)
        Veteran,  // Kıdemli: +15% Saldırı Hasarı, +10% Can/Zırh (★★)
        Elite,    // Seçkin: +30% Saldırı Hasarı, +20% Can/Zırh (★★★)
        Heroic    // Kahraman: +50% Saldırı Hasarı, +35% Can/Zırh, Otomatik Can Yenileme (★★★★)
    }

    /// <summary>
    /// Birimlerin çatışma tecrübesi (XP) ve seviye atlama motoru
    /// </summary>
    public class UnitCombatExperience
    {
        public int CurrentXp { get; private set; } = 0;
        public UnitRank Rank { get; private set; } = UnitRank.Recruit;
        public int KillsCount { get; private set; } = 0;

        // Seviye Atlama Eşikleri
        public const int VeteranXpThreshold = 100;
        public const int EliteXpThreshold = 250;
        public const int HeroicXpThreshold = 500;

        public float AttackMultiplier => Rank switch
        {
            UnitRank.Heroic => 1.50f,
            UnitRank.Elite => 1.30f,
            UnitRank.Veteran => 1.15f,
            _ => 1.0f
        };

        public float HealthMultiplier => Rank switch
        {
            UnitRank.Heroic => 1.35f,
            UnitRank.Elite => 1.20f,
            UnitRank.Veteran => 1.10f,
            _ => 1.0f
        };

        public string RankBadge => Rank switch
        {
            UnitRank.Heroic => "★★★★",
            UnitRank.Elite => "★★★",
            UnitRank.Veteran => "★★",
            _ => "★"
        };

        public event Action<UnitRank> OnRankPromoted;

        public bool AddCombatXp(int amount)
        {
            CurrentXp += amount;
            UnitRank oldRank = Rank;

            if (CurrentXp >= HeroicXpThreshold) Rank = UnitRank.Heroic;
            else if (CurrentXp >= EliteXpThreshold) Rank = UnitRank.Elite;
            else if (CurrentXp >= VeteranXpThreshold) Rank = UnitRank.Veteran;

            if (Rank != oldRank)
            {
                OnRankPromoted?.Invoke(Rank);
                return true; // Seviye atladı
            }
            return false;
        }

        public void RegisterKill()
        {
            KillsCount++;
            AddCombatXp(50); // Her leş için +50 XP
        }
    }

    public enum BattleEventCategory { Combat, Building, Territory, Trade, Rank }

    public struct BattleLogRecord
    {
        public string Timestamp;
        public BattleEventCategory Category;
        public string Message;
        public uint HighlightColorRgba;

        public BattleLogRecord(string time, BattleEventCategory cat, string msg, uint color)
        {
            Timestamp = time;
            Category = cat;
            Message = msg;
            HighlightColorRgba = color;
        }
    }

    /// <summary>
    /// Muharebe Olaylarını (Leşler, binalar, teslimatlar, terfiler) hafızada tutan BattleLog sistemi
    /// </summary>
    public class BattleLogSystem
    {
        private readonly List<BattleLogRecord> _logs = new List<BattleLogRecord>(100);
        public IReadOnlyList<BattleLogRecord> Logs => _logs;

        public event Action<BattleLogRecord> OnNewLogAdded;

        public void RecordEvent(BattleEventCategory category, string message, uint colorHex = 0x00E5FFFF)
        {
            string time = DateTime.Now.ToString("HH:mm:ss");
            var record = new BattleLogRecord(time, category, message, colorHex);

            _logs.Insert(0, record);
            if (_logs.Count > 100) _logs.RemoveAt(_logs.Count - 1);

            OnNewLogAdded?.Invoke(record);
        }

        public List<BattleLogRecord> FilterByCategory(BattleEventCategory category)
        {
            return _logs.FindAll(l => l.Category == category);
        }

        public void Clear() => _logs.Clear();
    }
}
""".trimIndent()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(AmberGold, MatrixGreen))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BİRİM TECRÜBESİ (XP), RÜTBE & BATTLELOG SİSTEMİ (C#)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Birimlerin çatışarak tecrübe kazanması, rütbe atlaması (Acemi ★, Kıdemli ★★, Seçkin ★★★, Kahraman ★★★★), saldırı ve can bonusları (+15% ile +50% arası) ile in-game olayları filtreleyip gösteren BattleLog motoru.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "UnitCombatExperience.cs & BattleLogSystem.cs",
            description = "XP eşikleri, istatistik çarpanları, rütbe rozetleri ve filtrelenebilir BattleLog kayıt yapısı",
            code = xpAndBattleLogCode,
            onCopy = {
                val clip = ClipData.newPlainText("UnitCombatExperience.cs", xpAndBattleLogCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "UnitCombatExperience.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
