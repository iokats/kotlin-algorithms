package com.iekatsatos.algorithms.graph.representation

import com.iekatsatos.algorithms.graph.Edge
import com.iekatsatos.algorithms.graph.Graph

/**
 * A [com.iekatsatos.algorithms.graph.Graph] implementation backed by an adjacency list.
 *
 * The graph is constructed from an explicit set of [vertices] and [edges], internally, it eagerly builds a `T -> neighbors`
 * lookup ([adjacencyList]) so that [neighborsOf] runs in constant time rather than scanning [edges] on every call.
 *
 * Memory usage is Θ(n + m), where `n` is the number of vertices and `m` is the number of edges: every vertex gets an
 * entry in [adjacencyList] (even with no neighbors), and every edge contributes two entries across the two endpoints'
 * neighbor sets.
 *
 * @param T the vertex type
 * @property vertices the vertices in the graph
 * @property edges the edges in the graph, connecting pairs of [vertices]. Every vertex referenced by an edge must also
 *   appear in [vertices], or construction fails — see [validateEdgeVertices].
 *
 * @constructor validates that [edges] only reference declared [vertices] and builds the internal adjacency list.
 */
data class AdjacencyListGraph<T>(
    val vertices: Set<T> = setOf(),
    val edges: Set<Edge<T>> = setOf()
): Graph<T> {

    private val adjacencyList: MutableMap<T, MutableSet<T>> = mutableMapOf()

    init {

        validateEdgeVertices()
    }

    /**
     * Returns all the adjacent vertices, which are connected by an [Edge] to the given [vertex].
     *
     * @param vertex the vertex whose neighbors are requested
     * @return the set of vertices adjacent to [vertex], or an empty set if it has none
     * @throws NoSuchElementException if [vertex] is not a vertex of the graph
     */
    override fun neighborsOf(vertex: T): Set<T> =
        adjacencyList[vertex]?: throw NoSuchElementException("Vertex $vertex does not exist.")

    private fun validateEdgeVertices() {

        require(edges.all { it.u in vertices && it.v in vertices }) {
            "Edges contain undeclared vertices."
        }

        vertices.forEach { adjacencyList[it] = mutableSetOf() }

        edges.forEach { updateAdjacencyList(it) }
    }

    private fun updateAdjacencyList(edge: Edge<T>) {

        adjacencyList[edge.u]?.add(edge.v)
        adjacencyList[edge.v]?.add(edge.u)
    }
}