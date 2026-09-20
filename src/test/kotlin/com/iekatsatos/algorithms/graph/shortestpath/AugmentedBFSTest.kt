package com.iekatsatos.algorithms.graph.shortestpath

import com.iekatsatos.algorithms.graph.Edge
import com.iekatsatos.algorithms.graph.representation.AdjacencyListGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class AugmentedBFSTest {

    @Test
    fun shortestPath_whenStartingVertexNotFound_thenThrowsException() {

        // given
        val startingVertex = 's'
        val vertices = setOf('a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('a', 'c'),
            Edge('b', 'c'),
            Edge('b', 'd'),
            Edge('c', 'e'),
            Edge('d', 'e')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val exception = assertThrows<NoSuchElementException> {
            AugmentedBFS.shortestPath(graph = graph, startingVertex = startingVertex)
        }

        // then
        assertEquals("Vertex $startingVertex does not exist.", exception.message)
    }

    @Test
    fun shortestPath_whenSingleVertexGraph_thenOnlyStartingVertexReachable() {

        // given
        val vertices = setOf('s')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        assertEquals(1, distance.size)
        assertEquals(mapOf('s' to 0), distance)
    }

    @Test
    fun shortestPath_whenDisconnected_thenOnlyStartingVertexReachable() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        assertEquals(1, distance.size)
        assertEquals(mapOf('s' to 0), distance)
    }

    @Test
    fun shortestPath_whenLinearGraph_thenVisitsAllInOrder() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('d', 'e'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        val expected = mapOf(
            's' to 0,
            'a' to 1,
            'b' to 2,
            'c' to 3,
            'd' to 4,
            'e' to 5,
        )
        assertEquals(expected, distance)
    }

    @Test
    fun shortestPath_whenCyclicGraph_thenHandlesCycle() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('d', 'e'),
            Edge('e', 's'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        val expected = mapOf(
            's' to 0,
            'a' to 1,
            'b' to 2,
            'c' to 3,
            'd' to 2,
            'e' to 1,
        )
        assertEquals(expected, distance)
    }

    @Test
    fun shortestPath_whenCompleteGraph_thenExploresAllVertices() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('s', 'b'),
            Edge('s', 'c'),
            Edge('s', 'd'),
            Edge('s', 'e'),
            Edge('a', 'b'),
            Edge('a', 'c'),
            Edge('a', 'd'),
            Edge('a', 'e'),
            Edge('b', 'c'),
            Edge('b', 'd'),
            Edge('b', 'e'),
            Edge('c', 'd'),
            Edge('c', 'e'),
            Edge('d', 'e'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        val expected = mapOf(
            's' to 0,
            'a' to 1,
            'b' to 1,
            'c' to 1,
            'd' to 1,
            'e' to 1,
        )
        assertEquals(expected, distance)
    }

    @Test
    fun shortestPath_whenSelfLoopExists_thenHandlesLoopSafely() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('c', 'c'),
            Edge('d', 'e'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        val expected = mapOf(
            's' to 0,
            'a' to 1,
            'b' to 2,
            'c' to 3,
            'd' to 4,
            'e' to 5,
        )
        assertEquals(expected, distance)
    }

    @Test
    fun shortestPath_whenMultiplePaths_thenFindsShortestPath() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('s', 'c'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('d', 'e'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val distance = AugmentedBFS.shortestPath(graph = graph, startingVertex = 's')

        // then
        val expected = mapOf(
            's' to 0,
            'a' to 1,
            'b' to 2,
            'c' to 1,
            'd' to 2,
            'e' to 3,
        )
        assertEquals(expected, distance)
    }
}