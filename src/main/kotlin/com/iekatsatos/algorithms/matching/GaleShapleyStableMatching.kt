package com.iekatsatos.algorithms.matching

/**
 * Alias for a 2D matrix of integers.
 * Each row represents a preference list.
 */
typealias IntMatrix = Array<IntArray>

/**
 * Implementation of the Gale-Shapley Stable Matching algorithm.
 *
 * This version assumes:
 * - Equal number of men and women
 * - Preferences are complete (no ties, no missing entries)
 *
 * Time Complexity: O(n^2)
 */
object GaleShapleyStableMatching {

    /**
     * Computes a stable matching between men and women.
     *
     * @param menPreferences menPreferences[i] = ordered list of women indices preferred by man i
     * @param womenPreferences womenPreferences[j] = ordered list of men indices preferred by woman j
     *
     * @return An array `engagedWomen` where:
     *         engagedWomen[w] = m → woman w is matched with man m
     *         engagedWomen[w] = -1 → woman w is unmatched (should not happen if inputs are valid)
     */
    fun match(
        menPreferences: IntMatrix,
        womenPreferences: IntMatrix
    ): IntArray {

        val n = menPreferences.size

        val ranking = Array(n) { IntArray(n) }
        for (womanIndex in 0 until n) {
            for (rank in womenPreferences[womanIndex].indices) {
                val manIndex = womenPreferences[womanIndex][rank]
                ranking[womanIndex][manIndex] = rank
            }
        }

        val freeMen = ArrayDeque<Int>().apply {
            repeat(n) { addLast(it) }
        }

        val engagedWomen = IntArray(n) { -1 }
        val nextProposalIndex = IntArray(n)

        while (freeMen.isNotEmpty()) {
            val freeManIndex = freeMen.removeFirst()

            val preferredWomanIndex = menPreferences[freeManIndex][nextProposalIndex[freeManIndex]++]

            val currentFiance = engagedWomen[preferredWomanIndex]

            if (currentFiance == -1) {

                engagedWomen[preferredWomanIndex] = freeManIndex

            } else if (ranking[preferredWomanIndex][freeManIndex] < ranking[preferredWomanIndex][currentFiance]) {

                engagedWomen[preferredWomanIndex] = freeManIndex
                freeMen.addFirst(currentFiance)

            } else {

                freeMen.addFirst(freeManIndex)
            }
        }
        return engagedWomen
    }
}