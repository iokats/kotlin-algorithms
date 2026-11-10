package com.iekatsatos.algorithms.graph

/**
 * A [Graph] whose edges have a direction: an [Edge] `(u, v)` leads from the source `u` to the target `v`, and can be
 * traversed only that way.
 *
 * In a directed graph, [neighborsOf] returns the *successors* of a vertex. This interface adds the opposite direction
 * ([predecessorsOf]), which algorithms such as topological sorting (Kahn's algorithm) and strongly connected components
 * require. Algorithms that are only meaningful on directed graphs should accept this type.
 *
 * @param T the vertex type.
 */
interface DirectedGraph<T>: Graph<T> {

    /**
     * Returns the vertices that have an edge pointing to [vertex].
     *
     * @param vertex the vertex whose predecessors are requested.
     * @return the set of predecessors of [vertex], or an empty set if there are none.
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun predecessorsOf(vertex: T): Set<T>

    /**
     * Returns the vertices that [vertex] has an edge pointing to. Equivalent to [neighborsOf].
     *
     * @param vertex the vertex whose successors are requested.
     * @return the set of successors of [vertex], or an empty set if there are none.
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun successorsOf(vertex: T): Set<T> = neighborsOf(vertex)

    /**
     * Returns the number of edges leaving [vertex].
     *
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun outDegreeOf(vertex: T): Int = successorsOf(vertex).size

    /**
     * Returns the number of edges entering [vertex].
     *
     * @throws NoSuchElementException if [vertex] is not a vertex of this graph.
     */
    fun inDegreeOf(vertex: T): Int = predecessorsOf(vertex).size

    /**
     * Returns a graph with the same vertices in which every edge `(u, v)` is replaced by `(v, u)`.
     *
     * Needed, for example, by Kosaraju's algorithm for strongly connected components.
     *
     * @return the reversed graph.
     */
    fun reversed(): DirectedGraph<T>
}