package com.iekatsatos.algorithms.graph.representation

import com.iekatsatos.algorithms.graph.Edge
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class UndirectedAdjacencyListGraphTest {

    @Test
    fun constructor_whenNoArguments_thenEmptyGraph() {

        // when
        val graph = UndirectedAdjacencyListGraph<Int>()

        // then
        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun constructor_whenOnlyVerticesGiven_thenAllVerticesAreIsolated() {

        // when
        val graph = UndirectedAdjacencyListGraph<Int>(vertices = setOf(1, 2, 3))

        // then
        setOf(1, 2, 3).forEach { assertTrue(graph.neighborsOf(it).isEmpty()) }
    }

    @Test
    fun constructor_whenEdgesGivenWithoutVertices_thenThrowsIllegalArgumentException() {

        // when
        val exception = assertThrows<IllegalArgumentException> {
            UndirectedAdjacencyListGraph(edges = setOf(Edge(1, 2)))
        }

        // then
        assertEquals("Edges contain undeclared vertices.", exception.message)
    }

    @Test
    fun constructor_whenEdgeContainsUndeclaredVertex_thenThrowsIllegalArgumentException() {

        // given
        val vertices = setOf(1, 2, 3)
        val edges = setOf(Edge(1, 4))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            UndirectedAdjacencyListGraph(vertices, edges)
        }

        // then
        assertEquals("Edges contain undeclared vertices.", exception.message)
    }

    @Test
    fun constructor_whenFirstEndpointOfEdgeIsUndeclared_thenThrowsIllegalArgumentException() {

        // given
        val vertices = setOf(1, 2, 3)
        val edges = setOf(Edge(4, 1))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            UndirectedAdjacencyListGraph(vertices, edges)
        }

        // then
        assertEquals("Edges contain undeclared vertices.", exception.message)
    }

    @Test
    fun constructor_whenOnlyOneOfSeveralEdgesIsInvalid_thenThrowsIllegalArgumentException() {

        // given
        val vertices = setOf(1, 2, 3)
        val edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 99))

        // when / then
        assertThrows<IllegalArgumentException> {
            UndirectedAdjacencyListGraph(vertices, edges)
        }
    }

    @Test
    fun vertices_whenGiven_thenAreSetToTheGraph() {

        // given
        val givenVertices = setOf(1, 2, 3)

        // when
        val graph = UndirectedAdjacencyListGraph(
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
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        // then
        assertEquals(givenEdges, graph.edges)
    }

    @Test
    fun neighborsOf_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        val undefinedVertex = 4

        // when
        val exception = assertThrows<NoSuchElementException> {
            graph.neighborsOf(undefinedVertex)
        }

        // then
        assertEquals("Vertex $undefinedVertex does not exist.", exception.message)
    }

    @Test
    fun neighborsOf_whenGraphIsEmpty_thenThrowsNoSuchElementException() {

        // given
        val graph = UndirectedAdjacencyListGraph<Int>()

        // when / then
        assertThrows<NoSuchElementException> { graph.neighborsOf(1) }
    }

    @Test
    fun neighborsOf_whenVertexDoesNotHaveNeighbors_thenEmptySet() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val neighbors = graph.neighborsOf(3)

        // then
        assertTrue(neighbors.isEmpty())
    }

    @Test
    fun neighborsOf_whenVertexHasNeighbors_thenSetWithItsNeighbors() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val neighbors = graph.neighborsOf(1)

        // then
        assertEquals(setOf(2), neighbors)
    }

    @Test
    fun neighborsOf_whenEdgeIsDeclared_thenBothEndpointsSeeEachOther() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val neighbors1 = graph.neighborsOf(1)
        val neighbors2 = graph.neighborsOf(2)

        // then
        assertEquals(setOf(2), neighbors1)
        assertEquals(setOf(1), neighbors2)
    }

    @Test
    fun neighborsOf_whenVertexHasMultipleEdges_thenAllNeighborsAreReturned() {

        // given: star with center 0
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3, 4),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(0, 3), Edge(0, 4))
        )

        // when / then
        assertEquals(setOf(1, 2, 3, 4), graph.neighborsOf(0))
        (1..4).forEach { assertEquals(setOf(0), graph.neighborsOf(it)) }
    }

    @Test
    fun neighborsOf_whenPath_thenMiddleVertexHasBothNeighbors() {

        // given: 1 - 2 - 3
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2), Edge(2, 3))
        )

        // when / then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(1, 3), graph.neighborsOf(2))
        assertEquals(setOf(2), graph.neighborsOf(3))
    }

    @Test
    fun neighborsOf_whenTriangle_thenEachVertexSeesTheOtherTwo() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 1))
        )

        // when / then
        assertEquals(setOf(2, 3), graph.neighborsOf(1))
        assertEquals(setOf(1, 3), graph.neighborsOf(2))
        assertEquals(setOf(1, 2), graph.neighborsOf(3))
    }

    @Test
    fun neighborsOf_whenBothOrientationsOfEdgeGiven_thenNoDuplicateNeighbors() {

        // given: Edge(1, 2) and Edge(2, 1) describe the same connection
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2),
            edges = setOf(Edge(1, 2), Edge(2, 1))
        )

        // when / then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(1), graph.neighborsOf(2))
    }

    @Test
    fun neighborsOf_whenSelfLoop_thenVertexIsItsOwnNeighbor() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2),
            edges = setOf(Edge(1, 1))
        )

        // when / then
        assertEquals(setOf(1), graph.neighborsOf(1))
        assertTrue(graph.neighborsOf(2).isEmpty())
    }

    @Test
    fun neighborsOf_whenGraphIsDisconnected_thenComponentsAreIndependent() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'x', 'y', 'z'),
            edges = setOf(Edge('a', 'b'), Edge('x', 'y'))
        )

        // when / then
        assertEquals(setOf('b'), graph.neighborsOf('a'))
        assertEquals(setOf('y'), graph.neighborsOf('x'))
        assertTrue(graph.neighborsOf('z').isEmpty())
    }

    @Test
    fun neighborsOf_whenVertexTypeIsNotInt_thenWorksWithAnyHashableType() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf("A", "B", "C"),
            edges = setOf(Edge("A", "B"), Edge("B", "C"))
        )

        // when / then
        assertEquals(setOf("A", "C"), graph.neighborsOf("B"))
    }

    @Test
    fun neighborsOfWithComparator_whenAscendingComparator_thenNeighborsAreSortedAscending() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3, 4),
            edges = setOf(Edge(0, 3), Edge(0, 1), Edge(0, 4), Edge(0, 2))
        )

        // when
        val neighbors = graph.neighborsOf(0, naturalOrder())

        // then
        assertEquals(listOf(1, 2, 3, 4), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenDescendingComparator_thenNeighborsAreSortedDescending() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3, 4),
            edges = setOf(Edge(0, 3), Edge(0, 1), Edge(0, 4), Edge(0, 2))
        )

        // when
        val neighbors = graph.neighborsOf(0, reverseOrder())

        // then
        assertEquals(listOf(4, 3, 2, 1), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenCustomAttribute_thenSortedByThatAttribute() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf("root", "ccc", "a", "bb"),
            edges = setOf(Edge("root", "ccc"), Edge("root", "a"), Edge("root", "bb"))
        )

        // when
        val neighbors = graph.neighborsOf("root", compareBy { it.length })

        // then
        assertEquals(listOf("a", "bb", "ccc"), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenVertexHasNoNeighbors_thenEmptyList() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val neighbors = graph.neighborsOf(3, naturalOrder())

        // then
        assertEquals(emptyList(), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertThrows<NoSuchElementException> { graph.neighborsOf(4, naturalOrder()) }
    }

    @Test
    fun neighborsOfWithComparator_whenCalled_thenContainsSameElementsAsUnorderedVersion() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(0, 3))
        )

        // when
        val ordered = graph.neighborsOf(0, naturalOrder())

        // then
        assertEquals(graph.neighborsOf(0), ordered.toSet())
        assertEquals(graph.neighborsOf(0).size, ordered.size)
    }

    @Test
    fun degreeOf_whenVertexIsIsolated_thenZero() {

        // given
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertEquals(0, graph.degreeOf(3))
    }

    @Test
    fun degreeOf_whenVertexHasNeighbors_thenNumberOfNeighbors() {

        // given: star with center 0
        val graph = UndirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(0, 3))
        )

        // when / then
        assertEquals(3, graph.degreeOf(0))
        assertEquals(1, graph.degreeOf(1))
    }

    @Test
    fun degreeOf_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = UndirectedAdjacencyListGraph(vertices = setOf(1, 2))

        // when / then
        assertThrows<NoSuchElementException> { graph.degreeOf(3) }
    }

    // ---------------------------------------------------------------- invariants

    @Test
    fun neighborsOf_whenAnyGraph_thenAdjacencyIsSymmetric() {

        // given
        val vertices = setOf(1, 2, 3, 4, 5, 6)
        val graph = UndirectedAdjacencyListGraph(
            vertices = vertices,
            edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 1), Edge(4, 5), Edge(6, 6))
        )

        // when / then: v in N(u) <=> u in N(v)
        for (u in vertices) {
            for (v in vertices) {
                assertEquals(
                    v in graph.neighborsOf(u),
                    u in graph.neighborsOf(v),
                    "asymmetric adjacency between $u and $v"
                )
            }
        }
    }

    @Test
    fun neighborsOf_whenAnyEdgeDeclared_thenBothEndpointsAreAdjacent() {

        // given
        val edges = setOf(Edge(1, 2), Edge(2, 3), Edge(4, 1))
        val graph = UndirectedAdjacencyListGraph(vertices = setOf(1, 2, 3, 4), edges = edges)

        // when / then
        edges.forEach {
            assertTrue(it.v in graph.neighborsOf(it.u), "${it.v} should be a neighbor of ${it.u}")
            assertTrue(it.u in graph.neighborsOf(it.v), "${it.u} should be a neighbor of ${it.v}")
        }
    }

    @Test
    fun equals_whenSameVerticesAndEdges_thenGraphsAreEqual() {

        // given
        val first = UndirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(1, 2)))
        val second = UndirectedAdjacencyListGraph(setOf(3, 2, 1), setOf(Edge(1, 2)))

        // then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun equals_whenDifferentEdges_thenGraphsAreNotEqual() {

        // given
        val first = UndirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(1, 2)))
        val second = UndirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(2, 3)))

        // then
        assertNotEquals(first, second)
    }

    @Test
    fun equals_whenDifferentVertices_thenGraphsAreNotEqual() {

        // given
        val first = UndirectedAdjacencyListGraph(setOf(1, 2), setOf(Edge(1, 2)))
        val second = UndirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(1, 2)))

        // then
        assertNotEquals(first, second)
    }
}