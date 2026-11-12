package com.example.model

/**
 * State representing historical timeline progress toward the next era requirement
 */
data class EraProgressState(
    val currentEra: Era,
    val nextEra: Era?,
    val progressPercent: Float, // 0f to 1f
    val researchedCountInCurrentEra: Int,
    val totalTechsInCurrentEra: Int,
    val hasRequiredGold: Boolean,
    val hasRequiredTech: Boolean,
    val isReadyToUpgrade: Boolean,
    val goldNeeded: Int,
    val techNeeded: Int
)

object EraProgressCalculator {
    fun calculate(
        currentEra: Era,
        techTree: List<TechNode>,
        resources: RtsResources
    ): EraProgressState {
        val nextEra = when (currentEra) {
            Era.MEDIEVAL_5TH -> Era.CLASSICAL_GUNPOWDER
            Era.CLASSICAL_GUNPOWDER -> Era.INDUSTRIAL
            Era.INDUSTRIAL -> Era.MODERN
            Era.MODERN -> null
        }

        val eraTechs = techTree.filter { it.era == currentEra }
        val totalEraTechs = eraTechs.size.coerceAtLeast(1)
        val researchedEraTechs = eraTechs.count { it.isResearched }

        if (nextEra == null) {
            // Already at the pinnacle era (Modern Warfare)
            return EraProgressState(
                currentEra = currentEra,
                nextEra = null,
                progressPercent = 1.0f,
                researchedCountInCurrentEra = researchedEraTechs,
                totalTechsInCurrentEra = totalEraTechs,
                hasRequiredGold = true,
                hasRequiredTech = true,
                isReadyToUpgrade = false,
                goldNeeded = 0,
                techNeeded = 0
            )
        }

        val techProgressRatio = researchedEraTechs.toFloat() / totalEraTechs.toFloat()
        val goldRatio = if (nextEra.upgradeCostGold > 0) {
            (resources.gold / nextEra.upgradeCostGold.toDouble()).toFloat().coerceIn(0f, 1f)
        } else 1.0f

        val techPointsRatio = if (nextEra.upgradeCostTech > 0) {
            (resources.techPoints / nextEra.upgradeCostTech.toDouble()).toFloat().coerceIn(0f, 1f)
        } else 1.0f

        // Weighted progress toward the next era:
        // 50% AR-GE research completion, 25% Gold reserve, 25% Tech Points reserve
        val combinedProgress = (techProgressRatio * 0.50f) + (goldRatio * 0.25f) + (techPointsRatio * 0.25f)

        val hasGold = resources.gold >= nextEra.upgradeCostGold
        val hasTech = resources.techPoints >= nextEra.upgradeCostTech
        val isReady = hasGold && hasTech

        val goldNeeded = (nextEra.upgradeCostGold - resources.gold.toInt()).coerceAtLeast(0)
        val techNeeded = (nextEra.upgradeCostTech - resources.techPoints.toInt()).coerceAtLeast(0)

        return EraProgressState(
            currentEra = currentEra,
            nextEra = nextEra,
            progressPercent = combinedProgress.coerceIn(0f, 1f),
            researchedCountInCurrentEra = researchedEraTechs,
            totalTechsInCurrentEra = totalEraTechs,
            hasRequiredGold = hasGold,
            hasRequiredTech = hasTech,
            isReadyToUpgrade = isReady,
            goldNeeded = goldNeeded,
            techNeeded = techNeeded
        )
    }
}
