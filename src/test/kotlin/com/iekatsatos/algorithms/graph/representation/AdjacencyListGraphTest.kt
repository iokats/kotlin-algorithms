package com.iekatsatos.algorithms.graph.representation

import com.iekatsatos.algorithms.graph.Edge
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdjacencyListGraphTest {

    @Test
    fun constructor_whenEdgeContainsUndeclaredVertex_thenThrowsIllegalArgumentException() {

        // given
        val vertices = setOf(1, 2, 3)
        val edges = setOf(Edge(1, 4))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            AdjacencyListGraph(vertices, edges)
        }

        // then
        assertEquals("Edges contain undeclared vertices.", exception.message)
    }

    @Test
    fun vertices_whenGiven_thenAreSetToTheGraph() {

        // given
        val givenVertices = setOf(1, 2, 3)

        // when
        val graph = AdjacencyListGraph(
            vertices = givenVertices,
            edges = setOf(Edge(1, 2))
        )

        // then
        assertEquals(givenVertices, graph.vertices)
    }

    @Test
    fun edges_whenGiven_thenAreSetToTheGraph() {

        // given
        val givenEdges = setOf(Edge(1, 2))

        // when
        val graph = AdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        // then
        assertEquals(givenEdges, graph.edges)
    }

    @Test
    fun neighborsOf_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val givenEdges = setOf(Edge(1, 2))

        val graph = AdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        val undefinedVertex = 4

        // when
        val exception = assertThrows<NoSuchElementException> {
            graph.neighborsOf(undefinedVertex)
        }

        // then
        assertEquals("Vertex $undefinedVertex" +
                " does not exist", exception.message)
    }

    @Test
    fun neighborsOf_whenVertexDoesNotHaveNeighbors_thenEmptySet() {

        // given
        val givenEdges = setOf(Edge(1, 2))

        val graph = AdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        // when
        val neighbors = graph.neighborsOf(3)

        // then
        assertTrue(neighbors.isEmpty())
    }

    @Test
    fun neighborsOf_whenVertexHasNeighbors_thenSetWithItsNeighbors() {

        // given
        val givenEdges = setOf(Edge(1, 2))

        val graph = AdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        // when
        val neighbors = graph.neighborsOf(1)

        // then
        assertEquals(setOf(2), neighbors)
    }

    @Test
    fun neighborsOf_whenUndirectedGraphBothVerticesInTheSameEdge_thenSetWithItsNeighbors() {

        // given
        val givenEdges = setOf(Edge(1, 2))

        val graph = AdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        // when
        val neighbors1 = graph.neighborsOf(1)
        val neighbors2 = graph.neighborsOf(2)

        // then
        assertEquals(setOf(2), neighbors1)
        assertEquals(setOf(1), neighbors2)
    }
}