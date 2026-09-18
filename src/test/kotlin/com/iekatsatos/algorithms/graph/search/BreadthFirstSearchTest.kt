package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Edge
import com.iekatsatos.algorithms.graph.AdjacencyListGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BreadthFirstSearchTest {

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
            BreadthFirstSearch.search(graph = graph, startingVertex = startingVertex)
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
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s'), reachableVertices)
    }

    @Test
    fun search_whenEmptyGraph_thenOnlyStartingVertexReachable() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s'), reachableVertices)
    }

    @Test
    fun search_whenLinearGraph_thenVisitsAllInOrder() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd', 'e', 'x', 'y', 'z')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd'),
            Edge('d', 'e'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'b', 'c', 'd', 'e'), reachableVertices)
    }

    @Test
    fun search_whenCyclicGraph_thenHandlesCycle() {

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
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'e', 'b', 'd', 'c'), reachableVertices)
    }

    @Test
    fun search_whenCompleteGraph_thenExploresAllVertices() {

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
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

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
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'b', 'c', 'd', 'e'), reachableVertices)
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
            Edge('d', 'e'),
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'b', 'c', 'd', 'e'), reachableVertices)
    }

    @Test
    fun search_whenMultiplePaths_thenFindsShortestPath() {

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
        val reachableVertices = BreadthFirstSearch.search(graph = graph, startingVertex = 's')

        // then
        assertEquals(listOf('s', 'a', 'c', 'b', 'd', 'e'), reachableVertices)
    }
}