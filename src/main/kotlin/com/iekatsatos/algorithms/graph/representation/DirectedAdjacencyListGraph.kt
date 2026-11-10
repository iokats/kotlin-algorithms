package com.iekatsatos.algorithms.graph.representation

import com.iekatsatos.algorithms.graph.DirectedGraph
import com.iekatsatos.algorithms.graph.Edge

/**
 * A [DirectedGraph] backed by adjacency lists.
 *
 * The graph is constructed from an explicit set of [vertices] and [edges]. Internally it eagerly builds two lookups,
 * one from each vertex to its successors and one from each vertex to its predecessors, so that [neighborsOf],
 * [successorsOf] and [predecessorsOf] run in constant time.
 *
 * An [Edge] `(u, v)` is directed from `u` to `v`: `v` becomes a successor of `u`, and `u` a predecessor of `v`.
 * `Edge(a, b)` and `Edge(b, a)` are two distinct edges. Self-loops `(v, v)` are allowed; they make `v` both a
 * successor and a predecessor of itself. Duplicate edges cannot occur, since [edges] is a set.
 *
 * Memory usage is Θ(n + m), where `n` is the number of vertices and `m` is the number of edges: every vertex has an
 * entry in both lookups (even without neighbors), and every edge contributes one entry to each.
 *
 * As a `data class`, equality and [hashCode] are structural, based on [vertices] and [edges]. The internal lookups
 * are derived from them and are not part of the generated members.
 *
 * @param T the vertex type
 * @property vertices the vertices in the graph
 * @property edges the directed edges of the graph. Every vertex referenced by an edge must also appear in [vertices],
 *   or construction fails.
 * @throws IllegalArgumentException if any edge references a vertex that is not in [vertices]
 */
data class DirectedAdjacencyListGraph<T>(
    override val vertices: Set<T> = setOf(),
    override val edges: Set<Edge<T>> = setOf()
) : DirectedGraph<T> {

    /** Maps each vertex to the vertices it points to. Every vertex has an entry, possibly empty. */
    private val successors: Map<T, MutableSet<T>> = vertices.associateWith { mutableSetOf() }

    /** Maps each vertex to the vertices pointing to it. Every vertex has an entry, possibly empty. */
    private val predecessors: Map<T, MutableSet<T>> = vertices.associateWith { mutableSetOf() }

    init {
        require(edges.all { it.u in vertices && it.v in vertices }) {
            "Edges contain undeclared vertices."
        }

        edges.forEach { edge ->
            successors.getValue(edge.u).add(edge.v)
            predecessors.getValue(edge.v).add(edge.u)
        }
    }

    /**
     * Returns the successors of [vertex], that is, the vertices at the head of an edge leaving it.
     *
     * @param vertex the vertex whose successors are requested
     * @return the successors of [vertex], or an empty set if it has none
     * @throws NoSuchElementException if [vertex] is not in [vertices]
     */
    override fun neighborsOf(vertex: T): Set<T> =
        successors[vertex] ?: throw NoSuchElementException("Vertex $vertex does not exist.")

    /**
     * Returns the predecessors of [vertex], that is, the vertices at the tail of an edge entering it.
     *
     * @param vertex the vertex whose predecessors are requested
     * @return the predecessors of [vertex], or an empty set if it has none
     * @throws NoSuchElementException if [vertex] is not in [vertices]
     */
    override fun predecessorsOf(vertex: T): Set<T> =
        predecessors[vertex] ?: throw NoSuchElementException("Vertex $vertex does not exist.")

    /**
     * Returns a new graph with the same vertices in which every edge `(u, v)` is replaced by `(v, u)`.
     *
     * Runs in O(n + m), since the new graph builds its own lookups.
     *
     * @return the reversed graph
     */
    override fun reversed(): DirectedAdjacencyListGraph<T> =
        DirectedAdjacencyListGraph(vertices, edges.map { Edge(it.v, it.u) }.toSet())
}