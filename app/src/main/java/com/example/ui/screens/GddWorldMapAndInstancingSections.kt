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
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Terrain
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

// ==================== TAB 12: 3D DÜNYA HARİTASI & KAYNAK YÖNETİMİ ====================
@Composable
fun InteractiveWorldMapAndResourceManagerSection() {
    val context = LocalContext.current

    val resourceManagerCode = """
using System;
using Microsoft.Xna.Framework;

namespace ZeroHourMobile.Economy
{
    /// <summary>
    /// MonoGame Kaynak Yönetim Sınıfı (Altın, Petrol ve Enerji / MW)
    /// C&C Generals / Rise of Kingdoms tarzı üretim kesintisi ve verim hesaplayıcı.
    /// </summary>
    public class ResourceManager
    {
        // 1. Kaynak Stokları
        public double Gold { get; private set; } = 1500.0;
        public double Oil { get; private set; } = 800.0;
        public int EnergyProduced { get; private set; } = 150; // Santraller (MW)
        public int EnergyConsumed { get; private set; } = 80;  // Fabrikalar, Radarlar (MW)

        // Maksimum Depolama Kapasiteleri
        public double MaxGold { get; set; } = 10000.0;
        public double MaxOil { get; set; } = 5000.0;

        // Kaynak Artış Oranları (Saniye başına)
        public double GoldPerSecond { get; set; } = 35.0;
        public double OilPerSecond { get; set; } = 20.0;

        // Enerji Durumu ve Verimlilik Katsayısı
        public bool HasEnergyDeficit => EnergyConsumed > EnergyProduced;
        public float FactoryEfficiency => HasEnergyDeficit ? 0.50f : 1.0f; // Açıkta üretim hızı %50 düşer

        public event Action<string> OnResourceDeficitAlert;

        public ResourceManager(double startingGold = 1500.0, double startingOil = 800.0)
        {
            Gold = startingGold;
            Oil = startingOil;
        }

        /// <summary>
        /// Her karede (Frame) kaynak artışını deltaTime'a bağlı olarak işletir
        /// </summary>
        public void Update(GameTime gameTime)
        {
            float dt = (float)gameTime.ElapsedGameTime.TotalSeconds;

            // Kaynak artışı (Enerji açığı tedarik deposunu yavaşlatmaz, fabrikayı yavaşlatır)
            Gold = Math.Min(Gold + (GoldPerSecond * dt), MaxGold);
            Oil = Math.Min(Oil + (OilPerSecond * dt), MaxOil);
        }

        /// <summary>
        /// Üretim emri için gerekli kaynakları kontrol eder ve anında düşer
        /// </summary>
        public bool TryDeductCost(int costGold, int costOil, int energyRequirement = 0)
        {
            if (Gold < costGold)
            {
                OnResourceDeficitAlert?.Invoke($"Yetersiz Altın! Gerekli: {costGold}, Mevcut: {(int)Gold}");
                return false;
            }

            if (Oil < costOil)
            {
                OnResourceDeficitAlert?.Invoke($"Yetersiz Petrol! Gerekli: {costOil}, Mevcut: {(int)Oil}");
                return false;
            }

            // Kaynakları güvenle düş
            Gold -= costGold;
            Oil -= costOil;
            EnergyConsumed += energyRequirement;
            return true;
        }

        /// <summary>
        /// İptal edilen üretim siparişinin kaynaklarını oyuncuya iade eder
        /// </summary>
        public void RefundCost(int refundGold, int refundOil, int freedEnergy = 0)
        {
            Gold = Math.Min(Gold + refundGold, MaxGold);
            Oil = Math.Min(Oil + refundOil, MaxOil);
            EnergyConsumed = Math.Max(0, EnergyConsumed - freedEnergy);
        }

        /// <summary>
        /// Yeni enerji santrali kurulduğunda üretimi artırır
        /// </summary>
        public void RegisterPowerPlant(int outputMw)
        {
            EnergyProduced += outputMw;
        }

        public void UnregisterPowerPlant(int outputMw)
        {
            EnergyProduced = Math.Max(0, EnergyProduced - outputMw);
        }
    }
}
""".trimIndent()

    val worldMapProjectionCode = """
using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ZeroHourMobile.WorldMap
{
    /// <summary>
    /// 3D Dünya Haritası Mesh ve Çizim Motoru
    /// KARŞILAŞTIRMA:
    /// 1. 3D Küre (Sphere): Görsel olarak etkileyici fakat arkada kalan yüzlerin culling'i,
    ///    dokunmatik ekran raycasting karmaşıklığı ve yüksek vertex sayısı nedeniyle mobilde pil tüketir.
    /// 2. 3D Silindirik / Düzlemsel Harita (Flat Isometric 3D Plane): Mobil strateji için en verimli format.
    ///    Ekran dokunma unproject hesabı O(1), vertex sayısı %70 daha az, kamera kaydırma (Pan) pürüzsüz.
    /// </summary>
    public class WorldMapRenderer
    {
        private GraphicsDevice _graphicsDevice;
        private BasicEffect _mapEffect;
        private VertexBuffer _mapVertexBuffer;
        private IndexBuffer _mapIndexBuffer;
        private Texture2D _worldTexture;
        private Texture2D _reliefHeightMap;

        public Matrix WorldMatrix { get; set; }
        public Matrix ViewMatrix { get; set; }
        public Matrix ProjectionMatrix { get; set; }

        public WorldMapRenderer(GraphicsDevice device, Texture2D worldTex, Texture2D heightTex)
        {
            _graphicsDevice = device;
            _worldTexture = worldTex;
            _reliefHeightMap = heightTex;

            InitializeShader();
            BuildFlatReliefMesh(gridWidth: 64, gridHeight: 32, scaleX: 2000f, scaleZ: 1000f);
        }

        private void InitializeShader()
        {
            _mapEffect = new BasicEffect(_graphicsDevice)
            {
                TextureEnabled = true,
                Texture = _worldTexture,
                LightingEnabled = true
            };

            _mapEffect.EnableDefaultLighting();
            _mapEffect.DirectionalLight0.Direction = new Vector3(-1, -1.5f, -0.5f);
            _mapEffect.DirectionalLight0.DiffuseColor = new Vector3(0.9f, 0.85f, 0.75f);
            _mapEffect.AmbientLightColor = new Vector3(0.35f, 0.40f, 0.50f);

            WorldMatrix = Matrix.Identity;
        }

        /// <summary>
        /// Yükseklik haritası (Heightmap) destekli optimize edilmiş 3D zemin mesh'i oluşturur
        /// </summary>
        private void BuildFlatReliefMesh(int gridWidth, int gridHeight, float scaleX, float scaleZ)
        {
            int vertexCount = (gridWidth + 1) * (gridHeight + 1);
            VertexPositionNormalTexture[] vertices = new VertexPositionNormalTexture[vertexCount];

            float dx = scaleX / gridWidth;
            float dz = scaleZ / gridHeight;

            int idx = 0;
            for (int z = 0; z <= gridHeight; z++)
            {
                for (int x = 0; x <= gridWidth; x++)
                {
                    float posX = (x * dx) - (scaleX / 2f);
                    float posZ = (z * dz) - (scaleZ / 2f);
                    float u = (float)x / gridWidth;
                    float v = (float)z / gridHeight;

                    // Okyanuslar Y=0, Dağlık bölgeler Y=15..35 yükseklik kavisindedir
                    float reliefY = (float)Math.Sin(u * Math.PI * 4) * (float)Math.Cos(v * Math.PI * 2) * 8f;
                    if (reliefY < 0) reliefY = 0; // Deniz seviyesi tabanı

                    vertices[idx] = new VertexPositionNormalTexture(
                        new Vector3(posX, reliefY, posZ),
                        Vector3.Up,
                        new Vector2(u, v)
                    );
                    idx++;
                }
            }

            int indexCount = gridWidth * gridHeight * 6;
            short[] indices = new short[indexCount];
            int iIdx = 0;

            for (short z = 0; z < gridHeight; z++)
            {
                for (short x = 0; x < gridWidth; x++)
                {
                    short topLeft = (short)(z * (gridWidth + 1) + x);
                    short topRight = (short)(topLeft + 1);
                    short bottomLeft = (short)((z + 1) * (gridWidth + 1) + x);
                    short bottomRight = (short)(bottomLeft + 1);

                    indices[iIdx++] = topLeft;
                    indices[iIdx++] = bottomLeft;
                    indices[iIdx++] = topRight;

                    indices[iIdx++] = topRight;
                    indices[iIdx++] = bottomLeft;
                    indices[iIdx++] = bottomRight;
                }
            }

            _mapVertexBuffer = new VertexBuffer(_graphicsDevice, typeof(VertexPositionNormalTexture), vertices.Length, BufferUsage.WriteOnly);
            _mapVertexBuffer.SetData(vertices);

            _mapIndexBuffer = new IndexBuffer(_graphicsDevice, IndexElementSize.SixteenBits, indices.Length, BufferUsage.WriteOnly);
            _mapIndexBuffer.SetData(indices);
        }

        public void DrawWorldMap(Camera3D camera)
        {
            _mapEffect.World = WorldMatrix;
            _mapEffect.View = camera.ViewMatrix;
            _mapEffect.Projection = camera.ProjectionMatrix;

            _graphicsDevice.SetVertexBuffer(_mapVertexBuffer);
            _graphicsDevice.Indices = _mapIndexBuffer;
            _graphicsDevice.RasterizerState = RasterizerState.CullNone;

            foreach (EffectPass pass in _mapEffect.CurrentTechnique.Passes)
            {
                pass.Apply();
                _graphicsDevice.DrawIndexedPrimitives(
                    PrimitiveType.TriangleList,
                    0,
                    0,
                    _mapIndexBuffer.IndexCount / 3
                );
            }
        }
    }
}
""".trimIndent()

    val regionAndIntercontinentalCode = """
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ZeroHourMobile.Territory
{
    public enum Faction { Turkey, USA, Russia, China, Neutral }
    public enum Era { Medieval_5th, Gunpowder_18th, Industrial_19th, Modern_21st }

    public class Region
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public Faction Owner { get; set; }
        public Era CurrentEra { get; set; }
        public BoundingBox Bounds { get; set; }
        public Vector3 CapitalCoords { get; set; }
        public Color BorderColor { get; set; }
        public bool IsHighlighted { get; set; }

        public int GoldYield { get; set; }
        public int OilYield { get; set; }
        public int EnergyYield { get; set; }
    }

    /// <summary>
    /// Raycasting ile ekranda dokunulan bölgeyi yakalar ve sınırlarını neon shader ile aydınlatır
    /// </summary>
    public class RegionManager
    {
        public List<Region> Regions { get; private set; } = new List<Region>();
        public Region SelectedRegion { get; private set; }

        public RegionManager()
        {
            InitStrategicRegions();
        }

        private void InitStrategicRegions()
        {
            // 🇹🇷 Türkiye / Anadolu Başlangıç Bölgesi
            Regions.Add(new Region
            {
                Id = "anadolu",
                Name = "Anadolu & Doğu Akdeniz",
                Owner = Faction.Turkey,
                CurrentEra = Era.Modern_21st,
                CapitalCoords = new Vector3(390f, 0f, 350f), // 39°N, 35°E
                Bounds = new BoundingBox(new Vector3(340f, 0f, 300f), new Vector3(440f, 30f, 400f)),
                BorderColor = Color.Crimson,
                GoldYield = 65, OilYield = 85, EnergyYield = 120
            });

            // 🇺🇸 ABD / Kuzey Amerika
            Regions.Add(new Region
            {
                Id = "north_america",
                Name = "Kuzey Amerika & Pentagon",
                Owner = Faction.USA,
                CurrentEra = Era.Modern_21st,
                CapitalCoords = new Vector3(-770f, 0f, 380f), // 38°N, 77°W
                Bounds = new BoundingBox(new Vector3(-850f, 0f, 300f), new Vector3(-690f, 30f, 460f)),
                BorderColor = Color.CornflowerBlue,
                GoldYield = 90, OilYield = 70, EnergyYield = 180
            });

            // 🇷🇺 Rusya / Avrasya
            Regions.Add(new Region
            {
                Id = "eurasia",
                Name = "Avrasya & Sibirya",
                Owner = Faction.Russia,
                CurrentEra = Era.Modern_21st,
                CapitalCoords = new Vector3(370f, 0f, 550f), // 55°N, 37°E
                Bounds = new BoundingBox(new Vector3(280f, 0f, 480f), new Vector3(500f, 30f, 650f)),
                BorderColor = Color.Red,
                GoldYield = 75, OilYield = 110, EnergyYield = 140
            });

            // 🇨🇳 Çin / Doğu Asya
            Regions.Add(new Region
            {
                Id = "east_asia",
                Name = "Doğu Asya & Pasifik",
                Owner = Faction.China,
                CurrentEra = Era.Modern_21st,
                CapitalCoords = new Vector3(1160f, 0f, 390f), // 39°N, 116°E
                Bounds = new BoundingBox(new Vector3(1050f, 0f, 300f), new Vector3(1250f, 30f, 480f)),
                BorderColor = Color.Goldenrod,
                GoldYield = 110, OilYield = 50, EnergyYield = 200
            });
        }

        /// <summary>
        /// Dokunmatik ekrandan 3D Dünya Haritasına Raycast göndererek bölge seçimini tespit eder
        /// </summary>
        public Region RaycastPickRegion(Ray touchRay)
        {
            float closestDistance = float.MaxValue;
            Region picked = null;

            foreach (var region in Regions)
            {
                float? hitDistance = touchRay.Intersects(region.Bounds);
                if (hitDistance.HasValue && hitDistance.Value < closestDistance)
                {
                    closestDistance = hitDistance.Value;
                    picked = region;
                }
            }

            foreach (var r in Regions) r.IsHighlighted = false;

            if (picked != null)
            {
                picked.IsHighlighted = true;
                SelectedRegion = picked;
            }

            return picked;
        }
    }

    /// <summary>
    /// Kıtalararası Askeri Hareket & Balistik Füze (BORA, TAYFUN, ICBM) Parabolik Yay Uçuş Motoru
    /// </summary>
    public static class UnitMovement3D
    {
        /// <summary>
        /// 3D Parabolik Balistik Yay Formülü:
        /// Y(t) = Lerp(Y_start, Y_end, t) + 4 * MaxHeight * t * (1 - t)
        /// </summary>
        public static Vector3 CalculateParabolicTrajectory(Vector3 start, Vector3 target, float progress, float arcApexHeight)
        {
            float t = MathHelper.Clamp(progress, 0f, 1f);
            Vector3 linearPos = Vector3.Lerp(start, target, t);

            // Tepe noktasında maksimum yüksekliğe ulaşan yerçekimi kavisi
            float heightOffset = 4.0f * arcApexHeight * t * (1.0f - t);
            linearPos.Y += heightOffset;

            return linearPos;
        }

        /// <summary>
        /// Birim türüne göre geçiş kuralını doğrular (Kara, Denizaltı, Jet)
        /// </summary>
        public static bool ValidateNavigationalTerrain(string unitType, bool isOceanMesh, bool isLandMesh)
        {
            return unitType switch
            {
                "Tank" => isLandMesh,
                "Infantry" => isLandMesh,
                "Submarine" => isOceanMesh, // Denizaltılar sadece okyanusta seyreder
                "Jet" => true,              // Jetler kıta ve okyanus üzerinden havada uçar
                "Missile" => true,          // Füzeler yörüngesel yay çizer
                _ => false
            };
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
                    Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "3D DÜNYA HARİTASI, BÖLGE SİSTEMİ & KAYNAK MOTORU",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "MonoGame C# • Altın, Petrol, Enerji • Raycast Bölge Seçimi • Kıtalararası Parabolik Kavis. Mobil strateji oyunumuzun 3 temel kaynağını (Altın, Petrol, Enerji / MW) saniye bazlı yöneten ve enerji kesintisinde fabrika hızını %50 düşüren mimari.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "ResourceManager.cs (C# Kaynak Yönetimi)",
            description = "Altın, Petrol ve Enerji (MW) takibi, üretim maliyet kontrolü, yetersiz kaynak blokajı ve elektrik kesintisi verim düşüşü",
            code = resourceManagerCode,
            onCopy = {
                val clip = ClipData.newPlainText("ResourceManager.cs", resourceManagerCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "ResourceManager.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "WorldMapRenderer.cs (3D Zemin Mesh)",
            description = "3D Küre vs Düzlem/Silindirik Projeksiyon karşılaştırması ve DrawWorldMap çizim döngüsü",
            code = worldMapProjectionCode,
            onCopy = {
                val clip = ClipData.newPlainText("WorldMapRenderer.cs", worldMapProjectionCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "WorldMapRenderer.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "RegionManager.cs & UnitMovement3D.cs (Bölge ve Balistik Yay)",
            description = "Bölge sahipliği, dokunulan bölgeyi Raycast ile yakalayıp sınırları parlatma ve BORA/TAYFUN 3D parabolik kavis matematiği",
            code = regionAndIntercontinentalCode,
            onCopy = {
                val clip = ClipData.newPlainText("RegionManager.cs", regionAndIntercontinentalCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "RegionManager.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

// ==================== TAB 13: 3D VARLIKLAR & GPU INSTANCED RENDERING ====================
@Composable
fun ThreeDAssetsAndInstancingSection() {
    val context = LocalContext.current

    val gameObject3DCode = """
using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ZeroHourMobile.Engine3D
{
    public enum Era { Medieval_5th, Gunpowder_18th, Industrial_19th, Modern_21st }

    /// <summary>
    /// Tüm 3D birimlerin ve binaların türediği temel sınıf
    /// </summary>
    public abstract class GameObject3D
    {
        public string Id { get; set; }
        public Vector3 Position { get; set; }
        public Quaternion Rotation { get; set; } = Quaternion.Identity;
        public Vector3 Scale { get; set; } = Vector3.One;
        public Model CurrentModel { get; protected set; }
        public Era CurrentEra { get; protected set; }

        public Matrix WorldMatrix => Matrix.CreateScale(Scale) * 
                                     Matrix.CreateFromQuaternion(Rotation) * 
                                     Matrix.CreateTranslation(Position);

        public GameObject3D(string id, Vector3 spawnPos, Era era)
        {
            Id = id;
            Position = spawnPos;
            CurrentEra = era;
        }

        public abstract void Update(GameTime gameTime);

        public virtual void Draw(Camera3D camera)
        {
            if (CurrentModel == null) return;

            foreach (ModelMesh mesh in CurrentModel.Meshes)
            {
                foreach (BasicEffect effect in mesh.Effects)
                {
                    effect.EnableDefaultLighting();
                    effect.World = mesh.ParentBone.Transform * WorldMatrix;
                    effect.View = camera.ViewMatrix;
                    effect.Projection = camera.ProjectionMatrix;
                }
                mesh.Draw();
            }
        }

        /// <summary>
        /// Dönem geçişlerinde .fbx 3D modelini dinamik olarak yeniler
        /// </summary>
        public virtual void UpdateMeshForEra(Era newEra, Model newModel)
        {
            CurrentEra = newEra;
            CurrentModel = newModel;
        }
    }

    /// <summary>
    /// Savaş Jetleri ve SİHA'lar için irtifa ve dinamik kanat yatışı (Banking) sağlayan 3D uçak sınıfı
    /// </summary>
    public class Aircraft3D : GameObject3D
    {
        public float FlightAltitude { get; set; } = 45f;
        public float Speed { get; set; } = 120f;
        public Vector3 TargetDestination { get; set; }

        private float _currentBankRoll = 0f;

        public Aircraft3D(string id, Vector3 spawnPos, Era era, Model jetModel) 
            : base(id, new Vector3(spawnPos.X, 45f, spawnPos.Z), era)
        {
            CurrentModel = jetModel;
        }

        public override void Update(GameTime gameTime)
        {
            float dt = (float)gameTime.ElapsedGameTime.TotalSeconds;
            Vector3 direction = TargetDestination - Position;
            direction.Y = 0; // Yatay düzlemde yön

            if (direction.Length() > 2f)
            {
                direction.Normalize();

                // İleri hareket
                Position += direction * Speed * dt;

                // Hedefe doğru yaw rotasyonu
                float targetYaw = (float)Math.Atan2(direction.X, direction.Z);
                Quaternion targetRot = Quaternion.CreateFromYawPitchRoll(targetYaw, 0f, _currentBankRoll);
                Rotation = Quaternion.Lerp(Rotation, targetRot, dt * 5.0f);
            }
        }

        /// <summary>
        /// Uçak dönerken kanatlarını yana yatırır (Roll / Banking)
        /// </summary>
        public void ApplyBankRoll(float rollAngleRadians)
        {
            _currentBankRoll = MathHelper.Clamp(rollAngleRadians, -0.6f, 0.6f);
        }
    }

    /// <summary>
    /// Kara Piyadesi (Y=0 düzleminde yürüyüş, çağ atlayınca okçudan piyade tüfeğine model değişimi)
    /// </summary>
    public class Soldier3D : GameObject3D
    {
        public float WalkSpeed { get; set; } = 22f;
        public Vector3 MoveTarget { get; set; }

        public Soldier3D(string id, Vector3 spawnPos, Era era, Model soldierModel) 
            : base(id, new Vector3(spawnPos.X, 0f, spawnPos.Z), era)
        {
            CurrentModel = soldierModel;
        }

        public override void Update(GameTime gameTime)
        {
            float dt = (float)gameTime.ElapsedGameTime.TotalSeconds;
            Vector3 dir = MoveTarget - Position;
            dir.Y = 0;

            if (dir.Length() > 1.5f)
            {
                dir.Normalize();
                Position = new Vector3(Position.X + dir.X * WalkSpeed * dt, 0f, Position.Z + dir.Z * WalkSpeed * dt);

                float yaw = (float)Math.Atan2(dir.X, dir.Z);
                Rotation = Quaternion.CreateFromYawPitchRoll(yaw, 0f, 0f);
            }
        }
    }
}
""".trimIndent()

    val instancedForestCode = """
using System;
using System.Runtime.InteropServices;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace ZeroHourMobile.Optimization
{
    /// <summary>
    /// Tek bir Draw Call ile binlerce ağacı çizmek için GPU Instanced Render yapısı
    /// Her ağaç için ayrı Matrix ve TintColor GPU'ya aktarılır.
    /// </summary>
    [StructLayout(LayoutKind.Sequential, Pack = 1)]
    public struct TreeInstanceData : IVertexType
    {
        public Matrix WorldMatrix;
        public Color TintColor;

        public static readonly VertexDeclaration VertexDeclaration = new VertexDeclaration(
            new VertexElement(0, VertexElementFormat.Vector4, VertexElementUsage.BlendWeight, 0),
            new VertexElement(16, VertexElementFormat.Vector4, VertexElementUsage.BlendWeight, 1),
            new VertexElement(32, VertexElementFormat.Vector4, VertexElementUsage.BlendWeight, 2),
            new VertexElement(48, VertexElementFormat.Vector4, VertexElementUsage.BlendWeight, 3),
            new VertexElement(64, VertexElementFormat.Color, VertexElementUsage.Color, 1)
        );

        VertexDeclaration IVertexType.VertexDeclaration => VertexDeclaration;
    }

    /// <summary>
    /// Haritadaki 10.000 ağacı tek seferde GPU'da çizen Instanced Rendering Yöneticisi
    /// Mobil işlemcide CPU Draw Call darboğazını (Overhead) sıfıra indirir.
    /// </summary>
    public class TreeForestManager
    {
        private GraphicsDevice _graphicsDevice;
        private DynamicVertexBuffer _instanceBuffer;
        private TreeInstanceData[] _instances;
        private Model _treeModel;
        private Effect _instancingEffect;

        public int TreeCount => _instances.Length;

        public TreeForestManager(GraphicsDevice device, Model treeModel, Effect instancingShader, int treeCount = 5000)
        {
            _graphicsDevice = device;
            _treeModel = treeModel;
            _instancingEffect = instancingShader;
            _instances = new TreeInstanceData[treeCount];

            GenerateRandomForest();
            InitInstanceBuffer();
        }

        private void GenerateRandomForest()
        {
            Random rng = new Random(1453);
            for (int i = 0; i < _instances.Length; i++)
            {
                float x = (float)(rng.NextDouble() * 1800.0 - 900.0);
                float z = (float)(rng.NextDouble() * 900.0 - 450.0);
                float scale = (float)(rng.NextDouble() * 0.4 + 0.8);
                float rotY = (float)(rng.NextDouble() * Math.PI * 2.0);

                Matrix world = Matrix.CreateScale(scale) *
                               Matrix.CreateRotationY(rotY) *
                               Matrix.CreateTranslation(new Vector3(x, 0f, z));

                // Farklı yeşil tonları ile görsel çeşitlilik
                byte greenVariant = (byte)rng.Next(180, 255);
                Color tint = new Color((byte)50, greenVariant, (byte)50);

                _instances[i] = new TreeInstanceData
                {
                    WorldMatrix = world,
                    TintColor = tint
                };
            }
        }

        private void InitInstanceBuffer()
        {
            _instanceBuffer = new DynamicVertexBuffer(
                _graphicsDevice,
                TreeInstanceData.VertexDeclaration,
                _instances.Length,
                BufferUsage.WriteOnly
            );
            _instanceBuffer.SetData(_instances);
        }

        /// <summary>
        /// Binlerce ağacı tek bir DrawInstancedPrimitives çağrısıyla çizer
        /// </summary>
        public void DrawInstancedTrees(Camera3D camera)
        {
            if (_instances.Length == 0) return;

            _instancingEffect.Parameters["View"]?.SetValue(camera.ViewMatrix);
            _instancingEffect.Parameters["Projection"]?.SetValue(camera.ProjectionMatrix);

            foreach (ModelMesh mesh in _treeModel.Meshes)
            {
                foreach (ModelMeshPart meshPart in mesh.MeshParts)
                {
                    _graphicsDevice.SetVertexBuffers(
                        new VertexBufferBinding(meshPart.VertexBuffer, meshPart.VertexOffset, 0),
                        new VertexBufferBinding(_instanceBuffer, 0, 1) // Instance frekansı 1: Her ağaç için 1 veri
                    );

                    _graphicsDevice.Indices = meshPart.IndexBuffer;

                    foreach (EffectPass pass in _instancingEffect.CurrentTechnique.Passes)
                    {
                        pass.Apply();
                        _graphicsDevice.DrawInstancedPrimitives(
                            PrimitiveType.TriangleList,
                            0,
                            0,
                            meshPart.PrimitiveCount,
                            _instances.Length
                        );
                    }
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
                brush = Brush.horizontalGradient(listOf(MatrixGreen, CyanNeon))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Terrain, contentDescription = null, tint = MatrixGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "3D VARLIKLAR, HAREKET FİZİĞİ & GPU INSTANCED RENDERING",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "GameObject3D temel sınıfı, uçuş irtifası ve Quaternion.Lerp ile kanat yatışı (Banking) yapan Aircraft3D, Y=0 zemin yürüyüşü yapan Soldier3D ve binlerce ağacı tek draw call ile çizen GPU Instanced Rendering.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }

        CodeDisplayCard(
            fileName = "GameObject3D.cs & Aircraft3D.cs (3D Varlık Hiyerarşisi)",
            description = "Temel 3D sınıf, irtifa, Quaternion.Lerp ile bank/roll yatışı, kara piyadesi ve çağ atlama .fbx mesh güncellemesi",
            code = gameObject3DCode,
            onCopy = {
                val clip = ClipData.newPlainText("GameObject3D.cs", gameObject3DCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "GameObject3D.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )

        CodeDisplayCard(
            fileName = "TreeForestManager.cs (GPU Instanced Rendering)",
            description = "DrawInstancedPrimitives ile haritadaki 10.000 ağacı tek GPU çağrısında çizerek mobil CPU darboğazını sıfırlayan mimari",
            code = instancedForestCode,
            onCopy = {
                val clip = ClipData.newPlainText("TreeForestManager.cs", instancedForestCode)
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "TreeForestManager.cs Panoya Kopyalandı!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
