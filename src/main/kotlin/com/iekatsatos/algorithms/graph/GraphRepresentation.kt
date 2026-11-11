package com.iekatsatos.algorithms.graph

/**
 * The data structure that backs a graph created by [GraphBuilder].
 */
enum class GraphRepresentation {
    /** Neighbors stored per vertex. Memory is Θ(n + m); suited to sparse graphs. */
    ADJACENCY_LIST,
}
