package com.iekatsatos.algorithms.graph.shortestpath

import com.iekatsatos.algorithms.graph.Graph

/**
 * Augmented BFS (Breadth-First Search) extends the standard search algorithm by tracking additional metadata —
 * specifically, the shortest path distance from a given starting vertex to every reachable vertex.
 *
 * Like standard BFS, it explores vertices layer by layer. Because each layer corresponds to a distance increment
 * of 1 edge, the first time a vertex is discovered, the layer it belongs to directly represents the shortest distance
 * (in number of edges) from the starting vertex.
 *
 * Vertices awaiting exploration are stored in a FIFO queue along with a map tracking their calculated distances.
 * The map also functions as an exploration tracker — once a vertex is assigned a distance in the map, it is considered
 * explored and will not be re-enqueued.
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the number
 * of edges.
 */
object AugmentedBFS {

    /**
     * Computes the shortest path distances from [startingVertex] to all reachable vertices in [graph] using
     * breadth-first search.
     *
     * @param T the vertex type
     * @param graph the graph to search
     * @param startingVertex the vertex to begin calculating distances from
     * @return a map where each key is a vertex reachable from [startingVertex] and the corresponding value is the
     *    shortest distance (number of edges) to it from [startingVertex]. Unreachable vertices are excluded from the map
     */
    fun <T> shortestPath(graph: Graph<T>, startingVertex: T): Map<T, Int> {
        val distance: MutableMap<T, Int> = mutableMapOf(startingVertex to 0)
        val queue: ArrayDeque<T> = ArrayDeque<T>().apply { addLast(startingVertex) }

        while (queue.isNotEmpty()) {
            val currentVertex = queue.removeFirst()

            for (neighbor in graph.neighborsOf(currentVertex)) {
                if (neighbor !in distance) {
                    distance[neighbor] = distance.getValue(currentVertex) + 1
                    queue.addLast(neighbor)
                }
            }
        }
        return distance
    }
}