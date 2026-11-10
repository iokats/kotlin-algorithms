package com.iekatsatos.algorithms.graph.sort

import com.iekatsatos.algorithms.graph.DirectedGraph
import com.iekatsatos.algorithms.graph.Graph

/**
 * Topological sort is an algorithm that, given a directed acyclic graph (DAG), assigns every vertex a label so that
 * for every edge `(u, v)` the label of `u` is smaller than the label of `v`. In other words, every vertex comes
 * before all the vertices it points to.
 *
 * This is the DFS-based formulation. The algorithm loops over all vertices of the graph, and whenever it meets a
 * vertex that has not been explored yet, it runs a depth-first search from it. A vertex is labeled when its search
 * finishes, that is, after all the vertices reachable from it have been labeled. Labels are handed out in decreasing
 * order, starting from `n`: the first vertex to finish (a sink) gets `n`, and the last one gets `1`. Therefore, every
 * vertex receives a smaller label than all of its successors.
 *
 * The algorithm requires a [DirectedGraph], since the order is defined by the direction of the edges. The graph must
 * be acyclic: a cycle makes a valid ordering impossible, and this implementation does not detect cycles, so for a
 * cyclic graph the returned labels do not satisfy the ordering property.
 *
 * A DAG usually has many valid topological orders. Which one is returned depends on the iteration order of
 * [Graph.vertices] and of the neighbor sets, which is an implementation detail of the graph and should not be relied
 * upon.
 *
 * Because the search is recursive, the recursion depth can reach `n` on long, path-like graphs, and a very large graph
 * may cause a [StackOverflowError].
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the number
 * of edges. The space complexity is O(n), for the explored set, the result map, and the call stack.
 */
object TopoSort {

    /**
     * Computes a topological ordering of [graph], returned as a mapping from each vertex to its label.
     *
     * The labels are the distinct integers `1..n`, where `n` is the number of vertices, and for every edge `(u, v)`
     * of [graph] the label of `u` is smaller than the label of `v`. Isolated vertices are labeled as well.
     *
     * @param T the vertex type
     * @param graph the directed acyclic graph to sort
     * @return a map in which each key is a vertex of [graph] and the value is its label in the topological order.
     *   An empty graph yields an empty map.
     * @throws StackOverflowError if the recursion becomes too deep for the call stack
     */
    fun <T> sort(graph: DirectedGraph<T>): Map<T, Int> {
        val exploredVertices: MutableSet<T> = mutableSetOf()
        val vertexOrder: MutableMap<T, Int> = mutableMapOf()

        for (vertex in graph.vertices) {
            if (vertex !in exploredVertices) {
                dfsTopo(graph, vertex, exploredVertices, vertexOrder)
            }
        }
        return vertexOrder
    }

    private fun <T> dfsTopo(
        graph: Graph<T>,
        startingVertex: T,
        exploredVertices: MutableSet<T>,
        vertexOrder: MutableMap<T, Int>
    ) {
        exploredVertices.add(startingVertex)

        for (neighbor in graph.neighborsOf(startingVertex)) {
            if (neighbor !in exploredVertices) {
                dfsTopo(graph, neighbor, exploredVertices, vertexOrder)
            }
        }
        vertexOrder[startingVertex] = graph.vertices.size - vertexOrder.size
    }
}