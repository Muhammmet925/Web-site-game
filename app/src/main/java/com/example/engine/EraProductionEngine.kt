package com.example.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

// ==================== 1. ÇAĞ VE MEDENİYET YAPISI ====================

/**
 * Historical and Modern Eras with sequencing and dynamic factory visual states.
 */
enum class EngineEra(val order: Int, val title: String, val visualAssetId: String) {
    BRONZE_AGE(1, "Bronz Çağı", "factory_bronze"),
    GUNPOWDER_AGE(2, "Barut Çağı", "factory_gunpowder"),
    INDUSTRIAL_AGE(3, "Sanayi Çağı", "factory_industrial"),
    MODERN_AGE(4, "Modern Çağ", "factory_modern")
}

/**
 * Typealias providing direct access to 'Era' inside com.example.engine package
 */
typealias Era = EngineEra

/**
 * Civilizations with unique multipliers according to business rules:
 * - OSMANLI: %15 üretim hızı bonusu (1.15x)
 * - ROMA: %10 daha ucuz askeri maliyet (0.90x)
 * - ALMANYA: %12 daha yüksek birim canı (1.12x)
 * - BRITANYA, ÇİN, KORE: genişletilebilir dengeli çarpanlar
 */
enum class Civilization(
    val displayName: String,
    val flagEmoji: String,
    val productionSpeedMultiplier: Float, // Üretim hız çarpanı (1.15 = %15 daha hızlı)
    val costDiscountMultiplier: Float,    // Askeri maliyet çarpanı (0.90 = %10 daha ucuz)
    val healthMultiplier: Float,          // Birim can çarpanı (1.12 = %12 daha yüksek can)
    val uniqueDescription: String
) {
    OSMANLI(
        displayName = "Osmanlı",
        flagEmoji = "🇹🇷",
        productionSpeedMultiplier = 1.15f,
        costDiscountMultiplier = 1.0f,
        healthMultiplier = 1.0f,
        uniqueDescription = "+%15 Üretim Hızı, Barut Çağında Yeniçeri & Şahi Topu"
    ),
    ROMA(
        displayName = "Roma",
        flagEmoji = "🏛️",
        productionSpeedMultiplier = 1.0f,
        costDiscountMultiplier = 0.90f,
        healthMultiplier = 1.0f,
        uniqueDescription = "%10 Daha Ucuz Askeri Maliyet, Ağır Zırhlı Lejyoner Doktrini"
    ),
    ALMANYA(
        displayName = "Almanya",
        flagEmoji = "🇩🇪",
        productionSpeedMultiplier = 1.0f,
        costDiscountMultiplier = 1.0f,
        healthMultiplier = 1.12f,
        uniqueDescription = "+%12 Birim Canı (HP), Modern Çağda Leopard 2A7 & Tiger Tankı"
    ),
    BRITANYA(
        displayName = "Britanya",
        flagEmoji = "🇬🇧",
        productionSpeedMultiplier = 1.05f,
        costDiscountMultiplier = 0.95f,
        healthMultiplier = 1.05f,
        uniqueDescription = "Gelişmiş Kraliyet Donanması ve Küresel Lojistik Ağı"
    ),
    CIN(
        displayName = "Çin",
        flagEmoji = "🇨🇳",
        productionSpeedMultiplier = 1.10f,
        costDiscountMultiplier = 0.92f,
        healthMultiplier = 1.0f,
        uniqueDescription = "Hızlı Nüfus Seferberliği ve Çin Seddi Savunma Tahkimatı"
    )
}

// ==================== 2. ÇAĞLAR ARASI ASKERİ BİRİM EVRİMİ (POLYMORPHISM) ====================

/**
 * Abstract BaseUnit defining the common military contract.
 */
abstract class BaseUnit(
    val baseName: String,
    val baseHealth: Float,
    val baseDamage: Float,
    val baseCost: Int,
    val baseTrainingTimeSeconds: Float,
    val era: EngineEra,
    val civilization: Civilization
) {
    abstract val unitRole: String

    // Dynamic calculations applying Era & Civilization multipliers
    open val name: String
        get() = baseName

    open val health: Float
        get() = baseHealth * civilization.healthMultiplier

    open val damage: Float
        get() = baseDamage

    open val cost: Int
        get() = (baseCost * civilization.costDiscountMultiplier).toInt()

    open val trainingTime: Float
        get() = (baseTrainingTimeSeconds / civilization.productionSpeedMultiplier).coerceAtLeast(1f)
}

/**
 * Infantry Unit with dynamic evolution based on Era and Civilization:
 * - Barut Çağı + Osmanlı = "Yeniçeri" (Özel hasar bonuslu piyade)
 * - Bronz Çağı + Roma = "Lejyoner"
 * - Modern Çağ + Osmanlı/Türkiye = "Bordo Bereli Komando"
 * - Modern Çağ + Almanya = "Panzergrenadier"
 */
