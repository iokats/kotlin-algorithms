package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Graph

/**
 * BFS (Breadth-First Search) is a search algorithm that, given a starting vertex, explores vertices in layers.
 *
 * As a result, it finds the shortest path from the starting vertex to any reachable vertex (if there are multiple
 * paths from the starting vertex to a given vertex, this algorithm finds the shortest one). Here, distance is
 * defined as the number of edges on the path between the starting vertex and a given reachable vertex.
 *
 * It first explores all vertices directly connected to the starting vertex (Layer 1). Then it explores the vertices
 * directly connected to the vertices in Layer 1, and so on. Every vertex is explored once, and therefore belongs to
 * exactly one layer — each explored vertex is marked as explored to enforce this.
 *
 * Vertices awaiting exploration are kept in a FIFO queue: a vertex is enqueued when it is first discovered, and dequeued
 * only once every vertex discovered before it has been processed. This guarantees the algorithm fully explores one layer
 * before moving on to the next.
 *
 * The time complexity of the algorithm is linear, O(n + m), where `n` is the number of vertices and `m` is the number
 * of edges.
 */
object BreadthFirstSearch {

    /**
     * Performs a breadth-first search on [graph], starting from [startingVertex], and returns a list with all the
     * reachable vertices from this starting vertex.
     *
     * @param T the vertex type
     * @param graph the graph to search
     * @param startingVertex the vertex to begin the search from
     * @return a list of all vertices reachable from [startingVertex], including [startingVertex] itself as the first
     *   element, in the order they were explored (i.e. layer by layer, breadth-first)
     * @throws NoSuchElementException if [startingVertex] is not a vertex of [graph]
     */
    fun <T> search(graph: Graph<T>, startingVertex: T): List<T> {
        val exploredVertices: MutableSet<T> = mutableSetOf(startingVertex)
        val queue: ArrayDeque<T> = ArrayDeque<T>().apply { addLast(startingVertex) }

        val traversalOrder: MutableList<T> = mutableListOf()

        while (queue.isNotEmpty()) {

            val currentVertex = queue.removeFirst()
            traversalOrder.add(currentVertex)

            for (neighbor in graph.neighborsOf(currentVertex)) {
                if (neighbor !in exploredVertices) {
                    exploredVertices.add(neighbor)
                    queue.addLast(neighbor)
                }
            }
        }
        return traversalOrder
    }
}