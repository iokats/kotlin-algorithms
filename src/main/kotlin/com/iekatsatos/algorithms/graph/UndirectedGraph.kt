package com.iekatsatos.algorithms.graph

/**
 * A [Graph] whose edges have no direction: an edge between `u` and `v` can be traversed both ways.
 *
 * Implementations must guarantee symmetry: `v in neighborsOf(u)` if and only if `u in neighborsOf(v)`. Algorithms that
 * rely on this property (connected components, spanning trees, bipartiteness checks) should accept this type rather
 * than the base [Graph].
 *
 * This type is deliberately not related to [DirectedGraph]: an undirected graph is not substitutable for a directed
 * one (every edge would be a 2-cycle), nor the other way around.
 *
 * @param T the vertex type.
 */
interface UndirectedGraph<T>: Graph<T> {

    /**
     * Returns the number of edges incident to [vertex], that is, the number of its neighbors.
     *
     * @param vertex the vertex whose degree is requested.
     * @return the degree of [vertex].
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun degreeOf(vertex: T): Int = neighborsOf(vertex).size
}