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
     * The iteration order of the returned set is determined by the implementation and should not be relied upon.
     * Use the overload that accepts a comparator when a specific order is required.
     *
     * @param vertex the vertex whose neighbors are requested.
     * @return the set of vertices adjacent to [vertex], or an empty set if it has none.
     *  @throws NoSuchElementException if [vertex] is not a vertex of this graph (implementations may define this
     *      *   differently, see the implementing class).
     */
    fun neighborsOf(vertex: T): Set<T>

    /**
     * Returns all the adjacent vertices of the given [vertex], sorted according to [comparator].
     *
     * Sorting typically costs O(d log d), where `d` is the number of neighbors of [vertex]. Implementations that
     * already store their neighbors in a sorted structure may override this to avoid the extra work.
     *
     * @param vertex the vertex whose neighbors are requested.
     * @param comparator the ordering used to sort the returned neighbors. Vertices that compare as equal keep
     *   their relative order from [neighborsOf], since the sort is stable.
     * @return the neighbors of [vertex] as a list sorted by [comparator], or an empty list if it has none.
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph (implementations may define this
     *   differently, see the implementing class).
     */
    fun neighborsOf(vertex: T, comparator: Comparator<in T>): List<T>
}