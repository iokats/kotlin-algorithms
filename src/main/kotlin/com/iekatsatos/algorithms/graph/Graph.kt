package com.iekatsatos.algorithms.graph

/**
 * A read-only graph over vertices of type [T].
 *
 * @param T the vertex type. Should have well-defined [equals]/[hashCode] semantics, since vertices are used as set/map
 * keys throughout the graph algorithms in this package.
 */
interface Graph<T> {

    /**
     * Returns all the adjacent vertices, which are connected by an [Edge] to the given [vertex].
     *
     * @param vertex the vertex whose neighbors are requested.
     * @return the set of vertices adjacent to [vertex], or an empty set if it has none.
     */
    fun neighborsOf(vertex: T): Set<T>
}