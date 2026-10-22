package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Edge
import com.iekatsatos.algorithms.graph.representation.AdjacencyListGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class DFSIterativeTest {

    @Test
    fun search_whenStartingVertexNotFound_thenThrowsException() {

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
            DFSIterative.search(graph = graph, startingVertex = startingVertex)
        }

        // then
        assertEquals("Vertex $startingVertex does not exist.", exception.message)
    }

    @Test
    fun search_whenSingleVertexGraph_thenOnlyStartingVertexReachable() {

        // given
        val vertices = setOf('s')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s'), reachableVertices)
    }

    @Test
    fun search_whenEmptyGraph_thenOnlyStartingVertexReachable() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s'), reachableVertices)
    }

    @Test
    fun search_whenLinearGraph_thenVisitsAllInDepthOrder() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('d', 'e')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'b', 'c', 'd', 'e'), reachableVertices)
    }

    @Test
    fun search_whenBranchingGraph_thenExploresDeepBranchFirst() {

        // given
        // s connects to both a and b.
        // Depth-first traversal will fully explore down a -> c -> d before backtracking to b.
        val vertices = setOf('s', 'a', 'b', 'c', 'd')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('s', 'b'),
            Edge('a', 'c'),
            Edge('c', 'd')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'c', 'd', 'b'), reachableVertices)
    }

    @Test
    fun search_whenCyclicGraph_thenHandlesCycleWithoutInfiniteLoop() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('d', 'e'),
            Edge('e', 's')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'b', 'c', 'd', 'e'), reachableVertices)
    }

    @Test
    fun search_whenDisconnectedGraph_thenIgnoresUnreachableVertices() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('s', 'b'),
            Edge('a', 'c'),
            Edge('b', 'c'),
            Edge('b', 'd'),
            Edge('c', 'e'),
            Edge('d', 'e'),
            Edge('x', 'y')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'c', 'b', 'd', 'e'), reachableVertices)
    }

    @Test
    fun search_whenSelfLoopExists_thenHandlesLoopSafely() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('c', 'c'),
            Edge('d', 'e')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = DFSIterative.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'b', 'c', 'd', 'e'), reachableVertices)
    }

    @Test
    fun search_whenDeepLinearGraph_thenDoesNotThrowStackOverflow() {
        val size = 10_000
        val vertices = (0..size).toSet()
        val edges = (0 until size).map { Edge(it, it + 1) }.toSet()
        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        val result = DFSIterative.search(graph = graph, startingVertex = 0)

        assertEquals(size + 1, result.size)
    }
}