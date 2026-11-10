package com.iekatsatos.algorithms.graph.representation

import com.iekatsatos.algorithms.graph.Edge
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class DirectedAdjacencyListGraphTest {

    @Test
    fun constructor_whenNoArguments_thenEmptyGraph() {

        // when
        val graph = DirectedAdjacencyListGraph<Int>()

        // then
        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun constructor_whenOnlyVerticesGiven_thenAllVerticesAreIsolated() {

        // when
        val graph = DirectedAdjacencyListGraph<Int>(vertices = setOf(1, 2, 3))

        // then
        setOf(1, 2, 3).forEach {
            assertTrue(graph.neighborsOf(it).isEmpty())
            assertTrue(graph.predecessorsOf(it).isEmpty())
        }
    }

    @Test
    fun constructor_whenEdgesGivenWithoutVertices_thenThrowsIllegalArgumentException() {

        // when
        val exception = assertThrows<IllegalArgumentException> {
            DirectedAdjacencyListGraph(edges = setOf(Edge(1, 2)))
        }

        // then
        assertEquals("Edges contain undeclared vertices.", exception.message)
    }

    @Test
    fun constructor_whenTargetOfEdgeIsUndeclared_thenThrowsIllegalArgumentException() {

        // given
        val vertices = setOf(1, 2, 3)
        val edges = setOf(Edge(1, 4))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            DirectedAdjacencyListGraph(vertices, edges)
        }

        // then
        assertEquals("Edges contain undeclared vertices.", exception.message)
    }

    @Test
    fun constructor_whenSourceOfEdgeIsUndeclared_thenThrowsIllegalArgumentException() {

        // given
        val vertices = setOf(1, 2, 3)
        val edges = setOf(Edge(4, 1))

        // when
        val exception = assertThrows<IllegalArgumentException> {
            DirectedAdjacencyListGraph(vertices, edges)
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
            DirectedAdjacencyListGraph(vertices, edges)
        }
    }

    @Test
    fun vertices_whenGiven_thenAreSetToTheGraph() {

        // given
        val givenVertices = setOf(1, 2, 3)

        // when
        val graph = DirectedAdjacencyListGraph(
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
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = givenEdges
        )

        // then
        assertEquals(givenEdges, graph.edges)
    }

    @Test
    fun neighborsOf_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val exception = assertThrows<NoSuchElementException> {
            graph.neighborsOf(4)
        }

        // then
        assertEquals("Vertex 4 does not exist.", exception.message)
    }

    @Test
    fun neighborsOf_whenGraphIsEmpty_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph<Int>()

        // when / then
        assertThrows<NoSuchElementException> { graph.neighborsOf(1) }
    }

    @Test
    fun neighborsOf_whenVertexHasNoOutgoingEdges_thenEmptySet() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertTrue(graph.neighborsOf(3).isEmpty())
    }

    @Test
    fun neighborsOf_whenEdgeIsDeclared_thenOnlySourceSeesTarget() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then: the edge can only be traversed from 1 to 2
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertTrue(graph.neighborsOf(2).isEmpty())
    }

    @Test
    fun neighborsOf_whenVertexHasMultipleOutgoingEdges_thenAllSuccessorsAreReturned() {

        // given: star with center 0, edges pointing outwards
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3, 4),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(0, 3), Edge(0, 4))
        )

        // when / then
        assertEquals(setOf(1, 2, 3, 4), graph.neighborsOf(0))
        (1..4).forEach { assertTrue(graph.neighborsOf(it).isEmpty()) }
    }

    @Test
    fun neighborsOf_whenPath_thenFollowsEdgeDirection() {

        // given: 1 -> 2 -> 3
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2), Edge(2, 3))
        )

        // when / then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(3), graph.neighborsOf(2))
        assertTrue(graph.neighborsOf(3).isEmpty())
    }

    @Test
    fun neighborsOf_whenBothOrientationsOfEdgeGiven_thenTheyAreTwoDistinctEdges() {

        // given: 1 -> 2 and 2 -> 1
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2),
            edges = setOf(Edge(1, 2), Edge(2, 1))
        )

        // when / then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(1), graph.neighborsOf(2))
        assertEquals(2, graph.edges.size)
    }

    @Test
    fun neighborsOf_whenCycle_thenEachVertexSeesOnlyItsSuccessor() {

        // given: 1 -> 2 -> 3 -> 1
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 1))
        )

        // when / then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(3), graph.neighborsOf(2))
        assertEquals(setOf(1), graph.neighborsOf(3))
    }

    @Test
    fun neighborsOf_whenSelfLoop_thenVertexIsItsOwnSuccessor() {

        // given
        val graph = DirectedAdjacencyListGraph(
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
        val graph = DirectedAdjacencyListGraph(
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
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf("A", "B", "C"),
            edges = setOf(Edge("A", "B"), Edge("B", "C"))
        )

        // when / then
        assertEquals(setOf("C"), graph.neighborsOf("B"))
    }

    @Test
    fun neighborsOfWithComparator_whenAscendingComparator_thenSortedAscending() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3, 4),
            edges = setOf(Edge(0, 3), Edge(0, 1), Edge(0, 4), Edge(0, 2))
        )

        // when
        val neighbors = graph.neighborsOf(0, naturalOrder())

        // then
        assertEquals(listOf(1, 2, 3, 4), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenDescendingComparator_thenSortedDescending() {

        // given
        val graph = DirectedAdjacencyListGraph(
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
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf("root", "ccc", "a", "bb"),
            edges = setOf(Edge("root", "ccc"), Edge("root", "a"), Edge("root", "bb"))
        )

        // when
        val neighbors = graph.neighborsOf("root", compareBy { it.length })

        // then
        assertEquals(listOf("a", "bb", "ccc"), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenOnlyIncomingEdges_thenEmptyList() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 3), Edge(2, 3))
        )

        // when
        val neighbors = graph.neighborsOf(3, naturalOrder())

        // then
        assertEquals(emptyList<Int>(), neighbors)
    }

    @Test
    fun neighborsOfWithComparator_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertThrows<NoSuchElementException> { graph.neighborsOf(4, naturalOrder()) }
    }

    @Test
    fun successorsOf_whenCalled_thenEqualsNeighborsOf() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3, 4),
            edges = setOf(Edge(1, 2), Edge(1, 3), Edge(3, 4))
        )

        // when / then
        graph.vertices.forEach { assertEquals(graph.neighborsOf(it), graph.successorsOf(it)) }
    }

    @Test
    fun successorsOf_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph(vertices = setOf(1, 2))

        // when / then
        assertThrows<NoSuchElementException> { graph.successorsOf(3) }
    }

    @Test
    fun predecessorsOf_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val exception = assertThrows<NoSuchElementException> {
            graph.predecessorsOf(4)
        }

        // then
        assertEquals("Vertex 4 does not exist.", exception.message)
    }

    @Test
    fun predecessorsOf_whenGraphIsEmpty_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph<Int>()

        // when / then
        assertThrows<NoSuchElementException> { graph.predecessorsOf(1) }
    }

    @Test
    fun predecessorsOf_whenVertexHasNoIncomingEdges_thenEmptySet() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertTrue(graph.predecessorsOf(1).isEmpty())
        assertTrue(graph.predecessorsOf(3).isEmpty())
    }

    @Test
    fun predecessorsOf_whenEdgeIsDeclared_thenOnlyTargetSeesSource() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertEquals(setOf(1), graph.predecessorsOf(2))
        assertTrue(graph.predecessorsOf(1).isEmpty())
    }

    @Test
    fun predecessorsOf_whenVertexHasMultipleIncomingEdges_thenAllPredecessorsAreReturned() {

        // given: edges converge on 0
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(1, 0), Edge(2, 0), Edge(3, 0))
        )

        // when / then
        assertEquals(setOf(1, 2, 3), graph.predecessorsOf(0))
        (1..3).forEach { assertTrue(graph.predecessorsOf(it).isEmpty()) }
    }

    @Test
    fun predecessorsOf_whenDiamond_thenSinkHasBothMiddleVerticesAsPredecessors() {

        // given: a -> b, a -> c, b -> d, c -> d
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'c', 'd'),
            edges = setOf(Edge('a', 'b'), Edge('a', 'c'), Edge('b', 'd'), Edge('c', 'd'))
        )

        // when / then
        assertTrue(graph.predecessorsOf('a').isEmpty())
        assertEquals(setOf('a'), graph.predecessorsOf('b'))
        assertEquals(setOf('a'), graph.predecessorsOf('c'))
        assertEquals(setOf('b', 'c'), graph.predecessorsOf('d'))
    }

    @Test
    fun predecessorsOf_whenSelfLoop_thenVertexIsItsOwnPredecessor() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2),
            edges = setOf(Edge(1, 1))
        )

        // when / then
        assertEquals(setOf(1), graph.predecessorsOf(1))
        assertTrue(graph.predecessorsOf(2).isEmpty())
    }

    @Test
    fun outDegreeOf_whenVertexIsIsolated_thenZero() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertEquals(0, graph.outDegreeOf(3))
    }

    @Test
    fun outDegreeOf_whenVertexHasOutgoingEdges_thenCountsOnlyOutgoing() {

        // given: 0 -> 1, 0 -> 2, 3 -> 0
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(3, 0))
        )

        // when / then
        assertEquals(2, graph.outDegreeOf(0))
        assertEquals(0, graph.outDegreeOf(1))
        assertEquals(1, graph.outDegreeOf(3))
    }

    @Test
    fun inDegreeOf_whenVertexIsIsolated_thenZero() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when / then
        assertEquals(0, graph.inDegreeOf(3))
    }

    @Test
    fun inDegreeOf_whenVertexHasIncomingEdges_thenCountsOnlyIncoming() {

        // given: 0 -> 1, 0 -> 2, 3 -> 0
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(3, 0))
        )

        // when / then
        assertEquals(1, graph.inDegreeOf(0))
        assertEquals(1, graph.inDegreeOf(1))
        assertEquals(0, graph.inDegreeOf(3))
    }

    @Test
    fun degrees_whenSelfLoop_thenCountedOnceInEachDirection() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1),
            edges = setOf(Edge(1, 1))
        )

        // when / then
        assertEquals(1, graph.outDegreeOf(1))
        assertEquals(1, graph.inDegreeOf(1))
    }

    @Test
    fun degrees_whenVertexDoesNotExist_thenThrowsNoSuchElementException() {

        // given
        val graph = DirectedAdjacencyListGraph(vertices = setOf(1, 2))

        // when / then
        assertThrows<NoSuchElementException> { graph.outDegreeOf(3) }
        assertThrows<NoSuchElementException> { graph.inDegreeOf(3) }
    }

    @Test
    fun degrees_whenAnyGraph_thenSumOfInDegreesEqualsSumOfOutDegreesEqualsEdgeCount() {

        // given
        val vertices = setOf(1, 2, 3, 4, 5)
        val graph = DirectedAdjacencyListGraph(
            vertices = vertices,
            edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 1), Edge(4, 4), Edge(1, 5))
        )

        // when
        val totalIn = vertices.sumOf { graph.inDegreeOf(it) }
        val totalOut = vertices.sumOf { graph.outDegreeOf(it) }

        // then
        assertEquals(graph.edges.size, totalIn)
        assertEquals(graph.edges.size, totalOut)
    }

    @Test
    fun adjacency_whenAnyGraph_thenSuccessorsAndPredecessorsAreConsistent() {

        // given
        val vertices = setOf(1, 2, 3, 4, 5, 6)
        val graph = DirectedAdjacencyListGraph(
            vertices = vertices,
            edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 1), Edge(4, 5), Edge(6, 6))
        )

        // when / then: v in successors(u) <=> u in predecessors(v)
        for (u in vertices) {
            for (v in vertices) {
                assertEquals(
                    v in graph.successorsOf(u),
                    u in graph.predecessorsOf(v),
                    "inconsistent adjacency between $u and $v"
                )
            }
        }
    }

    @Test
    fun adjacency_whenAnyEdgeDeclared_thenSourceReachesTargetButNotNecessarilyBack() {

        // given
        val edges = setOf(Edge(1, 2), Edge(2, 3), Edge(4, 1))
        val graph = DirectedAdjacencyListGraph(vertices = setOf(1, 2, 3, 4), edges = edges)

        // when / then
        edges.forEach {
            assertTrue(it.v in graph.neighborsOf(it.u), "${it.v} should be a successor of ${it.u}")
            assertTrue(it.u in graph.predecessorsOf(it.v), "${it.u} should be a predecessor of ${it.v}")
            assertTrue(it.u !in graph.neighborsOf(it.v), "${it.u} should not be a successor of ${it.v}")
        }
    }

    @Test
    fun reversed_whenGraphIsEmpty_thenEmptyGraph() {

        // given
        val graph = DirectedAdjacencyListGraph<Int>()

        // when / then
        assertEquals(graph, graph.reversed())
    }

    @Test
    fun reversed_whenCalled_thenKeepsTheSameVertices() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2))
        )

        // when
        val reversed = graph.reversed()

        // then
        assertEquals(graph.vertices, reversed.vertices)
    }

    @Test
    fun reversed_whenCalled_thenEveryEdgeIsFlipped() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2), Edge(2, 3))
        )

        // when
        val reversed = graph.reversed()

        // then
        assertEquals(setOf(Edge(2, 1), Edge(3, 2)), reversed.edges)
    }

    @Test
    fun reversed_whenCalled_thenSuccessorsAndPredecessorsSwapRoles() {

        // given: 1 -> 2, 1 -> 3, 2 -> 3
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3),
            edges = setOf(Edge(1, 2), Edge(1, 3), Edge(2, 3))
        )

        // when
        val reversed = graph.reversed()

        // then
        graph.vertices.forEach {
            assertEquals(graph.predecessorsOf(it), reversed.neighborsOf(it))
            assertEquals(graph.neighborsOf(it), reversed.predecessorsOf(it))
        }
    }

    @Test
    fun reversed_whenAppliedTwice_thenEqualsOriginalGraph() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3, 4),
            edges = setOf(Edge(1, 2), Edge(2, 3), Edge(3, 1), Edge(4, 4))
        )

        // when / then
        assertEquals(graph, graph.reversed().reversed())
    }

    @Test
    fun reversed_whenSelfLoop_thenSelfLoopIsKept() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1),
            edges = setOf(Edge(1, 1))
        )

        // when / then
        assertEquals(setOf(Edge(1, 1)), graph.reversed().edges)
    }

    @Test
    fun reversed_whenCalled_thenOriginalGraphIsUnchanged() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2),
            edges = setOf(Edge(1, 2))
        )

        // when
        graph.reversed()

        // then
        assertEquals(setOf(Edge(1, 2)), graph.edges)
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertTrue(graph.neighborsOf(2).isEmpty())
    }

    // ---------------------------------------------------------------- equality

    @Test
    fun equals_whenSameVerticesAndEdges_thenGraphsAreEqual() {

        // given
        val first = DirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(1, 2)))
        val second = DirectedAdjacencyListGraph(setOf(3, 2, 1), setOf(Edge(1, 2)))

        // then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun equals_whenEdgeDirectionDiffers_thenGraphsAreNotEqual() {

        // given: unlike the undirected graph, direction is part of the graph's identity
        val first = DirectedAdjacencyListGraph(setOf(1, 2), setOf(Edge(1, 2)))
        val second = DirectedAdjacencyListGraph(setOf(1, 2), setOf(Edge(2, 1)))

        // then
        assertNotEquals(first, second)
    }

    @Test
    fun equals_whenDifferentEdges_thenGraphsAreNotEqual() {

        // given
        val first = DirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(1, 2)))
        val second = DirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(2, 3)))

        // then
        assertNotEquals(first, second)
    }

    @Test
    fun equals_whenDifferentVertices_thenGraphsAreNotEqual() {

        // given
        val first = DirectedAdjacencyListGraph(setOf(1, 2), setOf(Edge(1, 2)))
        val second = DirectedAdjacencyListGraph(setOf(1, 2, 3), setOf(Edge(1, 2)))

        // then
        assertNotEquals(first, second)
    }
}