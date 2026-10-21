package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Graph

/**
 * Represents a generic strategy for graph searching and traversal algorithms (e.g., Breadth-First Search,
 * Depth-First Search).
 *
 * Implementations of this interface process a graph starting from a specified origin vertex and produce a sequence
 * representing the order in which reachable vertices are visited.
 */
interface GraphSearchAlgorithm {

    /**
     * Performs a graph search on [graph] starting from [startingVertex] and returns a list of all reachable vertices
     * in the order they were visited by the algorithm.
     *
     * @param graph the graph to search
     * @param startingVertex the vertex from which to begin the search
     * @param comparator the ordering used to sort the neighbors of each explored vertex, or `null` to use the
     *   graph's own iteration order
     * @return a list containing all vertices reachable from [startingVertex], beginning with [startingVertex] itself
     * @throws NoSuchElementException if [startingVertex] is not present in [graph]
     */
    fun <T> search(graph: Graph<T>, startingVertex: T, comparator: Comparator<in T>? = null): List<T>
}