package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Graph

/**
 * DFS (Depth-First Search) is a search algorithm that, given a starting vertex, explores the graph by going as deep
 * as possible along each path before backtracking.
 *
 * It first explores one vertex directly connected to the starting vertex, then one vertex directly connected to that
 * vertex, and so on, until it reaches a vertex with no unexplored neighbors. It then backtracks to the most recently
 * explored vertex that still has unexplored neighbors and continues from there. Every vertex is explored once -- each
 * explored vertex is marked as explored to enforce this.
 *
 * Unlike BFS, DFS does not find shortest paths: the path by which a vertex is first reached depends on the order in
 * which neighbors are visited, and is not necessarily the one with the fewest edges.
 *
 * Vertices awaiting exploration are kept in a LIFO stack: the most recently discovered vertex is the first to be
 * explored. This guarantees the algorithm fully explores everything reachable through a vertex before returning to
 * the vertices discovered earlier. Since a vertex can be pushed onto the stack more than once (via different
 * neighbors), the explored check is performed when a vertex is popped, and any vertex already explored is skipped.
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the number
 * of edges (O(n + m log n) when a comparator is used to order neighbors).
 */
object DepthFirstSearch: GraphSearchAlgorithm {

    /**
     * Performs a depth-first search on [graph], starting from [startingVertex], and returns a list with all the
     * reachable vertices from this starting vertex.
     *
     * When a [comparator] is given, the neighbors of every vertex are sorted by it, and the traversal descends into
     * the first neighbor in that order before the others. To achieve this with a stack, neighbors are pushed in
     * reverse order, so the first one is popped first. Without a comparator, neighbors are visited in the graph's own
     * iteration order.
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
     */
    override fun <T> search(
        graph: Graph<T>,
        startingVertex: T,
        comparator: Comparator<in T>?
    ): List<T> {

        val exploredVertices: MutableSet<T> = mutableSetOf()
        val stack: ArrayDeque<T> = ArrayDeque<T>().apply { addLast(startingVertex) }

        val traversalOrder: MutableList<T> = mutableListOf()

        while (stack.isNotEmpty()) {

            val currentVertex = stack.removeLast()

            if (currentVertex !in exploredVertices) {

                traversalOrder.add(currentVertex)

                exploredVertices.add(currentVertex)

                for (neighbor in graph.orderedNeighborsOf(currentVertex, comparator).asReversed()) {
                    if (neighbor !in exploredVertices) {
                        stack.addLast(neighbor)
                    }
                }
            }
        }
        return traversalOrder
    }
}