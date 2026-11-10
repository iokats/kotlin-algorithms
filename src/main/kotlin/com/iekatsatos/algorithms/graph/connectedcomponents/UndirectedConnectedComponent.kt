package com.iekatsatos.algorithms.graph.connectedcomponents

import com.iekatsatos.algorithms.graph.UndirectedGraph
import com.iekatsatos.algorithms.graph.search.BreadthFirstSearch

/**
 * Finds the connected components of an undirected graph.
 *
 * A connected component is a maximal set of vertices in which every two vertices are joined by a path. Every vertex
 * belongs to exactly one component, so the components partition the vertices of the graph. A vertex without edges
 * forms a component of its own.
 *
 * The algorithm loops over all vertices of the graph. Whenever it meets a vertex that has not been explored yet,
 * that vertex belongs to a new component, so it runs a graph search (BFS or DFS) from it. Everything the search
 * reaches is collected into that component and marked as explored. Because each vertex is explored once, the
 * components are found without overlap.
 *
 * The algorithm requires an [UndirectedGraph], because it relies on the symmetry of adjacency: if `v` is reachable
 * from `u`, then `u` is reachable from `v`. On a directed graph, reachability is one-way and this approach would
 * give order-dependent results. Directed graphs need a different notion (strongly connected components).
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the
 * number of edges. The space complexity is O(n), for the explored set and the result.
 */
object UndirectedConnectedComponent {

    /**
     * Identifies all connected components of [graph] and returns a mapping from each vertex to the 1-based index of
     * the connected component it belongs to.
     *
     * A connected component is a maximal set of vertices in which every two vertices are joined by a path. Vertices
     * with the same index belong to the same component, and a vertex without edges forms a component of its own.
     * The number of components is the highest index in the result, or zero for an empty graph.
     *
     * The algorithm loops over all vertices of [graph]. Whenever it meets a vertex that has no index yet, it starts a
     * new component and runs a [BreadthFirstSearch] from that vertex, assigning the new index to every vertex the
     * search reaches. Because [graph] is undirected, the search reaches exactly the vertices of that component.
     *
     * The indices are assigned in the order in which components are first encountered while iterating over the vertices
     * of the graph, which is an implementation detail of the graph and should not be relied upon. Only whether two
     * vertices share an index is meaningful, not the index value itself.
     *
     * The time complexity is O(n + m), where `n` is the number of vertices and `m` is the number of edges, since
     * every vertex is explored once. The space complexity is O(n).
     *
     * @param T the vertex type
     * @param graph the undirected graph whose components are requested
     * @return a map in which each key is a vertex of [graph] and the value is the 1-based index of the connected
     *   component to which it belongs. An empty graph yields an empty map.
     */
    fun <T> findComponents(graph: UndirectedGraph<T>): Map<T, Int> {
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