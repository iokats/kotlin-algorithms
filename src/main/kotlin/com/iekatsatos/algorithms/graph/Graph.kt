package com.iekatsatos.algorithms.graph

/**
 * A read-only representation of a mathematical graph consisting of vertices of type [T] and directed or undirected [Edge]s.
 *
 * This interface provides fundamental accessors to inspect the structural components of the graph ([vertices] and [edges])
 * as well as adjacency relationships via [neighborsOf].
 *
 * @param T the vertex type. Implementations rely on [T] having well-defined [Any.equals] and [Any.hashCode]
 *   semantics, as vertices serve as keys in set and map lookups throughout graph algorithms.
 */
interface Graph<T> {

    /**
     * The set of all unique vertices contained within this graph.
     */
    val vertices: Set<T>

    /**
     * The set of all unique edges connecting vertices in this graph.
     */
    val edges: Set<Edge<T>>

    /**
     * Returns all the adjacent vertices, which are connected by an [Edge] to the given [vertex].
     *
     * @param vertex the vertex whose neighbors are requested.
     * @return the set of vertices adjacent to [vertex], or an empty set if it has none.
     */
    fun neighborsOf(vertex: T): Set<T>
}