class Infantry(
    era: EngineEra,
    civilization: Civilization
) : BaseUnit(
    baseName = "Standart Piyade",
    baseHealth = 100f * era.order,
    baseDamage = 25f * era.order,
    baseCost = 80 * era.order,
    baseTrainingTimeSeconds = 4f * era.order,
    era = era,
    civilization = civilization
) {
    override val unitRole: String = "Piyade"

    override val name: String
        get() = when (era) {
            EngineEra.BRONZE_AGE -> when (civilization) {
                Civilization.ROMA -> "Roma Lejyoneri"
                Civilization.OSMANLI -> "Alp Mızrakçısı"
                else -> "Bronz Mızraklı Piyade"
            }
            EngineEra.GUNPOWDER_AGE -> when (civilization) {
                Civilization.OSMANLI -> "Yeniçeri Taburu" // Özel Osmanlı birimi
                Civilization.ROMA -> "Muhafız Tüfekçisi"
                Civilization.ALMANYA -> "Landsknecht Arkebüzcüsü"
                else -> "Misketli Barut Piyadesi"
            }
            EngineEra.INDUSTRIAL_AGE -> when (civilization) {
                Civilization.OSMANLI -> "Nizam-ı Cedid Tüfekçisi"
                Civilization.ALMANYA -> "Prusya Piyadesi"
                else -> "Endüstriyel Siper Piyadesi"
            }
            EngineEra.MODERN_AGE -> when (civilization) {
                Civilization.OSMANLI -> "Bordo Bereli & SAT Komandosu"
                Civilization.ALMANYA -> "Panzergrenadier Özel Birliği"
                else -> "Modern Taktik Komando"
            }
        }

    override val damage: Float
        get() {
            var dmg = baseDamage
            // Barut Çağı + Osmanlı özel hasar bonusu (+%25)
            if (era == EngineEra.GUNPOWDER_AGE && civilization == Civilization.OSMANLI) {
                dmg *= 1.25f
            }
            return dmg
        }
}

/**
 * Vehicle (Tank / Araba / Kuşatma) Unit with dynamic evolution:
 * - Modern Çağ + Almanya = "Leopard 2A7 Tankı" (+%12 can bonuslu araç)
 * - Modern Çağ + Osmanlı = "Altay T2 Ana Muharebe Tankı"
 * - Barut Çağı + Osmanlı = "Şahi Kuşatma Topu"
 * - Bronz Çağı + Roma = "Roma Ağır Savaş Arabası"
 */
class Vehicle(
    era: EngineEra,
    civilization: Civilization
) : BaseUnit(
    baseName = "Zırhlı Araç / Tank",
    baseHealth = 280f * era.order,
    baseDamage = 60f * era.order,
    baseCost = 220 * era.order,
    baseTrainingTimeSeconds = 8f * era.order,
    era = era,
    civilization = civilization
) {
    override val unitRole: String = "Zırhlı Araç"

    override val name: String
        get() = when (era) {
            EngineEra.BRONZE_AGE -> when (civilization) {
                Civilization.ROMA -> "Roma Dört Atlı Savaş Arabası"
                else -> "Ağır Bronz Koçbaşı Arabası"
            }
            EngineEra.GUNPOWDER_AGE -> when (civilization) {
                Civilization.OSMANLI -> "Şahi Kuşatma Döküm Topu"
                else -> "Bronz Gülle Top Arabası"
            }
            EngineEra.INDUSTRIAL_AGE -> when (civilization) {
                Civilization.ALMANYA -> "A7V / Tiger Öncüsü Tank"
                else -> "Mark I Paletli Taarruz Tankı"
            }
            EngineEra.MODERN_AGE -> when (civilization) {
                Civilization.ALMANYA -> "Leopard 2A7 Ağır Muharebe Tankı" // Özel Alman zırhlısı
                Civilization.OSMANLI -> "Altay T2 Akkor Korumalı Tank"
                Civilization.ROMA -> "Centauro II Zırhlı Avcısı"
                else -> "M1A2 Abrams Modern Tank"
            }
        }

    override val health: Float
        get() {
            var hp = baseHealth * civilization.healthMultiplier
            // Modern Çağ + Almanya özel ek gövde sağlamlığı (+%15 ek zırh)
            if (era == EngineEra.MODERN_AGE && civilization == Civilization.ALMANYA) {
                hp *= 1.15f
            }
            return hp
        }
}

// ==================== 3. GERÇEK ZAMANLI ASKERİ FABRİKA VE ÜRETİM KUYRUĞU ====================

data class ProductionTask(
    val taskId: String = UUID.randomUUID().toString(),
    val unit: BaseUnit,
    val totalSeconds: Float,
    var remainingSeconds: Float
) {
    val progressPercent: Float
        get() = (1f - (remainingSeconds / totalSeconds.coerceAtLeast(0.1f))).coerceIn(0f, 1f)
}

