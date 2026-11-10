package com.iekatsatos.algorithms.graph.sort

import com.iekatsatos.algorithms.graph.Edge
import com.iekatsatos.algorithms.graph.Graph
import com.iekatsatos.algorithms.graph.representation.DirectedAdjacencyListGraph
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TopoSortTest {

    @Test
    fun sort_whenGraphIsEmpty_thenEmptyMap() {

        // given
        val graph = DirectedAdjacencyListGraph<Char>()

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(emptyMap(), order)
    }

    @Test
    fun sort_whenSingleVertex_thenLabelIsOne() {

        // given
        val graph = DirectedAdjacencyListGraph(vertices = setOf('a'))

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(mapOf('a' to 1), order)
    }

    @Test
    fun sort_whenNoEdges_thenAllVerticesGetDistinctLabels() {

        // given
        val graph = DirectedAdjacencyListGraph(vertices = setOf('a', 'b', 'c'))

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
    }

    @Test
    fun sort_whenLinearChain_thenLabelsFollowTheChain() {

        // given: a -> b -> c
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'c'),
            edges = setOf(Edge('a', 'b'), Edge('b', 'c'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(mapOf('a' to 1, 'b' to 2, 'c' to 3), order)
    }

    @Test
    fun sort_whenChainVerticesIterateSinkFirst_thenLabelsStillFollowTheChain() {

        // given: the DFS starts from the sink because of the vertex iteration order
        val graph = DirectedAdjacencyListGraph(
            vertices = linkedSetOf('c', 'b', 'a'),
            edges = setOf(Edge('a', 'b'), Edge('b', 'c'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(mapOf('a' to 1, 'b' to 2, 'c' to 3), order)
    }

    @Test
    fun sort_whenChainVerticesIterateInMixedOrder_thenLabelsStillFollowTheChain() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = linkedSetOf('b', 'c', 'a'),
            edges = setOf(Edge('a', 'b'), Edge('b', 'c'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(mapOf('a' to 1, 'b' to 2, 'c' to 3), order)
    }

    @Test
    fun sort_whenLongChain_thenLabelsFollowTheChain() {

        // given: 1 -> 2 -> ... -> 500
        val size = 500
        val graph = DirectedAdjacencyListGraph(
            vertices = (1..size).toSet(),
            edges = (1 until size).map { Edge(it, it + 1) }.toSet()
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals((1..size).associateWith { it }, order)
    }

    @Test
    fun sort_whenDiamond_thenSourceIsFirstAndSinkIsLast() {

        // given: a -> b, a -> c, b -> d, c -> d
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'c', 'd'),
            edges = setOf(Edge('a', 'b'), Edge('a', 'c'), Edge('b', 'd'), Edge('c', 'd'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
        assertEquals(1, order['a'])
        assertEquals(4, order['d'])
    }

    @Test
    fun sort_whenOneSourceAndManySinks_thenSourceIsFirst() {

        // given: 0 -> 1, 0 -> 2, 0 -> 3
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(0, 1), Edge(0, 2), Edge(0, 3))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
        assertEquals(1, order[0])
    }

    @Test
    fun sort_whenManySourcesAndOneSink_thenSinkIsLast() {

        // given: 1 -> 0, 2 -> 0, 3 -> 0
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(0, 1, 2, 3),
            edges = setOf(Edge(1, 0), Edge(2, 0), Edge(3, 0))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
        assertEquals(4, order[0])
    }

    @Test
    fun sort_whenTransitiveEdgeIsPresent_thenOrderIsStillValid() {

        // given: a -> b -> c plus the shortcut a -> c
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'c'),
            edges = setOf(Edge('a', 'b'), Edge('b', 'c'), Edge('a', 'c'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(mapOf('a' to 1, 'b' to 2, 'c' to 3), order)
    }

    @Test
    fun sort_whenLargerDag_thenAllEdgesAreRespected() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('s', 'a', 'b', 'c', 'd', 'e'),
            edges = setOf(
                Edge('s', 'a'), Edge('s', 'b'),
                Edge('a', 'c'), Edge('b', 'c'), Edge('b', 'd'),
                Edge('c', 'e'), Edge('d', 'e')
            )
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
        assertEquals(1, order['s'])
        assertEquals(6, order['e'])
    }

    @Test
    fun sort_whenDisconnectedDag_thenEdgesOfEveryComponentAreRespected() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'c', 'x', 'y', 'z'),
            edges = setOf(Edge('a', 'b'), Edge('b', 'c'), Edge('x', 'y'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
    }

    @Test
    fun sort_whenIsolatedVerticesAmongConnectedOnes_thenAllLabelsAreDistinct() {

        // given: z is isolated
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'z'),
            edges = setOf(Edge('a', 'b'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
    }

    @Test
    fun sort_whenVerticesAndEdgesAreInReverseDependencyOrder_thenOrderIsStillValid() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = linkedSetOf('e', 'd', 'c', 'b', 'a'),
            edges = linkedSetOf(Edge('d', 'e'), Edge('c', 'd'), Edge('b', 'c'), Edge('a', 'b'), Edge('a', 'e'))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertValidTopologicalOrder(graph, order)
    }

    @Test
    fun sort_whenSameGraphWithDifferentVertexOrders_thenEveryOrderIsValid() {

        // given
        val edges = setOf(Edge(1, 2), Edge(1, 3), Edge(2, 4), Edge(3, 4), Edge(4, 5))
        val vertexOrders = listOf(
            linkedSetOf(1, 2, 3, 4, 5),
            linkedSetOf(5, 4, 3, 2, 1),
            linkedSetOf(3, 1, 5, 2, 4)
        )

        for (vertices in vertexOrders) {

            val graph = DirectedAdjacencyListGraph(vertices = vertices, edges = edges)

            // when
            val order = TopoSort.sort(graph)

            // then
            assertValidTopologicalOrder(graph, order)
        }
    }

    @Test
    fun sort_whenAnyDag_thenLabelsAreExactlyOneToN() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf(1, 2, 3, 4, 5, 6),
            edges = setOf(Edge(1, 2), Edge(3, 2), Edge(2, 4), Edge(5, 6))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals((1..6).toSet(), order.values.toSet())
    }

    @Test
    fun sort_whenAnyDag_thenEveryVertexIsLabeled() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf("A", "B", "C", "D"),
            edges = setOf(Edge("A", "B"), Edge("C", "D"))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(graph.vertices, order.keys)
    }

    @Test
    fun sort_whenVertexTypeIsNotChar_thenWorksWithAnyHashableType() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf("shirt", "tie", "jacket"),
            edges = setOf(Edge("shirt", "tie"), Edge("tie", "jacket"))
        )

        // when
        val order = TopoSort.sort(graph)

        // then
        assertEquals(mapOf("shirt" to 1, "tie" to 2, "jacket" to 3), order)
    }

    @Test
    fun sort_whenCalledTwiceOnTheSameGraph_thenGraphIsUnchangedAndOrderIsValidBothTimes() {

        // given
        val graph = DirectedAdjacencyListGraph(
            vertices = setOf('a', 'b', 'c'),
            edges = setOf(Edge('a', 'b'), Edge('b', 'c'))
        )

        // when
        val first = TopoSort.sort(graph)
        val second = TopoSort.sort(graph)

        // then
        assertEquals(first, second)
        assertEquals(setOf('a', 'b', 'c'), graph.vertices)
        assertEquals(setOf(Edge('a', 'b'), Edge('b', 'c')), graph.edges)
    }

    /**
     * Asserts that [order] is a valid topological labeling of [graph]: every vertex has a label, the labels are
     * distinct and form the range 1..n, and every edge u -> v satisfies label(u) < label(v).
     */
    private fun <T> assertValidTopologicalOrder(graph: Graph<T>, order: Map<T, Int>) {

        assertEquals(graph.vertices, order.keys, "every vertex must be labeled")
        assertEquals(
            (1..graph.vertices.size).toSet(),
            order.values.toSet(),
            "labels must be distinct and form the range 1..n"
        )
        for (edge in graph.edges) {
            assertTrue(
                order.getValue(edge.u) < order.getValue(edge.v),
                "edge ${edge.u} -> ${edge.v} violates the order: ${order[edge.u]} !< ${order[edge.v]}"
            )
        }
    }
}