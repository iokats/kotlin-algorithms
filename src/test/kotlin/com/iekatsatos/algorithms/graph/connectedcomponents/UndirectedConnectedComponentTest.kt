package com.iekatsatos.algorithms.graph.connectedcomponents

import com.iekatsatos.algorithms.graph.Edge
import com.iekatsatos.algorithms.graph.representation.AdjacencyListGraph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UndirectedConnectedComponentTest {

    @Test
    fun findComponents_whenEmptyGraph_thenReturnsEmptyMap() {

        // given
        val graph = AdjacencyListGraph<Char>(vertices = emptySet())

        // when
        val componentByVertex = UndirectedConnectedComponent.findComponents(graph = graph)

        // then
        assertEquals(emptyMap(), componentByVertex)
    }

    @Test
    fun findComponents_whenSingleVertexGraph_thenSingleComponent() {

        // given
        val vertices = setOf('a')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val componentByVertex = UndirectedConnectedComponent.findComponents(graph = graph)

        // then
        assertEquals(mapOf('a' to 1), componentByVertex)
    }

    @Test
    fun findComponents_whenFullyConnectedGraph_thenSingleComponent() {

        // given
        val vertices = setOf('s', 'a', 'b', 'c', 'd')
        val edges = setOf(
            Edge('s', 'a'),
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'd')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val componentByVertex = UndirectedConnectedComponent.findComponents(graph = graph)

        // then
        val expected = mapOf(
            's' to 1,
            'a' to 1,
            'b' to 1,
            'c' to 1,
            'd' to 1
        )
        assertEquals(expected, componentByVertex)
    }

    @Test
    fun findComponents_whenAllVerticesIsolated_thenEachVertexInOwnComponent() {

        // given
        val vertices = setOf('a', 'b', 'c', 'd')
        val graph = AdjacencyListGraph(vertices = vertices)

        // when
        val componentByVertex = UndirectedConnectedComponent.findComponents(graph = graph)

        // then
        val expected = mapOf(
            'a' to 1,
            'b' to 2,
            'c' to 3,
            'd' to 4
        )
        assertEquals(expected, componentByVertex)
    }

    @Test
    fun findComponents_whenMultipleDisconnectedComponents_thenAssignsUniqueComponentIndices() {

        // given
        val vertices = setOf('a', 'b', 'c', 'x', 'y', 'z')
        val edges = setOf(
            // Component 1
            Edge('a', 'b'),
            Edge('b', 'c'),
            // Component 2
            Edge('x', 'y')
            // 'z' is isolated (Component 3)
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val componentByVertex = UndirectedConnectedComponent.findComponents(graph = graph)

        // then
        val expected = mapOf(
            'a' to 1,
            'b' to 1,
            'c' to 1,
            'x' to 2,
            'y' to 2,
            'z' to 3
        )
        assertEquals(expected, componentByVertex)
    }

    @Test
    fun findComponents_whenCyclicComponent_thenHandlesCycleSafely() {

        // given
        val vertices = setOf('a', 'b', 'c')
        val edges = setOf(
            Edge('a', 'b'),
            Edge('b', 'c'),
            Edge('c', 'a')
        )

        val graph = AdjacencyListGraph(vertices = vertices, edges = edges)

        // when
        val componentByVertex = UndirectedConnectedComponent.findComponents(graph = graph)

        // then
        val expected = mapOf(
            'a' to 1,
            'b' to 1,
            'c' to 1
        )
        assertEquals(expected, componentByVertex)
    }
}