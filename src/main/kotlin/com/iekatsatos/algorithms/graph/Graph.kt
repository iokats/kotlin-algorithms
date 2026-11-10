package com.iekatsatos.algorithms.graph

/**
 * A read-only representation of a graph consisting of vertices of type [T] and [Edge]s connecting them.
 *
 * It provides accessors for the structure of the graph ([vertices] and [edges]) and the single-step traversal relation
 * [neighborsOf]. Whether edges have a direction is defined by the subtypes [DirectedGraph] and [UndirectedGraph]; code
 * that only needs to traverse a graph can depend on this type alone.
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
     * The set of all unique edges of this graph.
     */
    val edges: Set<Edge<T>>

    /**
     * Returns the vertices that can be reached from [vertex] by traversing a single edge.
     *
     * In a directed graph these are the successors of [vertex]; in an undirected graph, all adjacent vertices.
     *
     * The iteration order of the returned set is determined by the implementation and should not be relied upon.
     * Use the overload that accepts a comparator when a specific order is required.
     *
     * @param vertex the vertex whose neighbors are requested.
     * @return the set of vertices reachable from [vertex] in one step, or an empty set if there are none.
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun neighborsOf(vertex: T): Set<T>

    /**
     * Returns the vertices reachable from [vertex] in one step, sorted according to [comparator].
     *
     * Sorting costs O(d log d), where `d` is the number of neighbors of [vertex]. Implementations that already store
     * their neighbors in a sorted structure may override this to avoid the extra work.
     *
     * @param vertex the vertex whose neighbors are requested.
     * @param comparator the ordering used to sort the returned neighbors. Vertices that compare as equal keep their
     *   relative order from [neighborsOf], since the sort is stable.
     * @return the neighbors of [vertex] as a sorted list, or an empty list if there are none.
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun neighborsOf(vertex: T, comparator: Comparator<in T>): List<T> =
        neighborsOf(vertex).sortedWith(comparator)
}