package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Graph

/**
 * DFS (Depth-First Search) is a search algorithm that, given a starting vertex, explores the graph by going as deep
 * as possible along each path before backtracking.
 *
 * This is the recursive formulation of the algorithm: instead of managing an explicit stack, it relies on the call
 * stack. Exploring a vertex marks it as explored and then recursively explores each of its unexplored neighbors in
 * turn. When a vertex has no unexplored neighbors left, the call returns and the search backtracks to the previous
 * vertex. Every vertex is explored once -- each explored vertex is marked as explored to enforce this.
 *
 * Unlike the iterative [DFSIterative], neighbors are visited in the order given by [orderedNeighborsOf], with no
 * reversal, so the first neighbor in that order is explored first. For the same graph and comparator, both
 * implementations produce the same traversal order.
 *
 * Unlike BFS, DFS does not find the shortest paths: the path by which a vertex is first reached depends on the order
 * in which neighbors are visited, and is not necessarily the one with the fewest edges.
 *
 * Because every recursive call adds a frame to the call stack, the recursion depth can reach `n` on long, path-like
 * graphs, and a very large graph may cause a [StackOverflowError]. Prefer [DFSIterative] when the graph may be deep.
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the number
 * of edges (O(n + m log n) when a comparator is used to order neighbors). The space complexity is O(n), for the
 * explored set, the result list, and the call stack.
 */
object DFSRecursive : GraphSearchAlgorithm {

    /**
     * Performs a recursive depth-first search on [graph], starting from [startingVertex], and returns a list with all
     * the reachable vertices from this starting vertex.
     *
     * When a [comparator] is given, the neighbors of every vertex are sorted by it, and the traversal descends into
     * the first neighbor in that order before the others. Without a comparator, neighbors are visited in the graph's
     * own iteration order, which is an implementation detail of the graph and should not be relied upon.
     *
     * The time complexity is O(n + m) without a comparator. With a comparator, each vertex's neighbors are sorted,
     * which makes it O(n + m log n).
     *
     * @param T the vertex type
     * @param graph the graph to search
     * @param startingVertex the vertex to begin the search from
     * @param comparator the ordering used to sort the neighbors of each explored vertex, or `null` to use the
     *   graph's own iteration order
     * @return a list of all vertices reachable from [startingVertex], including [startingVertex] itself as the first
     *   element, in the order they were explored (i.e. depth first, backtracking when a path is exhausted)
     * @throws NoSuchElementException if [startingVertex] is not a vertex of [graph]
     * @throws StackOverflowError if the recursion becomes too deep for the call stack
     */
    override fun <T> search(
        graph: Graph<T>,
        startingVertex: T,
        comparator: Comparator<in T>?
    ): List<T> {

        val exploredVertices: MutableSet<T> = mutableSetOf()
        val traversalOrder: MutableList<T> = mutableListOf()

        return search(graph, startingVertex, comparator, exploredVertices, traversalOrder)
    }

    private fun <T> search(
        graph: Graph<T>,
        startingVertex: T,
        comparator: Comparator<in T>?,
        exploredVertices: MutableSet<T>,
        traversalOrder: MutableList<T>
    ): List<T> {

        exploredVertices.add(startingVertex)
        traversalOrder.add(startingVertex)

        for (neighbor in graph.orderedNeighborsOf(startingVertex, comparator)) {

            if (neighbor !in exploredVertices) {

                search(graph, neighbor, comparator, exploredVertices, traversalOrder)
            }
        }
        return traversalOrder
    }
}