data class FactoryVisualState(
    val factoryId: String,
    val currentEra: EngineEra,
    val civilization: Civilization,
    val visualAssetId: String, // e.g. "factory_bronze", "factory_gunpowder", "factory_modern"
    val isProducing: Boolean,
    val activeTask: ProductionTask?,
    val queue: List<ProductionTask>,
    val totalProducedCount: Int = 0
)

/**
 * MilitaryFactory: Represents the base production facility.
 * Uses Kotlin Coroutines & StateFlow/SharedFlow for asynchronous, thread-safe production queues.
 */
class MilitaryFactory(
    val id: String = "military_factory_main",
    initialEra: EngineEra = EngineEra.MODERN_AGE,
    initialCivilization: Civilization = Civilization.OSMANLI,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private var era: EngineEra = initialEra
    private var civilization: Civilization = initialCivilization

    private val _factoryState = MutableStateFlow(
        FactoryVisualState(
            factoryId = id,
            currentEra = era,
            civilization = civilization,
            visualAssetId = era.visualAssetId,
            isProducing = false,
            activeTask = null,
            queue = emptyList()
        )
    )
    val factoryState: StateFlow<FactoryVisualState> = _factoryState.asStateFlow()

    private val _unitProducedEvent = MutableSharedFlow<BaseUnit>(extraBufferCapacity = 10)
    val unitProducedEvent: SharedFlow<BaseUnit> = _unitProducedEvent.asSharedFlow()

    private var productionJob: Job? = null
    private val internalQueue = mutableListOf<ProductionTask>()

    init {
        updateVisualState()
    }

    /**
     * Enqueues a new unit into the military factory queue.
     * Takes civilization production speed bonus into account.
     */
    @Synchronized
    fun enqueueUnit(unit: BaseUnit): Boolean {
        if (internalQueue.size >= 8) return false // Max queue limit

        val effectiveTrainingTime = unit.trainingTime
        val task = ProductionTask(
            unit = unit,
            totalSeconds = effectiveTrainingTime,
            remainingSeconds = effectiveTrainingTime
        )

        internalQueue.add(task)
        updateStateSnapshot()

        if (productionJob == null || productionJob?.isActive != true) {
            startProductionLoop()
        }
        return true
    }

    /**
     * Cancels an enqueued unit at index.
     */
    @Synchronized
    fun cancelUnit(index: Int): Boolean {
        if (index in 0 until internalQueue.size) {
            internalQueue.removeAt(index)
            updateStateSnapshot()
            return true
        }
        return false
    }

    /**
     * Updates Era and triggers dynamic visual state update ("factory_bronze", "factory_modern", etc.)
     */
    fun updateEra(newEra: EngineEra) {
        this.era = newEra
        updateVisualState()
    }

    /**
     * Updates Civilization to change future unit costs and speed multipliers.
     */
    fun updateCivilization(newCiv: Civilization) {
        this.civilization = newCiv
        updateVisualState()
    }

    private fun updateVisualState() {
        _factoryState.update { current ->
            current.copy(
                currentEra = era,
                civilization = civilization,
                visualAssetId = era.visualAssetId
            )
        }
    }

    private fun updateStateSnapshot() {
        val active = internalQueue.firstOrNull()
        val rest = if (internalQueue.size > 1) internalQueue.subList(1, internalQueue.size).toList() else emptyList()

        _factoryState.update { current ->
            current.copy(
                isProducing = active != null,
                activeTask = active,
                queue = rest
            )
        }
    }

    /**
     * Background asynchronous coroutine loop ticking production seconds.
     */
    private fun startProductionLoop() {
        productionJob?.cancel()
        productionJob = scope.launch {
            while (isActive) {
                var currentTask: ProductionTask? = null
                synchronized(this@MilitaryFactory) {
                    currentTask = internalQueue.firstOrNull()
                }

                if (currentTask == null) {
                    updateStateSnapshot()
                    break
                }

                // Tick countdown step (100ms interval for ultra-smooth UI progress)
                delay(100L)
                currentTask?.let { task ->
                    task.remainingSeconds = (task.remainingSeconds - 0.1f).coerceAtLeast(0f)
                    updateStateSnapshot()

                    if (task.remainingSeconds <= 0f) {
                        // Unit finished!
                        var finishedUnit: BaseUnit? = null
                        synchronized(this@MilitaryFactory) {
                            if (internalQueue.isNotEmpty()) {
                                finishedUnit = internalQueue.removeAt(0).unit
                            }
                        }
                        finishedUnit?.let { unit ->
                            _factoryState.update { it.copy(totalProducedCount = it.totalProducedCount + 1) }
                            _unitProducedEvent.emit(unit)
                        }
                        updateStateSnapshot()
                    }
                }
            }
        }
    }
}
