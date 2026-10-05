package com.iekatsatos.algorithms.graph.connectedcomponents

import com.iekatsatos.algorithms.graph.Graph
import com.iekatsatos.algorithms.graph.search.BreadthFirstSearch

/**
 * Undirected Connected Component identification partitions an undirected graph into disjoint subgraphs in which any
 * two vertices are connected to each other by a path.
 *
 * It iterates through all vertices in the graph and uses [BreadthFirstSearch] to discover all vertices reachable from
 * each unvisited node. Every time an unvisited vertex is encountered, a new component index is initialized, and all
 * newly discovered vertices belong to that same connected component layer.
 *
 * Previously visited vertices are tracked in a map to ensure that each connected component is fully explored exactly
 * once, avoiding redundant traversals across already partitioned vertices.
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the number
 * of edges.
 */
object UndirectedConnectedComponent {

    /**
     * Identifies all connected components in [graph] and returns a mapping from each vertex to its 1-based connected
     * component index.
     *
     * @param T the vertex type
     * @param graph the undirected graph to search
     * @return a map where each key is a vertex in [graph] and the corresponding value is the 1-based index of the
     *    connected component to which it belongs
     */
    fun <T> findComponents(graph: Graph<T>): Map<T, Int> {
        val componentByVertex: MutableMap<T, Int> = mutableMapOf()

        var connectedComponentIndex = 0

        for (vertex in graph.vertices ) {

            if (vertex !in componentByVertex) {

                connectedComponentIndex++

                val connectedComponent = BreadthFirstSearch.search(graph, vertex)

                for (v in connectedComponent) {
                    componentByVertex[v] = connectedComponentIndex
                }
            }
        }
        return componentByVertex
    }
}