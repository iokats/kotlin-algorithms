package com.iekatsatos.algorithms.matching

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class GaleShapleyStableMatchingTest {

    @Test
    fun `case 1 Love Triangle - Total Conflict`() {
        val men = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(0, 1, 2),
            intArrayOf(0, 1, 2)
        )
        val women = arrayOf(
            intArrayOf(2, 1, 0),
            intArrayOf(2, 1, 0),
            intArrayOf(2, 1, 0)
        )
        // Man 0 -> Woman 2, Man 1 -> Woman 1, Man 2 -> Woman 0
        val expected = intArrayOf(2, 1, 0)
        val result = GaleShapleyStableMatching.match(men, women)

        assertContentEquals(expected, result)
    }

    @Test
    fun `case 2 Perfect Agreement`() {
        val men = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(1, 2, 0),
            intArrayOf(2, 0, 1)
        )
        val women = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(1, 2, 0),
            intArrayOf(2, 0, 1)
        )

        val expected = intArrayOf(0, 1, 2)
        assertContentEquals(expected, GaleShapleyStableMatching.match(men, women))
    }

    @Test
    fun `case 3 10x10 Complex Matching`() {
        val men = arrayOf(
            intArrayOf(7, 5, 3, 9, 8, 4, 0, 6, 2, 1),
            intArrayOf(0, 5, 9, 8, 7, 4, 1, 6, 2, 3),
            intArrayOf(2, 4, 5, 0, 3, 7, 1, 6, 8, 9),
            intArrayOf(2, 4, 1, 8, 3, 5, 7, 9, 6, 0),
            intArrayOf(8, 2, 9, 5, 0, 6, 1, 3, 7, 4),
            intArrayOf(5, 6, 8, 9, 7, 4, 3, 0, 2, 1),
            intArrayOf(7, 0, 2, 6, 3, 5, 4, 1, 8, 9),
            intArrayOf(2, 1, 7, 5, 0, 6, 3, 4, 9, 8),
            intArrayOf(9, 1, 4, 6, 2, 7, 5, 0, 3, 8),
            intArrayOf(4, 5, 1, 2, 3, 8, 9, 6, 7, 0)
        )
        val women = arrayOf(
            intArrayOf(7, 3, 8, 2, 9, 4, 0, 1, 6, 5),
            intArrayOf(1, 2, 5, 4, 3, 6, 0, 7, 8, 9),
            intArrayOf(0, 9, 6, 7, 3, 8, 4, 2, 5, 1),
            intArrayOf(7, 2, 5, 8, 6, 1, 0, 4, 3, 9),
            intArrayOf(9, 2, 0, 7, 5, 1, 8, 3, 4, 6),
            intArrayOf(0, 7, 5, 3, 6, 8, 4, 9, 2, 1),
            intArrayOf(5, 1, 9, 4, 8, 6, 7, 0, 2, 3),
            intArrayOf(5, 9, 2, 1, 0, 7, 8, 4, 6, 3),
            intArrayOf(6, 0, 2, 5, 1, 3, 4, 7, 9, 8),
            intArrayOf(8, 5, 0, 9, 3, 4, 2, 7, 1, 6)
        )

        // Expected matching (Man index -> Woman index)
        val expected = intArrayOf(2, 4, 6, 3, 9, 7, 5, 0, 1, 8)
        assertContentEquals(expected, GaleShapleyStableMatching.match(men, women))
    }

    // Helper to compare array contents
    private fun assertContentEquals(expected: IntArray, actual: IntArray) {
        assertEquals(expected.size, actual.size, "Array size mismatch")
        for (i in expected.indices) {
            assertEquals(expected[i], actual[i], "Mismatch at index $i")
        }
    }
}